package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.ButtonExit;
import kriuchkov.maksim.spaceshooter.sprite.ButtonStart;
import kriuchkov.maksim.spaceshooter.sprite.Circle;
import kriuchkov.maksim.spaceshooter.sprite.Star;
import ru.geekbrains.math.Rect;

public class GameScreen extends BaseScreen {


    private TextureAtlas atlas;
    private Texture bg;

    private Background background;

    private Circle circle;

    private Star[] stars;

    private static final int STAR_COUNT = 32;

    @Override
    public void show() {
        super.show();

        atlas = new TextureAtlas("textures/texture_atlas.atlas");
        circle = new Circle(atlas);
        bg = new Texture("background_simple.png");
        background = new Background(bg);

        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
        return circle.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        super.touchDragged(touch, pointer);
        return circle.touchDragged(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        super.touchUp(touch, pointer, button);
        return circle.touchUp(touch, pointer, button);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        circle.resize(worldBounds);
        for(Star star : stars)
            star.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        bg.dispose();
    }

    private void update(float delta) {
        circle.update(delta);
        for(Star star : stars)
            star.update(delta);
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars)
            star.draw(batch);
        circle.draw(batch);
        batch.end();
    }
}
