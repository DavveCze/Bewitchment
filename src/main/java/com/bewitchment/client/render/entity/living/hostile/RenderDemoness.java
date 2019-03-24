package com.bewitchment.client.render.entity.living.hostile;

import com.bewitchment.Bewitchment;
import com.bewitchment.client.model.entity.living.hostile.ModelDemoness;
import com.bewitchment.common.entity.hostile.EntityDemoness;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDemoness extends RenderLiving<EntityDemoness>
{
	private static final ResourceLocation[] TEX = {
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_0.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_1.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_2.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_3.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_4.png"),
		new ResourceLocation(Bewitchment.MOD_ID, "textures/entity/demoness_5.png")};
	
	public RenderDemoness(RenderManager manager)
	{
		super(manager, new ModelDemoness(), 0.3f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityDemoness entity)
	{
		return TEX[entity.getDataManager().get(EntityDemoness.SKIN)];
	}
	
	@Override
	protected boolean canRenderName(EntityDemoness entity)
	{
		return true;
	}
	
	@Override
	protected void preRenderCallback(EntityDemoness entity, float partialTickTime)
	{
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.scale(1.6, 1.6, 1.6);
	}
}