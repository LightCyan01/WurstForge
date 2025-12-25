/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

public final class PotionSaverHack extends Hack
{
	public PotionSaverHack()
	{
		super("PotionSaver", "Pauses potion timers while standing still.");
		setCategory(Category.OTHER);
	}
	
	@Override
	protected void onEnable()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	public boolean shouldFreeze()
	{
		if(!isEnabled())
			return false;
		
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return false;
		
		return player.movementInput.moveForward == 0 &&
			player.movementInput.moveStrafe == 0 &&
			!player.movementInput.jump &&
			!player.isSneaking();
	}
}
