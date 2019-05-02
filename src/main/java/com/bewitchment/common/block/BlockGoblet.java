package com.bewitchment.common.block;

import com.bewitchment.common.block.util.ModBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@SuppressWarnings("deprecation")
public class BlockGoblet extends ModBlock {
	private static final AxisAlignedBB BOX = new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.375, 0.625);
	private final boolean filled;

	public BlockGoblet(boolean filled) {
		super(filled ? "filled_goblet" : "goblet", Material.IRON, SoundType.METAL, 0.3f, 1, "pickaxe", 0);
		setLightOpacity(0);
		this.filled = filled;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return BOX;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return filled;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return filled ? 8 : 0;
	}
}