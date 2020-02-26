package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
import ru.geekbrains.math.Rect;

public class PlayerShip extends Sprite {

    private Vector2 attractor;
    private boolean movingByTouch;

    private static final float VELOCITY = 0.005f;

    public PlayerShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        regions[0].setRegionWidth(regions[0].getRegionWidth() / 2);

        v = new Vector2();
        attractor = new Vector2();
        pos.set(0, -0.25f);
        movingByTouch = false;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.1f);
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
        attractor.set(touch);
        return super.touchDragged(touch, pointer);
    }

    @Override
    public void update(float delta) {
        if(movingByTouch && !attractor.epsilonEquals(pos, VELOCITY)) {
            v.set(attractor).sub(pos).setLength(VELOCITY);
            pos.add(v);
        }
    }
}
