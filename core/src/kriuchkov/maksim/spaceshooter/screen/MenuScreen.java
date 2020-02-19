package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture background;

    private Vector2 touch;
    private Vector2 v;
    private Vector2 pos;

    @Override
    public void show() {
        super.show();
        touch = new Vector2();
        v = new Vector2();
        pos = new Vector2();

        img = new Texture("badlogic.jpg");
        background = new Texture("background_simple.png");
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(img, pos.x - img.getWidth() / 2.0f, pos.y - img.getHeight() / 2.0f);
        batch.end();
    }

    @Override
    public void dispose () {
        img.dispose();
        background.dispose();
        super.dispose();
    }
}
