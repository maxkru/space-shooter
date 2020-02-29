package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.Regions;

public class EnemyShip extends Sprite {

    private Rect worldBounds;
    private int hp;

    public EnemyShip() {

    }

    public void set(TextureRegion region, Vector2 pos, Vector2 v, float heightProportion, Rect worldBounds, int hp) {
        regions = Regions.split(region, 1, 2,2);
        this.pos.set(pos);
        this.v.set(v);
        setHeightProportion(heightProportion);
        this.worldBounds = worldBounds;
        this.hp = hp;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        if (worldBounds.getBottom() > getTop())
            destroy();
    }

    public int getHp() {
        return hp;
    }
}
