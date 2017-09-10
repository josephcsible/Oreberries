package josephcsible.oreberries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import josephcsible.oreberries.proxy.CommonProxy;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class VillagerTinkerTrades implements ITradeList
{

    private final List<ItemStack> allowedIngredients = new ArrayList<>();
    private final int max = 17;
    private final int min = 7;

    public VillagerTinkerTrades()
    {
        super();

        // vanilla blocks
        allowedIngredients.add(new ItemStack(Blocks.PISTON, 64));
        allowedIngredients.add(new ItemStack(Blocks.STICKY_PISTON, 64));

        // tconstruct blocks
        /*allowedIngredients.add(new ItemStack(TinkerWorld.barricadeBirch, 64));
        allowedIngredients.add(new ItemStack(TinkerWorld.barricadeJungle, 64));
        allowedIngredients.add(new ItemStack(TinkerWorld.barricadeOak, 64));
        allowedIngredients.add(new ItemStack(TinkerWorld.barricadeSpruce, 64));*/
        maybeAddTinkerItem("punji", 64);
        maybeAddTinkerItem("tooltables", 3, 3); // tool station
        maybeAddTinkerItem("tooltables", 3, 2); // part builder
        maybeAddTinkerItem("tooltables", 3, 4); // pattern chest
        maybeAddTinkerItem("tooltables", 3, 1); // stencil table
        /*for (int sc = 0; sc < 4; sc++)
        {
            allowedIngredients.add(new ItemStack(TinkerMechworks.landmine, 64, sc));
        }*/
    }

    private void maybeAddTinkerItem(String name, int quantity, int meta) {
    	Item item = Item.REGISTRY.getObject(new ResourceLocation("tconstruct", name));
    	if(item != null)
    		allowedIngredients.add(new ItemStack(item, quantity, meta));
    }

    private void maybeAddTinkerItem(String name, int quantity) {
    	maybeAddTinkerItem(name, quantity, 0);
    }

    @Override
    public void addMerchantRecipe (IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        ItemStack ingredient;
        ItemStack ingredient2;
        ItemStack result;

        for (BlockOreberryBush block : CommonProxy.oreberryBushBlocks)
        {
        	if(!block.config.tradeable) continue;
            int num = getNextInt(random, min, max);

            ingredient = getIngredient(random, num);
            if (ingredient.getCount() < 13)
            {
                ingredient2 = getIngredient(random, ingredient);
            }
            else
            {
                ingredient2 = ItemStack.EMPTY;
            }
            result = new ItemStack(block, calcStackSize(ingredient, ingredient2));
            recipeList.add(new MerchantRecipe(ingredient, ingredient2, result));
        }
    }

    private static int calcStackSize (ItemStack ingredient, ItemStack ingredient2)
    {
        if (ingredient == ItemStack.EMPTY)
            return 1;
        int num = ingredient.getCount();
        if (ingredient2 != ItemStack.EMPTY)
            num += ingredient2.getCount();

        return Math.max(1, Math.round((num - 5) / 4));
    }

    private ItemStack getIngredient (Random random, ItemStack ingredient)
    {
        int sc;
        ItemStack is;
        int tries = 0;
        while (true)
        {
            sc = getNextInt(random, 0, allowedIngredients.size() - 1);
            is = allowedIngredients.get(sc);

            if (is != ingredient || is.getItemDamage() != ingredient.getItemDamage())
                break;

            tries++;
            if (tries == 5)
                return ItemStack.EMPTY;
        }
        int num = getNextInt(random, 0, Math.min(is.getCount(), max - ingredient.getCount()));
        return is.copy().splitStack(num);
    }

    private ItemStack getIngredient (Random random, int num)
    {
        int sc = getNextInt(random, 0, allowedIngredients.size() - 1);
        ItemStack item = allowedIngredients.get(sc);
        return item.copy().splitStack(Math.min(num, item.getCount()));
    }

    private static int getNextInt (Random random, int min, int max)
    {
        return random.nextInt(Math.max(1, (max - min) + 1)) + min;
    }
}
