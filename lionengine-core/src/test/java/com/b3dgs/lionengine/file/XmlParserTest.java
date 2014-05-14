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
package com.b3dgs.lionengine.file;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.UtilFile;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.FactoryMediaProvider;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.mock.FactoryMediaMock;

/**
 * Test the XML parser.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class XmlParserTest
{
    /** Resources path. */
    private static String PATH;

    /**
     * Prepare the test.
     */
    @BeforeClass
    public static void prepareTest()
    {
        FactoryMediaProvider.setFactoryMedia(new FactoryMediaMock());
        XmlParserTest.PATH = UtilFile.getPath("src", "test", "resources", "file");
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        FactoryMediaProvider.setFactoryMedia(null);
    }

    /** Default test file xml. */
    private Media fileXml;

    /**
     * Test the write and read in XML with parser.
     * 
     * @throws XmlNodeNotFoundException If error.
     */
    @Test
    public void testXmlWriteRead() throws XmlNodeNotFoundException
    {
        fileXml = Core.MEDIA.create(XmlParserTest.PATH, "test.xml");
        try
        {
            testWriteXml();
            testReadXml();
            testWrongReadXml();
            testWrongWriteXml();
        }
        finally
        {
            UtilFile.deleteFile(new java.io.File(fileXml.getPath()));
        }
    }

    /**
     * Test write in xml file.
     */
    private void testWriteXml()
    {
        final XmlNode root = File.createXmlNode("root");
        final XmlNode child = File.createXmlNode("child");
        root.add(child);

        child.writeBoolean("boolean", XmlNodeMock.BOOL_VALUE);
        child.writeByte("byte", XmlNodeMock.BYTE_VALUE);
        child.writeShort("short", XmlNodeMock.SHORT_VALUE);
        child.writeInteger("integer", XmlNodeMock.INT_VALUE);
        child.writeFloat("float", XmlNodeMock.FLOAT_VALUE);
        child.writeLong("long", XmlNodeMock.LONG_VALUE);
        child.writeDouble("double", XmlNodeMock.DOUBLE_VALUE);
        child.writeString("string", XmlNodeMock.STRING_VALUE);
        child.writeString("null", null);

        final XmlParser parserSave = File.createXmlParser();
        parserSave.save(root, fileXml);

        root.add(new XmlNodeMock());
        parserSave.save(root, fileXml);
    }

    /**
     * Test wrong write in xml file.
     */
    private void testWrongWriteXml()
    {
        final XmlParser parserSave = File.createXmlParser();
        try
        {
            parserSave.save(File.createXmlNode("child"), Core.MEDIA.create(""));
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }

        parserSave.save(new XmlNodeMock(), fileXml);
        final java.io.File file = new java.io.File(UtilFile.getPath(XmlParserTest.PATH, "foo"));
        if (file.mkdir())
        {
            try
            {
                parserSave.save(File.createXmlNode("child"), Core.MEDIA.create(file.getPath()));
                Assert.fail();
            }
            catch (final LionEngineException exception)
            {
                // Success
            }
            finally
            {
                UtilFile.deleteDirectory(file);
            }
        }
    }

    /**
     * Test read in xml file.
     * 
     * @throws XmlNodeNotFoundException If node note found, error case.
     */
    private void testReadXml() throws XmlNodeNotFoundException
    {
        final XmlParser parserLoad = File.createXmlParser();
        final XmlNode root = parserLoad.load(fileXml);
        final XmlNode child = root.getChild("child");

        Assert.assertEquals(Boolean.valueOf(XmlNodeMock.BOOL_VALUE), Boolean.valueOf(child.readBoolean("boolean")));
        Assert.assertEquals(XmlNodeMock.BYTE_VALUE, child.readByte("byte"));
        Assert.assertEquals(XmlNodeMock.SHORT_VALUE, child.readShort("short"));
        Assert.assertEquals(XmlNodeMock.INT_VALUE, child.readInteger("integer"));
        Assert.assertEquals(XmlNodeMock.FLOAT_VALUE, child.readFloat("float"), XmlNodeMock.FLOAT_PRECISION);
        Assert.assertEquals(XmlNodeMock.LONG_VALUE, child.readLong("long"));
        Assert.assertEquals(XmlNodeMock.DOUBLE_VALUE, child.readDouble("double"), XmlNodeMock.DOUBLE_PRECISION);
        Assert.assertEquals(XmlNodeMock.STRING_VALUE, child.readString("string"));
        Assert.assertEquals(null, child.readString("null"));
    }

    /**
     * Test wrong read data in xml file.
     */
    private void testWrongReadXml()
    {
        final XmlParser parserLoad = File.createXmlParser();
        final XmlNode root = parserLoad.load(fileXml);
        try
        {
            root.getChild("none");
            Assert.fail();
        }
        catch (final XmlNodeNotFoundException exception)
        {
            // Success
        }
        try
        {
            root.readInteger("wrong");
            Assert.fail();
        }
        catch (final NumberFormatException exception)
        {
            // Success
        }

        try
        {
            final XmlParser parser = File.createXmlParser();
            parser.load(Core.MEDIA.create(XmlParserTest.PATH, "malformed.xml"));
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }
    }
}