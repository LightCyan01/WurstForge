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
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Automatically uses healing potions when hurt.
 */
public final class AutoPotionHack extends Hack
{
	private final SliderSetting health = new SliderSetting("Health",
		"Use potions when health drops below this value.",
		6, 1, 20, 1, ValueDisplay.INTEGER);
	
	public AutoPotionHack()
	{
		super("AutoPotion", "Automatically uses healing potions when hurt.");
		setCategory(Category.COMBAT);
		addSetting(health);
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
		
		if(WMinecraft.getPlayer().getHealth() > health.getValueI())
			return;
		
		// Find healing potion in hotbar
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(i);
			if(stack.isEmpty())
				continue;
			
			if(stack.getItem() == Items.SPLASH_POTION || 
				stack.getItem() == Items.POTIONITEM)
			{
				// Check if it's a healing potion
				if(PotionUtils.getEffectsFromStack(stack).stream()
					.anyMatch(e -> e.getPotion().isBeneficial()))
				{
					int oldSlot = WMinecraft.getPlayer().inventory.currentItem;
					WMinecraft.getPlayer().inventory.currentItem = i;
					
					mc.playerController.processRightClick(WMinecraft.getPlayer(),
						WMinecraft.getWorld(), net.minecraft.util.EnumHand.MAIN_HAND);
					
					WMinecraft.getPlayer().inventory.currentItem = oldSlot;
					return;
				}
			}
		}
	}
}
