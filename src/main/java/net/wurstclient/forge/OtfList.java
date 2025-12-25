/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.wurstclient.forge.other_features.HackListOtf;
import net.wurstclient.forge.other_features.WurstLogoOtf;

/**
 * Contains all "Other Features" (OTFs) - features that appear in settings
 * but aren't toggleable hacks.
 */
public final class OtfList
{
	// Add all other features here
	public final WurstLogoOtf wurstLogoOtf = new WurstLogoOtf();
	public final HackListOtf hackListOtf = new HackListOtf();
	
	private final List<OtherFeature> otfs = new ArrayList<>();
	
	public OtfList()
	{
		// Register all OTFs
		otfs.add(wurstLogoOtf);
		otfs.add(hackListOtf);
	}
	
	public Collection<OtherFeature> getAllOtfs()
	{
		return Collections.unmodifiableList(otfs);
	}
}

