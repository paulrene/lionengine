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
package com.b3dgs.lionengine.geom;

/**
 * Line interface.
 */
public interface Line
{
    /**
     * Set the line coordinates.
     * 
     * @param x1 The x coordinate of the start point
     * @param y1 The y coordinate of the start point
     * @param x2 The x coordinate of the end point
     * @param y2 The y coordinate of the end point
     */
    void set(double x1, double y1, double x2, double y2);

    /**
     * Get the x1 location.
     * 
     * @return The x1 location.
     */
    double getX1();

    /**
     * Get the x2 location.
     * 
     * @return The x2 location.
     */
    double getX2();

    /**
     * Get the y1 location.
     * 
     * @return The y1 location.
     */
    double getY1();

    /**
     * Get the y2 location.
     * 
     * @return The y2 location.
     */
    double getY2();
}
