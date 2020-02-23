package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;

public abstract class Sprite extends Rect {

    protected Vector2 v;

    protected float angle;
    protected float scale = 1f;

    protected TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        if(region == null)
            throw new IllegalArgumentException("Текстура не была задана (null).");
        this.regions = new TextureRegion[]{region};
    }

    public void draw(SpriteBatch batch) {
        batch.draw(regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle);
    }

    public void resize(Rect worldBounds) {
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public void update(float delta) {

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }
}
