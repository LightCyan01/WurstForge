/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Automatically stores items in chests.
 */
public final class AutoStoreHack extends Hack
{
	private int timer;
	
	public AutoStoreHack()
	{
		super("AutoStore", "Automatically stores items from inventory into chests.");
		setCategory(Category.OTHER);
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
		
		if(!(mc.currentScreen instanceof GuiChest))
			return;
		
		timer++;
		if(timer < 2)
			return;
		timer = 0;
		
		GuiChest chest = (GuiChest)mc.currentScreen;
		int chestSlots = chest.inventorySlots.inventorySlots.size() - 36;
		
		// Move items from player inventory to chest
		for(int i = chestSlots; i < chest.inventorySlots.inventorySlots.size(); i++)
		{
			ItemStack stack = chest.inventorySlots.getSlot(i).getStack();
			if(!stack.isEmpty())
			{
				mc.playerController.windowClick(
					chest.inventorySlots.windowId, i, 0,
					ClickType.QUICK_MOVE, WMinecraft.getPlayer());
				return;
			}
		}
	}
}
