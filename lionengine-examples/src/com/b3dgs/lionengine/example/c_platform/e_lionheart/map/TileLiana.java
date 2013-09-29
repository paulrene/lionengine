package com.b3dgs.lionengine.example.c_platform.e_lionheart.map;

import com.b3dgs.lionengine.game.purview.Localizable;

/**
 * Tile liana implementation.
 */
public final class TileLiana
        extends Tile
{
    /**
     * @see Tile#Tile(int, int, Integer, int, TileCollision)
     */
    public TileLiana(int width, int height, Integer pattern, int number, TileCollision collision)
    {
        super(width, height, pattern, number, collision);
    }

    /**
     * Get the liana steep collision.
     * 
     * @param c The collision type.
     * @param localizable The localizable.
     * @param offset The offset.
     * @return The collision.
     */
    private Double getLianaSteep(TileCollision c, Localizable localizable, int offset)
    {
        final int startY = getTop() + (isLeft(c.getGroup()) ? 0 : 2);
        return getCollisionY(c.getGroup(), localizable, startY, offset, -10, 5, -2);
    }

    /**
     * Get the liana leaning right collision.
     * 
     * @param c The collision type.
     * @param localizable The localizable.
     * @param offset The offset.
     * @return The collision.
     */
    private Double getLianaLeaning(TileCollision c, Localizable localizable, int offset)
    {
        return getCollisionY(c.getGroup(), localizable, getBottom(), offset, -halfTileHeight, 0, -1);
    }

    /*
     * TilePlatform
     */

    @Override
    public Double getCollisionY(Localizable localizable)
    {
        final TileCollision c = getCollision();
        switch (c)
        {
            case LIANA_HORIZONTAL:
                return getGround(localizable, -2);

            case LIANA_STEEP_RIGHT_2:
                return getLianaSteep(c, localizable, 0);

            case LIANA_STEEP_LEFT_1:
                return getLianaSteep(c, localizable, -14);
            case LIANA_STEEP_LEFT_2:
                return getLianaSteep(c, localizable, 0);

            case LIANA_LEANING_RIGHT_1:
                return getLianaLeaning(c, localizable, halfTileHeight);
            case LIANA_LEANING_RIGHT_2:
                return getLianaLeaning(c, localizable, 0);
            case LIANA_LEANING_RIGHT_3:
                return getLianaLeaning(c, localizable, -halfTileHeight);

            case LIANA_LEANING_LEFT_1:
                return getLianaLeaning(c, localizable, halfTileHeight);
            case LIANA_LEANING_LEFT_2:
                return getLianaLeaning(c, localizable, 0);
            case LIANA_LEANING_LEFT_3:
                return getLianaLeaning(c, localizable, -halfTileHeight);

            default:
                return null;
        }
    }
}