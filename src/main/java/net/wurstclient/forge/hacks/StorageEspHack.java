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

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WPlayer;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.utils.RenderUtils;

public final class StorageEspHack extends Hack
{
	private final CheckboxSetting chests = new CheckboxSetting("Chests", true);
	private final CheckboxSetting enderChests = new CheckboxSetting("Ender Chests", true);
	private final CheckboxSetting shulkers = new CheckboxSetting("Shulker Boxes", true);
	private final CheckboxSetting furnaces = new CheckboxSetting("Furnaces", false);
	private final CheckboxSetting dispensers = new CheckboxSetting("Dispensers", false);
	private final CheckboxSetting hoppers = new CheckboxSetting("Hoppers", false);
	
	private final ArrayList<BlockPos> chestList = new ArrayList<>();
	private final ArrayList<BlockPos> enderChestList = new ArrayList<>();
	private final ArrayList<BlockPos> shulkerList = new ArrayList<>();
	private final ArrayList<BlockPos> otherList = new ArrayList<>();
	
	private int boxList;
	
	public StorageEspHack()
	{
		super("StorageESP", "Highlights all storage blocks.");
		setCategory(Category.RENDER);
		addSetting(chests);
		addSetting(enderChests);
		addSetting(shulkers);
		addSetting(furnaces);
		addSetting(dispensers);
		addSetting(hoppers);
	}
	
	@Override
	protected void onEnable()
	{
		MinecraftForge.EVENT_BUS.register(this);
		boxList = GL11.glGenLists(1);
		GL11.glNewList(boxList, GL11.GL_COMPILE);
		AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN);
		GL11.glBegin(GL11.GL_LINES);
		RenderUtils.drawOutlinedBox(bb);
		GL11.glEnd();
		GL11.glEndList();
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
		GL11.glDeleteLists(boxList, 1);
		boxList = 0;
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		World world = WPlayer.getWorld(event.getPlayer());
		
		chestList.clear();
		enderChestList.clear();
		shulkerList.clear();
		otherList.clear();
		
		for(TileEntity te : world.loadedTileEntityList)
		{
			BlockPos pos = te.getPos();
			
			if(chests.isChecked() && te instanceof TileEntityChest)
				chestList.add(pos);
			else if(enderChests.isChecked() && te instanceof TileEntityEnderChest)
				enderChestList.add(pos);
			else if(shulkers.isChecked() && te instanceof TileEntityShulkerBox)
				shulkerList.add(pos);
			else if(furnaces.isChecked() && te instanceof TileEntityFurnace)
				otherList.add(pos);
			else if(dispensers.isChecked() && (te instanceof TileEntityDispenser || te instanceof TileEntityDropper))
				otherList.add(pos);
			else if(hoppers.isChecked() && (te instanceof TileEntityHopper || te instanceof TileEntityBrewingStand))
				otherList.add(pos);
		}
	}
	
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(2);
		
		GL11.glPushMatrix();
		GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX,
			-TileEntityRendererDispatcher.staticPlayerY,
			-TileEntityRendererDispatcher.staticPlayerZ);
		
		GL11.glColor4f(1, 0.8f, 0, 0.5f);
		for(BlockPos pos : chestList)
			renderBox(pos);
		
		GL11.glColor4f(1, 0, 1, 0.5f);
		for(BlockPos pos : enderChestList)
			renderBox(pos);
		
		GL11.glColor4f(1, 0.4f, 0.7f, 0.5f);
		for(BlockPos pos : shulkerList)
			renderBox(pos);
		
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
		for(BlockPos pos : otherList)
			renderBox(pos);
		
		GL11.glPopMatrix();
		
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void renderBox(BlockPos pos)
	{
		GL11.glPushMatrix();
		GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
		GL11.glCallList(boxList);
		GL11.glPopMatrix();
	}
}
