/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;

/**
 * Automatically jumps when you reach the edge of a block,
 * making parkour easier.
 */
public final class ParkourHack extends Hack
{
	private final SliderSetting minDepth = new SliderSetting("Min depth",
		"Won't jump over a pit if it isn't at least this deep.\n"
			+ "Increase to stop Parkour from jumping down stairs.\n"
			+ "Decrease to make Parkour jump at the edge of carpets.",
		0.5, 0.05, 10, 0.05, SliderSetting.ValueDisplay.DECIMAL);
	
	private final SliderSetting edgeDistance = new SliderSetting("Edge distance",
		"How close Parkour will let you get to the edge before jumping.",
		0.001, 0.001, 0.25, 0.001, SliderSetting.ValueDisplay.DECIMAL);
	
	private final CheckboxSetting sneakOption = new CheckboxSetting("Jump while sneaking",
		"Keeps Parkour active even while you are sneaking.", false);
	
	public ParkourHack()
	{
		super("Parkour", "Automatically jumps at edges for easier parkour.");
		setCategory(Category.MOVEMENT);
		addSetting(minDepth);
		addSetting(edgeDistance);
		addSetting(sneakOption);
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
	public void onUpdate(WUpdateEvent event)
	{
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null)
			return;
		
		if(!player.onGround || mc.gameSettings.keyBindJump.isKeyDown())
			return;
		
		if(!sneakOption.isChecked() && player.isSneaking())
			return;
		
		AxisAlignedBB box = player.getEntityBoundingBox();
		AxisAlignedBB adjustedBox = box.expand(0, -minDepth.getValue(), 0)
			.grow(-edgeDistance.getValue(), 0, -edgeDistance.getValue());
		
		// Check if there's no collision (meaning we're at an edge)
		if(!mc.world.getCollisionBoxes(player, adjustedBox).isEmpty())
			return;
		
		// Jump!
		player.jump();
	}
}
