/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.settings;

import java.awt.Color;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.clickgui.ColorComponent;
import net.wurstclient.forge.clickgui.Component;

public final class ColorSetting extends Setting
{
	private Color color;
	private final Color defaultColor;
	
	public ColorSetting(String name, String description, Color color)
	{
		super(name, description);
		this.color = Objects.requireNonNull(color);
		defaultColor = color;
	}
	
	public ColorSetting(String name, Color color)
	{
		this(name, null, color);
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public float[] getColorF()
	{
		float red = color.getRed() / 255F;
		float green = color.getGreen() / 255F;
		float blue = color.getBlue() / 255F;
		return new float[]{red, green, blue};
	}
	
	public int getColorI()
	{
		return color.getRGB() | 0xFF000000;
	}
	
	public int getColorI(int alpha)
	{
		return color.getRGB() & 0x00FFFFFF | alpha << 24;
	}
	
	public int getColorI(float alpha)
	{
		int a = (int)(Math.max(0, Math.min(1, alpha)) * 255);
		return getColorI(a);
	}
	
	public int getRed()
	{
		return color.getRed();
	}
	
	public int getGreen()
	{
		return color.getGreen();
	}
	
	public int getBlue()
	{
		return color.getBlue();
	}
	
	public Color getDefaultColor()
	{
		return defaultColor;
	}
	
	public void setColor(Color color)
	{
		this.color = Objects.requireNonNull(color);
		ForgeWurst.getForgeWurst().getHax().saveSettings();
	}
	
	@Override
	public Component getComponent()
	{
		return new ColorComponent(this);
	}
	
	@Override
	public void fromJson(JsonElement json)
	{
		if(!json.isJsonPrimitive())
			return;
		
		JsonPrimitive primitive = json.getAsJsonPrimitive();
		if(!primitive.isString())
			return;
		
		try
		{
			setColor(parseHex(primitive.getAsString()));
		}catch(Exception e)
		{
			setColor(defaultColor);
		}
	}
	
	@Override
	public JsonElement toJson()
	{
		return new JsonPrimitive(toHex(color));
	}
	
	private static Color parseHex(String hex)
	{
		if(hex.startsWith("#"))
			hex = hex.substring(1);
		
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		
		return new Color(r, g, b);
	}
	
	private static String toHex(Color color)
	{
		return String.format("#%02X%02X%02X", 
			color.getRed(), color.getGreen(), color.getBlue());
	}
}
