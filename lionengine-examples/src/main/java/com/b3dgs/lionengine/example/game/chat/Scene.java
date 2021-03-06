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
package com.b3dgs.lionengine.example.game.chat;

import com.b3dgs.lionengine.core.Context;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Resolution;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.graphic.Graphic;

/**
 * Game loop designed to handle our little world.
 */
class Scene extends Sequence
{
    /** Native resolution. */
    private static final Resolution NATIVE = new Resolution(320, 240, 60);

    /** World reference. */
    private final World<?> world;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);
        setExtrapolated(true);

        final boolean server = true;

        if (server)
        {
            final WorldServer worldServer = new WorldServer(context);
            worldServer.startServer("Test", 7777, "Welcome !");
            world = worldServer;
        }
        else
        {
            final WorldClient worldClient = new WorldClient(context);
            addKeyListener(worldClient.getChat());
            worldClient.setName("Unnamed");
            worldClient.connect("127.0.0.1", 7777);
            world = worldClient;
        }
    }

    /*
     * Sequence
     */

    @Override
    public void load()
    {
        // Nothing to do
    }

    @Override
    public void update(double extrp)
    {
        world.receiveMessages();
        world.update(extrp);
        world.sendMessages();
    }

    @Override
    public void render(Graphic g)
    {
        world.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        world.disconnect();
        Engine.terminate();
    }
}
