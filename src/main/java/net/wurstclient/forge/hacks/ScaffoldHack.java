/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class ScaffoldHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"Placement range.", 4, 1, 6, 0.5, ValueDisplay.DECIMAL);
	
	private final CheckboxSetting tower = new CheckboxSetting("Tower",
		"Build upward quickly while jumping.", true);
	
	private final CheckboxSetting rotate = new CheckboxSetting("Rotate",
		"Rotate towards block placement.", false);
	
	public ScaffoldHack()
	{
		super("Scaffold", "Automatically places blocks below you.");
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(tower);
		addSetting(rotate);
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
		
		BlockPos below = new BlockPos(player.posX, player.posY - 1, player.posZ);
		
		if(!WMinecraft.getWorld().isAirBlock(below))
			return;
		
		int blockSlot = findBlock();
		if(blockSlot == -1)
			return;
		
		int oldSlot = player.inventory.currentItem;
		player.inventory.currentItem = blockSlot;
		
		BlockPos placeOn = findPlaceablePosition(below);
		if(placeOn != null)
		{
			EnumFacing facing = getFacing(below, placeOn);
			Vec3d hitVec = new Vec3d(placeOn).add(0.5, 0.5, 0.5);
			
			mc.playerController.processRightClickBlock(player,
				WMinecraft.getWorld(), placeOn, facing, hitVec, EnumHand.MAIN_HAND);
			player.swingArm(EnumHand.MAIN_HAND);
		}
		
		player.inventory.currentItem = oldSlot;
		
		if(tower.isChecked() && mc.gameSettings.keyBindJump.isKeyDown() &&
			!WMinecraft.getWorld().isAirBlock(below))
		{
			player.motionY = 0.42;
			player.motionX *= 0.3;
			player.motionZ *= 0.3;
		}
	}
	
	private int findBlock()
	{
		for(int i = 0; i < 9; i++)
		{
			ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
			{
				Block block = ((ItemBlock)stack.getItem()).getBlock();
				if(block.getDefaultState().isFullBlock())
					return i;
			}
		}
		return -1;
	}
	
	private BlockPos findPlaceablePosition(BlockPos target)
	{
		EnumFacing[] facings = {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH,
			EnumFacing.EAST, EnumFacing.WEST, EnumFacing.UP};
		
		for(EnumFacing facing : facings)
		{
			BlockPos neighbor = target.offset(facing);
			IBlockState state = WMinecraft.getWorld().getBlockState(neighbor);
			
			if(!state.getMaterial().isReplaceable())
				return neighbor;
		}
		return null;
	}
	
	private EnumFacing getFacing(BlockPos target, BlockPos placeOn)
	{
		return EnumFacing.getFacingFromVector(
			(float)(target.getX() - placeOn.getX()),
			(float)(target.getY() - placeOn.getY()),
			(float)(target.getZ() - placeOn.getZ()));
	}
}
