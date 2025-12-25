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
 * Automatically throws items.
 */
public final class ThrowHack extends Hack
{
	private final SliderSetting delay = new SliderSetting("Delay",
		"Delay between throws in ticks.", 0, 0, 20, 1, ValueDisplay.INTEGER);
	
	private int timer;
	
	public ThrowHack()
	{
		super("Throw", "Automatically throws the item you're holding.");
		setCategory(Category.OTHER);
		addSetting(delay);
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
		
		if(timer > 0)
		{
			timer--;
			return;
		}
		
		timer = delay.getValueI();
		
		// Right click to throw
		if(mc.gameSettings.keyBindUseItem.isKeyDown())
		{
			if(WMinecraft.getPlayer().getHeldItemMainhand().isEmpty())
				return;
			
			mc.playerController.processRightClick(WMinecraft.getPlayer(),
				WMinecraft.getWorld(), net.minecraft.util.EnumHand.MAIN_HAND);
		}
	}
}
