/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.other_features;

import net.wurstclient.forge.OtherFeature;
import net.wurstclient.forge.settings.EnumSetting;

/**
 * Controls the visibility of the HackList (list of enabled hacks on screen).
 */
public final class HackListOtf extends OtherFeature
{
	private final EnumSetting<Mode> mode = new EnumSetting<>("Mode",
		"Auto mode shows the list if it fits.\n"
			+ "Count mode only shows the number of active hacks.\n"
			+ "Hidden mode hides the list completely.",
		Mode.values(), Mode.AUTO);
	
	private final EnumSetting<Position> position = new EnumSetting<>("Position",
		"Which side of the screen the HackList is shown on.",
		Position.values(), Position.LEFT);
	
	public HackListOtf()
	{
		super("HackList", "Controls the list of enabled hacks shown on screen.");
		addSetting(mode);
		addSetting(position);
	}
	
	public Mode getMode()
	{
		return mode.getSelected();
	}
	
	public Position getPosition()
	{
		return position.getSelected();
	}
	
	public boolean isVisible()
	{
		return mode.getSelected() != Mode.HIDDEN;
	}
	
	public boolean isCountMode()
	{
		return mode.getSelected() == Mode.COUNT;
	}
	
	public boolean isLeftSide()
	{
		return position.getSelected() == Position.LEFT;
	}
	
	public static enum Mode
	{
		AUTO("Auto"),
		COUNT("Count"),
		HIDDEN("Hidden");
		
		private final String name;
		
		private Mode(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
	
	public static enum Position
	{
		LEFT("Left"),
		RIGHT("Right");
		
		private final String name;
		
		private Position(String name)
		{
			this.name = name;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
