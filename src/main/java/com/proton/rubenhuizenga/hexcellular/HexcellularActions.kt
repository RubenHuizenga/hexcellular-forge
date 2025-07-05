package com.proton.rubenhuizenga.hexcellular

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import com.proton.rubenhuizenga.hexcellular.action.OpCreateProperty
import com.proton.rubenhuizenga.hexcellular.action.OpObserveProperty
import com.proton.rubenhuizenga.hexcellular.action.OpReadonlyProperty
import com.proton.rubenhuizenga.hexcellular.action.OpSetProperty
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.RegisterEvent
import org.apache.logging.log4j.LogManager

@Mod.EventBusSubscriber(modid = HexcellularMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object HexcellularActions {
	private val LOGGER = LogManager.getLogger("Hexcellular/Actions")

	@SubscribeEvent
	fun registerActions(event: RegisterEvent) {
		try {
			if (event.registryKey == HexActions.REGISTRY.key()) {
				LOGGER.info("Registering Hexcellular actions...")
				
				register(event, "create_property", "aawe", HexDir.SOUTH_WEST, OpCreateProperty)
				register(event, "observe_property", "aawd", HexDir.SOUTH_WEST, OpObserveProperty)
				register(event, "set_property", "aawq", HexDir.SOUTH_WEST, OpSetProperty)
				register(event, "readonly_property", "aawa", HexDir.SOUTH_WEST, OpReadonlyProperty)
				
				LOGGER.info("Successfully registered all Hexcellular actions")
			}
		} catch (e: Exception) {
			LOGGER.error("Failed to register Hexcellular actions", e)
			throw e
		}
	}

	private fun register(
		event: RegisterEvent,
		name: String,
		signature: String,
		startDir: HexDir,
		action: Action
	) {
		val id = HexcellularMain.id(name)
		LOGGER.debug("Registering action {} with pattern {}", id, signature)
		
		event.register(HexActions.REGISTRY.key(), id) {
			ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action).also {
				LOGGER.trace("Created ActionRegistryEntry for {}", id)
			}
		}
	}
}