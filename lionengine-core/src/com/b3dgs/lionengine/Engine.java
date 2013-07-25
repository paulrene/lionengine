package com.b3dgs.lionengine;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.b3dgs.lionengine.utility.UtilityFile;

/**
 * <b>LionEngine</b>.
 * <p>
 * By <a href="http://www.b3dgs.com"><b>Byron 3D Games Studio</b></a>
 * </p>
 * <p>
 * Here a standard engine initialization:
 * </p>
 * 
 * <pre>
 * public static void main(String args[])
 * {
 *     // Start engine (resources directory = &quot;resources&quot;)
 *     // The Engine is initialized with our parameters:
 *     // - The name of our program: &quot;First Code&quot;
 *     // - Our program version: &quot;1.0.0&quot;
 *     // - The main resources directory, relative to the execution directory: ./resources/
 *     // This mean that any resources loaded with Media.get(...) will have this directory as prefix
 *     // - The verbose level, in order to define the level of output filtering is screen is scaled
 *     // - The swing theme (general java appearance)
 *     Engine.start(&quot;First Code&quot;, Version.create(1, 0, 0), &quot;resources&quot;);
 * 
 *     // Configuration reference (native size = 320*240*16 at 60fps)
 *     // This mean that our native resolution is in 320*240
 *     // These data are used in case of rendering scaling, if the desired output is different
 *     // The last value is used to perform the frame rate calculation, corresponding to the native frame rate
 *     Display internal = new Display(320, 240, 16, 60);
 * 
 *     // Display configuration (desired = 640*480*16 at 60fps)
 *     // This is corresponding to the output configuration
 *     // As our native is in 320*240, the output will be scaled by 2
 *     // If the current frame rate is lower, the extrapolation value will allow to compensate any data calculation
 *     Display external = new Display(640, 480, 16, 60);
 * 
 *     // Final configuration (rendering will be scaled by 2 considering native and desired config)
 *     // This is the final configuration container, including window mode
 *     Config config = new Config(internal, external, true);
 * 
 *     // Program starter, the main thread, setup with our configuration
 *     // It just needs one sequence reference to start
 *     Loader loader = new Loader(config);
 *     loader.start(new Scene(loader));
 * }
 * </pre>
 * 
 * @author Pierre-Alexandre
 * @version 6.0.0
 * @since 13 June 2010
 */
public final class Engine
{
    /** Engine name. */
    public static final String NAME = "LionEngine";
    /** Engine version. */
    public static final String VERSION = "6.0.0";
    /** Engine last release date. */
    public static final String DATE = "13 June 2012";
    /** Engine author. */
    public static final String AUTHOR = "Pierre-Alexandre";
    /** Engine website. */
    public static final String WEBSITE = "http://www.b3dgs.com";
    /** Error message program name. */
    private static final String MESSAGE_ERROR_NAME = "Program name must not be null !";
    /** Error message program version. */
    private static final String MESSAGE_ERROR_VERSION = "The version must not be null !";
    /** Error message resource directory. */
    private static final String MESSAGE_ERROR_RESOURCE = "The resources directory must not be null !";
    /** Error message verbose. */
    private static final String MESSAGE_ERROR_VERBOSE = "The verbose level must not be null !";
    /** Error message theme. */
    private static final String MESSAGE_ERROR_THEME = "The theme must not be null !";
    /** Error message temp directory. */
    private static final String MESSAGE_ERROR_TEMP_DIRECTORY = "Temporary directory was not created !";
    /** Started engine flag. */
    private static boolean started = false;
    /** User program name. */
    private static String programName;
    /** User program version. */
    private static String programVersion;

    /**
     * Private constructor.
     */
    private Engine()
    {
        throw new RuntimeException();
    }

    /**
     * Start engine; has to be called before anything and only one time, in the main.
     * 
     * @param name The program name (must not be <code>null</code>).
     * @param version The program version (must not be <code>null</code>).
     * @param ressourcesDir The main resources directory (must not be <code>null</code>).
     */
    public static void start(String name, Version version, String ressourcesDir)
    {
        Engine.start(name, version, ressourcesDir, Verbose.INFORMATION, Theme.SYSTEM);
    }

    /**
     * Start engine; has to be called before anything and only one time, in the main.
     * 
     * @param name The program name (must not be <code>null</code>).
     * @param version The program version (must not be <code>null</code>).
     * @param resourcesDir The main resources directory (must not be <code>null</code>).
     * @param level The verbose level (must not be <code>null</code>).
     * @param theme The theme to use for any window (must not be <code>null</code>).
     */
    public static void start(String name, Version version, String resourcesDir, Verbose level, Theme theme)
    {
        Check.notNull(name, Engine.MESSAGE_ERROR_NAME);
        Check.notNull(version, Engine.MESSAGE_ERROR_VERSION);
        Check.notNull(resourcesDir, Engine.MESSAGE_ERROR_RESOURCE);
        Check.notNull(level, Engine.MESSAGE_ERROR_VERBOSE);
        Check.notNull(theme, Engine.MESSAGE_ERROR_THEME);

        if (!Engine.started)
        {
            Engine.init(name, version, resourcesDir, level, theme);

            // LionEngine started
            Verbose.info("Starting \"LionEngine ", Engine.VERSION, "\" for \"", Engine.programName, " ",
                    Engine.programVersion, "\"");
            Verbose.info("Execution directory = ", Media.WORKING_DIR);
            Verbose.info("Resources directory = ", Media.getRessourcesDir());
            Verbose.info("Temporary directory = ", Media.getTempDir());

            // Check version (clear temporary directory if version is different)
            final String versionFilename = Media.getPath(Media.getTempDir(), "version");
            Engine.checkVersion(versionFilename);
            Engine.storeVersion(versionFilename);

            Engine.started = true;
        }
    }

    /**
     * Terminate the engine. It is necessary to call this function only if the engine need to be started again during
     * the same jvm execution.
     */
    public static void terminate()
    {
        Media.setResourcesDirectory("");
        Media.setTempDirectory("");
        Media.setLoadFromJar(null, false);
        Engine.started = false;
    }

    /**
     * Get the program name (Engine must have been started).
     * 
     * @return The program name.
     */
    public static String getProgramName()
    {
        return Engine.programName;
    }

    /**
     * Get the program version (Engine must have been started).
     * 
     * @return The program version.
     */
    public static String getProgramVersion()
    {
        return Engine.programVersion;
    }

    /**
     * Initialize engine
     * 
     * @param name The program name.
     * @param version The program version.
     * @param resourcesDir The main resources directory.
     * @param level The verbose level.
     * @param theme The theme to use for any window.
     */
    private static void init(String name, Version version, String resourcesDir, Verbose level, Theme theme)
    {
        Verbose.set(level);
        Verbose.prepareLogger();
        Theme.set(theme);
        Engine.programName = name;
        Engine.programVersion = version.toString();
        Media.setTempDirectory(name);
        Media.setResourcesDirectory(resourcesDir);
    }

    /**
     * Check version by clearing temporary directory if version is different.
     * 
     * @param versionFilename The file describing the program version.
     */
    private static void checkVersion(String versionFilename)
    {
        final File tempDir = new File(Media.getTempDir());
        if (tempDir.exists())
        {
            boolean delete = true;
            try (DataInputStream reader = new DataInputStream(new FileInputStream(versionFilename));)
            {
                final String version = reader.readUTF();
                if (Engine.programVersion.equals(version))
                {
                    delete = false;
                }
            }
            catch (final IOException exception)
            {
                delete = true;
            }
            if (delete)
            {
                UtilityFile.deleteDirectory(tempDir);
            }
        }

        // Create temporary directory
        if (!tempDir.exists() && !tempDir.mkdir())
        {
            Verbose.warning(Engine.class, "checkVersion", Engine.MESSAGE_ERROR_TEMP_DIRECTORY);
        }
    }

    /**
     * Store version if needed for next time.
     * 
     * @param versionFilename The file describing the program version.
     */
    private static void storeVersion(String versionFilename)
    {
        if (!UtilityFile.exists(versionFilename))
        {
            try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(versionFilename));)
            {
                writer.writeUTF(Engine.programVersion);
                writer.flush();
            }
            catch (final IOException exception)
            {
                throw new LionEngineException(exception);
            }
        }
    }
}
