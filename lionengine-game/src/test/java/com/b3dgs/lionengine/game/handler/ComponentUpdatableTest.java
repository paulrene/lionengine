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
package com.b3dgs.lionengine.game.handler;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.Updatable;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.game.object.ObjectGame;
import com.b3dgs.lionengine.game.object.Setup;

/**
 * Test the component updatable.
 */
public class ComponentUpdatableTest
{
    /**
     * Prepare test.
     */
    @BeforeClass
    public static void setUp()
    {
        Medias.setLoadFromJar(ComponentUpdatableTest.class);
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        Medias.setLoadFromJar(null);
    }

    /**
     * Test the updater.
     */
    @Test
    public void testUpdater()
    {
        final ComponentUpdatable updatable = new ComponentUpdatable();
        final Handler handler = new Handler(new Services());
        handler.addComponent(updatable);

        final Updater object = new Updater(new Setup(Medias.create("object.xml")));
        handler.add(object);
        Assert.assertFalse(object.isUpdated());
        handler.update(1.0);

        Assert.assertTrue(object.isUpdated());

        handler.removeAll();
        handler.update(1.0);
    }

    /**
     * Updatable object mock.
     */
    private static class Updater extends ObjectGame implements Updatable
    {
        /** Updated flag. */
        private boolean updated;

        /**
         * Constructor.
         * 
         * @param setup The setup reference.
         */
        public Updater(Setup setup)
        {
            super(setup);
        }

        /**
         * Check if has been updated.
         * 
         * @return <code>true</code> if updated, <code>false</code> else.
         */
        public boolean isUpdated()
        {
            return updated;
        }

        @Override
        public void update(double extrp)
        {
            updated = true;
        }
    }
}