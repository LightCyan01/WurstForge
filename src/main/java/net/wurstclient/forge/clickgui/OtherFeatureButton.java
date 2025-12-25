/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.clickgui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.OtherFeature;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.Setting;

/**
 * A button for OtherFeature items in the ClickGUI.
 * Unlike HackButton, these don't toggle - they just show settings.
 */
public final class OtherFeatureButton extends Component
{
	private final OtherFeature feature;
	private Window settingsWindow;
	
	public OtherFeatureButton(OtherFeature feature)
	{
		this.feature = feature;
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
	{
		if(mouseButton != 0)
			return;
		
		// OtherFeatures always have settings, clicking opens settings window
		if(settingsWindow != null && !settingsWindow.isClosing())
		{
			settingsWindow.close();
			settingsWindow = null;
			return;
		}
		
		if(feature.getSettings().isEmpty())
			return;
		
		settingsWindow = new Window(feature.getName() + " Settings");
		for(Setting setting : feature.getSettings().values())
			settingsWindow.add(setting.getComponent());
		
		settingsWindow.setClosable(true);
		settingsWindow.setMinimizable(false);
		settingsWindow.pack();
		
		int scroll = getParent().isScrollingEnabled()
			? getParent().getScrollOffset() : 0;
		int x = getParent().getX() + getParent().getWidth() + 5;
		int y = getParent().getY() + 12 + getY() + scroll;
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		if(x + settingsWindow.getWidth() > sr.getScaledWidth())
			x = getParent().getX() - settingsWindow.getWidth() - 5;
		if(y + settingsWindow.getHeight() > sr.getScaledHeight())
			y -= settingsWindow.getHeight() - 14;
		settingsWindow.setX(x);
		settingsWindow.setY(y);
		
		ClickGui gui = ForgeWurst.getForgeWurst().getGui();
		gui.addWindow(settingsWindow);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		ClickGui gui = ForgeWurst.getForgeWurst().getGui();
		float[] bgColor = gui.getBgColor();
		float[] acColor = gui.getAcColor();
		float opacity = gui.getOpacity();
		
		int x1 = getX();
		int x2 = x1 + getWidth();
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		int scroll = getParent().isScrollingEnabled()
			? getParent().getScrollOffset() : 0;
		boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2
			&& mouseY < y2 && mouseY >= -scroll
			&& mouseY < getParent().getHeight() - 13 - scroll;
		
		// tooltip
		if(hovering)
			gui.setTooltip(feature.getDescription() + "\n\nClick to open settings");
		
		// color - cyan/blue for OtherFeatures to differentiate from hacks
		boolean hasOpenWindow = settingsWindow != null && !settingsWindow.isClosing();
		if(hasOpenWindow)
			GL11.glColor4f(0.3F, 0.6F, 1.0F, hovering ? opacity * 1.5F : opacity);
		else
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2],
				hovering ? opacity * 1.5F : opacity);
		
		// background
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
		
		// outline
		GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
		
		// feature name with gear icon indicator
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		FontRenderer fr = WMinecraft.getFontRenderer();
		String displayName = feature.getName() + " [...]";
		int fx = x1 + (getWidth() - fr.getStringWidth(displayName)) / 2;
		int fy = y1 + 2;
		fr.drawString(displayName, fx, fy, hasOpenWindow ? 0xaaccff : 0xf0f0f0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public int getDefaultWidth()
	{
		String displayName = feature.getName() + " [...]";
		return WMinecraft.getFontRenderer().getStringWidth(displayName) + 4;
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}
	
	public OtherFeature getFeature()
	{
		return feature;
	}
}
