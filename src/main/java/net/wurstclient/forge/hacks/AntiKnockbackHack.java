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
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Reduces knockback when hit by entities.
 */
public final class AntiKnockbackHack extends Hack
{
	private final SliderSetting hStrength = new SliderSetting(
		"Horizontal Strength",
		"How much horizontal knockback to reduce.\n"
			+ "100% = no knockback at all",
		1, 0, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	private final SliderSetting vStrength = new SliderSetting(
		"Vertical Strength",
		"How much vertical knockback to reduce.\n"
			+ "100% = no knockback at all",
		1, 0, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	public AntiKnockbackHack()
	{
		super("AntiKnockback",
			"Reduces knockback when attacked.\n"
				+ "Also known as Velocity or Anti-KB.");
		setCategory(Category.COMBAT);
		addSetting(hStrength);
		addSetting(vStrength);
	}
	
	public double getHStrength()
	{
		return isEnabled() ? hStrength.getValue() : 0;
	}
	
	public double getVStrength()
	{
		return isEnabled() ? vStrength.getValue() : 0;
	}
}
