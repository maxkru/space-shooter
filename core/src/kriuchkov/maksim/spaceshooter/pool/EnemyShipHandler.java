package kriuchkov.maksim.spaceshooter.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class EnemyShipHandler {

    private EnemyShipPool enemyShipPool;
    private TextureAtlas atlas;
    private Rect worldBounds;

    private Vector2 spawnLocation;

    private Vector2 v;

    public EnemyShipHandler(TextureAtlas atlas) {
        enemyShipPool = new EnemyShipPool();
        this.atlas = atlas;
        v = new Vector2(0f, -0.05f);
        spawnLocation = new Vector2();
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void update(float delta) {
        if (enemyShipPool.getActiveCount() < 2) {
            EnemyShip ship = enemyShipPool.obtain();
            spawnLocation.set(Rnd.nextFloat(worldBounds.getRight(), worldBounds.getLeft()), 0.6f);
            ship.set(
                    atlas.findRegion("enemy0"),
                    spawnLocation,
                    v,
                    0.1f,
                    worldBounds,
                    1
            );

        }
        enemyShipPool.updateAllActive(delta);
    }

    public void dispose() {
        enemyShipPool.dispose();
    }

    public void drawAllActive(SpriteBatch batch) {
        enemyShipPool.drawAllActive(batch);
    }

    public void freeAllDestroyedActiveObjects() {
        enemyShipPool.freeAllDestroyedActiveObjects();
    }
}
