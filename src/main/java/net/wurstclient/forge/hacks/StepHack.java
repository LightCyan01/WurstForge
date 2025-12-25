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
 * Allows you to step up blocks instantly.
 */
public final class StepHack extends Hack
{
	private final SliderSetting height = new SliderSetting("Height",
		"How high you can step up.", 1, 1, 10, 0.5, ValueDisplay.DECIMAL);
	
	public StepHack()
	{
		super("Step", "Allows you to step up full blocks.");
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
		if(WMinecraft.getPlayer() != null)
			WMinecraft.getPlayer().stepHeight = 0.6F;
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		WMinecraft.getPlayer().stepHeight = (float)height.getValue();
	}
}
