package com.bewitchment.registry.handler;

import com.bewitchment.core.CommonProxy.ModGui;
import com.bewitchment.registry.block.tile.BlockDistillery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof BlockDistillery.Tile) return new BlockDistillery.Container(player.inventory, (BlockDistillery.Tile) tile);
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile instanceof BlockDistillery.Tile) return new BlockDistillery.Gui((BlockDistillery.Container) getServerGuiElement(ModGui.DISTILLERY.ordinal(), player, world, x, y, z));
		return null;
	}
}