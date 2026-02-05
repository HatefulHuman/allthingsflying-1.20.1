// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
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

public class AerocraftItemThermalEngineModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "thermal_engine")), "main");
	private final ModelPart huoyantuowei;

	public AerocraftItemThermalEngineModel(ModelPart root) {
		this.huoyantuowei = root.getChild("huoyantuowei");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition huoyantuowei = partdefinition.addOrReplaceChild("huoyantuowei", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 1.0F));

		PartDefinition cube_r1 = huoyantuowei.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 3.1416F, 0.0F, -2.3562F));

		PartDefinition cube_r2 = huoyantuowei.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -3.1416F, 0.0F, -1.5708F));

		PartDefinition cube_r3 = huoyantuowei.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -3.1416F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = huoyantuowei.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r5 = huoyantuowei.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 3.1416F, 0.0F, 0.7854F));

		PartDefinition cube_r6 = huoyantuowei.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 3.1416F, 0.0F, 1.5708F));

		PartDefinition cube_r7 = huoyantuowei.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -3.1416F, 0.0F, 2.3562F));

		PartDefinition cube_r8 = huoyantuowei.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(-106, 0).addBox(-4.245F, -8.995F, -104.505F, 8.0F, 0.0F, 106.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition cube_r9 = huoyantuowei.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -2.7489F));

		PartDefinition cube_r10 = huoyantuowei.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, -1.9635F));

		PartDefinition cube_r11 = huoyantuowei.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, -1.1781F));

		PartDefinition cube_r12 = huoyantuowei.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -0.3927F));

		PartDefinition cube_r13 = huoyantuowei.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.3927F));

		PartDefinition cube_r14 = huoyantuowei.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 1.1781F));

		PartDefinition cube_r15 = huoyantuowei.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 1.9635F));

		PartDefinition cube_r16 = huoyantuowei.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(20, 68).addBox(-4.99F, -11.99F, -42.01F, 10.0F, 0.0F, 44.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 2.7489F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		huoyantuowei.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}