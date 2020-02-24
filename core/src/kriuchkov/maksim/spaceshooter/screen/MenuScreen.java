
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
    private Vector2 buf;
    private boolean touched;

    @Override
    public void show() {
        super.show();
        touch = new Vector2();
        v = new Vector2();
        pos = new Vector2(Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f);
        buf = new Vector2();
        touched = false;

        img = new Texture("circle.png");
        background = new Texture("background_simple.png");
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(touched && !touch.epsilonEquals(pos, 1f)) {
            v = buf.set(touch).sub(pos).nor();
            pos.add(v);
        }

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("MenuScreen: touchDown(x = %d, y = %d)\n", screenX, screenY);
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        touched = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.printf("MenuScreen: touchDragged(x = %d, y = %d)\n", screenX, screenY);
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touched = false;
        System.out.printf("MenuScreen: touchUp(x = %d, y = %d)\n", screenX, screenY);
        return true;
    }

}
