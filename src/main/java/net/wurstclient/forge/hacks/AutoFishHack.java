/*
 * Copyright (C) 2017 - 2025 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge.hacks;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wurstclient.fmlevents.WPacketInputEvent;
import net.wurstclient.fmlevents.WUpdateEvent;
import net.wurstclient.forge.Category;
import net.wurstclient.forge.Hack;
import net.wurstclient.forge.compatibility.WEnchantments;
import net.wurstclient.forge.compatibility.WItem;
import net.wurstclient.forge.compatibility.WMinecraft;
import net.wurstclient.forge.compatibility.WVec3d;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.wurstclient.forge.settings.CheckboxSetting;
import net.wurstclient.forge.settings.SliderSetting;
import net.wurstclient.forge.settings.SliderSetting.ValueDisplay;
import net.wurstclient.forge.utils.ChatUtils;
import net.wurstclient.forge.utils.PlayerControllerUtils;
import net.wurstclient.forge.utils.RenderUtils;

public final class AutoFishHack extends Hack
{
	private final SliderSetting validRange = new SliderSetting("Valid range",
		"Any bites that occur outside of this range\n" + "will be ignored.\n\n"
			+ "Increase your range if bites are not being\n"
			+ "detected, decrease it if other people's\n"
			+ "bites are being detected as yours.",
		1.5, 0.25, 8, 0.25, ValueDisplay.DECIMAL);
	private CheckboxSetting debugDraw = new CheckboxSetting("Debug draw",
		"Shows where bites are occurring and where\n"
			+ "they will be detected. Useful for optimizing\n"
			+ "your 'Valid range' setting.",
		false);

	private CheckboxSetting fmbAutoMinigame = new CheckboxSetting("FMB Auto Minigame",
		"When Fishing Made Better mod is detected,\n"
			+ "automatically plays the fishing minigame\n"
			+ "by controlling line tension.",
		true);
	
	private int timer;
	private Vec3d lastSoundPos;
	private int box;
	private int cross;
	
	private boolean isFMBInstalled = false;
	private Object fishingDataCapability = null;
	private Method isFishingMethod = null;
	private Method getReelAmountMethod = null;
	private Method getReelTargetMethod = null;
	private Method getErrorVarianceMethod = null;
	private Method setKeybindMethod = null;
	private Object reelInKeybind = null;
	private Object reelOutKeybind = null;
	private Object noneKeybind = null;
	private Class<?> fmbNetworkClass = null;
	private Object fmbNetworkInstance = null;
	private Class<?> fmbPacketClass = null;
	
	public AutoFishHack()
	{
		super("AutoFish", "Automatically catches fish using your\n"
			+ "best fishing rod. If it finds a better\n"
			+ "rod while fishing, it will automatically\n"
			+ "switch to it.\n\n"
			+ "Supports Fishing Made Better mod.");
		setCategory(Category.OTHER);
		addSetting(validRange);
		addSetting(debugDraw);
		addSetting(fmbAutoMinigame);
	}
	
	@Override
	protected void onEnable()
	{
		timer = 0;
		lastSoundPos = null;
		
		box = GL11.glGenLists(1);
		
		cross = GL11.glGenLists(1);
		GL11.glNewList(cross, GL11.GL_COMPILE);
		GL11.glColor4f(1, 0, 0, 0.5F);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(-0.125, 0, -0.125);
		GL11.glVertex3d(0.125, 0, 0.125);
		GL11.glVertex3d(0.125, 0, -0.125);
		GL11.glVertex3d(-0.125, 0, 0.125);
		GL11.glEnd();
		GL11.glEndList();
		
		isFMBInstalled = Loader.isModLoaded("fishingmadebetter");
		if(isFMBInstalled)
		{
			ChatUtils.message("Fishing Made Better detected - using FMB mode");
			initFMBCompatibility();
		}
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void initFMBCompatibility()
	{
		try
		{
			Class<?> capProviderClass = Class.forName(
				"net.theawesomegem.fishingmadebetter.common.capability.fishing.FishingCapabilityProvider");
			fishingDataCapability = capProviderClass.getField("FISHING_DATA_CAP").get(null);
			
			Class<?> fishingDataClass = Class.forName(
				"net.theawesomegem.fishingmadebetter.common.capability.fishing.IFishingData");
			isFishingMethod = fishingDataClass.getMethod("isFishing");
			getReelAmountMethod = fishingDataClass.getMethod("getReelAmount");
			getReelTargetMethod = fishingDataClass.getMethod("getReelTarget");
			getErrorVarianceMethod = fishingDataClass.getMethod("getErrorVariance");
			setKeybindMethod = fishingDataClass.getMethod("setKeybind", 
				Class.forName("net.theawesomegem.fishingmadebetter.common.networking.packet.PacketKeybindS$Keybind"));
			
			// Get Keybind enum values
			Class<?> keybindEnumClass = Class.forName(
				"net.theawesomegem.fishingmadebetter.common.networking.packet.PacketKeybindS$Keybind");
			reelInKeybind = keybindEnumClass.getField("REEL_IN").get(null);
			reelOutKeybind = keybindEnumClass.getField("REEL_OUT").get(null);
			noneKeybind = keybindEnumClass.getField("NONE").get(null);
			
			// Get network handler for sending packets
			fmbNetworkClass = Class.forName(
				"net.theawesomegem.fishingmadebetter.common.networking.PrimaryPacketHandler");
			fmbNetworkInstance = fmbNetworkClass.getField("INSTANCE").get(null);
			fmbPacketClass = Class.forName(
				"net.theawesomegem.fishingmadebetter.common.networking.packet.PacketKeybindS");
			
		}
		catch(Exception e)
		{
			ChatUtils.warning("FMB compatibility init failed: " + e.getMessage());
			isFMBInstalled = false;
		}
	}
	
	@Override
	protected void onDisable()
	{
		MinecraftForge.EVENT_BUS.unregister(this);
		GL11.glDeleteLists(box, 1);
		GL11.glDeleteLists(cross, 1);
	}
	
	@SubscribeEvent
	public void onUpdate(WUpdateEvent event)
	{
		// update range box
		if(debugDraw.isChecked())
		{
			GL11.glNewList(box, GL11.GL_COMPILE);
			AxisAlignedBB box = new AxisAlignedBB(-validRange.getValue(),
				-1 / 16.0, -validRange.getValue(), validRange.getValue(),
				1 / 16.0, validRange.getValue());
			GL11.glColor4f(1, 0, 0, 0.5F);
			GL11.glBegin(GL11.GL_LINES);
			RenderUtils.drawOutlinedBox(box);
			GL11.glEnd();
			GL11.glEndList();
		}
		
		EntityPlayerSP player = event.getPlayer();
		InventoryPlayer inventory = player.inventory;
		
		if(isFMBInstalled && fmbAutoMinigame.isChecked())
		{
			handleFMBMinigame(player);
		}
		
		if(timer < 0)
		{
			PlayerControllerUtils.windowClick_PICKUP(-timer);
			timer = 15;
			return;
		}
		
		int bestRodValue =
			getRodValue(inventory.getStackInSlot(inventory.currentItem));
		int bestRodSlot = bestRodValue > -1 ? inventory.currentItem : -1;
		
		for(int slot = 0; slot < 36; slot++)
		{
			ItemStack stack = inventory.getStackInSlot(slot);
			int rodValue = getRodValue(stack);
			
			if(rodValue > bestRodValue)
			{
				bestRodValue = rodValue;
				bestRodSlot = slot;
			}
		}
		
		if(bestRodSlot == inventory.currentItem)
		{
			if(timer > 0)
			{
				timer--;
				return;
			}
			
			// cast rod
			if(player.fishEntity == null)
				rightClick();
			
			return;
		}
		
		if(bestRodSlot == -1)
		{
			ChatUtils.message("Out of fishing rods.");
			setEnabled(false);
			return;
		}
		
		if(bestRodSlot < 9)
		{
			inventory.currentItem = bestRodSlot;
			return;
		}
		
		int firstEmptySlot = inventory.getFirstEmptyStack();
		if(firstEmptySlot != -1)
		{
			if(firstEmptySlot >= 9)
				PlayerControllerUtils
					.windowClick_QUICK_MOVE(36 + inventory.currentItem);
			
			PlayerControllerUtils.windowClick_QUICK_MOVE(bestRodSlot);
			
		}else
		{
			PlayerControllerUtils.windowClick_PICKUP(bestRodSlot);
			PlayerControllerUtils
				.windowClick_PICKUP(36 + inventory.currentItem);
			timer = -bestRodSlot;
		}
	}
	
	private void handleFMBMinigame(EntityPlayerSP player)
	{
		try
		{
			if(fishingDataCapability == null)
				return;
			
			Object capability = ((net.minecraftforge.common.capabilities.Capability<?>)fishingDataCapability);
			Object fishingData = player.getCapability(
				(net.minecraftforge.common.capabilities.Capability<?>)fishingDataCapability, null);
			
			if(fishingData == null)
				return;
			
			boolean isFishing = (Boolean)isFishingMethod.invoke(fishingData);
			if(!isFishing)
				return;
			
			int reelAmount = (Integer)getReelAmountMethod.invoke(fishingData);
			int reelTarget = (Integer)getReelTargetMethod.invoke(fishingData);
			int errorVariance = (Integer)getErrorVarianceMethod.invoke(fishingData);
			
			int diff = reelTarget - reelAmount;
			
			Object keybindToSend;
			if(Math.abs(diff) <= errorVariance)
			{
				keybindToSend = noneKeybind;
			}
			else if(diff > 0)
			{
				keybindToSend = reelOutKeybind;
			}
			else
			{
				// Target is lower, reel in
				keybindToSend = reelInKeybind;
			}
			
			// Send packet to server
			sendFMBKeybindPacket(keybindToSend);
			
		}
		catch(Exception e)
		{
			// Silently fail - don't spam chat
		}
	}
	
	private void sendFMBKeybindPacket(Object keybind)
	{
		try
		{
			// Create packet instance
			Object packet = fmbPacketClass.getConstructor(
				Class.forName("net.theawesomegem.fishingmadebetter.common.networking.packet.PacketKeybindS$Keybind")
			).newInstance(keybind);
			
			// Send to server
			Method sendToServerMethod = fmbNetworkInstance.getClass().getMethod("sendToServer", 
				net.minecraftforge.fml.common.network.simpleimpl.IMessage.class);
			sendToServerMethod.invoke(fmbNetworkInstance, packet);
			
		}
		catch(Exception e)
		{
			// Silently fail
		}
	}
	
	@SubscribeEvent
	public void onPacketInput(WPacketInputEvent event)
	{
		// Skip vanilla sound detection if FMB is handling it
		if(isFMBInstalled && fmbAutoMinigame.isChecked())
			return;
		
		EntityPlayerSP player = WMinecraft.getPlayer();
		if(player == null || player.fishEntity == null)
			return;
		
		if(!(event.getPacket() instanceof SPacketSoundEffect))
			return;
		
		// check sound type
		SPacketSoundEffect sound = (SPacketSoundEffect)event.getPacket();
		if(!SoundEvents.ENTITY_BOBBER_SPLASH.equals(sound.getSound()))
			return;
		
		if(debugDraw.isChecked())
			lastSoundPos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
		
		// check position
		EntityFishHook bobber = player.fishEntity;
		if(Math.abs(sound.getX() - bobber.posX) > validRange.getValue()
			|| Math.abs(sound.getZ() - bobber.posZ) > validRange.getValue())
			return;
		
		// catch fish
		rightClick();
	}
	
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		if(!debugDraw.isChecked())
			return;
		
		// GL settings
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL11.glPushMatrix();
		GL11.glTranslated(-TileEntityRendererDispatcher.staticPlayerX,
			-TileEntityRendererDispatcher.staticPlayerY,
			-TileEntityRendererDispatcher.staticPlayerZ);
		
		EntityFishHook bobber = WMinecraft.getPlayer().fishEntity;
		if(bobber != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(bobber.posX, bobber.posY, bobber.posZ);
			GL11.glCallList(box);
			GL11.glPopMatrix();
		}
		
		if(lastSoundPos != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(WVec3d.getX(lastSoundPos),
				WVec3d.getY(lastSoundPos), WVec3d.getZ(lastSoundPos));
			GL11.glCallList(cross);
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
		
		// GL resets
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	private int getRodValue(ItemStack stack)
	{
		if(WItem.isNullOrEmpty(stack)
			|| !(stack.getItem() instanceof ItemFishingRod))
			return -1;
		
		int luckOTSLvl = EnchantmentHelper
			.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, stack);
		int lureLvl =
			EnchantmentHelper.getEnchantmentLevel(Enchantments.LURE, stack);
		int unbreakingLvl = EnchantmentHelper
			.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
		int mendingBonus =
			EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
		int noVanishBonus = WEnchantments.hasVanishingCurse(stack) ? 0 : 1;
		
		return luckOTSLvl * 9 + lureLvl * 9 + unbreakingLvl * 2 + mendingBonus
			+ noVanishBonus;
	}
	
	private void rightClick()
	{
		ItemStack stack = WMinecraft.getPlayer().inventory.getCurrentItem();
		if(WItem.isNullOrEmpty(stack)
			|| !(stack.getItem() instanceof ItemFishingRod))
			return;
		
		try
		{
			Method rightClickMouse = mc.getClass().getDeclaredMethod(
				wurst.isObfuscated() ? "func_147121_ag" : "rightClickMouse");
			rightClickMouse.setAccessible(true);
			rightClickMouse.invoke(mc);
			
		}catch(ReflectiveOperationException e)
		{
			setEnabled(false);
			throw new RuntimeException(e);
		}
		
		timer = 15;
	}
}
