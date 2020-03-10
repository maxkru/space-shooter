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
    private Vector2 keyMovementDirection;

    private boolean manualShooting;
    private boolean autoShooting;

    private static final float SHIP_VELOCITY = 0.25f;
    private static final float BULLET_VELOCITY = 0.4f;

    private static final int PLAYER_SHIP_MAX_HP = 100;

    private static final float INITIAL_POS_X = 0f;
    private static final float INITIAL_POS_Y = -0.25f;


    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1 , 2, 2);

        attractor = new Vector2();
        keyMovementDirection = new Vector2();

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

    @Override
    public void update(float delta) {
        super.update(delta);

        if(movingByTouch && !attractor.epsilonEquals(pos, SHIP_VELOCITY * delta / 2f)) {
            v.set(attractor).sub(pos).setLength(SHIP_VELOCITY);
            pos.mulAdd(v, delta);
        } else if (movingByKeyboard) {
            keyMovementDirection.set(0,0);
            if (movingDown)
                keyMovementDirection.add(0,-1f);
            if (movingUp)
                keyMovementDirection.add(0,1f);
            if (movingLeft)
                keyMovementDirection.add(-1f,0);
            if (movingRight)
                keyMovementDirection.add(1f,0);
            keyMovementDirection.setLength(SHIP_VELOCITY);
            pos.mulAdd(keyMovementDirection, delta);
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

    public boolean collidesWith(Bullet bullet) {
        return bullet.getRight() > getLeft() && bullet.getLeft() < getRight() && bullet.getBottom() < this.pos.y && bullet.getTop() > getBottom();
    }

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
}
