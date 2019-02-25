package com.bewitchment.registry.block.tile;

import com.bewitchment.core.CommonProxy.ModGui;
import com.bewitchment.core.Main;
import com.bewitchment.registry.block.ModBlock;
import com.bewitchment.registry.capability.IMagicPower;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class BlockDistillery extends ModBlock implements ITileEntityProvider
{
	private static final AxisAlignedBB BBOX_X = new AxisAlignedBB(0.125, 0, 0, 0.875, 0.6875, 1), BBOX_Z = new AxisAlignedBB(0, 0, 0.125, 1, 0.6875, 0.875);
	
	public BlockDistillery(String name)
	{
		super(name, Material.IRON, SoundType.METAL, 5, 5, "pickaxe", 0);
		this.setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.SOUTH));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new Tile();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ)
	{
		player.openGui(Main.instance, ModGui.DISTILLERY.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		return super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops") && hasTileEntity(state) && world.getTileEntity(pos) instanceof Tile)
		{
			Tile tile = (Tile) world.getTileEntity(pos);
			for (int i = 0; i < tile.fuel.getSlots(); i++) InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.fuel.getStackInSlot(i));
			for (int i = 0; i < tile.input.getSlots(); i++) InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.input.getStackInSlot(i));
			for (int i = 0; i < tile.output.getSlots(); i++) InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), tile.output.getStackInSlot(i));
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getValue(BlockHorizontal.FACING).getAxis() == Axis.Z ? BBOX_X : BBOX_Z;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.getValue(BlockHorizontal.FACING).getAxis() == Axis.Z ? BBOX_X : BBOX_Z;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity, EnumHand hand)
	{
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.fromAngle(entity.rotationYaw));
	}
	
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.HORIZONTALS[meta & 3]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockHorizontal.FACING);
	}
	
	public static class Tile extends TileEntity implements ITickable
	{
		public static final int BURN_TIME = 1200;
		
		private IMagicPower magic_power = IMagicPower.Provider.CAPABILITY.getDefaultInstance();
		
		private int progress, totalTime, burnTime;
		
		public final ItemStackHandler fuel = new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}
		};
		public final ItemStackHandler input = new ItemStackHandler(6)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}
		};
		public final ItemStackHandler output = new ItemStackHandler(6)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}
		};
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag)
		{
			markDirty();
			tag.setTag("fuel", fuel.serializeNBT());
			tag.setTag("input", input.serializeNBT());
			tag.setTag("output", output.serializeNBT());
			tag.setInteger("progress", progress);
			tag.setInteger("totalTime", totalTime);
			tag.setInteger("burnTime", burnTime);
			tag.setInteger("power", magic_power.getAmount());
			return super.writeToNBT(tag);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			fuel.deserializeNBT(tag.getCompoundTag("fuel"));
			input.deserializeNBT(tag.getCompoundTag("input"));
			output.deserializeNBT(tag.getCompoundTag("output"));
			progress = tag.getInteger("progress");
			totalTime = tag.getInteger("totalTime");
			burnTime = tag.getInteger("burnTime");
			magic_power.setAmount(tag.getInteger("power"));
			super.readFromNBT(tag);
		}
		
		@Override
		public SPacketUpdateTileEntity getUpdatePacket()
		{
			return new SPacketUpdateTileEntity(getPos(), 1, writeToNBT(new NBTTagCompound()));
		}
		
		@Override
		public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet)
		{
			readFromNBT(packet.getNbtCompound());
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing face)
		{
			return capability == IMagicPower.Provider.CAPABILITY ? true : super.hasCapability(capability, face);
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing face)
		{
			return capability == IMagicPower.Provider.CAPABILITY ? IMagicPower.Provider.CAPABILITY.cast(magic_power) : super.getCapability(capability, face);
		}
		
		@Override
		public void update()
		{
		}
	}
	
	public static class Container extends net.minecraft.inventory.Container
	{
		public final Tile tile;
		
		public Container(InventoryPlayer inventory, Tile tile)
		{
			this.tile = tile;
			for (int i = 0; i < 3; i++)
			{
				addSlotToContainer(new ModSlot(tile.input, i * 4, 18, (18 * (i + 1)) - 1));
				addSlotToContainer(new ModSlot(tile.input, 1 + (i * 4), 36, (18 * (i + 1)) - 1));
				addSlotToContainer(new ModSlot(tile.output, 2 + (i * 4), 124, (18 * (i + 1)) - 1, Items.AIR));
				addSlotToContainer(new ModSlot(tile.output, 3 + (i * 4), 142, (18 * (i + 1)) - 1, Items.AIR));
			}
			addSlotToContainer(new ModSlot(tile.fuel, 12, 80, 58, Items.BLAZE_POWDER));
			for (int i = 0; i < 3; i++) for (int j = 0; j < 9; j++) addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			for (int i = 0; i < 9; i++) addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
		
		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int index)
	    {
			return ItemStack.EMPTY;
	    }
		
		@Override
		public boolean canInteractWith(EntityPlayer player)
		{
			return !player.isSpectator();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class Gui extends GuiContainer
	{
		private static final ResourceLocation TEX = new ResourceLocation(Main.MOD_ID, "textures/gui/distillery.png");
		
		private final Container container;
		
		public Gui(Container container)
		{
			super(container);
			this.container = container;
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			renderHoveredToolTip(mouseX, mouseY);
		}
		
		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
		{
			GlStateManager.color(1, 1, 1);
			mc.getTextureManager().bindTexture(TEX);
			int x = (width - xSize) / 2;
			int y = (height - ySize) / 2;
			drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
			if (container.tile.totalTime > 0) drawTexturedModalRect(x + 76, y + 16, 176, 0, (container.tile.progress * 24 / container.tile.totalTime) + 1, 17);
			int burnProgress = 14 - (int) Math.ceil((14 * (container.tile.burnTime / (double) Tile.BURN_TIME)));
			drawTexturedModalRect(x + 81, y + 36 + burnProgress, 242, burnProgress, 14, 14 - burnProgress);
		}
	}
}