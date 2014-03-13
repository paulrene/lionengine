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
package com.b3dgs.lionengine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the utility project stats class.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class UtilityProjectStatsTest
{
    /**
     * Test the project stats class.
     * 
     * @throws SecurityException If error.
     * @throws NoSuchMethodException If error.
     * @throws IllegalArgumentException If error.
     * @throws IllegalAccessException If error.
     * @throws InstantiationException If error.
     */
    @Test
    public void testProjectStatsClass() throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException
    {
        final Constructor<UtilityProjectStats> utilityProjectStats = UtilityProjectStats.class.getDeclaredConstructor();
        utilityProjectStats.setAccessible(true);
        try
        {
            final UtilityProjectStats clazz = utilityProjectStats.newInstance();
            Assert.assertNotNull(clazz);
            Assert.fail();
        }
        catch (final InvocationTargetException exception)
        {
            // Success
        }
    }

    /**
     * Test project stats.
     */
    @Test
    public void testProjectStats()
    {
        UtilityProjectStats.start("src");
    }
}