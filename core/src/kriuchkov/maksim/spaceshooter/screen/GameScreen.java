package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.EnemyShipPool;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.PlayerShip;
import kriuchkov.maksim.spaceshooter.sprite.Star;
import ru.geekbrains.math.Rect;

public class GameScreen extends BaseScreen {


    private TextureAtlas atlas;
    private TextureAtlas atlasMain;
    private Texture bg;

    private Background background;

    private PlayerShip playerShip;

    private Star[] stars;

    private EnemyShipPool enemyShipPool;
    private BulletPool bulletPool;

    private Music gameMusic;

    private static final int STAR_COUNT = 32;

    @Override
    public void show() {
        super.show();

        atlas = new TextureAtlas("textures/texture_atlas.atlas");
        atlasMain = new TextureAtlas("textures/mainAtlas.tpack");
        bulletPool = new BulletPool(20);
        enemyShipPool = new EnemyShipPool();
        playerShip = new PlayerShip(atlasMain, bulletPool);
        bg = new Texture("background_simple.png");
        background = new Background(bg);

        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    @Override
    public boolean keyDown(int keycode) {
        return playerShip.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return playerShip.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
        return playerShip.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        super.touchDragged(touch, pointer);
        return playerShip.touchDragged(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        super.touchUp(touch, pointer, button);
        return playerShip.touchUp(touch, pointer, button);
    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        playerShip.resize(worldBounds);
        for(Star star : stars)
            star.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        playerShip.dispose();
        gameMusic.dispose();
        enemyShipPool.dispose();
    }

    private void update(float delta) {
        playerShip.update(delta);

        for(Star star : stars)
            star.update(delta);

        enemyShipPool.updateAllActive(delta);

        bulletPool.updateAllActive(delta);
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars)
            star.draw(batch);
        bulletPool.drawAllActive(batch);
        enemyShipPool.drawAllActive(batch);
        playerShip.draw(batch);
        batch.end();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyShipPool.freeAllDestroyedActiveObjects();
    }
}
