/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Allows you to walk through blocks (noclip mode).
 */
public final class NoClipHack extends Hack
{
	public NoClipHack()
	{
		super("NoClip",
			"Allows you to walk through blocks.\n"
				+ "Warning: You will fall into the void!");
		setCategory(Category.MOVEMENT);
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
		if(WMinecraft.getPlayer() != null)
			WMinecraft.getPlayer().noClip = false;
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		WMinecraft.getPlayer().noClip = true;
		WMinecraft.getPlayer().fallDistance = 0;
		WMinecraft.getPlayer().onGround = false;
		
		// Allow flying through blocks
		WMinecraft.getPlayer().capabilities.isFlying = true;
		
		// Movement
		double speed = 0.2;
		WMinecraft.getPlayer().motionX = 0;
		WMinecraft.getPlayer().motionY = 0;
		WMinecraft.getPlayer().motionZ = 0;
		
		if(mc.gameSettings.keyBindForward.isKeyDown())
		{
			double yaw = Math.toRadians(WMinecraft.getPlayer().rotationYaw);
			WMinecraft.getPlayer().motionX -= Math.sin(yaw) * speed;
			WMinecraft.getPlayer().motionZ += Math.cos(yaw) * speed;
		}
		if(mc.gameSettings.keyBindBack.isKeyDown())
		{
			double yaw = Math.toRadians(WMinecraft.getPlayer().rotationYaw);
			WMinecraft.getPlayer().motionX += Math.sin(yaw) * speed;
			WMinecraft.getPlayer().motionZ -= Math.cos(yaw) * speed;
		}
		if(mc.gameSettings.keyBindLeft.isKeyDown())
		{
			double yaw = Math.toRadians(WMinecraft.getPlayer().rotationYaw - 90);
			WMinecraft.getPlayer().motionX -= Math.sin(yaw) * speed;
			WMinecraft.getPlayer().motionZ += Math.cos(yaw) * speed;
		}
		if(mc.gameSettings.keyBindRight.isKeyDown())
		{
			double yaw = Math.toRadians(WMinecraft.getPlayer().rotationYaw + 90);
			WMinecraft.getPlayer().motionX -= Math.sin(yaw) * speed;
			WMinecraft.getPlayer().motionZ += Math.cos(yaw) * speed;
		}
		if(mc.gameSettings.keyBindJump.isKeyDown())
			WMinecraft.getPlayer().motionY += speed;
		if(mc.gameSettings.keyBindSneak.isKeyDown())
			WMinecraft.getPlayer().motionY -= speed;
	}
}
