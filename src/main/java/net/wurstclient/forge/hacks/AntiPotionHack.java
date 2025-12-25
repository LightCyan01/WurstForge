/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class AntiPotionHack extends Hack
{
	private final CheckboxSetting blindness = new CheckboxSetting("Blindness", true);
	private final CheckboxSetting nausea = new CheckboxSetting("Nausea", true);
	private final CheckboxSetting hunger = new CheckboxSetting("Hunger", true);
	private final CheckboxSetting weakness = new CheckboxSetting("Weakness", true);
	private final CheckboxSetting slowness = new CheckboxSetting("Slowness", true);
	private final CheckboxSetting miningFatigue = new CheckboxSetting("Mining Fatigue", true);
	private final CheckboxSetting poison = new CheckboxSetting("Poison", false);
	private final CheckboxSetting wither = new CheckboxSetting("Wither", false);
	
	public AntiPotionHack()
	{
		super("AntiPotion", "Removes negative potion effects.");
		setCategory(Category.COMBAT);
		addSetting(blindness);
		addSetting(nausea);
		addSetting(hunger);
		addSetting(weakness);
		addSetting(slowness);
		addSetting(miningFatigue);
		addSetting(poison);
		addSetting(wither);
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
		
		if(blindness.isChecked())
			player.removePotionEffect(MobEffects.BLINDNESS);
		if(nausea.isChecked())
			player.removePotionEffect(MobEffects.NAUSEA);
		if(hunger.isChecked())
			player.removePotionEffect(MobEffects.HUNGER);
		if(weakness.isChecked())
			player.removePotionEffect(MobEffects.WEAKNESS);
		if(slowness.isChecked())
			player.removePotionEffect(MobEffects.SLOWNESS);
		if(miningFatigue.isChecked())
			player.removePotionEffect(MobEffects.MINING_FATIGUE);
		if(poison.isChecked())
			player.removePotionEffect(MobEffects.POISON);
		if(wither.isChecked())
			player.removePotionEffect(MobEffects.WITHER);
	}
}
