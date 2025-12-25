/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.wurstclient.forge.ForgeWurst;

@Mixin(BlockWeb.class)
public class BlockWebMixin
{
	@Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
	private void onEntityCollision(World worldIn, BlockPos pos,
		IBlockState state, Entity entityIn, CallbackInfo ci)
	{
		try
		{
			if(entityIn != Minecraft.getMinecraft().player)
				return;
			
			if(ForgeWurst.getForgeWurst() != null
				&& ForgeWurst.getForgeWurst().getHax() != null
				&& ForgeWurst.getForgeWurst().getHax().noSlowdownHack != null
				&& ForgeWurst.getForgeWurst().getHax().noSlowdownHack.isEnabled())
			{
				ci.cancel();
			}
		}
		catch(Exception e)
		{
		}
	}
}
