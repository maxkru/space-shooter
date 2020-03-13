package kriuchkov.maksim.spaceshooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {

    public Font(String fontFileInternalPath, String imageFileInternalPath) {
        super(Gdx.files.internal(fontFileInternalPath), Gdx.files.internal(imageFileInternalPath), false, false);
        getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setSize(float size) {
        getData().setScale(size / getCapHeight());
    }

    public GlyphLayout draw(Batch batch, CharSequence charSequence, float x, float y, int hAlign) {
        return super.draw(batch, charSequence, x, y, 0f, hAlign, false);
    }
}
