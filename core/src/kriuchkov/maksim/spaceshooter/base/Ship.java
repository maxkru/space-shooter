package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;
import ru.geekbrains.math.Rect;

public abstract class Ship extends Sprite {

    protected final Vector2 v0 = new Vector2();

    protected Rect worldBounds;

    protected BulletPool bulletPool;
    protected TextureRegion bulletTextureRegion;
    protected Sound bulletFireSound;
    protected Vector2 bulletV;
    protected Vector2 bulletEmitterPos;
    protected boolean isShooting;
    protected float delayBetweenShots;
    protected float sinceLastShot;
    protected float bulletHeight;
    protected int bulletDamage;

    protected int hp;

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
        }
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletEmitterPos.set(pos.x, pos.y + getHeight() * 0.45f);
        bullet.set(this, bulletTextureRegion, bulletEmitterPos, bulletV, bulletHeight, worldBounds, bulletDamage);
        bulletFireSound.play(0.2f);
    }

    public void dispose() {
        bulletFireSound.dispose();
    }
}
