package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.Explosion;

public class ExplosionPool extends SpritePool<Explosion> {

    private TextureAtlas atlas;
    private Sound explosionSound;

    public ExplosionPool(TextureAtlas atlas, Sound explosionSound) {
        this.atlas = atlas;
        this.explosionSound = explosionSound;
    }

    @Override
    protected Explosion createNewObject() {
        return new Explosion(atlas, explosionSound);
    }
}
