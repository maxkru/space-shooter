package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Ship;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import ru.geekbrains.math.Rect;

public class EnemyShip extends Ship {

    private Vector2 vThroughScreen;
    private boolean movingIn;
    private int collisionDamage;

    public EnemyShip(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletFireSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletFireSound = bulletFireSound;
        this.worldBounds = worldBounds;

        this.bulletV = new Vector2();
        this.bulletEmitterPos = new Vector2();

        this.bulletFireSoundVolume = 0.1f;

        this.vThroughScreen = new Vector2();
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
            int collisionDamage
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
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.pos.mulAdd(v, delta);
        if (movingIn && worldBounds.getTop() > getTop()) {
            sinceLastShot = delayBetweenShots;
            v.set(vThroughScreen);
            isShooting = true;
            movingIn = false;
        }
        if (worldBounds.getBottom() > getTop())
            destroy(false);
    }

    @Override
    protected void updateBulletEmitterPos() {
        bulletEmitterPos.set(pos.x, getBottom() + 0.01f);
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
}
