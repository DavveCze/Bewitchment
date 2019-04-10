package com.bewitchment.common.item;

import com.bewitchment.common.item.util.ModItem;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSalt extends ModItem {
	public ItemSalt() {
		super("salt", "salt", "itemSalt", "dustSalt", "foodSalt", "listAllSalt", "ingredientSalt", "pinchSalt", "portionSalt", "lumpSalt", "materialSalt");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		BlockPos pos0 = world.getBlockState(pos).getBlock().isReplaceable(world, pos) ? pos : pos.offset(face);
		ItemStack stack = player.getHeldItem(hand);
		if (player.canPlayerEdit(pos0, face, stack) && world.mayPlace(world.getBlockState(pos0).getBlock(), pos0, false, face, player) && ModObjects.salt_barrier.canPlaceBlockAt(world, pos0)) {
			stack.shrink(1);
			world.setBlockState(pos0, ModObjects.salt_barrier.getDefaultState());
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}