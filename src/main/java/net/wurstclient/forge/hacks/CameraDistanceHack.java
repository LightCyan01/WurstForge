/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Changes the camera distance.
 */
public final class CameraDistanceHack extends Hack
{
	private final SliderSetting distance = new SliderSetting("Distance",
		"Camera distance.", 10, 1, 50, 1, ValueDisplay.INTEGER);
	
	public CameraDistanceHack()
	{
		super("CameraDistance", "Changes the third-person camera distance.");
		setCategory(Category.RENDER);
		addSetting(distance);
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
	
	public double getDistance()
	{
		return isEnabled() ? distance.getValue() : 4.0;
	}
}
