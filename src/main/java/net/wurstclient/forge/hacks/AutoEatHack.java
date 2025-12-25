/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class AutoEatHack extends Hack
{
	private final SliderSetting minHunger = new SliderSetting("Min Hunger",
		"Eat when hunger drops to this level.", 16, 1, 20, 1, ValueDisplay.INTEGER);
	
	private final CheckboxSetting preferBestFood = new CheckboxSetting(
		"Prefer best food", "Prioritizes food that restores more hunger.", true);
	
	private int oldSlot = -1;
	private boolean eating = false;
	
	public AutoEatHack()
	{
		super("AutoEat", "Automatically eats food when hungry.");
		setCategory(Category.OTHER);
		addSetting(minHunger);
		addSetting(preferBestFood);
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
		stopEating();
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		FoodStats foodStats = WMinecraft.getPlayer().getFoodStats();
		
		if(foodStats.getFoodLevel() <= minHunger.getValueI() && foodStats.getFoodLevel() < 20)
		{
			int foodSlot = findBestFood();
			if(foodSlot != -1)
			{
				if(oldSlot == -1)
					oldSlot = WMinecraft.getPlayer().inventory.currentItem;
				
				WMinecraft.getPlayer().inventory.currentItem = foodSlot;
				net.minecraft.client.settings.KeyBinding.setKeyBindState(
					mc.gameSettings.keyBindUseItem.getKeyCode(), true);
				eating = true;
			}
		}
		else if(eating)
		{
			stopEating();
		}
	}
	
	private int findBestFood()
	{
		int bestSlot = -1;
		int bestValue = 0;
		
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(i);
			if(stack.isEmpty())
				continue;
			
			if(stack.getItem() instanceof ItemFood)
			{
				ItemFood food = (ItemFood)stack.getItem();
				int value = food.getHealAmount(stack);
				
				if(!preferBestFood.isChecked())
					return i;
				
				if(value > bestValue)
				{
					bestValue = value;
					bestSlot = i;
				}
			}
		}
		return bestSlot;
	}
	
	private void stopEating()
	{
		net.minecraft.client.settings.KeyBinding.setKeyBindState(
			mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		eating = false;
		if(oldSlot != -1 && WMinecraft.getPlayer() != null)
		{
			WMinecraft.getPlayer().inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
	}
}
