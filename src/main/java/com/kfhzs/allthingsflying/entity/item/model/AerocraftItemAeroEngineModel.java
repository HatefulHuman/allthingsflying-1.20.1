package com.kfhzs.allthingsflying.entity.item.model;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class AerocraftItemAeroEngineModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "rideable_item")), "main");
	private final ModelPart bone4;
	private final ModelPart bone2;
	private final ModelPart bone3;

	public AerocraftItemAeroEngineModel(ModelPart root) {
		this.bone4 = root.getChild("bone4");
		this.bone2 = this.bone4.getChild("bone2");
		this.bone3 = this.bone4.getChild("bone3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone4 = partdefinition.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 17.6711F, -21.0F));

		PartDefinition bone2 = bone4.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.1174F, -0.329F, -0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(19, 115).addBox(-84.0F, -45.5537F, 0.329F, 84.0F, 92.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, -0.005F, 27.0573F, -3.1416F, 1.3963F, 1.5708F));

		PartDefinition cube_r2 = bone2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(102, 18).addBox(-27.0F, -45.5537F, 0.329F, 27.0F, 92.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition bone3 = bone4.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.2249F, -0.329F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r3 = bone3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(19, 115).addBox(-84.0F, -46.4463F, 0.329F, 84.0F, 92.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.005F, 27.0571F, 3.1416F, 1.3963F, -1.5708F));

		PartDefinition cube_r4 = bone3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(102, 18).addBox(-27.0F, -46.4463F, 0.329F, 27.0F, 92.0F, 0.0F, new CubeDeformation(0.03F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//		if (entity instanceof RideableItemEntity rideableItem) {
//			// 根据实体旋转调整模型部件
//			bone4.yRot = netHeadYaw * Mth.DEG_TO_RAD; // 跟随Y轴旋转
//			bone4.xRot = headPitch * Mth.DEG_TO_RAD * 0.5f; // 跟随X轴旋转（减半）
//
//			// 添加一些动态效果
//			float rotationSpeed = rideableItem.getPlayerSpeed() > 0 ? 3.0f : 1.0f;
//			bone4.zRot = ageInTicks * rotationSpeed * 0.1f; // 缓慢旋转
//		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone4.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}