package josephcsible.oreberries.item;

import java.util.List;

import javax.annotation.Nullable;

import josephcsible.oreberries.OreberriesMod;
import josephcsible.oreberries.proxy.CommonProxy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOreberry extends Item {
	protected final String displayName;
	protected final @Nullable String tooltip;
	public ItemOreberry(String displayName, @Nullable String tooltip) {
		this.setCreativeTab(CommonProxy.creativeTab);
		this.setUnlocalizedName(OreberriesMod.MODID + ".oreberry"); // This should never be seen, but just in case...
		this.displayName = displayName;
		this.tooltip = tooltip;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag advanced) {
		if(tooltip != null) tooltips.add(tooltip);
    }

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return displayName;
	}
}
