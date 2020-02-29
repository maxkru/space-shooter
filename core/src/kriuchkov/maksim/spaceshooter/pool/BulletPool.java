package kriuchkov.maksim.spaceshooter.pool;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;

public class BulletPool extends SpritePool<Bullet> {

    public BulletPool() {
        super();
    }

    public BulletPool(int initialFree) {
        super(initialFree);
    }

    @Override
    protected Bullet createNewObject() {
        return new Bullet();
    }

}
