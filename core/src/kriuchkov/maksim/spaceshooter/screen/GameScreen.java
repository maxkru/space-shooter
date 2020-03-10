package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import kriuchkov.maksim.spaceshooter.utils.EnemyShipHandler;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.PlayerShip;
import kriuchkov.maksim.spaceshooter.sprite.Star;
import ru.geekbrains.math.Rect;

public class GameScreen extends BaseScreen {

    private enum GameState {
        PLAYING, PAUSED, GAME_OVER
    }

    private GameState gameState;

    private TextureAtlas atlas;
    private TextureAtlas atlasMain;
    private Texture bg;

    private Background background;

    private PlayerShip playerShip;

    private Star[] stars;

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyShipHandler enemyShipHandler;

    private Sound explosionSound;
    private Music gameMusic;

    private static final int STAR_COUNT = 32;

    @Override
    public void show() {
        super.show();

        atlas = new TextureAtlas("textures/texture_atlas.atlas");
        atlasMain = new TextureAtlas("textures/mainAtlas.tpack");
        bulletPool = new BulletPool(20);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(atlasMain, explosionSound);

        enemyShipHandler = new EnemyShipHandler(atlasMain, bulletPool, explosionPool);
        playerShip = new PlayerShip(atlasMain, bulletPool, explosionPool);
        bg = new Texture("background_simple.png");
        background = new Background(bg);

        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

        gameState = GameState.PLAYING;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    @Override
    public boolean keyDown(int keycode) {
        return gameState == GameState.PLAYING && playerShip.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return gameState == GameState.PLAYING && playerShip.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
        return gameState == GameState.PLAYING && playerShip.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        super.touchDragged(touch, pointer);
        return gameState == GameState.PLAYING && playerShip.touchDragged(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        super.touchUp(touch, pointer, button);
        return gameState == GameState.PLAYING && playerShip.touchUp(touch, pointer, button);
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
        enemyShipHandler.setWorldBounds(worldBounds);
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
        enemyShipHandler.dispose();
    }

    private void update(float delta) {
        for(Star star : stars)
            star.update(delta);
        enemyShipHandler.update(delta, gameState == GameState.PLAYING);
        bulletPool.updateAllActive(delta);
        if (gameState == GameState.PLAYING) {
            playerShip.update(delta);
            checkCollisions();
        }
        explosionPool.updateAllActive(delta);
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars)
            star.draw(batch);
        bulletPool.drawAllActive(batch);
        enemyShipHandler.drawAllActive(batch);
        explosionPool.drawAllActive(batch);
        if (gameState == GameState.PLAYING)
            playerShip.draw(batch);
        batch.end();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyShipHandler.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    public void checkCollisions() {
        for (EnemyShip enemyShip : enemyShipHandler.getActiveEnemyShips()) {
            float minDist = enemyShip.getHalfHeight() + playerShip.getHalfHeight();
            if (enemyShip.pos.dst2(playerShip.pos) < minDist * minDist) {
                enemyShip.destroy();
                // TODO: damage player ship, with amount specific for each ship type
            }
        }

        for (Bullet bullet : bulletPool.getActiveObjects()) {
            if (bullet.getOwner() == playerShip) {
                for (EnemyShip enemyShip : enemyShipHandler.getActiveEnemyShips()) {
                    if (enemyShip.collidesWith(bullet)) {
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy();
                    }
                }
            } else {
                if (playerShip.collidesWith(bullet)) {
                    playerShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }

        if (playerShip.isDestroyed()) {
            gameState = GameState.GAME_OVER;
            gameMusic.stop();
        }
    }

    @Override
    public void pause() {
        gameMusic.pause();
    }

    @Override
    public void resume() {
        gameMusic.play();
    }
}
