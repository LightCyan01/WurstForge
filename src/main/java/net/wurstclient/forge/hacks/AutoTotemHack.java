/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Automatically moves totems of undying to your offhand.
 */
public final class AutoTotemHack extends Hack
{
	public AutoTotemHack()
	{
		super("AutoTotem", "Automatically moves totems to your offhand.");
		setCategory(Category.COMBAT);
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
		
		// Check if offhand already has totem
		ItemStack offhand = WMinecraft.getPlayer().getHeldItemOffhand();
		if(offhand.getItem() == Items.TOTEM_OF_UNDYING)
			return;
		
		// Find totem in inventory
		int totemSlot = findTotem();
		if(totemSlot == -1)
			return;
		
		// Move totem to offhand
		// Click totem slot, click offhand, click original slot (if had item)
		mc.playerController.windowClick(0, totemSlot, 0,
			net.minecraft.inventory.ClickType.PICKUP, WMinecraft.getPlayer());
		mc.playerController.windowClick(0, 45, 0,
			net.minecraft.inventory.ClickType.PICKUP, WMinecraft.getPlayer());
		
		if(!offhand.isEmpty())
		{
			mc.playerController.windowClick(0, totemSlot, 0,
				net.minecraft.inventory.ClickType.PICKUP, WMinecraft.getPlayer());
		}
	}
	
	private int findTotem()
	{
		// Check hotbar first (slots 36-44)
		for(int i = 36; i < 45; i++)
		{
			ItemStack stack = WMinecraft.getPlayer().inventoryContainer
				.getSlot(i).getStack();
			if(stack.getItem() == Items.TOTEM_OF_UNDYING)
				return i;
		}
		
		// Check main inventory (slots 9-35)
		for(int i = 9; i < 36; i++)
		{
			ItemStack stack = WMinecraft.getPlayer().inventoryContainer
				.getSlot(i).getStack();
			if(stack.getItem() == Items.TOTEM_OF_UNDYING)
				return i;
		}
		
		return -1;
	}
}
