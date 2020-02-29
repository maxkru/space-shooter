package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import ru.geekbrains.math.Rect;

public class PlayerShip extends Sprite {

    private Vector2 attractor;
    private boolean movingByTouch;
    private boolean movingByKeyboard;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;
    private Vector2 keyMovementDirection;

    private Rect worldBounds;

    private BulletPool bulletPool;
    private TextureRegion bulletTextureRegion;

    private Sound bulletFireSound;

    private Vector2 bulletV;
    private Vector2 bulletEmitterPos;

    private boolean isShooting;


    private static final float SHIP_VELOCITY = 0.25f;
    private static final float BULLET_VELOCITY = 0.4f;

    private static final float DELAY_BETWEEN_SHOTS = 0.1f;

    private float sinceLastShot;


    public PlayerShip(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1 , 2, 2);

        v = new Vector2();
        attractor = new Vector2();
        keyMovementDirection = new Vector2();

        pos.set(0, -0.25f);

        movingByTouch = false;
        movingByKeyboard = false;

        this.bulletPool = bulletPool;
        bulletTextureRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, BULLET_VELOCITY);
        bulletEmitterPos = new Vector2();

        bulletFireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));

        sinceLastShot = 0f;
        isShooting = false;
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

        if (sinceLastShot < DELAY_BETWEEN_SHOTS)
            sinceLastShot += delta;
        if (isShooting && sinceLastShot >= DELAY_BETWEEN_SHOTS) {
            shoot();
            sinceLastShot -= DELAY_BETWEEN_SHOTS;
        }
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
                isShooting = true;
        }

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
                isShooting = false;
        }
        if (!movingRight && !movingLeft && !movingUp && !movingDown)
            movingByKeyboard = false;

        return true;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletEmitterPos.set(pos.x, pos.y + getHeight() * 0.45f);
        bullet.set(this, bulletTextureRegion, bulletEmitterPos, bulletV, 0.01f, worldBounds, 1);
        bulletFireSound.play(0.2f);
    }

    public void dispose() {
        bulletFireSound.dispose();
    }
}
