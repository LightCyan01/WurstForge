/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Extends your reach distance.
 * Uses PlayerControllerMPMixin to modify reach distance.
 */
public final class ReachHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"Attack and build reach range.", 6, 1, 10, 0.05, ValueDisplay.DECIMAL);
	
	public ReachHack()
	{
		super("Reach", "Gives you a longer reach distance.");
		setCategory(Category.OTHER);
		addSetting(range);
	}
	
	public double getReachDistance()
	{
		return range.getValue();
	}
}
