package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Ship;
import kriuchkov.maksim.spaceshooter.base.Sprite;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import kriuchkov.maksim.spaceshooter.utils.EnemyShipHandler;
import ru.geekbrains.math.Rect;

public class EnemyShip extends Ship {

    private Vector2 vThroughScreen;
    private boolean movingIn;
    private int collisionDamage;
    private EnemyShipHandler handler;
    private float delayBetweenSpawns;
    private float sinceLastSpawn;
    private boolean spawns;
    private Sprite homingSprite;

    private float shipVelocity;
    private float bulletVelocity;

    private Vector2 buf;

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, EnemyShipHandler handler, Sound bulletFireSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.handler = handler;
        this.bulletFireSound = bulletFireSound;
        this.worldBounds = worldBounds;

        this.bulletV = new Vector2();
        this.bulletEmitterPos = new Vector2();

        this.bulletFireSoundVolume = 0.1f;

        this.vThroughScreen = new Vector2();

        this.buf = new Vector2();
    }

    public void set(
            TextureRegion[] regions,
            TextureRegion bulletTextureRegion,
            Vector2 pos,
            Vector2 vIn,
            Vector2 vThroughScreen,
            float heightProportion,
            int hp,
            float bulletHeight,
            float bulletVelocity,
            int bulletDamage,
            float delayBetweenShots,
            int collisionDamage,
            boolean spawns,
            float delayBetweenSpawns
    ) {
        this.regions = regions;
        this.bulletTextureRegion = bulletTextureRegion;
        this.pos.set(pos);
        this.v.set(vIn);
        this.vThroughScreen.set(vThroughScreen);
        setHeightProportion(heightProportion);
        this.hp = hp;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, -bulletVelocity);
        this.bulletDamage = bulletDamage;
        this.delayBetweenShots = delayBetweenShots;
        this.collisionDamage = collisionDamage;
        this.spawns = spawns;
        this.delayBetweenSpawns = delayBetweenSpawns;

        this.homingSprite = null;
        this.shipVelocity = v.len();
        this.bulletVelocity = bulletVelocity;

        this.angle = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (homingSprite == null)
            this.pos.mulAdd(v, delta);
        else {
            buf.set(homingSprite.pos).sub(pos).setLength(shipVelocity);
            this.pos.mulAdd(buf, delta);
            angle = buf.angle() + 90;
        }

        if (movingIn && worldBounds.getTop() > getTop()) {
            sinceLastShot = delayBetweenShots;
            v.set(vThroughScreen);
            isShooting = true;
            movingIn = false;
        }
        if (sinceLastSpawn < delayBetweenSpawns)
            sinceLastSpawn += delta;
        if (spawns && sinceLastSpawn  >= delayBetweenSpawns) {
            handler.spawnKamikaze(pos);
            sinceLastSpawn = 0;
        }

        if (worldBounds.getBottom() > getTop())
            destroy(false);
    }

    @Override
    protected void updateBulletEmitterPos() {
        bulletEmitterPos.set(pos.x, getBottom() + 0.01f);
        if (homingSprite != null)
            bulletV.set(homingSprite.pos).sub(pos).setLength(bulletVelocity);
    }

    public void movingIn() {
        this.isShooting = false;
        this.movingIn = true;
    }

    public int getHp() {
        return hp;
    }

    public boolean collidesWith(Bullet bullet) {
        return bullet.getRight() > getLeft() && bullet.getLeft() < getRight() && bullet.getTop() > this.pos.y && bullet.getBottom() < getTop();
    }

    public int getCollisionDamage() {
        return collisionDamage;
    }

    public void setHomingSprite(Sprite sprite) {
        homingSprite = sprite;
    }
}
