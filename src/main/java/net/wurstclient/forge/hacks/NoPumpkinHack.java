/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Removes pumpkin overlay when wearing pumpkin.
 */
public final class NoPumpkinHack extends Hack
{
	public NoPumpkinHack()
	{
		super("NoPumpkin", "Removes the pumpkin overlay when wearing a pumpkin.");
		setCategory(Category.RENDER);
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
	public void onRenderBlockOverlay(RenderBlockOverlayEvent event)
	{
		if(event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK
			&& event.getBlockForOverlay().getBlock() == Blocks.PUMPKIN)
			event.setCanceled(true);
	}
}
