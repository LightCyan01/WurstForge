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

import java.util.Random;

/**
 * Automatically performs actions to prevent AFK kick.
 */
public final class AntiAfkHack extends Hack
{
	private final SliderSetting interval = new SliderSetting("Interval",
		"How often to perform anti-AFK action (in ticks).",
		100, 20, 600, 10, ValueDisplay.INTEGER);
	
	private int timer;
	private final Random random = new Random();
	
	public AntiAfkHack()
	{
		super("AntiAFK", "Performs random actions to prevent AFK kick.");
		setCategory(Category.OTHER);
		addSetting(interval);
	}
	
	@Override
	protected void onEnable()
	{
		timer = 0;
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
		
		timer++;
		if(timer < interval.getValueI())
			return;
		
		timer = 0;
		
		// Spin around randomly
		WMinecraft.getPlayer().rotationYaw += random.nextFloat() * 360 - 180;
	}
}
