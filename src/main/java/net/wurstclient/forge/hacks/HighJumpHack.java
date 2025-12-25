/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WEntityPlayerJumpEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;

/**
 * Makes you jump higher than normal.
 */
public final class HighJumpHack extends Hack
{
	private final SliderSetting height = new SliderSetting("Height",
		"Jump height multiplier.\n"
			+ "Higher values = higher jumps.",
		2, 1, 10, 0.5, SliderSetting.ValueDisplay.DECIMAL);
	
	public HighJumpHack()
	{
		super("HighJump", "Makes you jump higher.");
		setCategory(Category.MOVEMENT);
		addSetting(height);
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
	
	@SubscribeEvent
	public void onJump(WEntityPlayerJumpEvent event)
	{
		EntityPlayer player = event.getPlayer();
		if(player == null)
			return;
		
		// Multiply the jump motion by the height setting
		// Default jump motion is 0.42, we add extra based on setting
		float extraMotion = (float)((height.getValue() - 1) * 0.42);
		player.motionY += extraMotion;
	}
}
