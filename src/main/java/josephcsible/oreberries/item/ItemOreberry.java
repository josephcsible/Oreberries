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
