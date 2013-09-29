package com.b3dgs.lionengine.example.e_shmup.a_scrollingmap;

import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.Engine;
import com.b3dgs.lionengine.Loader;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.Version;

// Tutorial: Shmup Scrolling Map
// This tutorial shows how to make a simple scrolling using a MapTile for a Shoot'em Up game.

/**
 * Program starts here.
 */
public final class AppShmupScrollingMap
{
    /** Application name. */
    public static final String NAME = "Shmup Scrolling Map";
    /** Application version. */
    public static final Version VERSION = Version.create(1, 0, 0);
    /** Resources directory. */
    private static final String RESOURCES = Media.getPath("resources", "shmup");

    /**
     * Main function.
     * 
     * @param argv The arguments.
     */
    public static void main(String[] argv)
    {
        // Start engine
        Engine.start(AppShmupScrollingMap.NAME, AppShmupScrollingMap.VERSION, AppShmupScrollingMap.RESOURCES);

        // Resolution
        final Resolution output = new Resolution(640, 400, 60);

        // Configuration
        final Config config = new Config(output, 16, true);

        // Loader
        final Loader loader = new Loader(config);
        loader.start(new Scene(loader));
    }

    /**
     * Private constructor.
     */
    private AppShmupScrollingMap()
    {
        throw new RuntimeException();
    }
}
