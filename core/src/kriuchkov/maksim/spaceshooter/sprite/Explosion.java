package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;

public class Explosion extends Sprite {

    private static final float ANIMATE_INTERVAL = 0.017f;
    private float animateTimer;

    public Explosion(TextureAtlas atlas) {
        super(atlas.findRegion("explosion"),  9, 9, 74);

    }

    public void set(float height, Vector2 pos) {
        setHeightProportion(height);
        this.pos.set(pos);
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer >= ANIMATE_INTERVAL) {
            animateTimer -= ANIMATE_INTERVAL;
            frame++;
            if (frame == regions.length) {
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }
}
