package com.proton.rubenhuizenga.hexcellular.action

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.api.misc.MediaConstants
import com.proton.rubenhuizenga.hexcellular.PropertyIota
import com.proton.rubenhuizenga.hexcellular.StateStorage
import com.proton.rubenhuizenga.hexcellular.getProperty
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.ServerLevel

object OpSetProperty : ConstMediaAction {
	override val argc = 2
	override val mediaCost = MediaConstants.DUST_UNIT / 10
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val name = args.getProperty(0, argc)
		if ((args[0] as PropertyIota).readonly)
			throw MishapInvalidIota.of(args[0], 1, "writeable_prop")

		val iota = args[1]
		val trueName = MishapOthersName.getTrueNameFromDatum(iota, env.castingEntity as? ServerPlayer)
		if (trueName != null)
			throw MishapOthersName(trueName)
		StateStorage.setProperty(env.world, name, iota)
		return listOf()
	}
}