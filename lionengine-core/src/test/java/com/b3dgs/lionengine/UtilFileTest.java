/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.ValidationException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.mock.FactoryMediaMock;
import com.b3dgs.lionengine.mock.MediaMock;

/**
 * Test the utility file class.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
@SuppressWarnings("static-method")
public class UtilFileTest
{
    /** Mock media. */
    private static final FactoryMediaMock MOCK = new FactoryMediaMock();

    /**
     * Setup test.
     */
    @BeforeClass
    public static void setUp()
    {
        Medias.setFactoryMedia(MOCK);
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        Medias.setFactoryMedia(null);
    }

    /**
     * Test the constructor.
     * 
     * @throws Throwable If error.
     */
    @Test(expected = LionEngineException.class)
    public void testConstructor() throws Throwable
    {
        UtilTests.testPrivateConstructor(UtilFile.class);
    }

    /**
     * Test the utility file check.
     * 
     * @throws ValidationException If error.
     */
    @Test
    public void testCheck() throws ValidationException
    {
        final String file = "file1.txt";
        final String path = new MediaMock(file).getPath();
        final File descriptor = new File(path);

        Assert.assertTrue(UtilFile.exists(path));
        Assert.assertFalse(UtilFile.exists(null));
        Assert.assertTrue(UtilFile.isFile(path));
        Assert.assertEquals("txt", UtilFile.getExtension(path));
        Assert.assertEquals("txt", UtilFile.getExtension(descriptor));
        Assert.assertEquals(Constant.EMPTY_STRING, UtilFile.getExtension("noextension"));
        Assert.assertEquals(Constant.EMPTY_STRING, UtilFile.getExtension("noextension."));
        final String old = MOCK.getSeparator();
        MOCK.setSeparator(Constant.SLASH);
        Assert.assertEquals(file, UtilFile.getFilenameFromPath(path));
        MOCK.setSeparator(old);
        Assert.assertTrue(UtilFile.isFile(path));
        Assert.assertFalse(UtilFile.isFile(null));
        Assert.assertFalse(UtilFile.isDir(path));
        Assert.assertFalse(UtilFile.isDir(null));
        Assert.assertEquals("file1", UtilFile.removeExtension(file));
    }

    /**
     * Test the utility file directory manipulation.
     * 
     * @throws IOException If error.
     */
    @Test
    public void testDirectory() throws IOException
    {
        final File fileDir = Files.createTempDirectory("directory").toFile();
        fileDir.deleteOnExit();

        try
        {
            final String path = new File(new MediaMock("file").getPath()).getParentFile().getAbsolutePath();
            final String[] dirs = UtilFile.getDirsList(path);
            Assert.assertEquals(1, dirs.length);
            Assert.assertEquals("bis", dirs[0]);
            Assert.assertEquals(0, UtilFile.getDirsList(UtilFile.getPath("null")).length);

            final File file = new File(new MediaMock("file").getPath());
            final String[] files = UtilFile.getFilesList(file.getParentFile().getParentFile().getAbsolutePath());
            Assert.assertTrue("Count = " + files.length, files.length >= 25);
            Assert.assertEquals(0, UtilFile.getFilesList(UtilFile.getPath("null")).length);
            Assert.assertEquals(0, UtilFile.getFilesByExtension(UtilFile.getPath("null"), "txt").size());

            final File parent = new File(new MediaMock("file").getPath());
            final int count = UtilFile.getFilesByExtension(parent.getParentFile().getAbsolutePath(), "txt").size();
            Assert.assertEquals(2, count);
            Assert.assertFalse(UtilFile.getFilesByName(parent.getParentFile(), "file").isEmpty());
        }
        catch (final LionEngineException exception)
        {
            Assert.fail(exception.getMessage());
        }
        finally
        {
            UtilFile.deleteDirectory(fileDir);
        }

    }

    /**
     * Test the utility file path.
     */
    @Test
    public void testPath()
    {
        Assert.assertEquals(Constant.EMPTY_STRING,
                            UtilFile.getPathSeparator(Constant.DOT, Constant.EMPTY_STRING, Constant.EMPTY_STRING));
        Assert.assertEquals("null", UtilFile.getPathSeparator(Constant.DOT, (String) null, (String) null));
    }

    /**
     * Test the utility file creation.
     * 
     * @throws IOException If error.
     */
    @Test
    public void testCreation() throws IOException
    {
        final String dir = UtilFile.getPath("temp");
        final String test = UtilFile.getPath("temp", "test");
        final File tempDir = new File(dir);
        final File testFile = new File(test);
        Assert.assertTrue(tempDir.mkdirs());
        try
        {
            Assert.assertTrue(testFile.createNewFile());
        }
        catch (final IOException exception)
        {
            Assert.fail(exception.getMessage());
        }
        UtilFile.deleteDirectory(tempDir);

        Assert.assertEquals("path" + File.separator + "test", UtilFile.getPath("path", "test"));

        Verbose.info("*********************************** EXPECTED VERBOSE ***********************************");
        UtilFile.deleteFile(new File("null"));
        Verbose.info("****************************************************************************************");
    }

    /**
     * Test the utility file type.
     */
    @Test
    public void testType()
    {
        Assert.assertFalse(UtilFile.isType(new File("null"), Constant.EMPTY_STRING));
        Assert.assertTrue(UtilFile.isType(new MediaMock("file").getFile(), Constant.EMPTY_STRING));
        Assert.assertTrue(UtilFile.isType(new MediaMock("file1.txt").getFile(), "txt"));
    }
}
