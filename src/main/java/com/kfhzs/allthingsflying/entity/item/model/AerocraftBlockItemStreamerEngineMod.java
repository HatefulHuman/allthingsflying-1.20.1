package com.kfhzs.allthingsflying.entity.item.model;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

public class AerocraftBlockItemStreamerEngineMod<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "streamer_block_item")), "main");
	private final ModelPart liuguang;

	public AerocraftBlockItemStreamerEngineMod(ModelPart root) {
		this.liuguang = root.getChild("liuguang");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition liuguang = partdefinition.addOrReplaceChild("liuguang", CubeListBuilder.create().texOffs(-84, 20).addBox(-23.0F, 29.0F, 0.0F, 46.0F, 0.0F, 97.0F, new CubeDeformation(0.0F))
				.texOffs(30, 147).addBox(-25.0F, 32.0F, -22.0F, 50.0F, 0.0F, 97.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition cube_r1 = liuguang.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(33, 147).mirror().addBox(-20.0F, 0.0F, 0.0F, 46.0F, 0.0F, 97.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-20.0F, 4.0F, -22.0F, 0.0F, 0.0F, 1.8326F));

		PartDefinition cube_r2 = liuguang.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-81, 147).addBox(-26.0F, 0.0F, 0.0F, 46.0F, 0.0F, 97.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, 4.0F, -22.0F, 0.0F, 0.0F, -1.8326F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		liuguang.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}