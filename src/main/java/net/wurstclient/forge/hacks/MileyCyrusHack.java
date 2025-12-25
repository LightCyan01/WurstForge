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
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.utils.KeyBindingUtils;

/**
 * Makes you twerk by rapidly toggling sneak.
 * I came in like a wrecking ball...
 */
public final class MileyCyrusHack extends Hack
{
	private final SliderSetting twerkSpeed = new SliderSetting("Twerk speed",
		"I came in like a wreeecking baaall...", 5, 1, 10, 1,
		SliderSetting.ValueDisplay.INTEGER);
	
	private int timer;
	
	public MileyCyrusHack()
	{
		super("MileyCyrus", "Twerks by rapidly toggling sneak.");
		setCategory(Category.FUN);
		addSetting(twerkSpeed);
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
		KeyBindingUtils.resetPressed(mc.gameSettings.keyBindSneak);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		timer++;
		if(timer < 10 - twerkSpeed.getValueI())
			return;
		
		boolean currentState = mc.gameSettings.keyBindSneak.isKeyDown();
		KeyBindingUtils.setPressed(mc.gameSettings.keyBindSneak, !currentState);
		timer = -1;
	}
}
