package net.darkhood135.projectbiohazard.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public class AidMistParticle extends SingleQuadParticle {
    public AidMistParticle(ClientLevel level, double x, double y, double z,
                           double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());

        this.friction = 0.8f;
        this.lifetime = 30;                                           // shorter-lived
        this.quadSize = 0.06f + level.getRandom().nextFloat() * 0.04f; // small + thin, slight size variation
        this.gravity = 0.08f;                                          // hang/drift slightly
        this.setColor(0.6f, 1.0f, 0.6f);                             // light green tint over the texture
        this.alpha = 0.5f;                                           // slightly translucent
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new AidMistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 0.7f * (1.0f - (float) this.age / this.lifetime);  // ease to transparent
    }
}
