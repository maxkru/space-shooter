package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
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

    private static final float VELOCITY = 0.005f;

    public PlayerShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 1 , 2, 2);

        v = new Vector2();
        attractor = new Vector2();
        keyMovementDirection = new Vector2();
        pos.set(0, -0.25f);
        movingByTouch = false;
        movingByKeyboard = false;
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
        if(movingByTouch && !attractor.epsilonEquals(pos, VELOCITY)) {
            v.set(attractor).sub(pos).setLength(VELOCITY);
            pos.add(v);
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
            keyMovementDirection.setLength(VELOCITY);
            pos.add(keyMovementDirection);
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
        }
        if (!movingRight && !movingLeft && !movingUp && !movingDown)
            movingByKeyboard = false;

        return true;
    }
}
