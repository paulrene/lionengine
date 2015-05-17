/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.editor.properties.collision.editor;

import java.util.Collection;

import com.b3dgs.lionengine.editor.ObjectList;
import com.b3dgs.lionengine.game.Collision;
import com.b3dgs.lionengine.game.configurer.ConfigCollisions;
import com.b3dgs.lionengine.game.configurer.Configurer;

/**
 * Represents the collisions list, allowing to add and remove collisions.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class EntityCollisionList
        extends ObjectList<Collision>
{
    /** Configurer reference. */
    private final Configurer configurer;

    /**
     * Create an entity collision list and associate its configurer and properties.
     * 
     * @param configurer The configurer reference.
     */
    public EntityCollisionList(Configurer configurer)
    {
        super(Collision.class);
        this.configurer = configurer;
    }

    /**
     * Load the existing collisions from the entity configurer.
     */
    public void loadCollisions()
    {
        final ConfigCollisions configCollisions = ConfigCollisions.create(configurer);
        final Collection<Collision> collisions = configCollisions.getCollisions();
        loadObjects(collisions);
    }

    /*
     * ObjectList
     */

    @Override
    protected Collision copyObject(Collision collision)
    {
        return new Collision(collision.getName(), collision.getOffsetX(), collision.getOffsetY(), collision.getWidth(),
                collision.getHeight(), collision.hasMirror());
    }

    @Override
    protected Collision createDefaultObject()
    {
        return new Collision("default", 0, 0, 0, 0, false);
    }
}
