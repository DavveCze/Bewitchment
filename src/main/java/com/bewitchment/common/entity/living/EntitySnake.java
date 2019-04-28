package com.bewitchment.common.entity.living;

import com.bewitchment.Bewitchment;
import com.bewitchment.common.entity.spirits.demons.EntitySerpent;
import com.bewitchment.common.entity.util.ModEntityTameable;
import com.bewitchment.registry.ModObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntitySnake extends ModEntityTameable {
	public int animationTimer = 0;
	private int milkTimer = 0;

	public EntitySnake(World world) {
		super(world, new ResourceLocation(Bewitchment.MODID, "entities/snake"), Items.CHICKEN, Items.RABBIT);
		setSize(1, 0.3f);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable other) {
		EntityAgeable entity = new EntitySnake(world);
		entity.getDataManager().set(SKIN, world.rand.nextBoolean() ? getDataManager().get(SKIN) : other.getDataManager().get(SKIN));
		return entity;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.RABBIT;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue())) {
			applyEnchantments(this, entity);
			if (entity instanceof EntityLivingBase)
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 2000, 1, false, false));
		}
		return super.attackEntityAsMob(entity);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() != null && !(source.getTrueSource() instanceof EntityPlayer) && !(source.getTrueSource() instanceof EntityArrow))
			amount = (amount + 1) / 2f;
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public boolean canMateWith(EntityAnimal other) {
		if (other == this || !(other instanceof EntitySnake)) return false;
		return isTamed() && isInLove() && ((EntityTameable) other).isTamed() && other.isInLove() && !((EntityTameable) other).isSitting();
	}

	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		return effect.getPotion() != MobEffects.POISON && super.isPotionApplicable(effect);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && (getAttackTarget() == null || getAttackTarget().isDead || getRevengeTarget() == null || getRevengeTarget().isDead)) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() == Items.GLASS_BOTTLE) {
				if (milkTimer == 0 && getRNG().nextBoolean()) {
					if (getGrowingAge() >= 0) {
						world.playSound(null, getPosition(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1, 1);
						stack.shrink(1);
						if (stack.isEmpty()) player.setHeldItem(hand, new ItemStack(ModObjects.snake_venom));
						else if (!player.inventory.addItemStackToInventory(new ItemStack(ModObjects.snake_venom)))
							player.dropItem(new ItemStack(ModObjects.snake_venom), false);
						milkTimer = 3600;
						return true;
					}
				} else {
					setAttackTarget(player);
					setRevengeTarget(player);
				}
			}
		}
		return super.processInteract(player, hand);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}

	@Override
	protected int getSkinTypes() {
		return 6;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (milkTimer > 0) milkTimer--;
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt bolt) {
		if (!world.isRemote && !isDead) {
			EntitySerpent entity = new EntitySerpent(world);
			entity.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			entity.setNoAI(isAIDisabled());
			if (hasCustomName()) {
				entity.setCustomNameTag(getCustomNameTag());
				entity.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
			}
			world.spawnEntity(entity);
			setDead();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("animationTimer", animationTimer);
		tag.setInteger("milkTimer", milkTimer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		animationTimer = tag.getInteger("animationTimer");
		milkTimer = tag.getInteger("milkTimer");
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(15);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6);
	}

	//Todo: Revised version of MobHelper. https://github.com/Um-Mitternacht/Bewitchment/blob/diabolic-update/src/main/java/com/bewitchment/common/core/helper/MobHelper.java
	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(0, new EntityAIMate(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / 2));
		tasks.addTask(1, new EntityAIAttackMelee(this, 0.5, false));
		tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 5, 1));
		tasks.addTask(2, aiSit);
		tasks.addTask(3, new EntityAIFollowParent(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		tasks.addTask(3, new EntityAIWander(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 2 / 3));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(4, new EntityAIFollowOwner(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(), 2, 5));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(2, new EntityAITargetNonTamed<>(this, EntityPlayer.class, false, p -> p.getDistanceSq(this) < 1));
		targetTasks.addTask(3, new EntityAITargetNonTamed<>(this, EntityLivingBase.class, false, e -> e instanceof EntityBlindworm || e instanceof EntityChicken || e instanceof EntityLizard || e instanceof EntityRabbit || e instanceof EntityParrot || e.getClass().getName().equals("seraphaestus.historicizedmedicine.Mob.Rat.EntityRat")));
	}
}