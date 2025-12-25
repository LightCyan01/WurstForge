/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.clickgui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.ColorSetting;

public final class ColorComponent extends Component
{
	private final ColorSetting setting;
	
	public ColorComponent(ColorSetting setting)
	{
		this.setting = setting;
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
	{
		if(mouseButton == 0)
		{
			// Cycle through preset colors
			Color current = setting.getColor();
			Color[] presets = {
				Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
				Color.CYAN, Color.MAGENTA, Color.WHITE, Color.BLACK,
				Color.ORANGE, Color.PINK
			};
			
			int nextIndex = 0;
			for(int i = 0; i < presets.length; i++)
			{
				if(colorsEqual(current, presets[i]))
				{
					nextIndex = (i + 1) % presets.length;
					break;
				}
			}
			setting.setColor(presets[nextIndex]);
		}
		else if(mouseButton == 1)
		{
			// Reset to default
			setting.setColor(setting.getDefaultColor());
		}
	}
	
	private boolean colorsEqual(Color a, Color b)
	{
		return a.getRed() == b.getRed() 
			&& a.getGreen() == b.getGreen() 
			&& a.getBlue() == b.getBlue();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		ClickGui gui = ForgeWurst.getForgeWurst().getGui();
		float[] bgColor = gui.getBgColor();
		float opacity = gui.getOpacity();
		
		int x1 = getX();
		int x2 = x1 + getWidth();
		int x3 = x1 + 11;
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		int scroll = getParent().isScrollingEnabled()
			? getParent().getScrollOffset() : 0;
		boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2
			&& mouseY < y2 && mouseY >= -scroll
			&& mouseY < getParent().getHeight() - 13 - scroll;
		
		// tooltip
		if(hovering)
		{
			String tooltip = setting.getDescription();
			if(tooltip == null || tooltip.isEmpty())
				tooltip = "Left-click to cycle colors, right-click to reset";
			gui.setTooltip(tooltip);
		}
		
		// background
		GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2], opacity);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x3, y1);
		GL11.glVertex2i(x3, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
		
		// color preview box
		float[] colorF = setting.getColorF();
		GL11.glColor4f(colorF[0], colorF[1], colorF[2], 1.0F);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x3, y2);
		GL11.glVertex2i(x3, y1);
		GL11.glEnd();
		
		// box outline
		GL11.glColor4f(0.0625F, 0.0625F, 0.0625F, 0.5F);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x3, y2);
		GL11.glVertex2i(x3, y1);
		GL11.glEnd();
		
		// setting name
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		FontRenderer fr = WMinecraft.getFontRenderer();
		fr.drawString(setting.getName(), x3 + 2, y1 + 2, 0xf0f0f0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public int getDefaultWidth()
	{
		return WMinecraft.getFontRenderer().getStringWidth(setting.getName())
			+ 13;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}
}
