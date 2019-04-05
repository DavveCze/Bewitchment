package com.bewitchment.common.item;

import java.util.List;

import com.bewitchment.common.item.util.ModItem;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWaystone extends ModItem
{
	public ItemWaystone()
	{
		super("waystone");
		setMaxStackSize(1);
		setMaxDamage(3);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("location"))
		{
			BlockPos pos = BlockPos.fromLong(stack.getTagCompound().getLong("location"));
			tooltip.add(I18n.format("tooltip." + getTranslationKey().substring(5), pos.getX(), pos.getY(), pos.getZ(), stack.getTagCompound().getString("dimensionName")));
		}
		else tooltip.add(I18n.format("tooltip." + getTranslationKey().substring(5) + ".unbound"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("location"))
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setLong("location", player.getPosition().toLong());
			stack.getTagCompound().setInteger("dimension", player.dimension);
			stack.getTagCompound().setString("dimensionName", world.provider.getDimensionType().getName());
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return super.onItemRightClick(world, player, hand);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return (stack.hasTagCompound() && stack.getTagCompound().hasKey("location")) || super.hasEffect(stack);
	}
}