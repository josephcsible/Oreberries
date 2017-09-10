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

package josephcsible.oreberries.worldgen;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import josephcsible.oreberries.BlockOreberryBush;
import josephcsible.oreberries.config.OreberryConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenOreberryBush extends WorldGenerator
{
	public final OreberryConfig oreberryConfig;
	private final IBlockState newState;
	private static final Set<Block> replaceBlocks = new HashSet<>(Arrays.asList(Blocks.STONE, Blocks.GRASS, Blocks.DIRT, Blocks.WATER, Blocks.SAND, Blocks.GRAVEL, Blocks.SNOW));

	public WorldGenOreberryBush(BlockOreberryBush block)
	{
		this.newState = block.getDefaultState().withProperty(BlockOreberryBush.AGE, 3);
		this.oreberryConfig = block.config;
	}

	@Override
	public boolean generate (World world, Random random, BlockPos pos)
	{
		int type = random.nextInt(oreberryConfig.sizeChance);
		if (type == 11)
			generateMediumNode(world, random, pos);
		else if (type >= 5)
			generateSmallNode(world, random, pos);
		else
			generateTinyNode(world, random, pos);

		return true;
	}

	public void generateMediumNode (World world, Random random, BlockPos pos)
	{
		for (int xPos = -1; xPos <= 1; xPos++)
			for (int yPos = -1; yPos <= 1; yPos++)
				for (int zPos = -1; zPos <= 1; zPos++)
					if (random.nextInt(4) == 0)
						generateBerryBlock(world, pos.add(xPos, yPos, zPos));

		generateSmallNode(world, random, pos);
	}

	public void generateSmallNode (World world, Random random, BlockPos pos)
	{
		generateBerryBlock(world, pos);
		if (random.nextBoolean())
			generateBerryBlock(world, pos.east());
		if (random.nextBoolean())
			generateBerryBlock(world, pos.west());
		if (random.nextBoolean())
			generateBerryBlock(world, pos.south());
		if (random.nextBoolean())
			generateBerryBlock(world, pos.north());
		if (random.nextInt(4) != 0)
			generateBerryBlock(world, pos.up());
		// In 1.7.10, a typo led to up being checked twice, and down not being checked at all.
		// We emulate the effective probabilities of that here, but in a more obvious and efficient way.
	}

	public void generateTinyNode (World world, Random random, BlockPos pos)
	{
		generateBerryBlock(world, pos);
		if (random.nextInt(4) == 0)
			generateBerryBlock(world, pos.east());
		if (random.nextInt(4) == 0)
			generateBerryBlock(world, pos.west());
		if (random.nextInt(4) == 0)
			generateBerryBlock(world, pos.south());
		if (random.nextInt(4) == 0)
			generateBerryBlock(world, pos.north());
		if (random.nextInt(16) < 7)
			generateBerryBlock(world, pos.up());
		// In 1.7.10, a typo led to up being checked twice, and down not being checked at all.
		// We emulate the effective probabilities of that here, but in a more obvious and efficient way.
	}

	void generateBerryBlock (World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		if (world.isAirBlock(pos) || (state.getBlockHardness(world, pos) >= 0 && !state.isFullBlock()) ||
				state.getBlock().isReplaceableOreGen(state, world, pos, (s) -> replaceBlocks.contains(s.getBlock())
		))
			world.setBlockState(pos, newState, 2);
	}
}
