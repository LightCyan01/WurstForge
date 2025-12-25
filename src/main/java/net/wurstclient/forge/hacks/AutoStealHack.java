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

/**
 * Automatically steals items from chests.
 */
public final class AutoStealHack extends Hack
{
	private int timer = 0;
	
	public AutoStealHack()
	{
		super("AutoSteal", "Automatically steals items from chests.");
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
		
		if(!(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiChest))
			return;
		
		timer++;
		if(timer < 3) // Delay to prevent issues
			return;
		timer = 0;
		
		net.minecraft.client.gui.inventory.GuiChest chest = 
			(net.minecraft.client.gui.inventory.GuiChest)mc.currentScreen;
		
		// Get chest inventory size
		int chestSlots = chest.inventorySlots.inventorySlots.size() - 36;
		
		for(int i = 0; i < chestSlots; i++)
		{
			if(!chest.inventorySlots.getSlot(i).getStack().isEmpty())
			{
				mc.playerController.windowClick(
					chest.inventorySlots.windowId, i, 0,
					net.minecraft.inventory.ClickType.QUICK_MOVE,
					WMinecraft.getPlayer());
				break; // One item per tick
			}
		}
	}
}
