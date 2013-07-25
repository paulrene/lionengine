package com.b3dgs.lionengine.drawable;

import java.awt.Transparency;
import java.awt.image.BufferedImage;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.utility.UtilityImage;

/**
 * Tiled sprite implementation.
 */
final class SpriteTiledImpl
        extends SpriteImpl
        implements SpriteTiled
{
    /** Number of horizontal tiles. */
    private final int horizontalTiles;
    /** Number of vertical tiles. */
    private final int verticalTiles;
    /** Original tile width. */
    private final int tileOriginalWidth;
    /** Original tile height. */
    private final int tileOriginalHeight;
    /** Total number of tiles. */
    private final int tilesNumber;

    /**
     * Create a new tiled sprite.
     * 
     * @param media The sprite media.
     * @param tileWidth The tile width.
     * @param tileHeight The tile height.
     */
    SpriteTiledImpl(Media media, int tileWidth, int tileHeight)
    {
        super(media);

        Check.argument(tileWidth > 0 && tileHeight > 0, "Sprite tile size must be strictly positive !");

        tileOriginalWidth = tileWidth;
        tileOriginalHeight = tileHeight;
        horizontalTiles = widthOriginal / tileWidth;
        verticalTiles = heightOriginal / tileHeight;
        tilesNumber = horizontalTiles * verticalTiles;
    }

    /**
     * Create a new tiled sprite from an existing surface.
     * 
     * @param surface The surface reference.
     * @param tileWidth The tile width.
     * @param tileHeight The tile height.
     */
    SpriteTiledImpl(BufferedImage surface, int tileWidth, int tileHeight)
    {
        super(surface);

        Check.argument(tileWidth > 0 && tileHeight > 0, "Sprite tile size must be strictly positive !");

        tileOriginalWidth = tileWidth;
        tileOriginalHeight = tileHeight;
        horizontalTiles = widthOriginal / tileWidth;
        verticalTiles = heightOriginal / tileHeight;
        tilesNumber = horizontalTiles * verticalTiles;
    }

    /*
     * SpriteTiled
     */

    @Override
    public void render(Graphic g, int tile, int x, int y)
    {
        final int cx = tile % horizontalTiles;
        final int cy = (int) Math.floor(tile / (double) horizontalTiles);
        final int w = getTileWidth();
        final int h = getTileHeight();

        g.drawImage(surface, x, y, x + w, y + h, cx * w, cy * h, cx * w + w, cy * h + h);
    }

    @Override
    public int getTileWidth()
    {
        return getWidth() / getTilesHorizontal();
    }

    @Override
    public int getTileHeight()
    {
        return getHeight() / getTilesVertical();
    }

    @Override
    public int getTileWidthOriginal()
    {
        return tileOriginalWidth;
    }

    @Override
    public int getTileHeightOriginal()
    {
        return tileOriginalHeight;
    }

    @Override
    public int getTilesHorizontal()
    {
        return horizontalTiles;
    }

    @Override
    public int getTilesVertical()
    {
        return verticalTiles;
    }

    @Override
    public int getTilesNumber()
    {
        return tilesNumber;
    }

    @Override
    public BufferedImage getTile(int tile)
    {
        final BufferedImage buffer = UtilityImage.createBufferedImage(getTileWidth(), getTileHeight(),
                Transparency.BITMASK);
        final Graphic g = new Graphic(buffer.createGraphics());
        final int cx = tile % getTilesHorizontal();
        final int cy = (int) Math.floor(tile / (double) getTilesHorizontal());
        final int w = getTileWidth();
        final int h = getTileHeight();

        g.drawImage(surface, 0, 0, w, h, cx * w, cy * h, cx * w + w, cy * h + h);
        g.dispose();

        return buffer;
    }

    @Override
    public BufferedImage getTileReference(int tile)
    {
        final int cx = tile % getTilesHorizontal();
        final int cy = (int) Math.floor(tile / (double) getTilesHorizontal());
        final int w = getTileWidth();
        final int h = getTileHeight();

        return surface.getSubimage(cx * w, cy * h, w, h);
    }

    @Override
    public SpriteTiled instanciate()
    {
        return new SpriteTiledImpl(surface, getWidthOriginal(), getHeightOriginal());
    }
}
