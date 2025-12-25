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
import net.wurstclient.forge.compatibility.WMinecraft;

/**
 * Allows you to walk in inventories.
 */
public final class InvWalkHack extends Hack
{
	public InvWalkHack()
	{
		super("InvWalk", "Allows you to walk while inventory is open.");
		setCategory(Category.MOVEMENT);
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
		
		// If in GUI, allow movement
		if(mc.currentScreen != null)
		{
			// Set movement keys based on WASD
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindForward.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_W));
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindBack.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_S));
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindLeft.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_A));
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindRight.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_D));
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindJump.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_SPACE));
			net.minecraft.client.settings.KeyBinding.setKeyBindState(
				mc.gameSettings.keyBindSneak.getKeyCode(),
				org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LSHIFT));
		}
	}
}
