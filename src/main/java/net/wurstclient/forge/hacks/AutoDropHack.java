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
 * Automatically uses items.
 */
public final class AutoDropHack extends Hack
{
	public AutoDropHack()
	{
		super("AutoDrop",
			"Automatically drops all items except in hotbar.");
		setCategory(Category.OTHER);
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
		
		// Drop items from inventory slots (not hotbar, slots 9-35)
		for(int i = 9; i < 36; i++)
		{
			if(!WMinecraft.getPlayer().inventory.getStackInSlot(i).isEmpty())
			{
				mc.playerController.windowClick(0, i, 1,
					net.minecraft.inventory.ClickType.THROW,
					WMinecraft.getPlayer());
				break; // Only drop one per tick
			}
		}
	}
}
