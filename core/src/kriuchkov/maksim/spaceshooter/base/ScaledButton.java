package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Sprite {
    private static final float PRESS_SCALE = 0.9f;

    private int pointer;
    private boolean pressed;

    public ScaledButton(TextureRegion region) {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (pressed || !isMe(touch)) {
            return false;
        }

        this.pointer = pointer;
        this.scale = PRESS_SCALE;
        this.pressed = true;
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (!pressed || this.pointer != pointer)
            return false;

        boolean isMe = isMe(touch);
        if (isMe)
            action();

        pressed = false;
        scale = 1f;
        return isMe;
    }

    public abstract void action();
}
