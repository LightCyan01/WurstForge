/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.mixins;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.wurstclient.forge.ForgeWurst;

/**
 * Mixin for PortalGui hack - allows opening GUIs in portals.
 * Uses reflection to access the protected inPortal field.
 */
@Mixin(EntityPlayerSP.class)
public abstract class EntityPlayerSPMixin
{
	private static Field inPortalField;
	
	static
	{
		try
		{
			// Try both obfuscated and deobfuscated names
			try
			{
				inPortalField = Entity.class.getDeclaredField("inPortal");
			}
			catch(NoSuchFieldException e)
			{
				inPortalField = Entity.class.getDeclaredField("field_71087_bX");
			}
			inPortalField.setAccessible(true);
		}
		catch(Exception e)
		{
			// Field not found - mixin won't work
		}
	}
	
	@Inject(method = "onLivingUpdate", at = @At("HEAD"))
	private void onLivingUpdateHead(CallbackInfo ci)
	{
		try
		{
			if(inPortalField == null)
				return;
			
			if(ForgeWurst.getForgeWurst() != null
				&& ForgeWurst.getForgeWurst().getHax() != null
				&& ForgeWurst.getForgeWurst().getHax().portalGuiHack != null
				&& ForgeWurst.getForgeWurst().getHax().portalGuiHack.isEnabled())
			{
				inPortalField.setBoolean(this, false);
			}
		}
		catch(Exception e)
		{
			// Silently ignore exceptions for mod compatibility
		}
	}
}
