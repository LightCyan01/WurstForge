/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

/**
 * Instantly disables all enabled hacks. Useful for quickly going "legit"
 * when someone is watching or an admin shows up.
 */
public final class PanicHack extends Hack
{
	public PanicHack()
	{
		super("Panic", "Instantly disables all enabled hacks.\n\n"
			+ "Useful for quickly going legit when\n"
			+ "someone is watching.");
		setCategory(Category.OTHER);
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
		// Disable all other hacks
		for(Hack hack : wurst.getHax().getRegistry())
		{
			if(hack.isEnabled() && hack != this)
				hack.setEnabled(false);
		}
		
		// Then disable ourselves
		setEnabled(false);
	}
}
