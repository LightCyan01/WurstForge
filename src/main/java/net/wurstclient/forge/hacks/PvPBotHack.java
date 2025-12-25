/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEntity;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.RotationUtils;

/**
 * Automatically attacks players in PvP.
 */
public final class PvPBotHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"Attack range.", 4, 1, 6, 0.1, ValueDisplay.DECIMAL);
	
	public PvPBotHack()
	{
		super("PvPBot", "Automatically attacks nearby players.");
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
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return;
		
		if(player.getCooledAttackStrength(0) < 1)
			return;
		
		double rangeSq = Math.pow(range.getValue(), 2);
		EntityPlayer closest = null;
		double closestDist = Double.MAX_VALUE;
		
		for(Entity entity : WMinecraft.getWorld().loadedEntityList)
		{
			if(!(entity instanceof EntityPlayer))
				continue;
			if(entity == player)
				continue;
			
			EntityPlayer other = (EntityPlayer)entity;
			if(other.isDead || other.getHealth() <= 0)
				continue;
			
			double dist = WEntity.getDistanceSq(player, other);
			if(dist < rangeSq && dist < closestDist)
			{
				closest = other;
				closestDist = dist;
			}
		}
		
		if(closest != null)
		{
			RotationUtils.faceVectorPacket(closest.getEntityBoundingBox().getCenter());
			mc.playerController.attackEntity(player, closest);
			player.swingArm(EnumHand.MAIN_HAND);
		}
	}
}
