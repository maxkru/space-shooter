
package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.Circle;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture bg;

    private Background background;
    private Circle circle;

    private Vector2 pos;

    @Override
    public void show() {
        super.show();
        pos = new Vector2();

        img = new Texture("circle.png");
        circle = new Circle(img);
        bg = new Texture("background_simple.png");
        background = new Background(bg);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        pos.set(-0.5f,-0.5f);
        circle.update(delta);
        batch.begin();
        background.draw(batch);
        circle.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        img.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        System.out.printf("MenuScreen: touchDown(touch.x = %f, touch.y = %f)\n", touch.x, touch.y);
        circle.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        System.out.printf("MenuScreen: touchUp(touch.x = %f, touch.y = %f)\n", touch.x, touch.y);
        circle.touchUp(touch, pointer, button);
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
    }
}
