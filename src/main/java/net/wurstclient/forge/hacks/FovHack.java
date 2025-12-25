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
 * Increases field of view.
 */
public final class FovHack extends Hack
{
	private final SliderSetting fov = new SliderSetting("FOV",
		"Field of view.", 120, 30, 180, 1, ValueDisplay.INTEGER);
	
	private float originalFov;
	
	public FovHack()
	{
		super("FOV", "Changes your field of view.");
		setCategory(Category.RENDER);
		addSetting(fov);
	}
	
	@Override
	protected void onEnable()
	{
		originalFov = mc.gameSettings.fovSetting;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
		mc.gameSettings.fovSetting = originalFov;
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		mc.gameSettings.fovSetting = (float)fov.getValue();
	}
}
