package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.audio.Sound;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import kriuchkov.maksim.spaceshooter.utils.EnemyShipHandler;
import ru.geekbrains.math.Rect;

public class EnemyShipPool extends SpritePool<EnemyShip> {

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyShipHandler handler;
    private Sound bulletFireSound;
    private Rect worldBounds;

    public EnemyShipPool(BulletPool bulletPool, ExplosionPool explosionPool, EnemyShipHandler handler, Sound bulletFireSound) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.handler = handler;
        this.bulletFireSound = bulletFireSound;
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected EnemyShip createNewObject() {
        return new EnemyShip(bulletPool, explosionPool, handler, bulletFireSound, worldBounds);
    }

}
