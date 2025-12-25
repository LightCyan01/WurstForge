/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class BoatFlyHack extends Hack
{
	private final SliderSetting speed = new SliderSetting("Speed",
		"Flying speed.", 1.0, 0.1, 5.0, 0.1, ValueDisplay.DECIMAL);
	
	private final SliderSetting verticalSpeed = new SliderSetting("Vertical Speed",
		"Vertical flying speed.", 0.5, 0.1, 2.0, 0.1, ValueDisplay.DECIMAL);
	
	public BoatFlyHack()
	{
		super("BoatFly", "Allows flying while in a boat.");
		setCategory(Category.MOVEMENT);
		addSetting(speed);
		addSetting(verticalSpeed);
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
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return;
		
		if(!player.isRiding())
			return;
		
		if(!(player.getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat))
			return;
		
		net.minecraft.entity.Entity boat = player.getRidingEntity();
		double speedVal = speed.getValue();
		double vSpeed = verticalSpeed.getValue();
		
		boat.motionY = 0;
		
		double yaw = Math.toRadians(player.rotationYaw);
		
		if(mc.gameSettings.keyBindForward.isKeyDown())
		{
			boat.motionX = -Math.sin(yaw) * speedVal * 0.5;
			boat.motionZ = Math.cos(yaw) * speedVal * 0.5;
		}
		else if(mc.gameSettings.keyBindBack.isKeyDown())
		{
			boat.motionX = Math.sin(yaw) * speedVal * 0.5;
			boat.motionZ = -Math.cos(yaw) * speedVal * 0.5;
		}
		else
		{
			boat.motionX = 0;
			boat.motionZ = 0;
		}
		
		if(mc.gameSettings.keyBindJump.isKeyDown())
			boat.motionY = vSpeed;
		if(mc.gameSettings.keyBindSneak.isKeyDown())
			boat.motionY = -vSpeed;
	}
}
