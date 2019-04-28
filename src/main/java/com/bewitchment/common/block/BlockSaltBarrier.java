package com.bewitchment.common.block;

import com.bewitchment.Util;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.api.capability.extendedplayer.ExtendedPlayer;
import com.bewitchment.registry.ModObjects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockSaltBarrier extends BlockRedstoneWire {
	private static final AxisAlignedBB WALL = new AxisAlignedBB(0, -5, 0, 1, 5, 1);

	public BlockSaltBarrier() {
		super();
		Util.registerValues(this, "salt_barrier", Material.CIRCUITS, SoundType.SAND, 0, 0, "", 0);
		setCreativeTab(null);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(getItemDropped(state, world.rand, 0));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModObjects.salt;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return state.getBlock() == this;
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return 0;
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB box, List<AxisAlignedBB> boxes, Entity entity, boolean wut) {
		if (entity instanceof EntityLivingBase) {
			EnumCreatureAttribute attribute = ((EntityLivingBase) entity).getCreatureAttribute();
			if (attribute == EnumCreatureAttribute.UNDEAD || attribute == BewitchmentAPI.DEMON || attribute == BewitchmentAPI.SPIRIT || entity instanceof EntityBlaze || entity instanceof EntityGhast || entity instanceof EntityVex /* || entity instanceof EntityBatSwarm */ || (entity instanceof EntityPlayer && !((EntityPlayer) entity).isCreative() && !entity.getCapability(ExtendedPlayer.CAPABILITY, null).getTransformation().canCrossSalt)) addCollisionBoxToList(pos, box, boxes, WALL);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos to, Block block, BlockPos from) {
		if (!world.isRemote && !canPlaceBlockAt(world, to)) {
			dropBlockAsItem(world, to, state, 0);
			world.setBlockToAir(to);
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
	}
}