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
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Disables rain/snow and optionally changes the time of day (client-side only).
 */
public final class NoWeatherHack extends Hack
{
	private final CheckboxSetting disableRain =
		new CheckboxSetting("Disable Rain",
			"Prevents rain and snow from rendering.", true);
	
	private final CheckboxSetting changeTime =
		new CheckboxSetting("Change World Time",
			"Changes the world time (client-side only).", false);
	
	private final SliderSetting time = new SliderSetting("Time",
		"The time of day to set.\n"
			+ "0 = sunrise, 6000 = noon,\n"
			+ "12000 = sunset, 18000 = midnight",
		6000, 0, 23999, 100, ValueDisplay.INTEGER);
	
	public NoWeatherHack()
	{
		super("NoWeather",
			"Disables rain/snow and changes time of day.");
		setCategory(Category.RENDER);
		addSetting(disableRain);
		addSetting(changeTime);
		addSetting(time);
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
		if(WMinecraft.getWorld() == null)
			return;
		
		// Disable rain
		if(disableRain.isChecked())
		{
			WMinecraft.getWorld().setRainStrength(0);
			WMinecraft.getWorld().setThunderStrength(0);
		}
		
		// Change time (client-side only)
		if(changeTime.isChecked())
		{
			WMinecraft.getWorld().setWorldTime(time.getValueI());
		}
	}
	
	public boolean isRainDisabled()
	{
		return isEnabled() && disableRain.isChecked();
	}
	
	public boolean isTimeChanged()
	{
		return isEnabled() && changeTime.isChecked();
	}
	
	public long getChangedTime()
	{
		return time.getValueI();
	}
}
