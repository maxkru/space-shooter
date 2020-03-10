package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kriuchkov.maksim.spaceshooter.base.ScaledButton;
import kriuchkov.maksim.spaceshooter.screen.GameScreen;
import ru.geekbrains.math.Rect;

public class ButtonNewGame extends ScaledButton {

    private static final float POS_X = 0f;
    private static final float POS_Y = -0.15f;
    private GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void action() {
        gameScreen.reset();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.06f);
        pos.x = POS_X;
        pos.y = POS_Y;
    }
}
