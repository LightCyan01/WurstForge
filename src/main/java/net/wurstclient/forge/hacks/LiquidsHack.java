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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Lets you interact with liquids as if they were solid blocks.
 */
public final class LiquidsHack extends Hack
{
	public LiquidsHack()
	{
		super("Liquids", "Allows you to place blocks on liquids.");
		setCategory(Category.BLOCKS);
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
		
		RayTraceResult result = mc.objectMouseOver;
		if(result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
			return;
		
		BlockPos pos = result.getBlockPos();
		Material material = WMinecraft.getWorld().getBlockState(pos).getMaterial();
		
		// Check if looking at liquid
		if(material != Material.WATER && material != Material.LAVA)
			return;
		
		// Place block on top of liquid
		mc.playerController.processRightClickBlock(
			WMinecraft.getPlayer(),
			WMinecraft.getWorld(),
			pos,
			EnumFacing.UP,
			result.hitVec,
			EnumHand.MAIN_HAND);
		
		WMinecraft.getPlayer().swingArm(EnumHand.MAIN_HAND);
	}
}
