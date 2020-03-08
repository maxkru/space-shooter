package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        v.set(Rnd.nextFloat(-0.001f, 0.001f), Rnd.nextFloat(-0.2f, -0.05f));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.01f);
        float x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(x, y);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);

        if (getLeft() > worldBounds.getRight())
            setRight(worldBounds.getLeft());

        if (getRight() < worldBounds.getLeft())
            setLeft(worldBounds.getRight());

        if (getTop() < worldBounds.getBottom())
            setBottom(worldBounds.getTop());

        if (getBottom() > worldBounds.getTop())
            setTop(worldBounds.getBottom());
    }
}
