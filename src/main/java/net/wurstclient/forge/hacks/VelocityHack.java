/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;

public final class VelocityHack extends Hack
{
	private final SliderSetting horizontal = new SliderSetting("Horizontal",
		"Horizontal knockback multiplier.", 0, 0, 100, 1, ValueDisplay.PERCENTAGE);
	
	private final SliderSetting vertical = new SliderSetting("Vertical",
		"Vertical knockback multiplier.", 0, 0, 100, 1, ValueDisplay.PERCENTAGE);
	
	public VelocityHack()
	{
		super("Velocity", "Modifies knockback received from attacks.");
		setCategory(Category.COMBAT);
		addSetting(horizontal);
		addSetting(vertical);
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
		
		if(player.hurtTime == player.maxHurtTime && player.hurtTime > 0)
		{
			double hMulti = horizontal.getValue() / 100.0;
			double vMulti = vertical.getValue() / 100.0;
			
			player.motionX *= hMulti;
			player.motionZ *= hMulti;
			if(player.motionY > 0)
				player.motionY *= vMulti;
		}
	}
}
