package kriuchkov.maksim.spaceshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import kriuchkov.maksim.spaceshooter.base.BaseScreen;
import kriuchkov.maksim.spaceshooter.pool.BulletPool;
import kriuchkov.maksim.spaceshooter.pool.ExplosionPool;
import kriuchkov.maksim.spaceshooter.sprite.Background;
import kriuchkov.maksim.spaceshooter.sprite.Bullet;
import kriuchkov.maksim.spaceshooter.sprite.ButtonNewGame;
import kriuchkov.maksim.spaceshooter.sprite.EnemyShip;
import kriuchkov.maksim.spaceshooter.sprite.MessageGameOver;
import kriuchkov.maksim.spaceshooter.sprite.PlayerShip;
import kriuchkov.maksim.spaceshooter.sprite.Star;
import kriuchkov.maksim.spaceshooter.utils.EnemyShipHandler;
import kriuchkov.maksim.spaceshooter.utils.Font;
import ru.geekbrains.math.Rect;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 32;
    private static final float FONT_SIZE = 0.03f;

    private static final String SCORE_TEXT = "Score: ";
    private static final String HP_TEXT = "HP: ";
    private static final String LEVEL_TEXT = "Level ";
    private static final float TEXT_PADDING = 0.01f;

    private enum GameState {
        PLAYING, PAUSED, GAME_OVER
    }

    private GameState gameState;
    private GameState prePauseGameState;

    private TextureAtlas atlas;
    private TextureAtlas atlasMain;
    private Texture bg;

    private Background background;

    private PlayerShip playerShip;

    private Star[] stars;

    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyShipHandler enemyShipHandler;

    private Sound explosionSound;
    private Music gameMusic;

    private Font font;

    private int score;
    private int gameLevel;

    private StringBuilder sbTextPrint;

    @Override
    public void show() {
        super.show();

        atlas = new TextureAtlas("textures/texture_atlas.atlas");
        atlasMain = new TextureAtlas("textures/mainAtlas.tpack");
        bulletPool = new BulletPool(20);
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(atlasMain, explosionSound);

        playerShip = new PlayerShip(atlasMain, bulletPool, explosionPool);
        enemyShipHandler = new EnemyShipHandler(atlasMain, bulletPool, explosionPool, playerShip);
        bg = new Texture("background_simple.png");
        background = new Background(bg);

        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

        gameState = GameState.PLAYING;
        prePauseGameState = GameState.PLAYING;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();

        messageGameOver = new MessageGameOver(atlasMain);
        buttonNewGame = new ButtonNewGame(atlasMain, this);

        font = new Font("fonts/Carlito.fnt", "fonts/Carlito.png");
        font.setSize(FONT_SIZE);
        sbTextPrint = new StringBuilder();

        gameLevel = 1;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.P) {
            if (gameState == GameState.PAUSED)
                resume();
            else
                pause();
        }
        if (keycode == Input.Keys.R && gameState == GameState.GAME_OVER)
            reset();
        return gameState == GameState.PLAYING && playerShip.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return gameState == GameState.PLAYING && playerShip.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
        if (gameState == GameState.GAME_OVER)
            buttonNewGame.touchDown(touch, pointer, button);
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
        if (gameState == GameState.GAME_OVER)
            buttonNewGame.touchUp(touch, pointer, button);
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
        messageGameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
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
        font.dispose();
    }

    private void update(float delta) {
        for(Star star : stars)
            star.update(delta);
        explosionPool.updateAllActive(delta);
        if (gameState == GameState.PLAYING) {
            enemyShipHandler.update(delta, true);
            bulletPool.updateAllActive(delta);
            playerShip.update(delta);
            checkCollisions();
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars)
            star.draw(batch);
        if (gameState == GameState.PLAYING) {
            bulletPool.drawAllActive(batch);
            enemyShipHandler.drawAllActive(batch);
            playerShip.draw(batch);
        }
        explosionPool.drawAllActive(batch);
        if (gameState == GameState.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyShipHandler.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void checkCollisions() {
        for (EnemyShip enemyShip : enemyShipHandler.getActiveEnemyShips()) {
            float minDist = enemyShip.getHalfHeight() + playerShip.getHalfHeight();
            if (enemyShip.pos.dst2(playerShip.pos) < minDist * minDist) {
                enemyShip.destroy();
                playerShip.damage(enemyShip.getCollisionDamage());
                score += enemyShip.getPointsForDestruction();
            }
        }

        for (Bullet bullet : bulletPool.getActiveObjects()) {
            if (bullet.getOwner() == playerShip) {
                for (EnemyShip enemyShip : enemyShipHandler.getActiveEnemyShips()) {
                    if (enemyShip.collidesWith(bullet)) {
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy();
                        if (enemyShip.isDestroyed())
                            score += enemyShip.getPointsForDestruction();
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
        prePauseGameState = gameState;
        gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        gameMusic.play();
        gameState = prePauseGameState;
    }

    public void reset() {
        for (Bullet bullet : bulletPool.getActiveObjects()) {
            bullet.destroy();
        }
        enemyShipHandler.reset();
        playerShip.reset();
        gameState = GameState.PLAYING;
        gameMusic.play();
        score = 0;
        gameLevel = 1;
    }

    private void printInfo() {
        sbTextPrint.setLength(0);
        font.draw(batch, sbTextPrint.append(SCORE_TEXT).append(score), worldBounds.getLeft() + TEXT_PADDING, worldBounds.getTop() - TEXT_PADDING, Align.left);

        sbTextPrint.setLength(0);
        font.draw(batch, sbTextPrint.append(HP_TEXT).append(playerShip.getHp()), 0f, worldBounds.getTop() - TEXT_PADDING, Align.center);

        sbTextPrint.setLength(0);
        font.draw(batch, sbTextPrint.append(LEVEL_TEXT).append(gameLevel), worldBounds.getRight() - TEXT_PADDING, worldBounds.getTop() - TEXT_PADDING, Align.right);
    }

}
