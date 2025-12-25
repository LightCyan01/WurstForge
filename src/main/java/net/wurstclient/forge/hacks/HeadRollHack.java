/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Makes your head roll up and down like you're nodding.
 */
public final class HeadRollHack extends Hack
{
	public HeadRollHack()
	{
		super("HeadRoll", "Makes your head roll up and down.");
		setCategory(Category.FUN);
	}
	
	@Override
	protected void onEnable()
	{
		// Disable incompatible derps
		wurst.getHax().derpHack.setEnabled(false);
		
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
		
		float timer = WMinecraft.getPlayer().ticksExisted % 20 / 10F;
		float pitch = (float)Math.sin(timer * Math.PI) * 90F;
		
		WMinecraft.getPlayer().connection.sendPacket(
			new CPacketPlayer.Rotation(WMinecraft.getPlayer().rotationYaw, 
				pitch, WMinecraft.getPlayer().onGround));
	}
}
