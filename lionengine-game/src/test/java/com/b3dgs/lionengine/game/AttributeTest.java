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
package com.b3dgs.lionengine.game;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test attribute class.
 */
public class AttributeTest
{
    /**
     * Test attributes functions.
     */
    @Test
    public void testAttribute()
    {
        final Attribute attribute = new Attribute();
        Assert.assertEquals(0, attribute.get());
        final int step = 10;
        attribute.increase(step);
        Assert.assertEquals(step, attribute.get());
        attribute.set(step - 1);
        Assert.assertEquals(step - 1, attribute.get());
    }
}
