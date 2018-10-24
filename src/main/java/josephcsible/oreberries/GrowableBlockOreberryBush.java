/*
Oreberries Minecraft Mod
Copyright (C) 2018 Joseph C. Sible

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

package josephcsible.oreberries;

import java.util.Random;

import josephcsible.oreberries.config.GeneralConfig;
import josephcsible.oreberries.config.OreberryConfig;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowableBlockOreberryBush extends BlockOreberryBush implements IGrowable {
	public GrowableBlockOreberryBush(String name, OreberryConfig config) {
		super(name, config);
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return ageAndLightOkayToGrow(worldIn, pos, state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return rand.nextDouble() < GeneralConfig.bonemealGrowthChance;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		super.grow(worldIn, pos, state);
	}
}
