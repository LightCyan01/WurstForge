/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.other_features;

import java.awt.Color;
import java.util.function.BooleanSupplier;

import net.wurstclient.forge.OtherFeature;
import net.wurstclient.forge.settings.ColorSetting;
import net.wurstclient.forge.settings.EnumSetting;

public final class WurstLogoOtf extends OtherFeature
{
	private final ColorSetting bgColor = new ColorSetting("Background",
		"Background color for the Wurst logo.\n"
			+ "Only visible when RainbowUI is disabled.",
		Color.WHITE);
	
	private final ColorSetting txtColor =
		new ColorSetting("Text", "Text color for the Wurst logo.", Color.BLACK);
	
	private final EnumSetting<Visibility> visibility =
		new EnumSetting<>("Visibility",
			"Controls when the Wurst logo is shown on screen.",
			Visibility.values(), Visibility.ALWAYS);
	
	public WurstLogoOtf()
	{
		super("WurstLogo", "Shows the Wurst logo and version on the screen.");
		addSetting(visibility);
		addSetting(bgColor);
		addSetting(txtColor);
	}
	
	public boolean isVisible()
	{
		return visibility.getSelected().isVisible();
	}
	
	public int getBackgroundColor()
	{
		return bgColor.getColorI(128);
	}
	
	public int getTextColor()
	{
		return txtColor.getColorI();
	}
	
	public Color getBackgroundColorObj()
	{
		return bgColor.getColor();
	}
	
	public Color getTextColorObj()
	{
		return txtColor.getColor();
	}
	
	public static enum Visibility
	{
		ALWAYS("Always", () -> true),
		ONLY_OUTDATED("Only when outdated",
			() -> net.wurstclient.forge.ForgeWurst.getForgeWurst().getUpdater() != null 
				&& net.wurstclient.forge.ForgeWurst.getForgeWurst().getUpdater().isOutdated()),
		NEVER("Never", () -> false);
		
		private final String name;
		private final BooleanSupplier visible;
		
		private Visibility(String name, BooleanSupplier visible)
		{
			this.name = name;
			this.visible = visible;
		}
		
		public boolean isVisible()
		{
			return visible.getAsBoolean();
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
