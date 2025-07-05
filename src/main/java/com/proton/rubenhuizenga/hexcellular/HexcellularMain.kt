package com.proton.rubenhuizenga.hexcellular

import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import net.minecraftforge.registries.RegisterEvent
import org.apache.logging.log4j.LogManager

@Mod(HexcellularMain.MOD_ID)
object HexcellularMain {
	const val MOD_ID: String = "hexcellular"
	private const val PORT_AUTHOR = "RubenHuizenga"
	val LOGGER = LogManager.getLogger(MOD_ID)
	
	init {
		LOGGER.info("Initializing Hexcellular (Forge Port by $PORT_AUTHOR)")

		MOD_BUS.addListener(this::onRegisterIotaTypes)
		
		MinecraftForge.EVENT_BUS.register(this)
		
		println("Loaded Hexcellular (Forge Port by $PORT_AUTHOR)")
	}
	
	private fun onRegisterIotaTypes(event: RegisterEvent) {
		if (event.registryKey == HexIotaTypes.REGISTRY.key()) {
			event.register(HexIotaTypes.REGISTRY.key(), id("property")) {
				PropertyIota.TYPE
			}
		}
	}
	
	fun id(string: String) = ResourceLocation(MOD_ID, string)
}