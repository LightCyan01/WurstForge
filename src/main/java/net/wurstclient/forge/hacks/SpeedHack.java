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

/**
 * Increases your movement speed using mini-jumps.
 */
public final class SpeedHack extends Hack
{
	private final SliderSetting speed = new SliderSetting("Speed",
		"Speed multiplier.\nHigher = faster but more obvious.",
		1.5, 1.0, 3.0, 0.1, SliderSetting.ValueDisplay.DECIMAL);
	
	public SpeedHack()
	{
		super("Speed", "Increases your movement speed.");
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
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return;
		
		// Return if sneaking or not moving
		if(player.isSneaking() || (player.moveForward == 0 && player.moveStrafing == 0))
			return;
		
		// Auto-sprint if walking forward
		if(player.moveForward > 0 && !player.collidedHorizontally)
			player.setSprinting(true);
		
		// Activate mini jump if on ground
		if(!player.onGround)
			return;
		
		double multiplier = speed.getValue();
		player.motionX *= multiplier;
		player.motionZ *= multiplier;
		player.motionY += 0.1;
		
		// Limit speed to avoid detection
		double currentSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
		double maxSpeed = 0.66;
		
		if(currentSpeed > maxSpeed)
		{
			player.motionX = player.motionX / currentSpeed * maxSpeed;
			player.motionZ = player.motionZ / currentSpeed * maxSpeed;
		}
	}
}
