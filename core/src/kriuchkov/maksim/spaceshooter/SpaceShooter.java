package kriuchkov.maksim.spaceshooter;

import com.badlogic.gdx.Game;

import kriuchkov.maksim.spaceshooter.screen.MenuScreen;

public class SpaceShooter extends Game {

    @Override
    public void create () {
        setScreen(new MenuScreen());
    }

}
