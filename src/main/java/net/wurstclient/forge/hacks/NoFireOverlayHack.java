/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

/**
 * Removes fire overlay from screen.
 */
public final class NoFireOverlayHack extends Hack
{
	public NoFireOverlayHack()
	{
		super("NoFireOverlay", "Removes the fire overlay when you're on fire.");
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
		if(event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)
			event.setCanceled(true);
	}
}
