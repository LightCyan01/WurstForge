/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;

/**
 * Displays coordinates on screen.
 */
public final class CoordsHack extends Hack
{
	private final CheckboxSetting nether = new CheckboxSetting("Nether coords",
		"Also shows corresponding nether/overworld coordinates.", true);
	
	public CoordsHack()
	{
		super("Coords", "Displays your coordinates on screen.");
		setCategory(Category.RENDER);
		addSetting(nether);
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
	
	@SubscribeEvent
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		int x = (int)WMinecraft.getPlayer().posX;
		int y = (int)WMinecraft.getPlayer().posY;
		int z = (int)WMinecraft.getPlayer().posZ;
		
		String coords = String.format("XYZ: %d, %d, %d", x, y, z);
		
		ScaledResolution sr = new ScaledResolution(mc);
		mc.fontRenderer.drawStringWithShadow(coords, 2, sr.getScaledHeight() - 20, 0xFFFFFF);
		
		if(nether.isChecked())
		{
			boolean inNether = WMinecraft.getPlayer().dimension == -1;
			int netherX = inNether ? x * 8 : x / 8;
			int netherZ = inNether ? z * 8 : z / 8;
			String netherCoords = String.format("%s: %d, %d, %d", 
				inNether ? "Overworld" : "Nether", netherX, y, netherZ);
			mc.fontRenderer.drawStringWithShadow(netherCoords, 2, sr.getScaledHeight() - 10, 0xAAAAAA);
		}
	}
}
