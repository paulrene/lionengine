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
package com.b3dgs.lionengine.game.raster.it;

import com.b3dgs.lionengine.Timing;
import com.b3dgs.lionengine.core.Context;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.Resolution;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.game.camera.Camera;
import com.b3dgs.lionengine.game.feature.Services;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.MapTileGame;
import com.b3dgs.lionengine.game.map.feature.viewer.MapTileViewer;
import com.b3dgs.lionengine.game.map.feature.viewer.MapTileViewerModel;
import com.b3dgs.lionengine.game.raster.MapTileRastered;
import com.b3dgs.lionengine.game.raster.MapTileRasteredModel;
import com.b3dgs.lionengine.graphic.Graphic;

/**
 * Integration test for map tile rastered model.
 */
public class SceneMapTileRastered extends Sequence
{
    private final Services services = new Services();
    private final Camera camera = services.create(Camera.class);
    private final MapTile map = services.create(MapTileGame.class);
    private final MapTileViewer mapViewer = map.addFeatureAndGet(new MapTileViewerModel());
    private final MapTileRastered raster = map.addFeatureAndGet(new MapTileRasteredModel());
    private final Timing timingRaster = new Timing();
    private final Timing timing = new Timing();

    private boolean useRaster;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     */
    public SceneMapTileRastered(Context context)
    {
        super(context, new Resolution(320, 240, 60));
    }

    @Override
    public void load()
    {
        map.prepareFeatures(services);
        map.create(Medias.create("level.png"), 16, 16, 16);
        raster.loadSheets(Medias.create("raster.xml"), false);

        camera.setView(0, 0, getWidth(), getHeight(), getHeight());
        camera.setLimits(map);

        timingRaster.start();
        timing.start();
    }

    @Override
    public void update(double extrp)
    {
        if (timing.isStarted() && timingRaster.elapsed(150))
        {
            useRaster = !useRaster;
            if (useRaster)
            {
                mapViewer.addRenderer(raster);
            }
            else
            {
                mapViewer.removeRenderer(raster);
            }
            timingRaster.restart();
        }
        if (timing.elapsed(1000L))
        {
            end();
        }
    }

    @Override
    public void render(Graphic g)
    {
        mapViewer.render(g);
    }

    @Override
    public void onTerminated(boolean hasNextSequence)
    {
        Engine.terminate();
    }
}
