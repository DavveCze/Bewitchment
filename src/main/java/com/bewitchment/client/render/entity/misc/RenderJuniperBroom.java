package com.bewitchment.client.render.entity.misc;

import com.bewitchment.Bewitchment;
import com.bewitchment.api.entity.misc.EntityBroom;
import com.bewitchment.api.entity.misc.RenderBroom;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderJuniperBroom extends RenderBroom {
	private static final ResourceLocation TEX = new ResourceLocation(Bewitchment.MODID, "textures/entity/broom_juniper.png");

	public RenderJuniperBroom(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBroom entity) {
		return TEX;
	}
}