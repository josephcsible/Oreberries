package josephcsible.oreberries.proxy;

import josephcsible.oreberries.BlockOreberryBush;
import josephcsible.oreberries.OreberriesMod;
import josephcsible.oreberries.item.ItemEssenceBerry;
import josephcsible.oreberries.item.ItemNugget;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
    	ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
    	for(ItemNugget item : nuggets) {
    		int color = Integer.decode(item.config.color);
    		itemColors.registerItemColorHandler((stack, tintIndex) -> color, item);
    	}
    	BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
    	for(BlockOreberryBush block : oreberryBushBlocks) {
    		int color = Integer.decode(block.config.color);
    		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> color, block);
    		itemColors.registerItemColorHandler((stack, tintIndex) -> color, Item.getItemFromBlock(block), block.berries);
    	}
	}

	protected static class OreberryBushBlockStateMapper extends StateMapperBase {
		public static final OreberryBushBlockStateMapper OREBERRY = new OreberryBushBlockStateMapper(OreberriesMod.MODID, "oreberry_bush");
		public static final OreberryBushBlockStateMapper ESSENCE_BERRY = new OreberryBushBlockStateMapper(OreberriesMod.MODID, "essence_berry_bush");
		protected final ResourceLocation rl;

		protected OreberryBushBlockStateMapper(String resourceDomainIn, String resourcePathIn) {
			rl = new ResourceLocation(resourceDomainIn, resourcePathIn);
		}

		@Override
		public ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return new ModelResourceLocation(rl, this.getPropertyString(state.getProperties()));
		}
	}

	protected static final ModelResourceLocation nuggetMRL = new ModelResourceLocation(new ResourceLocation(OreberriesMod.MODID, "nugget"), "inventory");
	protected static final ModelResourceLocation oreberryMRL = new ModelResourceLocation(new ResourceLocation(OreberriesMod.MODID, "oreberry"), "inventory");
	protected static final ModelResourceLocation oreberryBushMRL = new ModelResourceLocation(new ResourceLocation(OreberriesMod.MODID, "oreberry_bush"), "inventory");
	protected static final ModelResourceLocation essenceBerryMRL = new ModelResourceLocation(new ResourceLocation(OreberriesMod.MODID, "essence_berry"), "inventory");
	protected static final ModelResourceLocation essenceBerryBushMRL = new ModelResourceLocation(new ResourceLocation(OreberriesMod.MODID, "essence_berry_bush"), "inventory");

    @SubscribeEvent
    public static void registerModels(@SuppressWarnings("unused") ModelRegistryEvent event) {
    	for(ItemNugget item : nuggets) {
    		ModelLoader.setCustomModelResourceLocation(item, 0, nuggetMRL);
    	}
    	for(BlockOreberryBush block : oreberryBushBlocks) {
    		if(ItemEssenceBerry.isEssence(block.config.special)) {
    			ModelLoader.setCustomStateMapper(block, OreberryBushBlockStateMapper.ESSENCE_BERRY);
    			ModelLoader.setCustomModelResourceLocation(block.berries, 0, essenceBerryMRL);
    			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, essenceBerryBushMRL);
    		} else {
	    		ModelLoader.setCustomStateMapper(block, OreberryBushBlockStateMapper.OREBERRY);
	    		ModelLoader.setCustomModelResourceLocation(block.berries, 0, oreberryMRL);
	    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, oreberryBushMRL);
    		}
    	}
    }
}
