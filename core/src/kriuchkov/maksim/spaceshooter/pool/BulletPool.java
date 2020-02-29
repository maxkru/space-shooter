package kriuchkov.maksim.spaceshooter.pool;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;

public class BulletPool extends SpritePool<Bullet> {
    
    @Override
    protected Bullet createNewObject() {
        return new Bullet();
    }

}
