package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
import ru.geekbrains.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private int damage;

    private Sprite owner;

    public Bullet() {
        regions = new TextureRegion[1];
    }

    public void set(Sprite owner, TextureRegion region, Vector2 pos, Vector2 v, float heightProportion, Rect worldBounds, int damage) {
        this.owner = owner;
        regions[0] = region;
        this.pos.set(pos);
        this.v.set(v);
        setHeightProportion(heightProportion);
        this.worldBounds = worldBounds;
        this.damage = damage;

    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        if (isOutside(worldBounds))
            destroy();
    }

    public int getDamage() {
        return damage;
    }
}
