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
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Makes nametags bigger and visible through walls.
 */
public final class NameTagsHack extends Hack
{
	private final CheckboxSetting unlimitedRange = new CheckboxSetting(
		"Unlimited Range", "Removes the distance limit for nametags.", true);
	private final CheckboxSetting seeThrough = new CheckboxSetting(
		"See Through", "Makes nametags visible through walls.", true);
	private final SliderSetting scale = new SliderSetting("Scale",
		"Nametag size multiplier.", 1.0, 0.5, 3.0, 0.1, ValueDisplay.DECIMAL);
	
	public NameTagsHack()
	{
		super("NameTags", "Makes nametags bigger and visible through walls.");
		setCategory(Category.RENDER);
		addSetting(unlimitedRange);
		addSetting(seeThrough);
		addSetting(scale);
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
	
	public boolean isUnlimitedRange()
	{
		return isEnabled() && unlimitedRange.isChecked();
	}
	
	public boolean isSeeThrough()
	{
		return isEnabled() && seeThrough.isChecked();
	}
	
	public double getScale()
	{
		return isEnabled() ? scale.getValue() : 1.0;
	}
}
