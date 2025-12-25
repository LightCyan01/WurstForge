/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;


public final class AirPlaceHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"How far you can place blocks.", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	public AirPlaceHack()
	{
		super("AirPlace", "Allows you to place blocks in mid-air.");
		setCategory(Category.BLOCKS);
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
		if(WMinecraft.getPlayer() == null)
			return;
		
		if(!mc.gameSettings.keyBindUseItem.isKeyDown())
			return;
		
		if(WMinecraft.getPlayer().getHeldItemMainhand().isEmpty())
			return;
		
		// Check if looking at air (no block hit)
		RayTraceResult result = mc.objectMouseOver;
		if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
			return;
		
		// Calculate position to place block
		Vec3d eyePos = WMinecraft.getPlayer().getPositionEyes(1.0F);
		Vec3d lookVec = WMinecraft.getPlayer().getLookVec();
		double r = range.getValue();
		
		Vec3d targetPos = eyePos.add(lookVec.x * r, lookVec.y * r, lookVec.z * r);
		BlockPos blockPos = new BlockPos(targetPos);
		
		// Check if the position is air
		if(WMinecraft.getWorld().getBlockState(blockPos).getMaterial() != Material.AIR)
			return;
		
		// Try to place block
		mc.playerController.processRightClickBlock(
			WMinecraft.getPlayer(),
			WMinecraft.getWorld(),
			blockPos,
			EnumFacing.UP,
			targetPos,
			EnumHand.MAIN_HAND);
		
		WMinecraft.getPlayer().swingArm(EnumHand.MAIN_HAND);
	}
	
	public double getRange()
	{
		return range.getValue();
	}
}
