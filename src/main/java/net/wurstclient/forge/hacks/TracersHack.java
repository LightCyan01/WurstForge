/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import org.lwjgl.opengl.GL11;

/**
 * Draws tracers to entities.
 */
public final class TracersHack extends Hack
{
	private final CheckboxSetting players = new CheckboxSetting("Players",
		"Draw tracers to players.", true);
	private final CheckboxSetting monsters = new CheckboxSetting("Monsters",
		"Draw tracers to monsters.", false);
	private final CheckboxSetting animals = new CheckboxSetting("Animals",
		"Draw tracers to animals.", false);
	
	public TracersHack()
	{
		super("Tracers", "Draws lines to entities.");
		setCategory(Category.RENDER);
		addSetting(players);
		addSetting(monsters);
		addSetting(animals);
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
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		if(WMinecraft.getPlayer() == null)
			return;
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(1.5F);
		
		GL11.glPushMatrix();
		GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX,
			-TileEntityRendererDispatcher.staticPlayerY,
			-TileEntityRendererDispatcher.staticPlayerZ);
		
		Vec3d start = WMinecraft.getPlayer().getPositionEyes(event.getPartialTicks());
		
		for(Entity entity : WMinecraft.getWorld().loadedEntityList)
		{
			if(entity == WMinecraft.getPlayer())
				continue;
			
			// Check entity type filters
			boolean shouldDraw = false;
			float r = 1, g = 1, b = 1;
			
			if(entity instanceof EntityPlayer && players.isChecked())
			{
				shouldDraw = true;
				r = 1; g = 0; b = 0; // Red for players
			}
			else if(entity instanceof IMob && monsters.isChecked())
			{
				shouldDraw = true;
				r = 1; g = 0.5f; b = 0; // Orange for monsters
			}
			else if(entity instanceof EntityAnimal && animals.isChecked())
			{
				shouldDraw = true;
				r = 0; g = 1; b = 0; // Green for animals
			}
			
			if(!shouldDraw)
				continue;
			
			Vec3d end = entity.getPositionVector().add(0, entity.getEyeHeight() / 2, 0);
			
			// Fade based on distance
			float distance = WMinecraft.getPlayer().getDistance(entity);
			float alpha = Math.max(0.2f, 1.0f - distance / 64f);
			
			GL11.glColor4f(r, g, b, alpha);
			GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3d(start.x, start.y, start.z);
			GL11.glVertex3d(end.x, end.y, end.z);
			GL11.glEnd();
		}
		
		GL11.glPopMatrix();
		
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
}
