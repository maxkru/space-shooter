package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kriuchkov.maksim.spaceshooter.base.ScaledButton;
import kriuchkov.maksim.spaceshooter.screen.GameScreen;
import ru.geekbrains.math.Rect;

public class ButtonNewGame extends ScaledButton {

    private static final float PADDING = 0.35f;
    private GameScreen gameScreen;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
    }

    @Override
    public void action() {
        gameScreen.reset();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.06f);
        setBottom(worldBounds.getBottom() + PADDING);
    }
}
