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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import josephcsible.oreberries.BlockOreberryBush;
import josephcsible.oreberries.proxy.CommonProxy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreberriesWorldGen implements IWorldGenerator {
	protected List<WorldGenOreberryBush> bushes = new ArrayList<>();

	public OreberriesWorldGen() {
		for(BlockOreberryBush block : CommonProxy.oreberryBushBlocks) {
			bushes.add(new WorldGenOreberryBush(block));
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == 0 && world.getWorldInfo().getTerrainType() != WorldType.FLAT) {
			generateOreBushes(random, chunkX * 16 + 8, chunkZ * 16 + 8, world);
		}
	}

	protected void generateOreBushes (Random random, int xChunk, int zChunk, World world)
	{
		for(WorldGenOreberryBush gen : bushes) {
			generateOreBush(random, xChunk, zChunk, world, gen, gen.oreberryConfig.getPreferredHeight(world), gen.oreberryConfig.getMaxHeight(world), gen.oreberryConfig.minHeight);
		}
	}

	protected void generateOreBush(Random random, int xChunk, int zChunk, World world, WorldGenOreberryBush bush, int y, int heightLimit, int depthLimit) {
		if(bush.oreberryConfig.rarity > 0 && random.nextInt(bush.oreberryConfig.rarity) == 0) {
			for (int i = 0; i < bush.oreberryConfig.density; i++)
			{
				BlockPos pos = findAdequateLocation(world, xChunk + random.nextInt(16), y, zChunk + random.nextInt(16), heightLimit, depthLimit);
				if (pos != null)
				{
					bush.generate(world, random, pos);
				}
			}
		}
	}

	protected BlockPos findAdequateLocation (World world, int x, int y, int z, int heightLimit, int depthLimit)
	{
		BlockPos pos = new BlockPos(x, y, z);
		do
		{
			if(world.isAirBlock(pos) && !world.isAirBlock(pos.up()))
				return pos.up();
			pos = pos.up();
		} while (pos.getY() < heightLimit);

		pos = new BlockPos(x, y, z);
		do
		{
			if(world.isAirBlock(pos) && !world.isAirBlock(pos.down()))
				return pos.down();
			pos = pos.down();
		} while (pos.getY() > depthLimit);

		return null;
	}

}
