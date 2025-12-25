/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

/**
 * Randomly toggles your skin layers for a spooky effect.
 */
public final class SkinDerpHack extends Hack
{
	private final Random random = new Random();
	
	public SkinDerpHack()
	{
		super("SkinDerp", "Randomly toggles skin layers.");
		setCategory(Category.FUN);
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
		
		// Re-enable all skin parts
		for(EnumPlayerModelParts part : EnumPlayerModelParts.values())
			mc.gameSettings.setModelPartEnabled(part, true);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		// Only toggle 25% of the time for less seizure-inducing effect
		if(random.nextInt(4) != 0)
			return;
		
		Set<EnumPlayerModelParts> enabledParts = mc.gameSettings.getModelParts();
		
		for(EnumPlayerModelParts part : EnumPlayerModelParts.values())
		{
			boolean isEnabled = enabledParts.contains(part);
			mc.gameSettings.setModelPartEnabled(part, !isEnabled);
		}
	}
}
