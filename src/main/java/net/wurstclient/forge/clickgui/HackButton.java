/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.clickgui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.wurstclient.forge.ForgeWurst;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.KeybindList;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.Setting;

public final class HackButton extends Component
{
	private final Hack hack;
	private Window settingsWindow;
	private boolean waitingForKeybind = false;
	
	public HackButton(Hack hack)
	{
		this.hack = hack;
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}
	
	@Override
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton)
	{
		// Middle click to set keybind
		if(mouseButton == 2)
		{
			waitingForKeybind = true;
			return;
		}
		
		// Right click to remove keybind
		if(mouseButton == 1)
		{
			String currentKey = getKeybindKey();
			if(currentKey != null)
			{
				ForgeWurst.getForgeWurst().getKeybinds().remove(currentKey);
			}
			return;
		}
		
		if(mouseButton != 0)
			return;
		
		if(!hack.getSettings().isEmpty() && mouseX > getX() + getWidth() - 12)
		{
			if(settingsWindow != null && !settingsWindow.isClosing())
			{
				settingsWindow.close();
				settingsWindow = null;
				return;
			}
			
			settingsWindow = new Window(hack.getName() + " Settings");
			for(Setting setting : hack.getSettings().values())
				settingsWindow.add(setting.getComponent());
			
			settingsWindow.setClosable(true);
			settingsWindow.setMinimizable(false);
			settingsWindow.pack();
			
			int scroll = getParent().isScrollingEnabled()
				? getParent().getScrollOffset() : 0;
			int x = getParent().getX() + getParent().getWidth() + 5;
			int y = getParent().getY() + 12 + getY() + scroll;
			ScaledResolution sr =
				new ScaledResolution(Minecraft.getMinecraft());
			if(x + settingsWindow.getWidth() > sr.getScaledWidth())
				x = getParent().getX() - settingsWindow.getWidth() - 5;
			if(y + settingsWindow.getHeight() > sr.getScaledHeight())
				y -= settingsWindow.getHeight() - 14;
			settingsWindow.setX(x);
			settingsWindow.setY(y);
			
			ClickGui gui = ForgeWurst.getForgeWurst().getGui();
			gui.addWindow(settingsWindow);
			return;
		}
		
		hack.setEnabled(!hack.isEnabled());
	}
	
	/**
	 * Called when a key is pressed while this button is waiting for a keybind.
	 * Returns true if the key was consumed.
	 */
	public boolean handleKeyPress(int keyCode)
	{
		if(!waitingForKeybind)
			return false;
		
		waitingForKeybind = false;
		
		// Escape cancels
		if(keyCode == Keyboard.KEY_ESCAPE)
			return true;
		
		// Delete/Backspace removes the keybind
		if(keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK)
		{
			String currentKey = getKeybindKey();
			if(currentKey != null)
				ForgeWurst.getForgeWurst().getKeybinds().remove(currentKey);
			return true;
		}
		
		String keyName = Keyboard.getKeyName(keyCode);
		if(keyName == null || keyName.isEmpty())
			return true;
		
		// Remove old keybind for this hack
		String currentKey = getKeybindKey();
		if(currentKey != null)
			ForgeWurst.getForgeWurst().getKeybinds().remove(currentKey);
		
		// Add new keybind
		ForgeWurst.getForgeWurst().getKeybinds().add(keyName, hack.getName().toLowerCase());
		
		return true;
	}
	
	/**
	 * Returns the key bound to this hack, or null if none.
	 */
	private String getKeybindKey()
	{
		KeybindList keybinds = ForgeWurst.getForgeWurst().getKeybinds();
		String hackName = hack.getName().toLowerCase();
		
		for(int i = 0; i < keybinds.size(); i++)
		{
			KeybindList.Keybind kb = keybinds.get(i);
			// Check if this keybind toggles just this hack
			if(kb.getCommands().equalsIgnoreCase(hackName))
				return kb.getKey();
		}
		return null;
	}
	
	public boolean isWaitingForKeybind()
	{
		return waitingForKeybind;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		ClickGui gui = ForgeWurst.getForgeWurst().getGui();
		float[] bgColor = gui.getBgColor();
		float[] acColor = gui.getAcColor();
		float opacity = gui.getOpacity();
		boolean settings = !hack.getSettings().isEmpty();
		
		int x1 = getX();
		int x2 = x1 + getWidth();
		int x3 = settings ? x2 - 11 : x2;
		int y1 = getY();
		int y2 = y1 + getHeight();
		
		int scroll = getParent().isScrollingEnabled()
			? getParent().getScrollOffset() : 0;
		boolean hovering = mouseX >= x1 && mouseY >= y1 && mouseX < x2
			&& mouseY < y2 && mouseY >= -scroll
			&& mouseY < getParent().getHeight() - 13 - scroll;
		boolean hHack = hovering && mouseX < x3;
		boolean hSettings = hovering && mouseX >= x3;
		
		// Get current keybind
		String keybindKey = getKeybindKey();
		
		// tooltip
		if(hHack)
		{
			String tooltip = hack.getDescription();
			if(keybindKey != null)
				tooltip += "\n\nKeybind: [" + keybindKey + "]";
			else
				tooltip += "\n\nMiddle-click to set keybind";
			tooltip += "\nRight-click to remove keybind";
			gui.setTooltip(tooltip);
		}
		
		// color
		if(waitingForKeybind)
			GL11.glColor4f(1, 1, 0, opacity * 1.5F); // Yellow when waiting
		else if(hack.isEnabled())
			GL11.glColor4f(0, 1, 0, hHack ? opacity * 1.5F : opacity);
		else
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2],
				hHack ? opacity * 1.5F : opacity);
		
		// background
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x3, y2);
		GL11.glVertex2i(x3, y1);
		if(settings)
		{
			GL11.glColor4f(bgColor[0], bgColor[1], bgColor[2],
				hSettings ? opacity * 1.5F : opacity);
			GL11.glVertex2i(x3, y1);
			GL11.glVertex2i(x3, y2);
			GL11.glVertex2i(x2, y2);
			GL11.glVertex2i(x2, y1);
		}
		GL11.glEnd();
		
		// outline
		GL11.glColor4f(acColor[0], acColor[1], acColor[2], 0.5F);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2i(x1, y1);
		GL11.glVertex2i(x1, y2);
		GL11.glVertex2i(x2, y2);
		GL11.glVertex2i(x2, y1);
		GL11.glEnd();
		
		if(settings)
		{
			// separator
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex2i(x3, y1);
			GL11.glVertex2i(x3, y2);
			GL11.glEnd();
			
			double xa1 = x3 + 1;
			double xa2 = (x3 + x2) / 2.0;
			double xa3 = x2 - 1;
			double ya1;
			double ya2;
			
			if(settingsWindow != null && !settingsWindow.isClosing())
			{
				ya1 = y2 - 3.5;
				ya2 = y1 + 3;
				GL11.glColor4f(hSettings ? 1 : 0.85F, 0, 0, 1);
			}else
			{
				ya1 = y1 + 3.5;
				ya2 = y2 - 3;
				GL11.glColor4f(0, hSettings ? 1 : 0.85F, 0, 1);
			}
			
			// arrow
			GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2d(xa1, ya1);
			GL11.glVertex2d(xa3, ya1);
			GL11.glVertex2d(xa2, ya2);
			GL11.glEnd();
			
			// outline
			GL11.glColor4f(0.0625F, 0.0625F, 0.0625F, 0.5F);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2d(xa1, ya1);
			GL11.glVertex2d(xa3, ya1);
			GL11.glVertex2d(xa2, ya2);
			GL11.glEnd();
		}
		
		// hack name with keybind
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		FontRenderer fr = WMinecraft.getFontRenderer();
		
		String displayName = hack.getName();
		if(waitingForKeybind)
			displayName = "Press key...";
		else if(keybindKey != null)
			displayName = hack.getName() + " [" + keybindKey + "]";
		
		int availableWidth = settings ? getWidth() - 11 : getWidth();
		// Truncate if needed
		if(fr.getStringWidth(displayName) > availableWidth - 2)
			displayName = hack.getName();
		
		int fx = x1 + (availableWidth - fr.getStringWidth(displayName)) / 2;
		int fy = y1 + 2;
		fr.drawString(displayName, fx, fy, waitingForKeybind ? 0xffff00 : 0xf0f0f0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public int getDefaultWidth()
	{
		String keybindKey = getKeybindKey();
		String displayName = hack.getName();
		if(keybindKey != null)
			displayName = hack.getName() + " [" + keybindKey + "]";
		
		int width = WMinecraft.getFontRenderer().getStringWidth(displayName) + 2;
		if(!hack.getSettings().isEmpty())
			width += 11;
		return Math.max(width, WMinecraft.getFontRenderer().getStringWidth(hack.getName()) + 2 + (hack.getSettings().isEmpty() ? 0 : 11));
	}
	
	@Override
	public int getDefaultHeight()
	{
		return 11;
	}
	
	public Hack getHack()
	{
		return hack;
	}
}
