package kriuchkov.maksim.spaceshooter.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import kriuchkov.maksim.spaceshooter.base.ScaledButton;
import kriuchkov.maksim.spaceshooter.screen.GameScreen;
import ru.geekbrains.math.Rect;

public class ButtonStart extends ScaledButton {


    private static final float PADDING = 0.05f;
    private final Game game;

    public ButtonStart(TextureAtlas atlas, Game game) {
        super(atlas.findRegion("button_go"));
        this.game = game;
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
        setLeft(worldBounds.getLeft() + PADDING);
        setBottom(worldBounds.getBottom() + PADDING);
    }
}
