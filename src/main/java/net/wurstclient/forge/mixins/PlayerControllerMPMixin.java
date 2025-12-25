/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.wurstclient.forge.ForgeWurst;

/**
 * Mixin for Reach hack - extends block reach distance.
 * Uses safe null checks for compatibility with other mods.
 */
@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin
{
	@Inject(method = "getBlockReachDistance", at = @At("HEAD"), cancellable = true)
	private void onGetBlockReachDistance(CallbackInfoReturnable<Float> cir)
	{
		try
		{
			if(ForgeWurst.getForgeWurst() != null
				&& ForgeWurst.getForgeWurst().getHax() != null
				&& ForgeWurst.getForgeWurst().getHax().reachHack != null
				&& ForgeWurst.getForgeWurst().getHax().reachHack.isEnabled())
			{
				cir.setReturnValue((float)ForgeWurst.getForgeWurst().getHax().reachHack.getReachDistance());
			}
		}
		catch(Exception e)
		{
			// Silently ignore exceptions for mod compatibility
		}
	}
}
