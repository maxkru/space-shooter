package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.Explosion;

public class ExplosionPool extends SpritePool<Explosion> {

    TextureAtlas atlas;

    public ExplosionPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected Explosion createNewObject() {
        return new Explosion(atlas);
    }
}
