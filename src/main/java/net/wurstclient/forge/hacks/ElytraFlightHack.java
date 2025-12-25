/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.EnumSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class ElytraFlightHack extends Hack
{
	private final EnumSetting<Mode> mode = new EnumSetting<>("Mode",
		"Flight mode.\n" +
		"Normal - Standard elytra control.\n" +
		"Boost - Forward boost.",
		Mode.values(), Mode.NORMAL);
	
	private final SliderSetting speed = new SliderSetting("Speed",
		"Flying speed.", 1.5, 0.5, 5.0, 0.1, ValueDisplay.DECIMAL);
	
	private final SliderSetting glideSpeed = new SliderSetting("Glide Speed",
		"Vertical glide speed.", 0.0, -0.5, 0.5, 0.01, ValueDisplay.DECIMAL);
	
	public ElytraFlightHack()
	{
		super("ElytraFlight", "Enhanced elytra flying.");
		setCategory(Category.MOVEMENT);
		addSetting(mode);
		addSetting(speed);
		addSetting(glideSpeed);
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
		
		ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(chest.getItem() != Items.ELYTRA)
			return;
		
		if(!player.isElytraFlying())
		{
			if(player.onGround || player.isInWater())
				return;
			
			player.connection.sendPacket(
				new CPacketEntityAction(player, CPacketEntityAction.Action.START_FALL_FLYING));
			return;
		}
		
		switch(mode.getSelected())
		{
			case NORMAL:
				handleNormalMode(player);
				break;
			case BOOST:
				handleBoostMode(player);
				break;
		}
	}
	
	private void handleNormalMode(EntityPlayerSP player)
	{
		double speedVal = speed.getValue();
		Vec3d look = player.getLookVec();
		
		if(mc.gameSettings.keyBindForward.isKeyDown())
		{
			player.motionX = look.x * speedVal;
			player.motionZ = look.z * speedVal;
			player.motionY = look.y * speedVal + glideSpeed.getValue();
		}
		else
		{
			player.motionX = 0;
			player.motionZ = 0;
			player.motionY = glideSpeed.getValue();
		}
		
		if(mc.gameSettings.keyBindJump.isKeyDown())
			player.motionY = speedVal * 0.5;
		if(mc.gameSettings.keyBindSneak.isKeyDown())
			player.motionY = -speedVal * 0.5;
	}
	
	private void handleBoostMode(EntityPlayerSP player)
	{
		if(!mc.gameSettings.keyBindForward.isKeyDown())
			return;
		
		double speedVal = speed.getValue();
		Vec3d look = player.getLookVec().normalize();
		
		player.motionX += look.x * speedVal * 0.1;
		player.motionY += look.y * speedVal * 0.1;
		player.motionZ += look.z * speedVal * 0.1;
		
		double currentSpeed = Math.sqrt(player.motionX * player.motionX +
			player.motionY * player.motionY + player.motionZ * player.motionZ);
		
		if(currentSpeed > speedVal * 2)
		{
			double factor = speedVal * 2 / currentSpeed;
			player.motionX *= factor;
			player.motionY *= factor;
			player.motionZ *= factor;
		}
	}
	
	private enum Mode
	{
		NORMAL("Normal"),
		BOOST("Boost");
		
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
