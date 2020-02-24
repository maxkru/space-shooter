package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BaseScreen implements Screen, InputProcessor {

    protected SpriteBatch batch;

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown(keycode = " + keycode + ")");
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp(keycode = " + keycode + ")");
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped(character = " + character + ")");
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.printf("touchDown(x = %d, y = %d)\n", screenX, screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.printf("touchUp(x = %d, y = %d)\n", screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.printf("touchDragged(x = %d, y = %d)\n", screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        System.out.printf("mouseMoved(x = %d, y = %d)\n", screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        System.out.printf("scrolled(amount = %d)\n", amount);
        return false;
    }

    @Override
    public void show() {
        System.out.println("show()");
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        System.out.printf("resize(width = %d, height = %d)\n", width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause()");
    }

    @Override
    public void resume() {
        System.out.println("resume()");
    }

    @Override
    public void hide() {
        System.out.println("hide()");
    }

    @Override
    public void dispose() {
        System.out.println("dispose()");
    }
}
