package com.proton.rubenhuizenga.hexcellular

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.INBTSerializable
import java.util.function.Consumer

class StateStorage : SavedData(), INBTSerializable<CompoundTag> {
	val properties: HashMap<String, CompoundTag> = HashMap()

	override fun save(nbt: CompoundTag): CompoundTag {
		return serializeNBT()
	}

	companion object {
		private fun createFromNbt(nbt: CompoundTag): StateStorage {
			val state = StateStorage()
			state.deserializeNBT(nbt)
			return state
		}

		fun getServerState(server: MinecraftServer): StateStorage {
			val persistentStateManager = server.getLevel(Level.OVERWORLD)!!.dataStorage
			return persistentStateManager.computeIfAbsent(
				{ tag: CompoundTag -> createFromNbt(tag) },
				{ StateStorage() },
				HexcellularMain.MOD_ID
			)
		}

		fun setProperty(world: ServerLevel, name: String, iota: Iota) {
			val state = getServerState(world.server)
			state.properties[name] = IotaType.serialize(iota)
			state.setDirty()
		}

		fun getProperty(world: ServerLevel, name: String): Iota {
			val state = getServerState(world.server)
			if (!state.properties.containsKey(name))
				return NullIota()
			return IotaType.deserialize(state.properties[name]!!, world)
		}
	}

	override fun serializeNBT(): CompoundTag {
		val nbt = CompoundTag()
		properties.forEach { (key, element) -> nbt.put(key, element) }
		return nbt
	}

	override fun deserializeNBT(nbt: CompoundTag) {
		properties.clear()
		nbt.allKeys.forEach { key -> properties[key] = nbt.getCompound(key) }
	}
}