/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;

/**
 * Automatically disconnects when your health gets too low.
 */
public final class AutoLeaveHack extends Hack
{
	private final SliderSetting health = new SliderSetting("Health",
		"Disconnect when health drops to this value or below.",
		4, 0.5, 20, 0.5, SliderSetting.ValueDisplay.DECIMAL);
	
	private final EnumSetting<Mode> mode = 
		new EnumSetting<>("Mode", Mode.values(), Mode.QUIT);
	
	public AutoLeaveHack()
	{
		super("AutoLeave", "Disconnects when your health is low.");
		setCategory(Category.COMBAT);
		addSetting(health);
		addSetting(mode);
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
		
		// Check if health is below threshold
		if(player.getHealth() > health.getValue())
			return;
		
		// Don't leave if already dead
		if(player.getHealth() <= 0)
			return;
		
		switch(mode.getSelected())
		{
			case QUIT:
				mc.world.sendQuittingDisconnectingPacket();
				break;
			
			case CHARS:
				// Send invalid characters to get kicked
				player.connection.sendPacket(
					new CPacketChatMessage("\u00a7"));
				break;
			
			case SELFKICK:
				// Send /selfkick command
				player.sendChatMessage("/selfkick");
				break;
		}
		
		setEnabled(false);
	}
	
	@Override
	public String getRenderName()
	{
		return getName() + " [" + mode.getSelected() + "]";
	}
	
	private enum Mode
	{
		QUIT("Quit"),
		CHARS("Chars"),
		SELFKICK("SelfKick");
		
		private final String name;
		
		private Mode(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
