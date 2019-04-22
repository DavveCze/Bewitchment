package com.bewitchment.common.entity.spirits.demons;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.BewitchmentAPI;
import com.bewitchment.common.entity.util.ModEntityMob;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDemon extends ModEntityMob implements IMerchant {
	public static final Animation TOSS = Animation.create(10);

	private MerchantRecipeList recipeList;
	private EntityPlayer buyer;
	private int careerID, careerLevel, wealth;

	public EntityDemon(World world) {
		super(world, new ResourceLocation(Bewitchment.MOD_ID, "entities/demon"));
		setSize(1.8f, 4.6f);
		isImmuneToFire = true;
		setPathPriority(PathNodeType.WATER, -1);
		setPathPriority(PathNodeType.LAVA, 8);
		setPathPriority(PathNodeType.DANGER_FIRE, 0);
		setPathPriority(PathNodeType.DAMAGE_FIRE, 0);
		experienceValue = 165;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{IAnimatedEntity.NO_ANIMATION, TOSS};
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	@Override
	public void setCustomer(EntityPlayer player) {
		buyer = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyer;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return recipeList;
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {
		this.recipeList = recipeList;
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public BlockPos getPos() {
		return getPos();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return BewitchmentAPI.DEMON;
	}

	@Override
	protected int getSkinTypes() {
		return 6;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data) {
		setCustomNameTag((rand.nextInt(3) == 0 ? new TextComponentTranslation("demon_prefix_" + rand.nextInt(53)).getFormattedText() + " " : "") + new TextComponentTranslation("demon_name_" + rand.nextInt(326)).getFormattedText());
		return super.onInitialSpawn(difficulty, data);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		super.attackEntityAsMob(entity);
		boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
		if (flag) {
			applyEnchantments(this, entity);
			if (entity instanceof EntityLivingBase && getAnimation() != TOSS) {
				setAnimation(TOSS);
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 2000, 1, false, false));
				entity.setFire(25);
				entity.motionY += 0.6;
			}
		}
		return flag;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}


	public float getBrightness() {
		return 0.3F;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		return effect.getPotion() != MobEffects.POISON && effect.getPotion() != MobEffects.WITHER && super.isPotionApplicable(effect);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setInteger("careerID", careerID);
		tag.setInteger("careerLevel", careerLevel);
		tag.setInteger("wealth", wealth);
		if (recipeList != null) tag.setTag("recipeList", recipeList.getRecipiesAsTags());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		careerID = tag.getInteger("careerID");
		careerLevel = tag.getInteger("careerLevel");
		wealth = tag.getInteger("wealth");
		if (tag.hasKey("recipeList")) recipeList.readRecipiesFromTags((NBTTagCompound) tag.getTag("recipeList"));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(16);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.66);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(175);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAttackMelee(this, 0.5, false));
		tasks.addTask(2, new EntityAIWatchClosest2(this, EntityPlayer.class, 5, 1));
		tasks.addTask(3, new EntityAILookIdle(this));
		tasks.addTask(3, new EntityAIWander(this, getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * (2 / 3d)));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, false, false, p -> p.getDistanceSq(this) < 2));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class, 10, false, false, e -> !e.isImmuneToFire()));
	}
}