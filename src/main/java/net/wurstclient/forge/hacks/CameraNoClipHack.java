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
 * Allows the third-person camera to go through blocks.
 * Uses EntityRendererMixin to modify camera clipping distance.
 */
public final class CameraNoClipHack extends Hack
{
	private final SliderSetting distance = new SliderSetting("Distance",
		"Maximum camera distance.", 4, 1, 10, 0.5, ValueDisplay.DECIMAL);
	
	public CameraNoClipHack()
	{
		super("CameraNoClip", 
			"Allows the third-person camera to move through blocks.");
		setCategory(Category.RENDER);
		addSetting(distance);
	}
	
	public double getDistance()
	{
		return distance.getValue();
	}
}
