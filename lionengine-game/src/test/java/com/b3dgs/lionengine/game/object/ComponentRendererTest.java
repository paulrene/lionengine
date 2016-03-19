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
package com.b3dgs.lionengine.game.object;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Renderable;

/**
 * Test the component renderer.
 */
public class ComponentRendererTest
{
    /**
     * Prepare test.
     */
    @BeforeClass
    public static void setUp()
    {
        Medias.setLoadFromJar(HandlerTest.class);
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
        final ComponentRenderer renderer = new ComponentRenderer();
        final Handler handler = new Handler();
        handler.addRenderable(renderer);

        final Renderer object = new Renderer(new Setup(Medias.create("object.xml")), new Services());
        handler.add(object);
        Assert.assertFalse(object.isRendered());
        handler.update(1.0);
        Assert.assertFalse(object.isRendered());
        handler.render(null);

        Assert.assertTrue(object.isRendered());

        handler.removeAll();
        handler.update(1.0);
    }

    /**
     * Renderable object mock.
     */
    private static class Renderer extends ObjectGame implements Renderable
    {
        /** Rendered flag. */
        private boolean rendered;

        /**
         * Constructor.
         * 
         * @param setup The setup reference.
         * @param services The services reference.
         */
        public Renderer(Setup setup, Services services)
        {
            super(setup, services);
        }

        /**
         * Check if has been rendered.
         * 
         * @return <code>true</code> if rendered, <code>false</code> else.
         */
        public boolean isRendered()
        {
            return rendered;
        }

        @Override
        public void render(Graphic g)
        {
            rendered = true;
        }
    }
}
