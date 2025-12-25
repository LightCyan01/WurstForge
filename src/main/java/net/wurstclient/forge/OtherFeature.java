/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.wurstclient.forge.settings.Setting;

/**
 * Base class for "Other Features" - features that appear in the settings
 * but aren't toggleable hacks (like WurstLogo visibility, HackList settings, etc.)
 */
public abstract class OtherFeature
{
	protected static final ForgeWurst WURST = ForgeWurst.getForgeWurst();
	
	private final String name;
	private final String description;
	private final LinkedHashMap<String, Setting> settings = new LinkedHashMap<>();
	
	public OtherFeature(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getDescription()
	{
		return description;
	}
	
	public final Map<String, Setting> getSettings()
	{
		return Collections.unmodifiableMap(settings);
	}
	
	protected final void addSetting(Setting setting)
	{
		String key = setting.getName().toLowerCase();
		
		if(settings.containsKey(key))
			throw new IllegalArgumentException(
				"Duplicate setting: " + name + " " + key);
		
		settings.put(key, setting);
	}
}
