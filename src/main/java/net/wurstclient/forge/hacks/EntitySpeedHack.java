/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class EntitySpeedHack extends Hack
{
	private final SliderSetting speed = new SliderSetting("Speed",
		"Speed multiplier for vehicles.", 2.0, 1.0, 10.0, 0.1, ValueDisplay.DECIMAL);
	
	public EntitySpeedHack()
	{
		super("EntitySpeed", "Makes vehicles faster.");
		setCategory(Category.MOVEMENT);
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
		
		Entity vehicle = WMinecraft.getPlayer().getRidingEntity();
		if(vehicle == null)
			return;
		
		if(!isValidVehicle(vehicle))
			return;
		
		double multiplier = speed.getValue();
		
		float forward = WMinecraft.getPlayer().movementInput.moveForward;
		float strafe = WMinecraft.getPlayer().movementInput.moveStrafe;
		
		if(forward == 0 && strafe == 0)
			return;
		
		double yaw = Math.toRadians(WMinecraft.getPlayer().rotationYaw);
		
		double motionX = -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
		double motionZ = Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
		
		double length = Math.sqrt(motionX * motionX + motionZ * motionZ);
		if(length > 0)
		{
			motionX /= length;
			motionZ /= length;
		}
		
		vehicle.motionX = motionX * 0.2 * multiplier;
		vehicle.motionZ = motionZ * 0.2 * multiplier;
		
		if(mc.gameSettings.keyBindJump.isKeyDown() && vehicle.onGround)
			vehicle.motionY = 0.5;
	}
	
	private boolean isValidVehicle(Entity entity)
	{
		return entity instanceof AbstractHorse
			|| entity instanceof EntityPig
			|| entity instanceof EntityBoat
			|| entity instanceof EntityMinecart;
	}
}
