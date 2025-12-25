/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

/**
 * Removes fog rendering.
 */
public final class NoFogHack extends Hack
{
	public NoFogHack()
	{
		super("NoFog", "Removes fog rendering.");
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
	public void onFogDensity(EntityViewRenderEvent.FogDensity event)
	{
		event.setDensity(0);
		event.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onFogColors(EntityViewRenderEvent.FogColors event)
	{
		// Keep colors but no fog
	}
}
