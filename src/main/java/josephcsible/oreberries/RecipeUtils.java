package josephcsible.oreberries;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public final class RecipeUtils {
	private RecipeUtils() {}

	public static @Nullable Triple<String, String, String> splitItemName(@Nullable String name) {
		if(name == null) return null;
		String modname, itemname, metadata = null;
		int colonPos = name.indexOf(':');
		if(colonPos == -1) {
			return Triple.of(null, name, null);
		}
		modname = name.substring(0, colonPos);
		itemname = name.substring(colonPos + 1);
		colonPos = itemname.indexOf(':');
		if(colonPos != -1) {
			metadata = itemname.substring(colonPos + 1);
			itemname = itemname.substring(0, colonPos);
		}
		return Triple.of(modname, itemname, metadata);
	}

	public static @Nullable ItemStack getItemFromName(Triple<String, String, String> name) {
		int metadata;
		if(name.getRight() != null) {
			try {
				metadata = Integer.parseInt(name.getRight());
			} catch(@SuppressWarnings("unused") NumberFormatException e) {
				OreberriesMod.logger.warn("Ignoring item with invalid metadata: {}", name);
				return null;
			}
		} else {
			metadata = 0;
		}
		Item item = Item.REGISTRY.getObject(new ResourceLocation(name.getLeft(), name.getMiddle()));
		if(item == null) return null;
		return new ItemStack(item, 1, metadata);
	}

	public static @Nullable ItemStack getItemFromOredict(String name, @Nullable Item exclude) {
		NonNullList<ItemStack> ores = OreDictionary.getOres(name);
		if(ores.isEmpty()) return null;
		ItemStack stack = ores.get(0);
		if(stack.getItem() == exclude) {
			// we don't want this particular one. try the next one
			if(ores.size() == 1) return null;
			stack = ores.get(1);
		}
		return stack;
	}

	private static int recipeCounter = 0;
	public static ResourceLocation getNewRecipeName() {
		return new ResourceLocation(OreberriesMod.MODID, "recipe_" + ++recipeCounter);
	}
}
