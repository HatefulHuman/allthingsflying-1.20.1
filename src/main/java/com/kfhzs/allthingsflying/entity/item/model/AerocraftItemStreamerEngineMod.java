package com.kfhzs.allthingsflying.entity.item.model;// Save this class in your mod and generate all required imports

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

/**
 * Made with Blockbench 5.0.7
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class AerocraftItemStreamerEngineMod<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "streamer_engine")), "main");
	private final ModelPart piaodai;

	public AerocraftItemStreamerEngineMod(ModelPart root) {
		this.piaodai = root.getChild("piaodai");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition piaodai = partdefinition.addOrReplaceChild("piaodai", CubeListBuilder.create().texOffs(-110, 1).addBox(-27.0F, 3.0F, 14.0F, 54.0F, 0.0F, 120.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -2.0F));

		PartDefinition cube_r1 = piaodai.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 140).addBox(-27.0F, 0.0F, 0.0F, 54.0F, 0.0F, 87.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.3412F, -21.3798F, -0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r2 = piaodai.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(101, 227).addBox(-27.0F, 0.0F, 0.0F, 54.0F, 0.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -46.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r3 = piaodai.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-106, 133).mirror().addBox(-18.0F, 0.0F, -1.0F, 48.0F, 0.0F, 120.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-24.0F, -6.0F, -48.0F, 0.0F, 0.0F, 1.9635F));

		PartDefinition cube_r4 = piaodai.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-106, 133).addBox(-30.0F, 0.0F, -1.0F, 48.0F, 0.0F, 120.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(24.0F, -6.0F, -48.0F, 0.0F, 0.0F, -1.9635F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		piaodai.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}