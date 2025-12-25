/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public final class ClickGuiKeybind
{
	public static final KeyBinding OPEN_CLICKGUI = new KeyBinding(
		"key.wurstforge.clickgui",
		Keyboard.KEY_RSHIFT,
		"key.categories.wurstforge");
	
	private final ForgeWurst wurst;
	
	public ClickGuiKeybind(ForgeWurst wurst)
	{
		this.wurst = wurst;
	}
	
	public void register()
	{
		ClientRegistry.registerKeyBinding(OPEN_CLICKGUI);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if(OPEN_CLICKGUI.isPressed())
			wurst.getHax().clickGuiHack.setEnabled(true);
	}
}
