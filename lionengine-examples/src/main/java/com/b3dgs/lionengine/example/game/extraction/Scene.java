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
package com.b3dgs.lionengine.example.game.extraction;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.core.Context;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Graphics;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.Resolution;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.awt.Keyboard;
import com.b3dgs.lionengine.core.awt.Mouse;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.Image;
import com.b3dgs.lionengine.game.Cursor;
import com.b3dgs.lionengine.game.camera.Camera;
import com.b3dgs.lionengine.game.feature.Factory;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.handler.ComponentDisplayable;
import com.b3dgs.lionengine.game.handler.ComponentRefreshable;
import com.b3dgs.lionengine.game.handler.Handler;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.feature.group.MapTileGroup;
import com.b3dgs.lionengine.game.map.feature.group.MapTileGroupModel;
import com.b3dgs.lionengine.game.map.feature.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.game.pathfinding.MapTilePath;
import com.b3dgs.lionengine.game.pathfinding.MapTilePathModel;
import com.b3dgs.lionengine.game.pathfinding.Pathfindable;
import com.b3dgs.lionengine.graphic.Graphic;
import com.b3dgs.lionengine.graphic.Text;
import com.b3dgs.lionengine.graphic.TextStyle;

/**
 * Game loop designed to handle our little world.
 * 
 * @see com.b3dgs.lionengine.example.core.minimal
 */
class Scene extends Sequence
{
    private static final Resolution NATIVE = new Resolution(320, 200, 60);

    private final Services services = new Services();
    private final Text text = services.add(Graphics.createText(Text.SANS_SERIF, 9, TextStyle.NORMAL));
    private final Handler handler = services.create(Handler.class);
    private final Cursor cursor = services.create(Cursor.class);
    private final Mouse mouse = getInputDevice(Mouse.class);
    private final Image hud;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public Scene(Context context)
    {
        super(context, NATIVE);

        hud = Drawable.loadImage(Medias.create("hud.png"));

        handler.addComponent(new ComponentRefreshable());
        handler.addComponent(new ComponentDisplayable());

        setSystemCursorVisible(false);
        getInputDevice(Keyboard.class).addActionPressed(Keyboard.ESCAPE, () -> end());
    }

    @Override
    public void load()
    {
        final MapTile map = services.create(MapTileGame.class);
        map.addFeature(new MapTileViewerModel());
        map.create(Medias.create("map", "level.png"));

        final MapTileGroup mapGroup = map.addFeatureAndGet(new MapTileGroupModel());
        final MapTilePath mapPath = map.addFeatureAndGet(new MapTilePathModel());

        final Camera camera = services.create(Camera.class);
        camera.setView(72, 12, 240, 176, getHeight());
        camera.setLimits(map);
        camera.setLocation(192, 96);

        handler.add(map);
        mapGroup.loadGroups(Medias.create("map", "groups.xml"));
        mapPath.loadPathfinding(Medias.create("map", "pathfinding.xml"));

        hud.load();
        hud.prepare();
        text.setLocation(74, 192);

        cursor.addImage(0, Medias.create("cursor.png"));
        cursor.addImage(1, Medias.create("cursor_order.png"));
        cursor.load();
        cursor.setGrid(map.getTileWidth(), map.getTileHeight());
        cursor.setInputDevice(mouse);
        cursor.setViewer(camera);

        services.add(Integer.valueOf(getConfig().getSource().getRate()));

        final Factory factory = services.create(Factory.class);
        handler.add(factory.create(Button.EXTRACT));
        handler.add(factory.create(GoldMine.GOLD_MINE));

        final Peon peon = factory.create(Peon.MEDIA);
        handler.add(peon);
        peon.getFeature(Pathfindable.class).setLocation(13, 10);
    }

    @Override
    public void update(double extrp)
    {
        text.setText(Constant.EMPTY_STRING);
        mouse.update(extrp);
        cursor.update(extrp);
        handler.update(extrp);
    }

    @Override
    public void render(Graphic g)
    {
        hud.render(g);
        handler.render(g);
        text.render(g);
        cursor.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
