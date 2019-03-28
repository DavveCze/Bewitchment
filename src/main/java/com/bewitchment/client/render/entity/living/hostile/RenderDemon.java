package com.bewitchment.client.render.entity.living.hostile;

import com.bewitchment.Bewitchment;
import com.bewitchment.client.model.entity.living.hostile.ModelDemon;
import com.bewitchment.common.entity.spirits.demons.EntityDemon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDemon extends RenderLiving<EntityDemon>
{
	private static final ResourceLocation[] TEX = {
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_0.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_1.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_2.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_3.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_4.png"),
			new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demon_5.png")};
	
	public RenderDemon(RenderManager manager)
	{
		super(manager, new ModelDemon(), 0.3f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityDemon entity)
	{
		return TEX[entity.getDataManager().get(EntityDemon.SKIN)];
	}
	
	@Override
	protected boolean canRenderName(EntityDemon entity)
	{
		return true;
	}
	
	@Override
	protected void preRenderCallback(EntityDemon entity, float partialTickTime)
	{
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.scale(1.6, 1.6, 1.6);
	}
}