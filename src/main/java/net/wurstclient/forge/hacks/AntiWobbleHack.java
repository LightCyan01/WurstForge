/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.init.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Removes the wobble/nausea effect from portals and potions.
 */
public final class AntiWobbleHack extends Hack
{
	public AntiWobbleHack()
	{
		super("AntiWobble", "Removes the nausea/wobble effect from portals.");
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
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		// Remove nausea effect to prevent wobble
		if(WMinecraft.getPlayer().isPotionActive(MobEffects.NAUSEA))
			WMinecraft.getPlayer().removeActivePotionEffect(MobEffects.NAUSEA);
		
		// Reset portal time to prevent portal wobble
		WMinecraft.getPlayer().prevTimeInPortal = 0;
		WMinecraft.getPlayer().timeInPortal = 0;
	}
}
