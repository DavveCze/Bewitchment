package com.bewitchment.common.entity.misc;

import com.bewitchment.api.registry.entity.EntityBroom;
import net.minecraft.world.World;

public class EntityJuniperBroom extends EntityBroom {
	public EntityJuniperBroom(World world) {
		super(world);
	}
	
	@Override
	protected float getSpeed() {
		return 0.8f;
	}
	
	@Override
	protected float getMaxSpeed() {
		return 1;
	}
	
	@Override
	protected int getMagicCost() {
		return 0;
	}
	
}