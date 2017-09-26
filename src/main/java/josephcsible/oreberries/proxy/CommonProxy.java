/*
Oreberries Minecraft Mod
Copyright (C) 2017 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.oreberries.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Triple;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import josephcsible.oreberries.BlockOreberryBush;
import josephcsible.oreberries.OreberriesMod;
import josephcsible.oreberries.RecipeUtils;
import josephcsible.oreberries.VillagerTinkerTrades;
import josephcsible.oreberries.config.GeneralConfig;
import josephcsible.oreberries.config.OreberriesJson;
import josephcsible.oreberries.config.OreberryConfig;
import josephcsible.oreberries.item.ItemNugget;
import josephcsible.oreberries.worldgen.OreberriesWorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
	public static final List<BlockOreberryBush> oreberryBushBlocks = new ArrayList<>();
	protected static final List<ItemNugget> nuggets = new ArrayList<>();
	public static final CreativeTabs creativeTab = new CreativeTabs(OreberriesMod.MODID) {
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return oreberryBushBlocks.isEmpty() ? ItemStack.EMPTY : new ItemStack(Item.getItemFromBlock(oreberryBushBlocks.get(0)));
		}
	};

	public void preInit(FMLPreInitializationEvent event) {
		OreberriesMod.logger = event.getModLog();

		File jsonFile = new File(event.getModConfigurationDirectory(), OreberriesMod.MODID + ".json");
		JsonObject json = OreberriesJson.read(jsonFile);

		for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
			String name = entry.getKey();
			OreberryConfig oreberryConfig = new OreberryConfig(name, (JsonObject) entry.getValue());
			if(oreberryConfig.smeltingResultNugget != null) {
				nuggets.add(new ItemNugget(name, oreberryConfig.smeltingResultNugget));
			}
			oreberryBushBlocks.add(new BlockOreberryBush(name, oreberryConfig));
		}

		if(OreberriesJson.needsWrite || GeneralConfig.rewriteJson) {
			json = new JsonObject();
			for(BlockOreberryBush block : oreberryBushBlocks) {
				json.add(block.config.name, block.config.toJson());
			}
			OreberriesJson.write(jsonFile, json);
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		for(BlockOreberryBush block : oreberryBushBlocks) {
			registry.register(block);
		}
	}

	@SubscribeEvent
	public static void registerVillagerProfessions(RegistryEvent.Register<VillagerProfession> event) {
		if(!GeneralConfig.enableVillager) return;
		VillagerProfession prof = new VillagerProfession(OreberriesMod.MODID + ":tinker",
				OreberriesMod.MODID + ":textures/entity/villager/tinker.png",
				"minecraft:textures/entity/zombie_villager/zombie_villager.png");
		event.getRegistry().register(prof);
		(new VillagerCareer(prof, "tinker")).addTrade(1, new VillagerTinkerTrades());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		for(ItemNugget item : nuggets) {
			registry.register(item);
			for(String oredictName : item.config.oredictNames) {
				OreDictionary.registerOre(oredictName, item);
			}
		}
		for(BlockOreberryBush block : oreberryBushBlocks) {
			registry.registerAll(new ItemBlock(block) {
				@Override
				public String getItemStackDisplayName(ItemStack stack) {
					return this.block.getLocalizedName();
				}
			}.setRegistryName(block.getRegistryName()), block.berries);
			for(String oredictName : block.config.oredictNames) {
				OreDictionary.registerOre(oredictName, block.berries);
			}
		}
	}

	public void init(@SuppressWarnings("unused") FMLInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new OreberriesWorldGen(), 0);
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		// Add recipes to make our stuff, and to make anything that's specified exactly (i.e., not as an oredict name)
		IForgeRegistry<IRecipe> registry = event.getRegistry();
		for(ItemNugget nugget : nuggets) {
			for(String ingotName : nugget.config.ingotNames) {
				Triple<String, String, String> splitName = RecipeUtils.splitItemName(ingotName);
				if(splitName == null) {
					// No name. Do nothing
				} else if(splitName.getLeft() == null) {
					// An oredict name. Add just the nugget recipe for now
					registry.register(new ShapedOreRecipe(null, new ItemStack(nugget, 9), "i", 'i', splitName.getMiddle()).setRegistryName(RecipeUtils.getNewRecipeName()));
				} else {
					// An item name. Add both recipes now
					ItemStack stack = RecipeUtils.getItemFromName(splitName);
					if(stack != null) {
						if(!nugget.addedIngotRecipe) {
							nugget.addedIngotRecipe = true;
							GameRegistry.addShapedRecipe(RecipeUtils.getNewRecipeName(), null, stack, "nnn", "nnn", "nnn", 'n', new ItemStack(nugget));
						}
						GameRegistry.addShapedRecipe(RecipeUtils.getNewRecipeName(), null, new ItemStack(nugget, 9), "i", 'i', stack);
					}
				}
			}
		}
		for(BlockOreberryBush block : oreberryBushBlocks) {
			Triple<String, String, String> name = RecipeUtils.splitItemName(block.config.smeltingResultString);
			if(name != null && name.getLeft() != null) {
				ItemStack stack = RecipeUtils.getItemFromName(name);
				if(stack != null) {
					GameRegistry.addSmelting(block.berries, stack, 0.2f);
				}
			}
		}
	}

	public void postInit(@SuppressWarnings("unused") FMLPostInitializationEvent event) {
		// Add recipes to make stuff that's specified as an oredict name
		for(ItemNugget nugget : nuggets) {
			if(nugget.addedIngotRecipe) continue;
			for(String ingotName : nugget.config.ingotNames) {
				Triple<String, String, String> splitName = RecipeUtils.splitItemName(ingotName);
				if(splitName != null && splitName.getLeft() == null) {
					// Now add the ingot recipe
					ItemStack stack = RecipeUtils.getItemFromOredict(splitName.getMiddle(), null);
					if(stack != null) {
						nugget.addedIngotRecipe = true;
						GameRegistry.addShapedRecipe(RecipeUtils.getNewRecipeName(), null, stack, "nnn", "nnn", "nnn", 'n', new ItemStack(nugget));
						break;
					}
				}
			}
		}
		for(BlockOreberryBush block : oreberryBushBlocks) {
			Triple<String, String, String> name = RecipeUtils.splitItemName(block.config.smeltingResultString);
			if(name != null && name.getLeft() == null) {
				ItemStack stack = RecipeUtils.getItemFromOredict(name.getMiddle(), block.berries);
				if(stack != null) {
					GameRegistry.addSmelting(block.berries, stack, 0.2f);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onConfigChangedEvent(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(OreberriesMod.MODID))
		{
			ConfigManager.sync(OreberriesMod.MODID, Config.Type.INSTANCE);
		}
	}

	public boolean isOreberryBushOpaqueCube(@SuppressWarnings("unused") IBlockState state) {
		return false;
	}
}
