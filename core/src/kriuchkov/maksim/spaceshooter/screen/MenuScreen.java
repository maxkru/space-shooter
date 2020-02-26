
package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.ButtonExit;
import kriuchkov.maksim.spaceshooter.sprite.Circle;
import kriuchkov.maksim.spaceshooter.sprite.Star;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen {

    private TextureAtlas atlas;

    private Texture bg;

    private Background background;
    private Circle circle;
    private Star[] stars;

    private static final int STAR_COUNT = 32;

    private ButtonExit buttonExit;

    private Vector2 pos;

    @Override
    public void show() {
        super.show();
        pos = new Vector2();

        atlas = new TextureAtlas("textures/texture_atlas.atlas");
        circle = new Circle(atlas);
        bg = new Texture("background_simple.png");
        background = new Background(bg);

        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

        buttonExit = new ButtonExit(atlas);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pos.set(-0.5f,-0.5f);

        update(delta);
        draw();
    }

    @Override
    public void dispose () {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        System.out.printf("MenuScreen: touchDown(touch.x = %f, touch.y = %f)\n", touch.x, touch.y);
//        circle.touchDown(touch, pointer, button);
        buttonExit.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        System.out.printf("MenuScreen: touchUp(touch.x = %f, touch.y = %f)\n", touch.x, touch.y);
//        circle.touchUp(touch, pointer, button);
        buttonExit.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        System.out.printf("MenuScreen: touchDragged(touch.x = %f, touch.y = %f)\n", touch.x, touch.y);
        circle.touchDragged(touch, pointer);
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        circle.resize(worldBounds);
        for(Star star : stars)
            star.resize(worldBounds);
        buttonExit.resize(worldBounds);
    }

    private void update(float delta) {
        for(Star star : stars)
            star.update(delta);
    }

    private void draw() {
        batch.begin();

        background.draw(batch);

        for(Star star : stars)
            star.draw(batch);

        buttonExit.draw(batch);

        batch.end();
    }
}
