package com.kfhzs.allthingsflying.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class WhiteLightParticle extends TextureSheetParticle {
    private static final int PARTICLE_LIFETIME = 20;
    private static final float INITIAL_SPEED = 0.5f;

    protected WhiteLightParticle(ClientLevel level, double x, double y, double z,
                                  double xd, double yd, double zd, SpriteSet spriteSet) {
        super(level, x, y, z, 0, 0, 0);

        this.lifetime = PARTICLE_LIFETIME;
        this.gravity = 0.0F;

        this.alpha = 0.0f;
        this.rCol = 1.0f;
        this.gCol = 0.21f;
        this.bCol = 0.21f;

        this.quadSize = 0.01f + level.random.nextFloat() / 20.0f;

        // 设置初始速度（向后飞行）
        this.xd = xd * INITIAL_SPEED;
        this.yd = yd * INITIAL_SPEED;
        this.zd = zd * INITIAL_SPEED;

        this.pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        updateColorAndAlpha();

        // 匀速向后飞行，无加速度
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
    }

    private void updateColorAndAlpha() {
        float ageRatio = (float) this.age / (float) this.lifetime;

        if (ageRatio <= 0.4f) {
            float progress = ageRatio / 0.4f;
            this.alpha = progress;

            this.rCol = 1.0f;
            this.gCol = 1.0f - progress * (1.0f - 0.21f);
            this.bCol = 1.0f - progress * (1.0f - 0.21f);
        } else {
            float progress = (ageRatio - 0.4f) / 0.6f;
            this.alpha = 1.0f - progress;

            this.rCol = 1.0f;
            this.gCol = 0.21f * (1.0f - progress);
            this.bCol = 0.21f * (1.0f - progress);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new WhiteLightParticle(level, x, y, z, xd, yd, zd, this.spriteSet);
        }
    }
}