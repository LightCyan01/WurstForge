/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

/**
 * Automatically attacks whatever you're looking at.
 */
public final class TriggerBotHack extends Hack
{
	private final SliderSetting range = new SliderSetting("Range",
		"Maximum attack range.", 4.25, 1, 6, 0.05, ValueDisplay.DECIMAL);
	private final SliderSetting cooldown = new SliderSetting("Cooldown",
		"Minimum attack cooldown (0 = attack at full strength only).",
		0.9, 0, 1, 0.01, ValueDisplay.PERCENTAGE);
	
	public TriggerBotHack()
	{
		super("TriggerBot", "Attacks whatever you're looking at.");
		setCategory(Category.COMBAT);
		addSetting(range);
		addSetting(cooldown);
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
		if(WMinecraft.getPlayer() == null)
			return;
		
		// Check cooldown
		if(WMinecraft.getPlayer().getCooledAttackStrength(0) < cooldown.getValueF())
			return;
		
		Entity target = mc.pointedEntity;
		if(target == null || !(target instanceof EntityLivingBase))
			return;
		
		EntityLivingBase living = (EntityLivingBase)target;
		if(living.isDead || living.getHealth() <= 0)
			return;
		
		// Check range
		if(WMinecraft.getPlayer().getDistance(target) > range.getValue())
			return;
		
		mc.playerController.attackEntity(WMinecraft.getPlayer(), target);
		WMinecraft.getPlayer().swingArm(EnumHand.MAIN_HAND);
	}
}
