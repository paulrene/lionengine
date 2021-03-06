/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.selector;

import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.geom.Rectangle;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Viewer;

/**
 * This class allows to perform a selection inside a dedicated area, and retrieve the {@link Rectangle} representing the
 * selection. Usage example:
 * <ul>
 * <li>{@link #setClickableArea(Viewer)} - Required to define the area where selection is allowed</li>
 * <li>{@link #setClickSelection(int)} - Recommended to set which mouse click should be used to start selection</li>
 * <li>{@link #setSelectionColor(ColorRgba)} - Optional for a custom color selection</li>
 * </ul>
 * Result can be retrieved by using {@link #addListener(SelectorListener)} to notify them the computed selection.
 * It will be then easy to check if objects are inside this area, and set them as <i>selected</i>.
 * 
 * @see SelectorListener
 * @see Cursor
 * @see Viewer
 */
public class Selector extends FeaturableModel implements SelectorConfigurer
{
    /** Selector model. */
    private final SelectorModel model = addFeatureAndGet(new SelectorModel());
    /** Selector refresher. */
    private final SelectorRefresher refresher = addFeatureAndGet(new SelectorRefresher(model));
    /** Selector displayer. */
    private final SelectorDisplayer displayer = addFeatureAndGet(new SelectorDisplayer(model));

    /**
     * Create the selector.
     */
    public Selector()
    {
        super();
    }

    /**
     * Add a selector listener.
     * 
     * @param listener The selector listener reference.
     */
    public void addListener(SelectorListener listener)
    {
        refresher.addListener(listener);
    }

    /**
     * Remove a selector listener.
     * 
     * @param listener The selector listener reference.
     */
    public void removeListener(SelectorListener listener)
    {
        refresher.removeListener(listener);
    }

    /**
     * Set the selection color.
     * 
     * @param color The selection color.
     */
    public void setSelectionColor(ColorRgba color)
    {
        displayer.setSelectionColor(color);
    }

    /*
     * SelectorConfigurer
     */

    @Override
    public void setClickSelection(int click)
    {
        model.setClickSelection(click);
    }

    @Override
    public void setClickableArea(Rectangle area)
    {
        model.setClickableArea(area);
    }

    @Override
    public void setClickableArea(Viewer viewer)
    {
        model.setClickableArea(viewer);
    }
}
