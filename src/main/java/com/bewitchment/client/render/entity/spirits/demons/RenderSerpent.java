package com.bewitchment.client.render.entity.spirits.demons;

import com.bewitchment.Bewitchment;
import com.bewitchment.client.model.entity.spirits.demons.ModelSerpent;
import com.bewitchment.common.entity.spirits.demons.EntitySerpent;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSerpent extends RenderLiving<EntitySerpent> {
	private static final ResourceLocation[] TEX = {
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_0.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_1.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_2.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_3.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_4.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_5.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_6.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_7.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/serpent_8.png")};

	public RenderSerpent(RenderManager manager) {
		super(manager, new ModelSerpent(), 0.3f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySerpent entity) {
		return TEX[entity.getDataManager().get(EntitySerpent.SKIN)];
	}
}