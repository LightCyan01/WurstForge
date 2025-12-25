/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Sends you falling very slowly.
 */
public final class SlowfallHack extends Hack
{
	private final SliderSetting fallSpeed = new SliderSetting("Fall Speed",
		"How fast you fall.", 0.125, 0.01, 0.25, 0.01, ValueDisplay.DECIMAL);
	
	public SlowfallHack()
	{
		super("Slowfall", "Makes you fall very slowly.");
		setCategory(Category.MOVEMENT);
		addSetting(fallSpeed);
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
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		if(WMinecraft.getPlayer().motionY < 0 && !WMinecraft.getPlayer().onGround)
		{
			WMinecraft.getPlayer().motionY = -fallSpeed.getValue();
		}
	}
}
