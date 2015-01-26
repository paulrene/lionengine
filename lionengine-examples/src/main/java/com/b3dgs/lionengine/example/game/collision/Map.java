/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.game.map.CollisionTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.TileGame;

/**
 * Map implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 * @see com.b3dgs.lionengine.example.game.map
 */
final class Map
        extends MapTileGame<TileGame>
{
    /**
     * Constructor.
     */
    Map()
    {
        super(16, 16, TileCollision.values());
    }

    /**
     * Adjust the collision.
     */
    public void adjustCollisions()
    {
        for (int tx = 0; tx < getWidthInTile(); tx++)
        {
            for (int ty = 0; ty < getHeightInTile(); ty++)
            {
                final TileGame tile = getTile(tx, ty);
                final TileGame top = getTile(tx, ty + 1);
                final TileGame bottom = getTile(tx, ty - 1);
                if (top != null && tile != null && top.getCollision() != TileCollision.NONE
                        && tile.getCollision() != TileCollision.NONE)
                {
                    tile.setCollision(TileCollision.WALL);
                }
                if ((top == null || top.getCollision() == TileCollision.NONE) && bottom != null && tile != null
                        && bottom.getCollision() != TileCollision.NONE && tile.getCollision() != TileCollision.NONE)
                {
                    tile.setCollision(TileCollision.TUBE);
                }
            }
        }
    }

    /*
     * MapTileGame
     */

    @Override
    public TileGame createTile(int width, int height, Integer pattern, int number, CollisionTile collision)
    {
        return new TileGame(width, height, pattern, number, collision);
    }

    @Override
    public TileCollision getCollisionFrom(String collision)
    {
        try
        {
            return TileCollision.valueOf(collision);
        }
        catch (final NullPointerException exception)
        {
            return TileCollision.NONE;
        }
    }
}