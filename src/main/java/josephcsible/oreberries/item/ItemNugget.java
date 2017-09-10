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
