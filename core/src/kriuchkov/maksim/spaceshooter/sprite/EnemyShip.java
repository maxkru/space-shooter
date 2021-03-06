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

    private static final double DEG_TO_RAD_FACTOR = Math.PI / 180.0;

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

    private int pointsForDestruction;

    private float bulletEmitterPosFactor;

    private Vector2 buf;

    /**
     * Constructor for EnemyShip. Many of the created object's parameters are not initialized,
     * and must be set using the set() method. (Note that this must not be called directly.
     * Obtaining a new EnemyShip entity must be done by calling EnemyShipPool.obtain())
     * @param bulletPool BulletPool that this ship will be using to spawn bullets
     * @param explosionPool ExplosionPool that this ship will be using to spawn explosions (when destroyed)
     * @param handler EnemyShipHandler that spawned this ship
     * @param bulletFireSound the Sound that this ship will play when firing a bullet
     * @param worldBounds the Rect that defines bounds of area visible to player
     *
     * */
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

    /**
     * Sets textures, position, velocities and other parameters for this EnemyShip.
     * @param regions textures for this ship (two frames)
     * @param bulletTextureRegion texture for bullet (single frame)
     * @param pos initial position of center of this sprite
     * @param vIn the velocity of ship while entering the screen from above, in units of "screen heights" per second
     *            (1.0f means ship passes full screen height in one second)
     * @param vThroughScreen the velocity of ship after fully entering the screen, in units of "screen heights" per second
     * @param heightProportion height of ship in units of screen height (1.0f = screen height)
     * @param hp minimal damage that ship must take to be destroyed
     * @param bulletHeight height of bullets emitted by this ship in units of screen height
     * @param bulletVelocity the velocity of bullets emitted by this ship in units of "screen heights" per second
     * @param bulletDamage damage dealt by bullets emitted by this ship
     * @param delayBetweenShots amount of time (in seconds) between this ship's two bullet shots
     * @param bulletEmitterPosFactor distance from center of sprite to the bullet emitter, in units of ship's half-heights
     * @param collisionDamage damage dealt to player ship upon colliding with this ship
     * @param pointsForDestruction how much will player's score increase when this ship is destroyed by player ship
     * @param spawns if true, this ship will spawn small ships occasionally
     * @param delayBetweenSpawns if 'spawns' parameter is 'true', amount of time (in seconds) between two spawns,
     *                           else not used (can safely be anything)
     * */
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
            float bulletEmitterPosFactor,
            int collisionDamage,
            int pointsForDestruction,
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
        this.bulletEmitterPosFactor = bulletEmitterPosFactor;
        this.collisionDamage = collisionDamage;
        this.pointsForDestruction = pointsForDestruction;
        this.spawns = spawns;
        this.delayBetweenSpawns = delayBetweenSpawns;

        this.homingSprite = null;
        this.shipVelocity = v.len();
        this.bulletVelocity = bulletVelocity;

        this.angle = 0;
        this.sinceLastSpawn = 0;
    }

    /**
     * Updates data for this EnemyShip (firing bullets, moving, spawning, destroying after leaving
     * bottom of screen).
     * @param delta amount of time (in seconds) that has passed since last update.
     * */
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
        if (homingSprite != null) { // angle != 0
            double angleRad = angle * DEG_TO_RAD_FACTOR;
            bulletEmitterPos.set(pos.x + ( (float) Math.sin(angleRad) ) * getHalfHeight() * bulletEmitterPosFactor,
                    pos.y - ( (float) Math.cos(angleRad) ) * getHalfHeight() * bulletEmitterPosFactor);
            bulletV.set(homingSprite.pos).sub(pos).setLength(bulletVelocity);
        } else {
            bulletEmitterPos.set(pos.x, pos.y - getHalfHeight() * bulletEmitterPosFactor);  // to avoid unnecessary calculations for angle == 0
        }
    }

    /**
     * Sets a flag that indicates that this ship is entering screen (and is not shooting).
     * */
    public void moveIn() {
        this.isShooting = false;
        this.movingIn = true;
    }

    /**
     * Checks whether this ship collides with the bullet.
     * Note that the 'collision box' is a rectangle, the back side of the sprite.
     * @param bullet the Bullet, collision with which is checked
     * @return 'true' if there is collision, 'false' if there is no collision
     * */
    public boolean collidesWith(Bullet bullet) {
        return bullet.getRight() > getLeft() && bullet.getLeft() < getRight() && bullet.getTop() > this.pos.y && bullet.getBottom() < getTop();
    }

    /**
     * @return amount of damage dealt to player ship upon colliding with this ship
     * */
    public int getCollisionDamage() {
        return collisionDamage;
    }

    /**
     * Sets the sprite to which's position this ship's velocity vector will be pointed at
     * (bullets will also be fired to that position).
     * The 'sprite' parameter is 'null' by default, which makes the ship move vertically down.
     * @param sprite the sprite to which this ship will move to
     * */
    public void setHomingSprite(Sprite sprite) {
        homingSprite = sprite;
    }

    /**
     * @return how much will player's score increase when this ship is destroyed by player ship
     * */
    public int getPointsForDestruction() {
        return pointsForDestruction;
    }
}
