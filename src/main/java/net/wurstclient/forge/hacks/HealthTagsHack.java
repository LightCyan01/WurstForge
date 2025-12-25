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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;

/**
 * Shows health in entity nametags.
 */
public final class HealthTagsHack extends Hack
{
	private final CheckboxSetting players = new CheckboxSetting("Players",
		"Show health on players.", true);
	private final CheckboxSetting mobs = new CheckboxSetting("Mobs",
		"Show health on mobs.", true);
	
	public HealthTagsHack()
	{
		super("HealthTags", "Shows health in nametags.");
		setCategory(Category.RENDER);
		addSetting(players);
		addSetting(mobs);
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
	
	public boolean shouldShowHealth(Entity entity)
	{
		if(!isEnabled())
			return false;
		
		if(entity instanceof EntityPlayer)
			return players.isChecked();
		
		if(entity instanceof EntityLivingBase)
			return mobs.isChecked();
		
		return false;
	}
	
	public String getHealthString(EntityLivingBase entity)
	{
		float health = entity.getHealth();
		float maxHealth = entity.getMaxHealth();
		
		// Color based on health percentage
		String color;
		float percentage = health / maxHealth;
		if(percentage > 0.66f)
			color = "\u00a7a"; // Green
		else if(percentage > 0.33f)
			color = "\u00a7e"; // Yellow
		else
			color = "\u00a7c"; // Red
		
		return " " + color + String.format("%.1f", health) + "\u00a7f/\u00a77" + String.format("%.1f", maxHealth);
	}
}
