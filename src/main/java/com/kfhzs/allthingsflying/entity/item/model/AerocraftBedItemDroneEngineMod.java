// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.kfhzs.allthingsflying.entity.item.model;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import com.kfhzs.allthingsflying.entity.animations.AerocraftItemDroneEngineAnimation;
import com.kfhzs.allthingsflying.entity.item.AerocraftItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class AerocraftBedItemDroneEngineMod<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "drone_engine_bed")), "main");
	private final ModelPart qixuan;
	private final ModelPart xf1;
	private final ModelPart xf4;
	private final ModelPart xf2;
	private final ModelPart xf3;

	public AerocraftBedItemDroneEngineMod(ModelPart root) {
		this.qixuan = root.getChild("qixuan");
		this.xf1 = this.qixuan.getChild("xf1");
		this.xf4 = this.qixuan.getChild("xf4");
		this.xf2 = this.qixuan.getChild("xf2");
		this.xf3 = this.qixuan.getChild("xf3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition qixuan = partdefinition.addOrReplaceChild("qixuan", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition xf1 = qixuan.addOrReplaceChild("xf1", CubeListBuilder.create().texOffs(22, 64).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offset(21.0F, 0.0F, -39.0F));

		PartDefinition cube_r1 = xf1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(81, 32).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r2 = xf1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 32).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r3 = xf1.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(67, 0).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition cube_r4 = xf1.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r5 = xf1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(108, 96).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r6 = xf1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 96).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r7 = xf1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(96, 64).addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition xf4 = qixuan.addOrReplaceChild("xf4", CubeListBuilder.create().texOffs(22, 64).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offset(21.0F, 0.0F, 39.0F));

		PartDefinition cube_r8 = xf4.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(81, 32).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r9 = xf4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(8, 32).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r10 = xf4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(67, 0).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r11 = xf4.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

		PartDefinition cube_r12 = xf4.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(108, 96).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition cube_r13 = xf4.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(40, 96).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r14 = xf4.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(96, 64).addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition xf2 = qixuan.addOrReplaceChild("xf2", CubeListBuilder.create().texOffs(22, 64).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offset(-21.0F, 0.0F, -39.0F));

		PartDefinition cube_r15 = xf2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(81, 32).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r16 = xf2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 32).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r17 = xf2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(67, 0).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r18 = xf2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));

		PartDefinition cube_r19 = xf2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(108, 96).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition cube_r20 = xf2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(40, 96).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r21 = xf2.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(96, 64).mirror().addBox(-5.0F, -18.0F, 12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition xf3 = qixuan.addOrReplaceChild("xf3", CubeListBuilder.create().texOffs(22, 64).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offset(-21.0F, 0.0F, 39.0F));

		PartDefinition cube_r22 = xf3.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(81, 32).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r23 = xf3.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(8, 32).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r24 = xf3.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(67, 0).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));

		PartDefinition cube_r25 = xf3.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r26 = xf3.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(108, 96).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r27 = xf3.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(40, 96).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r28 = xf3.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(96, 64).mirror().addBox(-5.0F, -18.0F, -12.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.03F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(((AerocrafEntity) entity).animationState,AerocraftItemDroneEngineAnimation.WURENJI_IDLE,ageInTicks,1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		qixuan.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public ModelPart root() {
		return this.qixuan;
	}
}