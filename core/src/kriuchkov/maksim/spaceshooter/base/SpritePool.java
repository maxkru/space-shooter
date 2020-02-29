package kriuchkov.maksim.spaceshooter.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SpritePool<T extends Sprite> {

    private List<T> activeObjects;
    private List<T> freeObjects;

    public SpritePool() {
        activeObjects = new ArrayList<>();
        freeObjects = new ArrayList<>();
    }

    protected abstract T createNewObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty())
            object = createNewObject();
        else
            object = freeObjects.remove(freeObjects.size() - 1);
        activeObjects.add(object);
        return object;
    }

    public void updateAllActive(float delta) {
        for(Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed())
                sprite.update(delta);
        }
    }

    public void drawAllActive(SpriteBatch batch) {
        for(Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed())
                sprite.draw(batch);
        }
    }

    public void freeAllDestroyedActiveObjects() {
        for(int i = 0; i < activeObjects.size(); i++) {
            T obj = activeObjects.get(i);
            if (obj.isDestroyed()) {
                free(obj);
                obj.flushDestroyed();
                i--;
            }
        }
    }

    private void free(T obj) {
        if (activeObjects.remove(obj))
            freeObjects.add(obj);
    }

    public void dispose() {
        freeObjects.clear();
        activeObjects.clear();
    }
}
