package com.bewitchment.common.entity.living;

import com.bewitchment.Bewitchment;
import com.bewitchment.common.entity.util.ModEntityTameable;
import com.bewitchment.registry.ModObjects;
import com.bewitchment.registry.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityToad extends ModEntityTameable {
	public static final DataParameter<Integer> ANIMATION_TIME = EntityDataManager.createKey(EntityToad.class, DataSerializers.VARINT);
	public static final DataParameter<Float> ANIMATION_HEIGHT = EntityDataManager.createKey(EntityToad.class, DataSerializers.FLOAT);

	public EntityToad(World world) {
		super(world, new ResourceLocation(Bewitchment.MODID, "entities/toad"), Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE, ModObjects.silver_scales, ModObjects.envenomed_fang);
		setSize(1, 0.3f);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable other) {
		EntityAgeable entity = new EntityToad(world);
		entity.getDataManager().set(SKIN, world.rand.nextBoolean() ? getDataManager().get(SKIN) : other.getDataManager().get(SKIN));
		return entity;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ModSounds.TOAD_IDLE;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSounds.TOAD_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSounds.TOAD_HURT;
	}

	@Override
	protected int getSkinTypes() {
		return 4;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == ModObjects.envenomed_fang;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue())) {
			applyEnchantments(this, entity);
			if (entity instanceof EntityLivingBase)
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 2000, 1, false, false));
		}
		return super.attackEntityAsMob(entity);
	}

	@Override
	public boolean canMateWith(EntityAnimal other) {
		if (other == this || !(other instanceof EntityToad)) return false;
		return isTamed() && isInLove() && ((EntityTameable) other).isTamed() && other.isInLove() && !((EntityTameable) other).isSitting();
	}

	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		return effect.getPotion() != MobEffects.SLOWNESS && super.isPotionApplicable(effect);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.5);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(ANIMATION_TIME, 0);
		dataManager.register(ANIMATION_HEIGHT, 0f);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(0, new EntityAIMate(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() / 2));
		tasks.addTask(1, new EntityAIAttackMelee(this, getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(), false));
		tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 5, 1));
		tasks.addTask(2, aiSit);
		tasks.addTask(3, new EntityAIFollowParent(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		tasks.addTask(3, new EntityAIWander(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 2 / 3));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(4, new EntityAIFollowOwner(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue(), 2, 5));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(0, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
		targetTasks.addTask(2, new EntityAITargetNonTamed<>(this, EntityPlayer.class, true, p -> p.getDistanceSq(this) < 1));
		targetTasks.addTask(3, new EntityAITargetNonTamed<>(this, EntityLivingBase.class, false, e -> e instanceof EntityEndermite || e instanceof EntitySilverfish));
	}
}