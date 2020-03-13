package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Ship;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import ru.geekbrains.math.Rect;

public class PlayerShip extends Ship {

    private Vector2 attractor;
    private boolean movingByTouch;
    private boolean movingByKeyboard;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    private boolean manualShooting;
    private boolean autoShooting;

    private static final float SHIP_VELOCITY = 0.25f;
    private static final float BULLET_VELOCITY = 0.4f;

    private static final int PLAYER_SHIP_MAX_HP = 100;

    private static final float INITIAL_POS_X = 0f;
    private static final float INITIAL_POS_Y = -0.25f;

    /**
     * @param atlas TextureAtlas which contains 'main_ship' region with 2 frames
     * @param bulletPool BulletPool that will be used by this ship to emit bullets
     * @param explosionPool ExplosionPool that will be used by this ship to spawn explosions
     * */
    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1 , 2, 2);

        attractor = new Vector2();

        reset();

        this.bulletPool = bulletPool;
        bulletTextureRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, BULLET_VELOCITY);
        bulletEmitterPos = new Vector2();

        bulletFireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        bulletFireSoundVolume = 0.05f;

        delayBetweenShots = 0.1f;
        sinceLastShot = 0f;
        isShooting = false;

        bulletHeight = 0.01f;
        bulletDamage = 1;

        this.explosionPool = explosionPool;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.1f);
        this.worldBounds = worldBounds;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        movingByTouch = true;
        attractor.set(touch);
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        movingByTouch = false;
        return super.touchUp(touch, pointer, button);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        movingByTouch = true; // TODO: implement better multi-touch solution
        attractor.set(touch);
        return super.touchDragged(touch, pointer);
    }

    /**
     * Updates data for this PlayerShip (firing bullets and moving using keyboard and touch/mouse).
     * @param delta amount of time (in seconds) that has passed since last update.
     * */
    @Override
    public void update(float delta) {
        super.update(delta);

        if(movingByTouch && !attractor.epsilonEquals(pos, SHIP_VELOCITY * delta / 2f)) {
            v.set(attractor).sub(pos).setLength(SHIP_VELOCITY);
            pos.mulAdd(v, delta);
        } else if (movingByKeyboard) {
            v.set(0,0);
            if (movingDown)
                v.add(0,-1f);
            if (movingUp)
                v.add(0,1f);
            if (movingLeft)
                v.add(-1f,0);
            if (movingRight)
                v.add(1f,0);
            v.setLength(SHIP_VELOCITY);
            pos.mulAdd(v, delta);
        } else {
            v.setZero();
        }

        if (pos.y > -getHalfHeight())
            pos.y = -getHalfHeight();
        else if (pos.y < -0.5f + getHalfHeight())
            pos.y = -0.5f + getHalfHeight();

        if (pos.x < worldBounds.getLeft() + getHalfWidth())
            pos.x = worldBounds.getLeft() + getHalfWidth();
        else if (pos.x > worldBounds.getRight() - getHalfWidth())
            pos.x = worldBounds.getRight() - getHalfWidth();

    }

    @Override
    protected void updateBulletEmitterPos() {
        bulletEmitterPos.set(pos.x, getTop() - 0.01f);
    }

    /**
     * Performs action when a key is pressed.
     * 'Down', 'Up', 'Right', 'Left' set the corresponding 'moving____' flags to true (which is set to false when the key is released).
     * 'Space' sets the 'manualShooting' flag to true (which is set to false when the key is released).
     * 'V' toggles the 'autoShooting' flag.
     * @param keyCode code of the pressed key (see {@link Input.Keys}).
     * */
    public boolean keyDown(int keyCode) {
        movingByKeyboard = true;

        switch (keyCode) {
            case Input.Keys.DOWN:
                movingDown = true;
                break;
            case Input.Keys.UP:
                movingUp = true;
                break;
            case Input.Keys.RIGHT:
                movingRight = true;
                break;
            case Input.Keys.LEFT:
                movingLeft = true;
                break;
            case Input.Keys.SPACE:
                manualShooting = true;
                break;
            case Input.Keys.V:
                autoShooting = !autoShooting;
        }
        isShooting = manualShooting || autoShooting;
        return true;
    }

    /**
     * Performs action when a key is released.
     * 'Down', 'Up', 'Right', 'Left' set the corresponding 'moving____' flags to false.
     * 'Space' sets the 'manualShooting' flag to false.
     * @param keyCode code of the released key (see {@link Input.Keys}).
     * */
    public boolean keyUp(int keyCode) {
        switch (keyCode) {
            case Input.Keys.DOWN:
                movingDown = false;
                break;
            case Input.Keys.UP:
                movingUp = false;
                break;
            case Input.Keys.RIGHT:
                movingRight = false;
                break;
            case Input.Keys.LEFT:
                movingLeft = false;
                break;
            case Input.Keys.SPACE:
                manualShooting = false;
        }
        if (!movingRight && !movingLeft && !movingUp && !movingDown)
            movingByKeyboard = false;
        isShooting = manualShooting || autoShooting;

        return true;
    }

    /**
     * Checks whether this ship collides with the bullet.
     * Note that the 'collision box' is a rectangle, the back side of the sprite.
     * @param bullet the Bullet, collision with which is checked
     * @return 'true' if there is collision, 'false' if there is no collision
     * */
    public boolean collidesWith(Bullet bullet) {
        return bullet.getRight() > getLeft() && bullet.getLeft() < getRight() && bullet.getBottom() < this.pos.y && bullet.getTop() > getBottom();
    }

    /**
     * Resets this PlayerShip, initializing movement flags, destruction flag, position,
     * and setting hp to maximum.
     * */
    public void reset() {
        hp = PLAYER_SHIP_MAX_HP;
        pos.set(INITIAL_POS_X, INITIAL_POS_Y);

        movingByTouch = false;
        movingByKeyboard = false;
        manualShooting = false;
        autoShooting = false;
        isShooting = false;

        movingDown = movingUp = movingRight = movingLeft = false;

        flushDestroyed();
    }

    /**
     * @return this ship's velocity vector
     * */
    public Vector2 getV() {
        return v;
    }
}
