package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Ship;
import kriuchkov.maksim.spaceshooter.base.Sprite;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.Regions;

public class EnemyShip extends Ship {

    public EnemyShip(BulletPool bulletPool, Sound bulletFireSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.bulletFireSound = bulletFireSound;
        this.worldBounds = worldBounds;

        this.bulletV = new Vector2();
        this.bulletEmitterPos = new Vector2();

        this.bulletFireSoundVolume = 0.1f;
    }

    public void set(
            TextureRegion[] regions,
            TextureRegion bulletTextureRegion,
            Vector2 pos,
            Vector2 v0,
            float heightProportion,
            int hp,
            float bulletHeight,
            float bulletVelocity,
            int bulletDamage,
            float delayBetweenShots
    ) {
        this.regions = regions;
        this.bulletTextureRegion = bulletTextureRegion;
        this.pos.set(pos);
        this.v0.set(v0);
        setHeightProportion(heightProportion);
        this.hp = hp;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, -bulletVelocity);
        this.bulletDamage = bulletDamage;
        this.delayBetweenShots = delayBetweenShots;

        v.set(v0); // TODO
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.pos.mulAdd(v, delta);
        if (worldBounds.getBottom() > getTop())
            destroy();
    }

    public int getHp() {
        return hp;
    }
}
