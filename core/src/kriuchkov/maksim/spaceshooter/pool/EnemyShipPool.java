package kriuchkov.maksim.spaceshooter.pool;

import kriuchkov.maksim.spaceshooter.base.SpritePool;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;

public class EnemyShipPool extends SpritePool<EnemyShip> {

    @Override
    protected EnemyShip createNewObject() {
        return new EnemyShip();
    }

}
