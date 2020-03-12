package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.audio.Sound;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import ru.geekbrains.math.Rect;

public class EnemyShipPool extends SpritePool<EnemyShip> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private Sound bulletFireSound;
    private Rect worldBounds;

    public EnemyShipPool(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletFireSound) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletFireSound = bulletFireSound;
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip createNewObject() {
        return new EnemyShip(bulletPool, explosionPool, bulletFireSound, worldBounds);
    }

}
