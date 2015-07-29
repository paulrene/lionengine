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
package com.b3dgs.lionengine.editor.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.UtilMath;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.editor.ObjectRepresentation;
import com.b3dgs.lionengine.editor.project.ProjectModel;
import com.b3dgs.lionengine.editor.project.tester.ObjectsTester;
import com.b3dgs.lionengine.editor.utility.UtilWorld;
import com.b3dgs.lionengine.game.Camera;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.object.Factory;
import com.b3dgs.lionengine.game.object.Handler;
import com.b3dgs.lionengine.game.object.ObjectGame;
import com.b3dgs.lionengine.game.object.Services;
import com.b3dgs.lionengine.game.object.Setup;
import com.b3dgs.lionengine.game.object.SetupSurface;
import com.b3dgs.lionengine.game.trait.transformable.Transformable;
import com.b3dgs.lionengine.geom.Geom;
import com.b3dgs.lionengine.geom.Point;
import com.b3dgs.lionengine.geom.Rectangle;

/**
 * Allows to control the object on the editor with the mouse.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class ObjectControl
{
    /** Mouse over object flag. */
    private final Map<ObjectGame, Boolean> objectsOver = new HashMap<>();
    /** Mouse selection object flag. */
    private final Map<ObjectGame, Boolean> objectsSelection = new HashMap<>();
    /** Camera reference. */
    private final Camera camera;
    /** Factory reference. */
    private final Factory factory;
    /** Map reference. */
    private final MapTile map;
    /** Handler object. */
    private final Handler handler;
    /** Moving object flag. */
    private boolean dragging;

    /**
     * Create the object control from object handler.
     * 
     * @param services The services reference.
     */
    public ObjectControl(Services services)
    {
        camera = services.get(Camera.class);
        factory = services.get(Factory.class);
        map = services.get(MapTile.class);
        handler = services.get(Handler.class);
    }

    /**
     * Update the mouse over object flag.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    public void updateMouseOver(int mx, int my)
    {
        resetMouseOver();
        final ObjectGame object = getObject(mx, my);
        if (object != null)
        {
            setMouseOver(object, true);
        }
    }

    /**
     * Update the dragging mouse value.
     * 
     * @param oldMx The mouse old horizontal location.
     * @param oldMy The mouse old vertical location.
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    public void updateDragging(int oldMx, int oldMy, int mx, int my)
    {
        if (!dragging)
        {
            dragging = true;
        }
        final int th = map.getTileHeight();
        final int areaY = UtilMath.getRounded(camera.getHeight(), th);
        final double ox = oldMx + camera.getX();
        final double oy = areaY - oldMy + camera.getY() - 1;
        final double x = mx + camera.getX();
        final double y = areaY - my + camera.getY() - 1;

        for (final Transformable object : handler.get(Transformable.class))
        {
            if (isSelected(object.getOwner()))
            {
                object.teleport(object.getX() + x - ox, object.getY() + y - oy);
            }
        }
    }

    /**
     * Reset the objects mouse over state.
     */
    public void resetMouseOver()
    {
        objectsOver.clear();
    }

    /**
     * Stop the dragging.
     */
    public void stopDragging()
    {
        dragging = false;
    }

    /**
     * Add a new object at the mouse location.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    public void addAt(int mx, int my)
    {
        final Media media = ProjectModel.INSTANCE.getSelection();
        if (ObjectsTester.isObjectFile(media))
        {
            try
            {
                final Setup setup = factory.getSetup(media);
                if (setup instanceof SetupSurface)
                {
                    final ObjectRepresentation object = factory.create(media, ObjectRepresentation.class);
                    final Point point = UtilWorld.getPoint(map, camera, mx, my);
                    object.place(UtilMath.getRounded(point.getX(), map.getTileWidth()),
                                 UtilMath.getRounded(point.getY(), map.getTileHeight()));
                    handler.add(object);
                }
            }
            catch (final LionEngineException exception)
            {
                Verbose.exception(ObjectControl.class, "addAt", exception, media.getPath());
            }
        }
    }

    /**
     * Remove the object at the specified location if exists.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    public void removeFrom(int mx, int my)
    {
        final ObjectGame object = getObject(mx, my);
        if (object != null)
        {
            object.destroy();
        }
    }

    /**
     * Select all objects inside the selection area.
     * 
     * @param selectionArea The selection area.
     */
    public void selectObjects(Rectangle selectionArea)
    {
        for (final Transformable object : handler.get(Transformable.class))
        {
            final int th = map.getTileHeight();
            final int height = camera.getHeight();
            final int offy = height - UtilMath.getRounded(height, th);
            final int sx = UtilMath.getRounded((int) selectionArea.getMinX(), map.getTileWidth());
            final int sy = UtilMath.getRounded(height - (int) selectionArea.getMinY() - offy, th);

            if (hitObject(object, sx, sy))
            {
                setObjectSelection(object.getOwner(), true);
            }
        }
    }

    /**
     * Unselect objects.
     */
    public void unselectObjects()
    {
        for (final ObjectGame object : handler.values())
        {
            setObjectSelection(object, false);
        }
    }

    /**
     * Set the mouse over state the the specified object.
     * 
     * @param object The object reference.
     * @param over <code>true</code> if mouse if over, <code>false</code> else.
     */
    public void setMouseOver(ObjectGame object, boolean over)
    {
        objectsOver.put(object, Boolean.valueOf(over));
    }

    /**
     * Set the object selected state.
     * 
     * @param object The object reference.
     * @param selected <code>true</code> if selected, <code>false</code> else.
     */
    public void setObjectSelection(ObjectGame object, boolean selected)
    {
        objectsSelection.put(object, Boolean.valueOf(selected));
    }

    /**
     * Set the object location.
     * 
     * @param object The object reference.
     * @param x The horizontal location.
     * @param y The vertical location.
     * @param side 1 for place, -1 for move.
     */
    public void setObjectLocation(Transformable object, int x, int y, int side)
    {
        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        object.teleport(UtilMath.getRounded(x + tw / 2, tw), UtilMath.getRounded(y + th / 2, th));
    }

    /**
     * Get the object at the specified mouse location.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     * @return The object reference, <code>null</code> if none.
     */
    public ObjectGame getObject(int mx, int my)
    {
        final Point tile = UtilWorld.getPoint(map, camera, mx, my);
        final int x = tile.getX();
        final int y = tile.getY();
        for (final Transformable object : handler.get(Transformable.class))
        {
            if (hitObject(object, x, y))
            {
                return object.getOwner();
            }
        }
        return null;
    }

    /**
     * Get the list of selected objects.
     * 
     * @return The selected objects.
     */
    public Collection<ObjectGame> getSelectedObjects()
    {
        final Collection<ObjectGame> list = new ArrayList<>(0);
        for (final ObjectGame object : handler.values())
        {
            if (isSelected(object))
            {
                list.add(object);
            }
        }
        return list;
    }

    /**
     * Check the mouse over object flag.
     * 
     * @param object The object to check.
     * @return <code>true</code> if over, <code>false</code> else.
     */
    public boolean isOver(ObjectGame object)
    {
        if (objectsOver.containsKey(object))
        {
            return objectsOver.get(object).booleanValue();
        }
        return false;
    }

    /**
     * Check the mouse object selection flag.
     * 
     * @param object The object to check.
     * @return <code>true</code> if selected, <code>false</code> else.
     */
    public boolean isSelected(ObjectGame object)
    {
        if (objectsSelection.containsKey(object))
        {
            return objectsSelection.get(object).booleanValue();
        }
        return false;
    }

    /**
     * Check if mouse if dragging.
     * 
     * @return <code>true</code> if dragging, <code>false</code> else.
     */
    public boolean isDragging()
    {
        return dragging;
    }

    /**
     * Check if cursor is over at least one object.
     * 
     * @return <code>true</code> if over, <code>false</code> else.
     */
    public boolean hasOver()
    {
        return !objectsOver.isEmpty();
    }

    /**
     * Check if there it at least one object selected.
     * 
     * @return <code>true</code> if there is selected objects, <code>false</code> else.
     */
    public boolean hasSelection()
    {
        for (final ObjectGame object : handler.values())
        {
            if (isSelected(object))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if object is hit.
     * 
     * @param object The object to check.
     * @param x The point x.
     * @param y The point y.
     * @return <code>true</code> if hit, <code>false</code> else.
     */
    private static boolean hitObject(Transformable object, int x, int y)
    {
        final Rectangle r = Geom.createRectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight());
        return r.contains(x, y);
    }
}
