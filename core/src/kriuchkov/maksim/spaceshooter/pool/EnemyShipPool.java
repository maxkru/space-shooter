package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import ru.geekbrains.math.Rect;

public class EnemyShipPool extends SpritePool<EnemyShip> {

    private BulletPool bulletPool;
    private Sound bulletFireSound;
    private Rect worldBounds;

    public EnemyShipPool(BulletPool bulletPool, Sound bulletFireSound) {
        this.bulletPool = bulletPool;
        this.bulletFireSound = bulletFireSound;
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip createNewObject() {
        return new EnemyShip(bulletPool, bulletFireSound, worldBounds);
    }

}
