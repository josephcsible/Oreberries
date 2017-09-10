package josephcsible.oreberries.item;

import josephcsible.oreberries.OreberriesMod;
import josephcsible.oreberries.config.NuggetConfig;
import josephcsible.oreberries.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemNugget extends Item {
	public final NuggetConfig config;
	public boolean addedIngotRecipe = false;
	public ItemNugget(String name, NuggetConfig config) {
		this.setCreativeTab(CommonProxy.creativeTab);
		this.setUnlocalizedName(OreberriesMod.MODID + ".nugget"); // This should never be seen, but just in case...
		this.setRegistryName(getFullName(name));
		this.config = config;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return config.name;
	}

	public static String getFullName(String name) {
		return OreberriesMod.MODID + ":" + name.toLowerCase() + "_nugget";
	}
}
