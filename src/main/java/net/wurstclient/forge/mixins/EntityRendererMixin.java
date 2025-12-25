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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.EntityRenderer;
import net.wurstclient.forge.ForgeWurst;

/**
 * Mixin for CameraNoClip hack - allows camera to go through blocks.
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
	/**
	 * Modifies the camera distance calculation to prevent clipping.
	 * When CameraNoClipHack is enabled, sets the distance to max.
	 */
	@ModifyVariable(method = "orientCamera", at = @At(value = "STORE"), ordinal = 3)
	private double modifyCameraDistance(double distance)
	{
		try
		{
			if(ForgeWurst.getForgeWurst() != null
				&& ForgeWurst.getForgeWurst().getHax() != null
				&& ForgeWurst.getForgeWurst().getHax().cameraNoClipHack != null
				&& ForgeWurst.getForgeWurst().getHax().cameraNoClipHack.isEnabled())
			{
				// Return max distance to prevent camera clipping
				return 4.0;
			}
		}
		catch(Exception e)
		{
			// Ignore exceptions for compatibility
		}
		return distance;
	}
}
