package com.proton.rubenhuizenga.hexcellular

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.ChatFormatting
import net.minecraft.server.level.ServerLevel

class PropertyIota(property: String, val readonly: Boolean = false) : Iota(TYPE, property) {
	override fun isTruthy() = true
	override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.name == (that as PropertyIota).name
	val name = payload as String

	override fun serialize(): Tag {
		val compound = CompoundTag()
		compound.putString("name", this.name)
		compound.putBoolean("readonly", this.readonly)
		return compound
	}

	companion object {
		@JvmField
		val TYPE: IotaType<PropertyIota> = object : IotaType<PropertyIota>() {
			override fun deserialize(nbt: Tag, world: ServerLevel) = PropertyIota((nbt as CompoundTag).getString("name"), nbt.getBoolean("readonly"))			
			override fun display(nbt: Tag) = Component.literal((nbt as CompoundTag).getString("name")).withStyle(if (nbt.getBoolean("readonly")) ChatFormatting.AQUA else ChatFormatting.GREEN)
			override fun color() = -0x591c5f
		}
	}
}

fun List<Iota>.getProperty(idx: Int, argc: Int): String {
	val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
	if (x is PropertyIota)
		return x.name
	throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "property")
}