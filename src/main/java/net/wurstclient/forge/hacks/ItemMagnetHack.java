/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Automatically pulls nearby items towards you.
 */
public final class ItemMagnetHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"How far away items will be pulled.", 5, 1, 10, 0.5, ValueDisplay.DECIMAL);
	private final SliderSetting speed = new SliderSetting("Speed",
		"How fast items are pulled.", 0.5, 0.1, 2.0, 0.1, ValueDisplay.DECIMAL);
	
	public ItemMagnetHack()
	{
		super("ItemMagnet", "Pulls nearby items towards you.");
		setCategory(Category.OTHER);
		addSetting(range);
		addSetting(speed);
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
		
		double r = range.getValue();
		double pullSpeed = speed.getValue();
		Vec3d playerPos = WMinecraft.getPlayer().getPositionVector();
		
		for(Entity entity : WMinecraft.getWorld().loadedEntityList)
		{
			if(!(entity instanceof EntityItem))
				continue;
			
			double distance = entity.getDistance(WMinecraft.getPlayer());
			if(distance > r || distance < 0.5)
				continue;
			
			// Calculate direction towards player
			Vec3d entityPos = entity.getPositionVector();
			Vec3d direction = playerPos.subtract(entityPos).normalize();
			
			// Move item towards player with smooth motion
			entity.motionX = direction.x * pullSpeed;
			entity.motionY = direction.y * pullSpeed + 0.1; // Slight upward motion
			entity.motionZ = direction.z * pullSpeed;
		}
	}
}
