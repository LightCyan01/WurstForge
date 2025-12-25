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
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Automatically regenerates health.
 */
public final class RegenHack extends Hack
{
	private final SliderSetting packets = new SliderSetting("Packets",
		"Number of packets to send per tick.", 50, 1, 100, 1, ValueDisplay.INTEGER);
	
	public RegenHack()
	{
		super("Regen", "Regenerates health faster by sending position packets.");
		setCategory(Category.COMBAT);
		addSetting(packets);
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
		
		if(!WMinecraft.getPlayer().onGround)
			return;
		
		if(WMinecraft.getPlayer().getHealth() >= WMinecraft.getPlayer().getMaxHealth())
			return;
		
		if(WMinecraft.getPlayer().getFoodStats().getFoodLevel() < 18)
			return;
		
		// Send position packets to speed up natural regen
		for(int i = 0; i < packets.getValueI(); i++)
		{
			WMinecraft.getPlayer().connection.sendPacket(
				new net.minecraft.network.play.client.CPacketPlayer());
		}
	}
}
