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
package com.b3dgs.lionengine.core;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.b3dgs.lionengine.LionEngineException;

/**
 * A media represents a path to a resources located outside. This abstraction allows to load a resource from any kind of
 * location, such as <code>HDD</code>, <code>JAR</code>... It could point to a file or a directory.
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * Engine.start(&quot;First Code&quot;, Version.create(1, 0, 0), Verbose.CRITICAL, &quot;resources&quot;);
 * Core.MEDIA.create(&quot;img&quot;, &quot;image.png&quot;);
 * System.out.println(Core.MEDIA.create()); // print: resources/img/image.png
 * </pre>
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public interface Media
{
    /**
     * Get the media path.
     * 
     * @return The media path.
     */
    String getPath();

    /**
     * Get the file descriptor.
     * 
     * @return The file descriptor.
     */
    File getFile();

    /**
     * Get the media input stream.
     * 
     * @return The media input stream.
     * @throws LionEngineException If error when getting the stream.
     */
    InputStream getInputStream() throws LionEngineException;

    /**
     * Get the media output stream.
     * 
     * @return The media output stream.
     * @throws LionEngineException If error when getting the stream.
     */
    OutputStream getOutputStream() throws LionEngineException;

    /**
     * Check if the following media point to an existing target (could be file of directory).
     * 
     * @return <code>true</code> if media is an existing file or directory, <code>false</code> else.
     */
    boolean exists();
}
