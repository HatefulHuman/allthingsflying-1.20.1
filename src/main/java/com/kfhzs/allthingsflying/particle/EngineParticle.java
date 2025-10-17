package com.kfhzs.allthingsflying.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EngineParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected EngineParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                             SpriteSet spriteSet, double xd, double yd, double zd, float rCol, float gCol, float bCol) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 0.8F; // 摩擦力
        this.gravity = -0.1F; // 轻微向上浮力
        // 设置粒子属性
        this.lifetime = 12; // 存活时间改为12tick
        this.sprites = spriteSet;
        // 设置颜色为纯黑色
        this.rCol = rCol;
        this.gCol = gCol;
        this.bCol = bCol;
        this.alpha = 1.0f; // 初始完全不透明

        // 设置精灵
        this.setSpriteFromAge(spriteSet);

        // 随机速度
        this.xd = xd + (Math.random() * 2.0 - 1.0) * 0.1;
        this.yd = yd + (Math.random() * 2.0 - 1.0) * 0.1;
        this.zd = zd + (Math.random() * 2.0 - 1.0) * 0.1;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        // 线性透明度衰减
        this.alpha = 1.0f - (float)this.age / (float)this.lifetime;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class ThermalEngineProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public ThermalEngineProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new EngineParticle(level, x, y, z, this.sprites, xd, yd, zd, 0.0f, 0.0f, 0.0f);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class CloudEngineProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public CloudEngineProvider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new EngineParticle(level, x, y, z, this.sprites, xd, yd, zd,1.0f, 1.0f, 1.0f);
        }
    }
}