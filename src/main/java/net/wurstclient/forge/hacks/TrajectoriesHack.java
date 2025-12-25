/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.ColorSetting;

import java.awt.Color;

public final class TrajectoriesHack extends Hack
{
	private final ColorSetting color = new ColorSetting("Color",
		"Trajectory line color.", Color.GREEN);
	
	public TrajectoriesHack()
	{
		super("Trajectories", "Shows where projectiles will land.");
		setCategory(Category.RENDER);
		addSetting(color);
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
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return;
		
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.isEmpty())
			return;
		
		if(!isThrowable(stack))
			return;
		
		ArrayList<Vec3d> path = calculatePath(player, stack);
		if(path.isEmpty())
			return;
		
		GL11.glPushMatrix();
		GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX,
			-TileEntityRendererDispatcher.staticPlayerY,
			-TileEntityRendererDispatcher.staticPlayerZ);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(2.0F);
		
		Color c = color.getColor();
		GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.75f);
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(Vec3d point : path)
			GL11.glVertex3d(point.x, point.y, point.z);
		GL11.glEnd();
		
		if(!path.isEmpty())
		{
			Vec3d end = path.get(path.size() - 1);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			for(int i = 0; i < 16; i++)
			{
				double angle = Math.PI * 2 * i / 16;
				GL11.glVertex3d(end.x + Math.cos(angle) * 0.3,
					end.y + 0.01,
					end.z + Math.sin(angle) * 0.3);
			}
			GL11.glEnd();
		}
		
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private boolean isThrowable(ItemStack stack)
	{
		return stack.getItem() instanceof ItemBow
			|| stack.getItem() instanceof ItemSnowball
			|| stack.getItem() instanceof ItemEgg
			|| stack.getItem() instanceof ItemEnderPearl
			|| stack.getItem() instanceof ItemPotion
			|| stack.getItem() instanceof ItemFishingRod;
	}
	
	private ArrayList<Vec3d> calculatePath(EntityPlayerSP player, ItemStack stack)
	{
		ArrayList<Vec3d> path = new ArrayList<>();
		
		double yaw = Math.toRadians(player.rotationYaw);
		double pitch = Math.toRadians(player.rotationPitch);
		
		double x = player.posX;
		double y = player.posY + player.getEyeHeight();
		double z = player.posZ;
		
		double motionX = -Math.sin(yaw) * Math.cos(pitch);
		double motionY = -Math.sin(pitch);
		double motionZ = Math.cos(yaw) * Math.cos(pitch);
		
		double velocity = getVelocity(stack);
		motionX *= velocity;
		motionY *= velocity;
		motionZ *= velocity;
		
		double gravity = getGravity(stack);
		
		for(int i = 0; i < 200; i++)
		{
			path.add(new Vec3d(x, y, z));
			
			x += motionX;
			y += motionY;
			z += motionZ;
			
			motionY -= gravity;
			
			double drag = 0.99;
			motionX *= drag;
			motionY *= drag;
			motionZ *= drag;
			
			RayTraceResult result = WMinecraft.getWorld().rayTraceBlocks(
				new Vec3d(x - motionX, y - motionY, z - motionZ),
				new Vec3d(x, y, z));
			
			if(result != null)
			{
				path.add(new Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z));
				break;
			}
			
			if(y < -64)
				break;
		}
		
		return path;
	}
	
	private double getVelocity(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBow)
			return 3.0;
		if(stack.getItem() instanceof ItemPotion)
			return 0.5;
		if(stack.getItem() instanceof ItemFishingRod)
			return 1.5;
		return 1.5;
	}
	
	private double getGravity(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBow)
			return 0.05;
		if(stack.getItem() instanceof ItemPotion)
			return 0.05;
		return 0.03;
	}
}
