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
 * Allows opening GUIs while in portals.
 * Uses EntityPlayerSPMixin to reset portal state.
 */
public final class PortalGuiHack extends Hack
{
	public PortalGuiHack()
	{
		super("PortalGUI", "Allows you to open GUIs while in portals.");
		setCategory(Category.OTHER);
	}
}
