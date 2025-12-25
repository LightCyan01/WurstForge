/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Automatically mines the block you're looking at.
 */
public final class AutoMineHack extends Hack
{
	public AutoMineHack()
	{
		super("AutoMine", "Automatically mines the block you're looking at.");
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
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		RayTraceResult result = mc.objectMouseOver;
		if(result == null || result.typeOfHit != RayTraceResult.Type.BLOCK)
			return;
		
		// Press attack key using proper method
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
	}
}
