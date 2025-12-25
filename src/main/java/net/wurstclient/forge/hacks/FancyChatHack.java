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
import net.wurstclient.fmlevents.WChatOutputEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;

/**
 * Converts your chat messages to fancy unicode characters.
 */
public final class FancyChatHack extends Hack
{
	private final String blacklist = "(){}[]|";
	
	public FancyChatHack()
	{
		super("FancyChat", "Makes your chat messages look fancy.");
		setCategory(Category.CHAT);
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
	public void onChatOutput(WChatOutputEvent event)
	{
		String message = event.getOriginalMessage();
		if(message.startsWith("/") || message.startsWith("."))
			return;
		
		String newMessage = convertString(message);
		event.setMessage(newMessage);
	}
	
	private String convertString(String input)
	{
		StringBuilder output = new StringBuilder();
		for(char c : input.toCharArray())
			output.append(convertChar(c));
		
		return output.toString();
	}
	
	private String convertChar(char c)
	{
		if(c < 0x21 || c > 0x80)
			return "" + c;
		
		if(blacklist.contains(Character.toString(c)))
			return "" + c;
		
		// Convert to fullwidth unicode characters
		return new String(Character.toChars(c + 0xfee0));
	}
}
