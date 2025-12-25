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

/**
 * Removes the slowdown effect when walking through webs.
 * Uses BlockWebMixin to cancel web collision slowdown.
 */
public final class NoSlowdownHack extends Hack
{
	public NoSlowdownHack()
	{
		super("NoSlowdown",
			"Cancels slowdown effects from cobwebs.");
		setCategory(Category.MOVEMENT);
	}
}
