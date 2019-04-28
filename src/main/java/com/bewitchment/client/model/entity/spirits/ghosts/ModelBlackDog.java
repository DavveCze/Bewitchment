package com.bewitchment.client.model.entity.spirits.ghosts;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * blackdog - cybercat5555
 * Created using Tabula 5.1.0
 */
public class ModelBlackDog extends ModelBase {
	public ModelRenderer leftforeleg;
	public ModelRenderer body;
	public ModelRenderer leftHindleg;
	public ModelRenderer rightHindleg;
	public ModelRenderer rightforeleg;
	public ModelRenderer mane00;
	public ModelRenderer tail;
	public ModelRenderer neck;
	public ModelRenderer mane02;
	public ModelRenderer mane01;
	public ModelRenderer head;
	public ModelRenderer leftEar00;
	public ModelRenderer rightEar00;
	public ModelRenderer muzzle;
	public ModelRenderer lowerJaw;
	public ModelRenderer leftEar01;
	public ModelRenderer rightEar01;

	public ModelBlackDog() {
		this.textureWidth  = 64;
		this.textureHeight = 64;
		this.leftHindleg   = new ModelRenderer(this, 0, 18);
		this.leftHindleg.setRotationPoint(1.5f, 16, 6);
		this.leftHindleg.addBox(-1, 0, -1, 2, 8, 2, 0);
		this.tail = new ModelRenderer(this, 9, 18);
		this.tail.setRotationPoint(0, 5.7f, 2);
		this.tail.addBox(-1, 0, -1, 2, 9, 2, 0);
		this.setRotateAngle(tail, -0.87f, 0, 0);
		this.neck = new ModelRenderer(this, 0, 32);
		this.neck.setRotationPoint(0, -5.4f, 0.5f);
		this.neck.addBox(-2.5f, -2.5f, -4, 5, 5, 4, 0);
		this.setRotateAngle(neck, -1.57f, 0, 0);
		this.muzzle = new ModelRenderer(this, 0, 10);
		this.muzzle.setRotationPoint(0, 0.7f, -3.9f);
		this.muzzle.addBox(-1.5f, -1, -3, 3, 2, 3, 0);
		this.rightEar01        = new ModelRenderer(this, 39, 14);
		this.rightEar01.mirror = true;
		this.rightEar01.setRotationPoint(-1.9f, -0.9f, 0);
		this.rightEar01.addBox(-0.5f, -0.1f, -1, 1, 3, 2, 0);
		this.setRotateAngle(rightEar01, 0, 0, -0.23f);
		this.mane01 = new ModelRenderer(this, 0, 48);
		this.mane01.setRotationPoint(0, -1.8f, -3);
		this.mane01.addBox(-3, -1, 0, 6, 2, 7, 0);
		this.setRotateAngle(mane01, 0.44f, 0, 0);
		this.leftforeleg = new ModelRenderer(this, 0, 18);
		this.leftforeleg.setRotationPoint(1.5f, 16, -4);
		this.leftforeleg.addBox(-1, 0, -1, 2, 8, 2, 0);
		this.mane00 = new ModelRenderer(this, 21, 0);
		this.mane00.setRotationPoint(0, -4, 0);
		this.mane00.addBox(-4, -3.5f, -3.01f, 8, 6, 7, 0);
		this.body = new ModelRenderer(this, 18, 14);
		this.body.setRotationPoint(0, 14, 1);
		this.body.addBox(-3, -2, -3, 6, 9, 6, 0);
		this.setRotateAngle(body, 1.57f, 0, 0);
		this.leftEar00 = new ModelRenderer(this, 16, 14);
		this.leftEar00.setRotationPoint(2, -2, -2);
		this.leftEar00.addBox(0, -1, -1, 2, 1, 2, 0);
		this.setRotateAngle(leftEar00, 0, 0, -0.55f);
		this.rightforeleg        = new ModelRenderer(this, 0, 18);
		this.rightforeleg.mirror = true;
		this.rightforeleg.setRotationPoint(-1.5f, 16, -4);
		this.rightforeleg.addBox(-1, 0, -1, 2, 8, 2, 0);
		this.leftEar01 = new ModelRenderer(this, 39, 14);
		this.leftEar01.setRotationPoint(1.9f, -0.9f, 0);
		this.leftEar01.addBox(-0.5f, -0.1f, -1, 1, 3, 2, 0);
		this.setRotateAngle(leftEar01, 0, 0, 0.23f);
		this.rightEar00        = new ModelRenderer(this, 16, 14);
		this.rightEar00.mirror = true;
		this.rightEar00.setRotationPoint(-2, -2, -2);
		this.rightEar00.addBox(-2, -1, -1, 2, 1, 2, 0);
		this.setRotateAngle(rightEar00, 0, 0, 0.55f);
		this.rightHindleg        = new ModelRenderer(this, 0, 18);
		this.rightHindleg.mirror = true;
		this.rightHindleg.setRotationPoint(-1.5f, 16, 6);
		this.rightHindleg.addBox(-1, 0, -1, 2, 8, 2, 0);
		this.mane02 = new ModelRenderer(this, 28, 48);
		this.mane02.setRotationPoint(0, -1, 2.7f);
		this.mane02.addBox(-3.5f, -1, 0, 7, 2, 7, 0);
		this.setRotateAngle(mane02, -1.29f, 0, 0);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0, 0, -2.9f);
		this.head.addBox(-3, -3, -4, 6, 6, 4, 0);
		this.lowerJaw = new ModelRenderer(this, 0, 43);
		this.lowerJaw.setRotationPoint(0, 2, -3.8f);
		this.lowerJaw.addBox(-1.5f, -0.4f, -3, 3, 1, 3, 0);
		this.body.addChild(this.tail);
		this.body.addChild(this.neck);
		this.head.addChild(this.muzzle);
		this.rightEar00.addChild(this.rightEar01);
		this.neck.addChild(this.mane01);
		this.body.addChild(this.mane00);
		this.head.addChild(this.leftEar00);
		this.leftEar00.addChild(this.leftEar01);
		this.head.addChild(this.rightEar00);
		this.mane00.addChild(this.mane02);
		this.neck.addChild(this.head);
		this.head.addChild(this.lowerJaw);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch, float scale) {
		this.leftHindleg.render(scale);
		this.leftforeleg.render(scale);
		this.body.render(scale);
		this.rightforeleg.render(scale);
		this.rightHindleg.render(scale);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer renderer, float x, float y, float z) {
		renderer.rotateAngleX = x;
		renderer.rotateAngleY = y;
		renderer.rotateAngleZ = z;
	}
}