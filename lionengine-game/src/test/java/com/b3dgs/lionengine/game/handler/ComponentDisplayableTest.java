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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.game.feature.Featurable;
import com.b3dgs.lionengine.game.feature.FeaturableModel;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.feature.displayable.DisplayableModel;
import com.b3dgs.lionengine.game.feature.identifiable.Identifiable;
import com.b3dgs.lionengine.game.feature.identifiable.IdentifiableModel;
import com.b3dgs.lionengine.game.feature.layerable.Layerable;
import com.b3dgs.lionengine.game.feature.layerable.LayerableModel;
import com.b3dgs.lionengine.game.handler.ComponentDisplayable;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Renderable;

/**
 * Test the component renderer layer class.
 */
public class ComponentDisplayableTest
{
    /**
     * Create a test object.
     * 
     * @param services The services reference.
     * @param last The last rendered element.
     * @return The created object.
     */
    private static Layerable createObject(Services services, final AtomicInteger last)
    {
        final FeaturableModel object = new FeaturableModel();
        object.addFeature(new IdentifiableModel());

        final LayerableModel layerable = object.addFeatureAndGet(new LayerableModel());
        layerable.prepare(object, services);

        object.addFeature(new DisplayableModel(new Renderable()
        {
            @Override
            public void render(Graphic g)
            {
                if (object.getFeature(Identifiable.class).getId() != null)
                {
                    last.set(object.getFeature(Identifiable.class).getId().intValue());
                }
            }
        }));

        return layerable;
    }

    /**
     * Test the component.
     */
    @Test
    public void testComponentLayer()
    {
        final ComponentDisplayable component = new ComponentDisplayable();
        final Services services = new Services();
        services.add(component);

        final AtomicInteger last = new AtomicInteger();

        final Layerable object1 = createObject(services, last);
        final Layerable object2 = createObject(services, last);
        final Layerable object3 = createObject(services, last);
        final Layerable object4 = createObject(services, last);

        object1.setLayer(4);
        object2.setLayer(6);
        object3.setLayer(5);
        object4.setLayer(4);
        last.set(-1);

        component.render(null, null);

        Assert.assertEquals(4, object1.getLayer().intValue());
        Assert.assertEquals(6, object2.getLayer().intValue());
        Assert.assertEquals(5, object3.getLayer().intValue());
        Assert.assertEquals(4, object4.getLayer().intValue());

        Assert.assertEquals(object2.getFeature(Identifiable.class).getId().intValue(), last.get());

        object2.getFeature(Identifiable.class).notifyDestroyed();
        object4.getFeature(Identifiable.class).notifyDestroyed();

        last.set(-1);
        component.render(null, null);

        Assert.assertEquals(object3.getFeature(Identifiable.class).getId().intValue(), last.get());
    }

    /**
     * Test the component with default value.
     */
    @Test
    public void testComponentLayerDefault()
    {
        final ComponentDisplayable component = new ComponentDisplayable();
        final Services services = new Services();
        services.add(component);

        final AtomicBoolean auto = new AtomicBoolean();

        final Featurable featurable = new FeaturableModel();
        featurable.addFeature(new DisplayableModel(new Renderable()
        {
            @Override
            public void render(Graphic g)
            {
                auto.set(true);
            }
        }));
        component.notifyHandlableAdded(featurable);
        component.render(null, null);

        Assert.assertTrue(auto.get());
    }
}
