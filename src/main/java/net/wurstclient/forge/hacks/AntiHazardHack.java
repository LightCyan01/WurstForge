/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.AxisAlignedBB;
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
 * Dodges dangerous projectiles and entities.
 */
public final class AntiHazardHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"Detection range for hazards.", 5, 2, 10, 0.5, ValueDisplay.DECIMAL);
	
	public AntiHazardHack()
	{
		super("AntiHazard", "Dodges arrows, TNT, creepers, and fireballs.");
		setCategory(Category.COMBAT);
		addSetting(range);
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
		AxisAlignedBB area = WMinecraft.getPlayer().getEntityBoundingBox().grow(r);
		
		for(Entity entity : WMinecraft.getWorld().loadedEntityList)
		{
			if(!area.intersects(entity.getEntityBoundingBox()))
				continue;
			
			boolean isDanger = entity instanceof EntityArrow
				|| entity instanceof EntityTNTPrimed
				|| entity instanceof EntityFireball
				|| (entity instanceof EntityCreeper && ((EntityCreeper)entity).getCreeperState() > 0);
			
			if(!isDanger)
				continue;
			
			// Move away from danger
			Vec3d playerPos = WMinecraft.getPlayer().getPositionVector();
			Vec3d dangerPos = entity.getPositionVector();
			Vec3d direction = playerPos.subtract(dangerPos).normalize();
			
			WMinecraft.getPlayer().motionX += direction.x * 0.3;
			WMinecraft.getPlayer().motionZ += direction.z * 0.3;
			break;
		}
	}
}
