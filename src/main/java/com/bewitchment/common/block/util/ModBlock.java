package com.bewitchment.common.block.util;

import java.util.ArrayList;
import java.util.List;

import com.bewitchment.Bewitchment;
import com.bewitchment.registry.IOreDictionaryContainer;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlock extends Block implements IOreDictionaryContainer
{
	private final List<String> oreDictionaryNames = new ArrayList<String>();
	
	public ModBlock(String name, Material mat, SoundType sound, float hardness, float resistance, String tool, int level, String... oreDictionaryNames)
	{
		super(mat);
		Bewitchment.proxy.registerValues(this, name, mat, sound, hardness, resistance, tool, level, oreDictionaryNames);
	}
	
	@Override
	public List<String> getOreDictionaryNames()
	{
		return oreDictionaryNames;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return getDefaultState().getMaterial() == Material.ICE || getDefaultState().getMaterial() == Material.GLASS ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getMaterial() == Material.WOOD;
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state)
    {
		return state.getMaterial() == Material.ICE || state.getMaterial() == Material.GLASS ? false : super.isFullCube(state);
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getMaterial() == Material.ICE || state.getMaterial() == Material.GLASS ? false : super.isOpaqueCube(state);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
    {
		return world.getBlockState(pos).getMaterial() == Material.WOOD;
    }
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return super.shouldSideBeRendered(state, world, pos, face) && (state.getMaterial() == Material.ICE || state.getMaterial() == Material.GLASS ? world.getBlockState(pos.offset(face)).getBlock() != this : true);
	}
}