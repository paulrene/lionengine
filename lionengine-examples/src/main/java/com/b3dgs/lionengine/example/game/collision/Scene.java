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
package com.b3dgs.lionengine.example.game.collision;

import com.b3dgs.lionengine.core.Context;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.Resolution;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.awt.Keyboard;
import com.b3dgs.lionengine.game.camera.Camera;
import com.b3dgs.lionengine.game.camera.CameraTracker;
import com.b3dgs.lionengine.game.collision.tile.MapTileCollision;
import com.b3dgs.lionengine.game.collision.tile.MapTileCollisionModel;
import com.b3dgs.lionengine.game.collision.tile.MapTileCollisionRenderer;
import com.b3dgs.lionengine.game.collision.tile.MapTileCollisionRendererModel;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.handler.ComponentDisplayable;
import com.b3dgs.lionengine.game.handler.ComponentRefreshable;
import com.b3dgs.lionengine.game.handler.Handler;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.MapTileRendererModel;
import com.b3dgs.lionengine.game.map.feature.group.MapTileGroup;
import com.b3dgs.lionengine.game.map.feature.group.MapTileGroupModel;
import com.b3dgs.lionengine.game.map.feature.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.map.feature.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.graphic.Graphic;

/**
 * Game loop designed to handle our little world.
 * 
 * @see com.b3dgs.lionengine.example.core.minimal
 */
class Scene extends Sequence
{
    private static final Resolution NATIVE = new Resolution(320, 240, 60);
    private static final ColorRgba BACKGROUND_COLOR = new ColorRgba(107, 136, 255);

    private final Services services = new Services();
    private final Handler handler = services.create(Handler.class);

    /**
     * Create the scene.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        handler.addComponent(new ComponentRefreshable());
        handler.addComponent(new ComponentDisplayable());

        services.add(getInputDevice(Keyboard.class)).addActionPressed(Keyboard.ESCAPE, () -> end());
        services.add(Integer.valueOf(getConfig().getSource().getRate()));
    }

    @Override
    public void load()
    {
        final MapTile map = services.create(MapTileGame.class);
        map.create(Medias.create("level.png"));

        final Camera camera = services.create(Camera.class);
        camera.setIntervals(16, 0);
        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLimits(map);
        final CameraTracker tracker = camera.addFeatureAndGet(new CameraTracker());
        handler.add(camera);

        final MapTileGroup mapGroup = map.addFeatureAndGet(new MapTileGroupModel());
        final MapTileCollision mapCollision = map.addFeatureAndGet(new MapTileCollisionModel());
        final MapTileCollisionRenderer mapCollisionRenderer = map.addFeatureAndGet(new MapTileCollisionRendererModel());

        final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel());
        mapViewer.addRenderer(new MapTileRendererModel());
        mapViewer.addRenderer(mapCollisionRenderer);
        handler.add(map);

        mapGroup.loadGroups(Medias.create("groups.xml"));
        mapCollision.loadCollisions(Medias.create("formulas.xml"), Medias.create("collisions.xml"));
        mapCollisionRenderer.createCollisionDraw();

        final Factory factory = services.create(Factory.class);
        final Mario mario = factory.create(Mario.MEDIA);
        handler.add(mario);
        tracker.track(mario);
    }

    @Override
    public void update(double extrp)
    {
        handler.update(extrp);
    }

    @Override
    public void render(Graphic g)
    {
        g.setColor(Scene.BACKGROUND_COLOR);
        g.drawRect(0, 0, getWidth(), getHeight(), true);

        handler.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
