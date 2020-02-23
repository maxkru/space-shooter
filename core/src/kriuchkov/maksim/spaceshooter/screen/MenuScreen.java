
package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture bg;

    private Background background;

    private Vector2 touch;
    private Vector2 v;
    private Vector2 pos;
    private Vector2 buf;
    private boolean touched;

    @Override
    public void show() {
        super.show();
        touch = new Vector2();
        v = new Vector2();
        pos = new Vector2();
        buf = new Vector2();
        touched = false;

        img = new Texture("circle.png");
        bg = new Texture("background_simple.png");
        background = new Background(bg);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(touched && !touch.epsilonEquals(pos, 1f)) {
            v = buf.set(touch).sub(pos).nor();
            pos.add(v);
        }
        pos.set(-0.5f,-0.5f);
        batch.begin();
        background.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        img.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("MenuScreen: touchDown(x = %d, y = %d)\n", screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.printf("MenuScreen: touchDragged(x = %d, y = %d)\n", screenX, screenY);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touched = false;
        System.out.printf("MenuScreen: touchUp(x = %d, y = %d)\n", screenX, screenY);
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
    }
}
