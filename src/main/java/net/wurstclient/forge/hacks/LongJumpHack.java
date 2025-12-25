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
 * Long jump hack.
 */
public final class LongJumpHack extends Hack
{
	private final SliderSetting jumpBoost = new SliderSetting("Jump Boost",
		"Horizontal jump boost multiplier.", 1.5, 1, 5, 0.1, ValueDisplay.DECIMAL);
	
	public LongJumpHack()
	{
		super("LongJump", "Allows you to jump further.");
		setCategory(Category.MOVEMENT);
		addSetting(jumpBoost);
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
		
		// Boost on jump
		if(mc.gameSettings.keyBindJump.isKeyDown() && WMinecraft.getPlayer().onGround)
		{
			double boost = jumpBoost.getValue();
			WMinecraft.getPlayer().motionX *= boost;
			WMinecraft.getPlayer().motionZ *= boost;
		}
	}
}
