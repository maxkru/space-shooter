package kriuchkov.maksim.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Font extends BitmapFont {

    public Font(String fontFileInternalPath, String imageFileInternalPath) {
        super(Gdx.files.internal(fontFileInternalPath), Gdx.files.internal(imageFileInternalPath), false, false);
    }

    public void setSize(float size) {
        getData().setScale(size / getCapHeight());
    }
}
