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
package com.b3dgs.lionengine.game;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Version;
import com.b3dgs.lionengine.core.EngineCore;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.mock.FactoryGraphicMock;
import com.b3dgs.lionengine.mock.FactoryMediaMock;

/**
 * Test the factory object game.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class FactoryObjectGameTest
{
    /**
     * Prepare the test.
     */
    @BeforeClass
    public static void prepareTest()
    {
        EngineCore.start("FactoryObjectGameTest", Version.create(1, 0, 0), Verbose.NONE, new FactoryGraphicMock(),
                new FactoryMediaMock());
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        EngineCore.terminate();
    }

    /**
     * Test the game factory.
     */
    @Test
    public void testFactoryObjectGame()
    {
        final FactoryObject factory = new FactoryObject();

        try
        {
            factory.create(TestObjectPackage.class);
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }

        try
        {
            factory.create(TestObjectConstructor.class);
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }

        Assert.assertNotNull(factory.getSetup(TestObject.class));
    }
}