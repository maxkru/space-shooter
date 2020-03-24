package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;
import kriuchkov.maksim.spaceshooter.sprite.Explosion;
import ru.geekbrains.math.Rect;

public abstract class Ship extends Sprite {

    protected static final float DAMAGE_ANIMATION_INTERVAL = 0.1f;

    protected final Vector2 v0 = new Vector2();

    protected Rect worldBounds;

    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletTextureRegion;
    protected Sound bulletFireSound;
    protected Vector2 bulletV;
    protected Vector2 bulletEmitterPos;
    protected boolean isShooting;
    protected float delayBetweenShots;
    protected float sinceLastShot;
    protected float bulletHeight;
    protected int bulletDamage;
    protected float bulletFireSoundVolume = 1f;
    protected float damageAnimationTimer;
    protected int hp;

    /**
     * This constructor doesn't set textures.
     * */
    protected Ship() {
        isShooting = true;
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
        isShooting = true;
    }

    @Override
    public void update(float delta) {
        if (sinceLastShot < delayBetweenShots)
            sinceLastShot += delta;
        if (isShooting && sinceLastShot >= delayBetweenShots) {
            shoot();
            sinceLastShot -= delayBetweenShots;
            if (sinceLastShot >= delayBetweenShots)
                sinceLastShot = 0;
        }

        damageAnimationTimer += delta;
        if (damageAnimationTimer >= DAMAGE_ANIMATION_INTERVAL) {
            frame = 0;
        }
    }

    /**
     * Emits a bullet from this ship.
     * Every time this method is called, {@code updateBulletEmitterPos()} is called, too, so that
     * the bullet is fired from the correct position.
     * */
    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        updateBulletEmitterPos();
        bullet.set(this, bulletTextureRegion, bulletEmitterPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        bulletFireSound.play(bulletFireSoundVolume);
    }

    /**
     * Method that updates the initial position of fired bullets. Called by {@code shoot()}.
     * */
    protected abstract void updateBulletEmitterPos();

    /**
     * Spawns an explosion of the same height as this sprite, centered at this ship's center.
     * */
    private void explode() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    /**
     * Same as calling {@code destroy(true)}, that is, with explosion.
     * */
    @Override
    public void destroy() {
        destroy(true);
    }

    /**
     * Sets 'destroyed' flag to 'true'. Also, if parameter is true, spawns an explosion of the same height
     * as this sprite, centered at this ship's center.
     * @param explosion if true, spawns explosion
     * */
    public void destroy(boolean explosion) {
        super.destroy();
        if (explosion)
            explode();
    }

    /**
     * Disposes all resources held by this object.
     * */
    public void dispose() {
        bulletFireSound.dispose();
    }

    /**
     * Subtracts 'damage' from this ship's HP. If HP gets non-positive, ship gets destroyed.
     * Also, starts 'damage taken' animation.
     * @param damageAmount how much damage the ship will take
     * */
    public void damage(int damageAmount) {
        this.hp -= damageAmount;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
        damageAnimationTimer = 0f;
        frame = 1;
    }

    /**
     * @return current 'hp' of this ship - minimal damage that it must take to be destroyed
     * */
    public int getHp() {
        return hp;
    }
}
