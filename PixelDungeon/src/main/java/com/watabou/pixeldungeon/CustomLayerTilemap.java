package com.watabou.pixeldungeon;

import com.watabou.noosa.CompositeImage;
import com.watabou.noosa.Image;
import com.watabou.pixeldungeon.levels.Level;

import java.util.ArrayList;

/**
 * Created by mike on 15.02.2018.
 * This file is part of Remixed Pixel Dungeon.
 */

public class CustomLayerTilemap extends DungeonTilemap {

    private ArrayList<CustomLayerTilemap> mLayers    = new ArrayList<>();
    private boolean                       trasparent = false;

    public CustomLayerTilemap(Level level, String tiles, int[] map) {
        super(level, tiles);
        map(map, level.getWidth());
    }

    public void addLayer(String tiles, int[] map) {
        mLayers.add(new CustomLayerTilemap(level, tiles, map));
    }

    public void setAlpha(int layer, float alpha) {
        mLayers.get(layer).alpha(alpha);
    }

    @Override
    public Image tile(int pos) {
        ArrayList<Image> imgs = new ArrayList<>();

        if (data[pos] >= 0) {
            Image img = new Image(getTexture());
            img.frame(getTileset().get(data[pos]));
            imgs.add(img);
        }

        for (CustomLayerTilemap layer : mLayers) {
            if (layer.data[pos] >= 0) {
                Image img = new Image(getTexture());
                img.frame(getTileset().get(layer.data[pos]));
                imgs.add(img);
            }
        }
        if (!imgs.isEmpty()) {
            return new CompositeImage(imgs);
        }
        return null;
    }

    @Override
    public void draw() {
/*
        if (trasparent) {
            GLES20.glBlendFuncSeparate(GLES20.GL_DST_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA, GLES20.GL_ZERO, GLES20.GL_ZERO);
        }
*/
        super.draw();
/*
        if (trasparent) {
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        }
*/
        for (CustomLayerTilemap layer : mLayers) {
//           GLES20.glBlendFuncSeparate(GLES20.GL_DST_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA, GLES20.GL_ZERO, GLES20.GL_ZERO);
            layer.draw();
        }
    }

    public void updateAll() {
        updated.set(0, 0, level.getWidth(), level.getHeight());

        for (CustomLayerTilemap layer : mLayers) {
            layer.updated.set(0, 0, level.getWidth(), level.getHeight());
        }
    }

    public void updateCell(int cell, Level level) {
        int x = level.cellX(cell);
        int y = level.cellY(cell);

        updated.union(x, y);
        for (CustomLayerTilemap layer : mLayers) {
            layer.updated.union(x, y);
        }
    }

    public void setTrasparent(boolean trasparent) {
        this.trasparent = trasparent;
    }
}
