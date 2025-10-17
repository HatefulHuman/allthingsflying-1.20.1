// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package com.kfhzs.allthingsflying.entity.item.model;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import com.kfhzs.allthingsflying.entity.animations.AerocraftItemDroneEngineAnimation;
import com.kfhzs.allthingsflying.entity.animations.AerocraftItemMagicEngineAnimation;
import com.kfhzs.allthingsflying.entity.item.AerocraftItemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;

public class AerocraftItemMagicEngineMod<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "magic_engine")), "main");
	private final ModelPart mofalizi;
	private final ModelPart mofalizi2;
	private final ModelPart mofalizi3;

	public AerocraftItemMagicEngineMod(ModelPart root) {
		this.mofalizi = root.getChild("mofalizi");
		this.mofalizi2 = this.mofalizi.getChild("mofalizi2");
		this.mofalizi3 = this.mofalizi2.getChild("mofalizi3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mofalizi = partdefinition.addOrReplaceChild("mofalizi", CubeListBuilder.create().texOffs(-3, 42).addBox(-10.0F, 0.0F, 0.0F, 19.0F, 0.0F, 15.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 23.0F, -8.0F));

		PartDefinition mofalizi2 = mofalizi.addOrReplaceChild("mofalizi2", CubeListBuilder.create().texOffs(-1, 29).addBox(-10.0F, 0.0F, 0.0F, 19.0F, 0.0F, 13.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition mofalizi3 = mofalizi2.addOrReplaceChild("mofalizi3", CubeListBuilder.create().texOffs(-7, 10).addBox(-10.0F, 0.0F, 0.0F, 19.0F, 0.0F, 19.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 13.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(((AerocrafEntity) entity).animationState, AerocraftItemMagicEngineAnimation.IDLE,ageInTicks,1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		mofalizi.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.mofalizi;
	}
}