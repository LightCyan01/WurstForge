/*
 * Copyright (C) 2017 - 2019 | Wurst-Imperium | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.forge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.wurstclient.forge.compatibility.WHackList;
import net.wurstclient.forge.hacks.*;
import net.wurstclient.forge.settings.Setting;
import net.wurstclient.forge.utils.JsonUtils;

public final class HackList extends WHackList
{
	public final AirJumpHack airJumpHack = register(new AirJumpHack());
	public final AirPlaceHack airPlaceHack = register(new AirPlaceHack());
	public final AntiAfkHack antiAfkHack = register(new AntiAfkHack());
	public final AntiBlindHack antiBlindHack = register(new AntiBlindHack());
	public final AntiCactusHack antiCactusHack = register(new AntiCactusHack());
	public final AntiEntityPushHack antiEntityPushHack = register(new AntiEntityPushHack());
	public final AntiHazardHack antiHazardHack = register(new AntiHazardHack());
	public final AntiKnockbackHack antiKnockbackHack = register(new AntiKnockbackHack());
	public final AntiPotionHack antiPotionHack = register(new AntiPotionHack());
	public final AntiSpamHack antiSpamHack = register(new AntiSpamHack());
	public final AntiWobbleHack antiWobbleHack = register(new AntiWobbleHack());
	public final AutoArmorHack autoArmorHack = register(new AutoArmorHack());
	public final AutoDropHack autoDropHack = register(new AutoDropHack());
	public final AutoEatHack autoEatHack = register(new AutoEatHack());
	public final AutoFarmHack autoFarmHack = register(new AutoFarmHack());
	public final AutoFishHack autoFishHack = register(new AutoFishHack());
	public final AutoJumpHack autoJumpHack = register(new AutoJumpHack());
	public final AutoLeaveHack autoLeaveHack = register(new AutoLeaveHack());
	public final AutoMineHack autoMineHack = register(new AutoMineHack());
	public final AutoPotionHack autoPotionHack = register(new AutoPotionHack());
	public final AutoRespawnHack autoRespawnHack = register(new AutoRespawnHack());
	public final AutoSprintHack autoSprintHack = register(new AutoSprintHack());
	public final AutoStealHack autoStealHack = register(new AutoStealHack());
	public final AutoStoreHack autoStoreHack = register(new AutoStoreHack());
	public final AutoSwimHack autoSwimHack = register(new AutoSwimHack());
	public final AutoSwingHack autoSwingHack = register(new AutoSwingHack());
	public final AutoSwitchHack autoSwitchHack = register(new AutoSwitchHack());
	public final AutoToolHack autoToolHack = register(new AutoToolHack());
	public final AutoTotemHack autoTotemHack = register(new AutoTotemHack());
	public final AutoWalkHack autoWalkHack = register(new AutoWalkHack());
	public final BlinkHack blinkHack = register(new BlinkHack());
	public final BoatFlyHack boatFlyHack = register(new BoatFlyHack());
	public final BunnyHopHack bunnyHopHack = register(new BunnyHopHack());
	public final CameraDistanceHack cameraDistanceHack = register(new CameraDistanceHack());
	public final CameraNoClipHack cameraNoClipHack = register(new CameraNoClipHack());
	public final ChestEspHack chestEspHack = register(new ChestEspHack());
	public final ClickGuiHack clickGuiHack = register(new ClickGuiHack());
	public final CoordsHack coordsHack = register(new CoordsHack());
	public final CriticalsHack criticalsHack = register(new CriticalsHack());
	public final DerpHack derpHack = register(new DerpHack());
	public final DolphinHack dolphinHack = register(new DolphinHack());
	public final ElytraFlightHack elytraFlightHack = register(new ElytraFlightHack());
	public final EntitySpeedHack entitySpeedHack = register(new EntitySpeedHack());
	public final FancyChatHack fancyChatHack = register(new FancyChatHack());
	public final FastBowHack fastBowHack = register(new FastBowHack());
	public final FastBreakHack fastBreakHack = register(new FastBreakHack());
	public final FastLadderHack fastLadderHack = register(new FastLadderHack());
	public final FastPlaceHack fastPlaceHack = register(new FastPlaceHack());
	public final FlightHack flightHack = register(new FlightHack());
	public final FovHack fovHack = register(new FovHack());
	public final FreecamHack freecamHack = register(new FreecamHack());
	public final FreezeHack freezeHack = register(new FreezeHack());
	public final FullbrightHack fullbrightHack = register(new FullbrightHack());
	public final GlideHack glideHack = register(new GlideHack());
	public final HeadRollHack headRollHack = register(new HeadRollHack());
	public final HealthTagsHack healthTagsHack = register(new HealthTagsHack());
	public final HighJumpHack highJumpHack = register(new HighJumpHack());
	public final InvWalkHack invWalkHack = register(new InvWalkHack());
	public final ItemEspHack itemEspHack = register(new ItemEspHack());
	public final ItemMagnetHack itemMagnetHack = register(new ItemMagnetHack());
	public final JesusHack jesusHack = register(new JesusHack());
	public final JetpackHack jetpackHack = register(new JetpackHack());
	public final KillauraHack killauraHack = register(new KillauraHack());
	public final LiquidsHack liquidsHack = register(new LiquidsHack());
	public final LongJumpHack longJumpHack = register(new LongJumpHack());
	public final MileyCyrusHack mileyCyrusHack = register(new MileyCyrusHack());
	public final MobEspHack mobEspHack = register(new MobEspHack());
	public final MobSpawnEspHack mobSpawnEspHack = register(new MobSpawnEspHack());
	public final NameTagsHack nameTagsHack = register(new NameTagsHack());
	public final NoClipHack noClipHack = register(new NoClipHack());
	public final NoFallHack noFallHack = register(new NoFallHack());
	public final NoFireOverlayHack noFireOverlayHack = register(new NoFireOverlayHack());
	public final NoFogHack noFogHack = register(new NoFogHack());
	public final NoHurtcamHack noHurtcamHack = register(new NoHurtcamHack());
	public final NoLevitationHack noLevitationHack = register(new NoLevitationHack());
	public final NoPumpkinHack noPumpkinHack = register(new NoPumpkinHack());
	public final NoSlowdownHack noSlowdownHack = register(new NoSlowdownHack());
	public final NoWeatherHack noWeatherHack = register(new NoWeatherHack());
	public final NoWebHack noWebHack = register(new NoWebHack());
	public final NukerHack nukerHack = register(new NukerHack());
	public final PanicHack panicHack = register(new PanicHack());
	public final ParkourHack parkourHack = register(new ParkourHack());
	public final PlayerEspHack playerEspHack = register(new PlayerEspHack());
	public final PortalGuiHack portalGuiHack = register(new PortalGuiHack());
	public final PvPBotHack pvpBotHack = register(new PvPBotHack());
	public final RadarHack radarHack = register(new RadarHack());
	public final RainbowUiHack rainbowUiHack = register(new RainbowUiHack());
	public final ReachHack reachHack = register(new ReachHack());
	public final RegenHack regenHack = register(new RegenHack());
	public final SafeWalkHack safeWalkHack = register(new SafeWalkHack());
	public final SkinDerpHack skinDerpHack = register(new SkinDerpHack());
	public final SlowfallHack slowfallHack = register(new SlowfallHack());
	public final SneakHack sneakHack = register(new SneakHack());
	public final SpeedHack speedHack = register(new SpeedHack());
	public final SpiderHack spiderHack = register(new SpiderHack());
	public final StepHack stepHack = register(new StepHack());
	public final StorageEspHack storageEspHack = register(new StorageEspHack());
	public final ScaffoldHack scaffoldHack = register(new ScaffoldHack());
	public final ThrowHack throwHack = register(new ThrowHack());
	public final TimerHack timerHack = register(new TimerHack());
	public final TiredHack tiredHack = register(new TiredHack());
	public final TracersHack tracersHack = register(new TracersHack());
	public final TrajectoriesHack trajectoriesHack = register(new TrajectoriesHack());
	public final TriggerBotHack triggerBotHack = register(new TriggerBotHack());
	public final TunnellerHack tunnellerHack = register(new TunnellerHack());
	public final VelocityHack velocityHack = register(new VelocityHack());
	public final XRayHack xRayHack = register(new XRayHack());
	
	private final Path enabledHacksFile;
	private final Path settingsFile;
	private boolean disableSaving;
	
	public HackList(Path enabledHacksFile, Path settingsFile)
	{
		this.enabledHacksFile = enabledHacksFile;
		this.settingsFile = settingsFile;
	}
	
	public void loadEnabledHacks()
	{
		JsonArray json;
		try(BufferedReader reader = Files.newBufferedReader(enabledHacksFile))
		{
			json = JsonUtils.jsonParser.parse(reader).getAsJsonArray();
			
		}catch(NoSuchFileException e)
		{
			saveEnabledHacks();
			return;
			
		}catch(Exception e)
		{
			System.out
				.println("Failed to load " + enabledHacksFile.getFileName());
			e.printStackTrace();
			
			saveEnabledHacks();
			return;
		}
		
		disableSaving = true;
		for(JsonElement e : json)
		{
			if(!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString())
				continue;
			
			Hack hack = get(e.getAsString());
			if(hack == null || !hack.isStateSaved())
				continue;
			
			hack.setEnabled(true);
		}
		disableSaving = false;
		
		saveEnabledHacks();
	}
	
	public void saveEnabledHacks()
	{
		if(disableSaving)
			return;
		
		JsonArray enabledHacks = new JsonArray();
		for(Hack hack : getRegistry())
			if(hack.isEnabled() && hack.isStateSaved())
				enabledHacks.add(new JsonPrimitive(hack.getName()));
			
		try(BufferedWriter writer = Files.newBufferedWriter(enabledHacksFile))
		{
			JsonUtils.prettyGson.toJson(enabledHacks, writer);
			
		}catch(IOException e)
		{
			System.out
				.println("Failed to save " + enabledHacksFile.getFileName());
			e.printStackTrace();
		}
	}
	
	public void loadSettings()
	{
		JsonObject json;
		try(BufferedReader reader = Files.newBufferedReader(settingsFile))
		{
			json = JsonUtils.jsonParser.parse(reader).getAsJsonObject();
			
		}catch(NoSuchFileException e)
		{
			saveSettings();
			return;
			
		}catch(Exception e)
		{
			System.out.println("Failed to load " + settingsFile.getFileName());
			e.printStackTrace();
			
			saveSettings();
			return;
		}
		
		disableSaving = true;
		for(Entry<String, JsonElement> e : json.entrySet())
		{
			if(!e.getValue().isJsonObject())
				continue;
			
			Hack hack = get(e.getKey());
			if(hack == null)
				continue;
			
			Map<String, Setting> settings = hack.getSettings();
			for(Entry<String, JsonElement> e2 : e.getValue().getAsJsonObject()
				.entrySet())
			{
				String key = e2.getKey().toLowerCase();
				if(!settings.containsKey(key))
					continue;
				
				settings.get(key).fromJson(e2.getValue());
			}
		}
		disableSaving = false;
		
		saveSettings();
	}
	
	public void saveSettings()
	{
		if(disableSaving)
			return;
		
		JsonObject json = new JsonObject();
		for(Hack hack : getRegistry())
		{
			if(hack.getSettings().isEmpty())
				continue;
			
			JsonObject settings = new JsonObject();
			for(Setting setting : hack.getSettings().values())
				settings.add(setting.getName(), setting.toJson());
			
			json.add(hack.getName(), settings);
		}
		
		try(BufferedWriter writer = Files.newBufferedWriter(settingsFile))
		{
			JsonUtils.prettyGson.toJson(json, writer);
			
		}catch(IOException e)
		{
			System.out.println("Failed to save " + settingsFile.getFileName());
			e.printStackTrace();
		}
	}
}
