package josephcsible.oreberries.item;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEssenceBerry extends ItemOreberry {
	public ItemEssenceBerry(String name, @Nullable String tooltip) {
		super(name, tooltip);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote)
        {
            worldIn.spawnEntity(new EntityXPOrb(worldIn, playerIn.posX, playerIn.posY + 1, playerIn.posZ, itemRand.nextInt(14) + 6));
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
	}

	public static boolean isEssence(String special) {
		return "essence".equals(special);
	}
}
