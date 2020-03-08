package kriuchkov.maksim.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.EnemyShipPool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.utils.Regions;

public class EnemyShipHandler {

    private static final float SMALL_SHIP_HEIGHT = 0.1f;
    private static final float SMALL_BULLET_HEIGHT = 0.01f;
    private static final float SMALL_BULLET_VELOCITY = 0.3f;
    private static final int SMALL_BULLET_DAMAGE = 1;
    private static final float SMALL_SHIP_DELAY_BETWEEN_SHOTS = 2f;
    private static final int SMALL_SHIP_HP = 5;

    private EnemyShipPool enemyShipPool;
    private TextureAtlas atlas;
    private Rect worldBounds;
    private Sound bulletFireSound;

    private static final float spawnInterval = 4.2f;
    private float spawnTimer;

    private final TextureRegion[] smallShipRegions;
    private TextureRegion bulletTextureRegion;

    private Vector2 spawnLocation;

    private Vector2 v0;

    public EnemyShipHandler(TextureAtlas atlas, BulletPool bulletPool) {
        this.atlas = atlas;

        this.smallShipRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        this.v0 = new Vector2(0f, -0.05f);
        this.spawnLocation = new Vector2();
        this.bulletTextureRegion = atlas.findRegion("bulletEnemy");

        this.bulletFireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.enemyShipPool = new EnemyShipPool(bulletPool, bulletFireSound);
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
        enemyShipPool.setWorldBounds(worldBounds);
    }

    private void spawn() {
        EnemyShip ship = enemyShipPool.obtain();
        spawnLocation.set(Rnd.nextFloat(worldBounds.getRight(), worldBounds.getLeft()), 0.6f);
        ship.set(
                smallShipRegions,
                bulletTextureRegion,
                spawnLocation,
                v0,
                SMALL_SHIP_HEIGHT,
                SMALL_SHIP_HP,
                SMALL_BULLET_HEIGHT,
                SMALL_BULLET_VELOCITY,
                SMALL_BULLET_DAMAGE,
                SMALL_SHIP_DELAY_BETWEEN_SHOTS
        );
    }

    public void update(float delta) {
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f;
            spawn();
        }
        enemyShipPool.updateAllActive(delta);
    }

    public void dispose() {
        enemyShipPool.dispose();
        bulletFireSound.dispose();
    }

    public void drawAllActive(SpriteBatch batch) {
        enemyShipPool.drawAllActive(batch);
    }

    public void freeAllDestroyedActiveObjects() {
        enemyShipPool.freeAllDestroyedActiveObjects();
    }
}
