/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Makes bows shoot instantly.
 */
public final class FastBowHack extends Hack
{
	public FastBowHack()
	{
		super("FastBow", "Makes bows shoot instantly at full power.");
		setCategory(Category.COMBAT);
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
		
		if(!WMinecraft.getPlayer().isHandActive())
			return;
		
		ItemStack stack = WMinecraft.getPlayer().getActiveItemStack();
		if(stack.isEmpty() || !(stack.getItem() instanceof ItemBow))
			return;
		
		int useCount = WMinecraft.getPlayer().getItemInUseMaxCount();
		if(useCount < 3)
			return;
		
		// Release bow immediately
		WMinecraft.getPlayer().connection.sendPacket(
			new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
				BlockPos.ORIGIN, EnumFacing.DOWN));
		
		mc.playerController.onStoppedUsingItem(WMinecraft.getPlayer());
	}
}
