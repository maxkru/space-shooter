package kriuchkov.maksim.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.EnemyShipPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import kriuchkov.maksim.spaceshooter.sprite.PlayerShip;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.utils.Regions;

public class EnemyShipHandler {

    private static final float SMALL_SHIP_HEIGHT = 0.1f;
    private static final float SMALL_SHIP_VELOCITY = 0.15f;
    private static final float SMALL_SHIP_IN_VELOCITY = 0.15f;
    private static final float SMALL_BULLET_HEIGHT = 0.01f;
    private static final float SMALL_BULLET_VELOCITY = 0.3f;
    private static final int SMALL_BULLET_DAMAGE = 1;
    private static final float SMALL_SHIP_DELAY_BETWEEN_SHOTS = 2f;
    private static final int SMALL_SHIP_HP = 5;
    private static final int SMALL_SHIP_COLLISION_DAMAGE = 10;
    private static final float SMALL_PROBABILITY = 0.5f;
    private static final int SMALL_SHIP_POINTS = 1;
    private static final float SMALL_BULLET_EMITTER_POS_FACTOR = 1f;

    private static final float MEDIUM_SHIP_HEIGHT = 0.15f;
    private static final float MEDIUM_SHIP_VELOCITY = 0.05f;
    private static final float MEDIUM_SHIP_IN_VELOCITY = 0.1f;
    private static final float MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final float MEDIUM_BULLET_VELOCITY = 0.25f;
    private static final int MEDIUM_BULLET_DAMAGE = 4;
    private static final float MEDIUM_SHIP_DELAY_BETWEEN_SHOTS = 2.5f;
    private static final int MEDIUM_SHIP_HP = 40;
    private static final int MEDIUM_SHIP_COLLISION_DAMAGE = 30;
    private static final float MEDIUM_PROBABILITY = 0.3f;
    private static final int MEDIUM_SHIP_POINTS = 8;
    private static final float MEDIUM_BULLET_EMITTER_POS_FACTOR = 0.9f;

    private static final float BIG_SHIP_HEIGHT = 0.2f;
    private static final float BIG_SHIP_VELOCITY = 0.01f;
    private static final float BIG_SHIP_IN_VELOCITY = 0.03f;
    private static final float BIG_BULLET_HEIGHT = 0.04f;
    private static final float BIG_BULLET_VELOCITY = 0.2f;
    private static final int BIG_BULLET_DAMAGE = 10;
    private static final float BIG_SHIP_DELAY_BETWEEN_SHOTS = 3f;
    private static final int BIG_SHIP_HP = 100;
    private static final int BIG_SHIP_COLLISION_DAMAGE = 75;
    private static final float BIG_SHIP_KAMIKAZE_SPAWN_DELAY = 10f;
    private static final float BIG_PROBABILITY = 0.2f;
    private static final int BIG_SHIP_POINTS = 20;
    private static final float BIG_BULLET_EMITTER_POS_FACTOR = 0.75f;

    private EnemyShipPool enemyShipPool;
    private TextureAtlas atlas;
    private Rect worldBounds;
    private Sound bulletFireSound;

    private static final float spawnInterval = 4.2f;
    private float spawnTimer;

    private final TextureRegion[] smallShipRegions;
    private final TextureRegion[] mediumShipRegions;
    private final TextureRegion[] bigShipRegions;
    private TextureRegion bulletTextureRegion;

    private Vector2 spawnLocation;

    private int level;

    private Vector2 vInSmall;
    private Vector2 vInMedium;
    private Vector2 vInBig;
    private Vector2 vSmall;
    private Vector2 vMedium;
    private Vector2 vBig;

    private PlayerShip playerShip;

    public EnemyShipHandler(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, PlayerShip playerShip) {
        this.atlas = atlas;

        this.smallShipRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2, 2);
        this.mediumShipRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        this.bigShipRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);


        this.vInSmall = new Vector2(0f, -SMALL_SHIP_IN_VELOCITY);
        this.vInMedium = new Vector2(0f, -MEDIUM_SHIP_IN_VELOCITY);
        this.vInBig = new Vector2(0f, -BIG_SHIP_IN_VELOCITY);

        this.vSmall = new Vector2(0f, -SMALL_SHIP_VELOCITY);
        this.vMedium = new Vector2(0f, -MEDIUM_SHIP_VELOCITY);
        this.vBig = new Vector2(0f, -BIG_SHIP_VELOCITY);

        this.spawnLocation = new Vector2();
        this.bulletTextureRegion = atlas.findRegion("bulletEnemy");

        this.bulletFireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.enemyShipPool = new EnemyShipPool(bulletPool, explosionPool, this, bulletFireSound);

        this.playerShip = playerShip;
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
        enemyShipPool.setWorldBounds(worldBounds);
    }

    private void spawn() {
        EnemyShip ship = enemyShipPool.obtain();
        float rand = (float) Math.random();
        if (rand < SMALL_PROBABILITY) {
            ship.set(
                    smallShipRegions,
                    bulletTextureRegion,
                    spawnLocation,  // unnecessary
                    vInSmall,
                    vSmall,
                    SMALL_SHIP_HEIGHT,
                    SMALL_SHIP_HP,
                    SMALL_BULLET_HEIGHT,
                    SMALL_BULLET_VELOCITY,
                    SMALL_BULLET_DAMAGE * level,
                    SMALL_SHIP_DELAY_BETWEEN_SHOTS,
                    SMALL_BULLET_EMITTER_POS_FACTOR,
                    SMALL_SHIP_COLLISION_DAMAGE,
                    SMALL_SHIP_POINTS,
                    false,
                    0f
            );
        } else if (rand < SMALL_PROBABILITY + MEDIUM_PROBABILITY) {
            ship.set(
                    mediumShipRegions,
                    bulletTextureRegion,
                    spawnLocation,  // unnecessary
                    vInMedium,
                    vMedium,
                    MEDIUM_SHIP_HEIGHT,
                    MEDIUM_SHIP_HP,
                    MEDIUM_BULLET_HEIGHT,
                    MEDIUM_BULLET_VELOCITY,
                    MEDIUM_BULLET_DAMAGE * level,
                    MEDIUM_SHIP_DELAY_BETWEEN_SHOTS,
                    MEDIUM_BULLET_EMITTER_POS_FACTOR,
                    MEDIUM_SHIP_COLLISION_DAMAGE,
                    MEDIUM_SHIP_POINTS,
                    false,
                    0f
            );
        } else {
            ship.set(
                    bigShipRegions,
                    bulletTextureRegion,
                    spawnLocation,  // unnecessary
                    vInBig,
                    vBig,
                    BIG_SHIP_HEIGHT,
                    BIG_SHIP_HP,
                    BIG_BULLET_HEIGHT,
                    BIG_BULLET_VELOCITY,
                    BIG_BULLET_DAMAGE * level,
                    BIG_SHIP_DELAY_BETWEEN_SHOTS,
                    BIG_BULLET_EMITTER_POS_FACTOR,
                    BIG_SHIP_COLLISION_DAMAGE,
                    BIG_SHIP_POINTS,
                    true,
                    BIG_SHIP_KAMIKAZE_SPAWN_DELAY
            );
        }
        ship.pos.set(
                Rnd.nextFloat(worldBounds.getRight() - ship.getHalfWidth(), worldBounds.getLeft() + ship.getHalfWidth()),
                worldBounds.getTop() + ship.getHalfHeight()
        );
        ship.moveIn();
    }

    public void spawnKamikaze(Vector2 pos) {
        EnemyShip enemyShip = enemyShipPool.obtain();
        enemyShip.set(
                smallShipRegions,
                bulletTextureRegion,
                pos,
                vInSmall,
                vSmall,
                SMALL_SHIP_HEIGHT,
                SMALL_SHIP_HP,
                SMALL_BULLET_HEIGHT,
                SMALL_BULLET_VELOCITY,
                SMALL_BULLET_DAMAGE * level,
                SMALL_SHIP_DELAY_BETWEEN_SHOTS,
                SMALL_BULLET_EMITTER_POS_FACTOR,
                SMALL_SHIP_COLLISION_DAMAGE,
                SMALL_SHIP_POINTS,
                false,
                0f
        );
        enemyShip.moveIn();
        enemyShip.setHomingSprite(playerShip);
    }

    public void update(float delta, boolean spawn, int level) {
        spawnTimer += delta;
        if (spawn && spawnTimer >= spawnInterval) {
            spawnTimer = 0f;
            spawn();
        }
        this.level = level;
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

    public List<EnemyShip> getActiveEnemyShips() {
        return enemyShipPool.getActiveObjects();
    }

    public void reset() {
        for (EnemyShip enemyShip : enemyShipPool.getActiveObjects()) {
            enemyShip.destroy(false);
        }
    }

}
