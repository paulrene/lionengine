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
package com.b3dgs.lionengine;

/**
 * Standard range description, with a minimum and a maximum.
 */
public class Range
{
    /** Positive integer numbers including 0. */
    public static final Range INT_POSITIVE = new Range(0, Integer.MAX_VALUE);
    /** Positive integer numbers excluding 0. */
    public static final Range INT_POSITIVE_STRICT = new Range(1, Integer.MAX_VALUE);

    /** Minimum value. */
    private int min;
    /** Maximum value. */
    private int max;

    /**
     * Create an empty range.
     */
    public Range()
    {
        min = 0;
        max = 0;
    }

    /**
     * Create a range.
     * 
     * @param min The minimum value.
     * @param max The maximum value.
     */
    public Range(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    /**
     * Set minimum value.
     * 
     * @param min The minimum value.
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * Set maximum value.
     * 
     * @param max The maximum value.
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * Get minimum value.
     * 
     * @return The minimum value.
     */
    public int getMin()
    {
        return min;
    }

    /**
     * Get maximum value.
     * 
     * @return The maximum value.
     */
    public int getMax()
    {
        return max;
    }
}