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

package josephcsible.oreberries;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import josephcsible.oreberries.config.GeneralConfig;
import josephcsible.oreberries.config.OreberryConfig;
import josephcsible.oreberries.item.ItemEssenceBerry;
import josephcsible.oreberries.item.ItemOreberry;
import josephcsible.oreberries.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockOreberryBush extends Block implements IPlantable, IGrowable {
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3); // small, medium, large, large with berries
	protected static final AxisAlignedBB[] OREBERRY_BUSH_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D),
			new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D),
			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
	};
	protected static final AxisAlignedBB[] OREBERRY_BUSH_COLLISION_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D),
			new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D),
			new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D)
	};

	public final ItemOreberry berries;
	public final OreberryConfig config;

	public BlockOreberryBush(String name, OreberryConfig config) {
		super(Material.LEAVES);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setHardness(0.3F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(CommonProxy.creativeTab);
		this.setUnlocalizedName(OreberriesMod.MODID + ".oreberry_bush"); // This should never be seen, but just in case...
		this.setRegistryName(OreberriesMod.MODID, name + "_oreberry_bush");
		this.config = config;
		if(ItemEssenceBerry.isEssence(config.special)) {
			berries = new ItemEssenceBerry(config.berryName, config.tooltip);
		} else {
			berries = new ItemOreberry(config.berryName, config.tooltip);
		}
		berries.setRegistryName(OreberriesMod.MODID, name + "_oreberry");
	}

	@Override
	public String getLocalizedName() {
		return config.bushName;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, AGE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(AGE);
	}

	/* The following methods define a berry bush's size depending on metadata */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return OREBERRY_BUSH_AABB[Math.min(state.getValue(AGE), 2)];
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
	{
		return OREBERRY_BUSH_COLLISION_AABB[Math.min(state.getValue(AGE), 2)];
	}

	/* Left-click harvests berries */
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		harvest(worldIn, pos, worldIn.getBlockState(pos), playerIn);
	}

	/* Right-click harvests berries */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return harvest(worldIn, pos, state, playerIn);
	}

	protected boolean harvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (state.getValue(AGE) >= 3)
		{
			if (world.isRemote)
				return true;

			world.setBlockState(pos, state.withProperty(AGE, 2));
			// TODO 1.7.10 would always drop this rather than give it to a FakePlayer; see if this causes issues
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(berries, world.rand.nextInt(3) + 1));
		}

		return false;
	}

	/* Render logic */

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return Minecraft.getMinecraft().gameSettings.fancyGraphics ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return !Minecraft.getMinecraft().gameSettings.fancyGraphics && state.getValue(AGE) >= 2;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess blockAccess, IBlockState state, BlockPos pos, EnumFacing facing) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(AGE) >= 2;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		if(side != EnumFacing.DOWN && state.getValue(AGE) < 2) {
			// This face is completely inside of our block, so it definitely has to be rendered
			return true;
		}
		if(!Minecraft.getMinecraft().gameSettings.fancyGraphics) {
			IBlockState touchingState = blockAccess.getBlockState(pos.offset(side));
			if(touchingState.getBlock() == this && touchingState.getValue(AGE) >= 2) {
				// Like vanilla leaves, don't render if we're on fast graphics and touching another one of ourself
				// Note that it can only possibly be touching if our neighbor is full-size
				return false;
			}
		}
		@SuppressWarnings("deprecation")
		boolean thisShouldNotBeDeprecated = super.shouldSideBeRendered(state, blockAccess, pos, side);
		return thisShouldNotBeDeprecated;
	}

	/* Bush growth */

	protected boolean ageAndLightOkayToGrow(World worldIn, BlockPos pos, IBlockState state) {
		return state.getValue(AGE) < 3 && (config.growsInLight || worldIn.getLight(pos) < 10);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!worldIn.isRemote && ageAndLightOkayToGrow(worldIn, pos, state) && ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextDouble() < GeneralConfig.tickGrowthChance)) {
			grow(worldIn, rand, pos, state);
			ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
		}
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		if (plantable instanceof BlockOreberryBush)
			return state.getValue(AGE) > 1;
		return super.canSustainPlant(state, world, pos, direction, plantable);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) &&
				(config.growsInLight || worldIn.getLight(pos) < 13) &&
				soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this) &&
				worldIn.isAirBlock(pos);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Cave;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.getDefaultState();
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if (!(entityIn instanceof EntityItem))
			entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
	}

	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return GeneralConfig.bonemealGrowthChance > 0.0D && ageAndLightOkayToGrow(worldIn, pos, state);
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return rand.nextDouble() < GeneralConfig.bonemealGrowthChance;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) + 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag advanced) {
		if(config.tooltip != null) tooltips.add(config.tooltip);
	}
}
