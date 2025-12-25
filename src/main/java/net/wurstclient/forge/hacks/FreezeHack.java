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
 * Stops all momentum.
 */
public final class FreezeHack extends Hack
{
	private double savedX, savedY, savedZ;
	private float savedYaw, savedPitch;
	
	public FreezeHack()
	{
		super("Freeze", "Stops all momentum and holds your position.");
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	protected void onEnable()
	{
		if(WMinecraft.getPlayer() != null)
		{
			savedX = WMinecraft.getPlayer().posX;
			savedY = WMinecraft.getPlayer().posY;
			savedZ = WMinecraft.getPlayer().posZ;
			savedYaw = WMinecraft.getPlayer().rotationYaw;
			savedPitch = WMinecraft.getPlayer().rotationPitch;
		}
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
		
		WMinecraft.getPlayer().setPosition(savedX, savedY, savedZ);
		WMinecraft.getPlayer().motionX = 0;
		WMinecraft.getPlayer().motionY = 0;
		WMinecraft.getPlayer().motionZ = 0;
	}
}
