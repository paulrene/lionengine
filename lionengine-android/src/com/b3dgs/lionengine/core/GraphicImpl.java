/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.GradientColor;
import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Resolution;

/**
 * Main interface with the graphic output, representing the screen buffer.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class GraphicImpl
        implements Graphic
{
    /**
     * Get the image buffer.
     * 
     * @param imageBuffer The image buffer.
     * @return The buffer.
     */
    private static Bitmap getBuffer(ImageBuffer imageBuffer)
    {
        return ((ImageBufferImpl) imageBuffer).getBuffer();
    }

    /** Paint mode. */
    private final Paint paint;
    /** The graphic output. */
    private Canvas g;
    /** Last transform. */
    private Transform lastTransform;

    /**
     * Constructor.
     */
    GraphicImpl()
    {
        paint = new Paint();
    }

    /**
     * Constructor.
     * 
     * @param g The graphics output.
     */
    GraphicImpl(Canvas g)
    {
        paint = new Paint();
        this.g = g;
    }

    /*
     * Graphic
     */

    @Override
    public void clear(Resolution resolution)
    {
        paint.setColor(Color.BLACK);
        g.drawRect(0, 0, resolution.getWidth(), resolution.getHeight(), paint);
    }

    @Override
    public void clear(int x, int y, int width, int height)
    {
        paint.setColor(Color.BLACK);
        g.drawRect(x, y, width, height, paint);
    }

    @Override
    public void dispose()
    {
        // Nothing to do
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        // TODO: CopyArea
    }

    @Override
    public void drawImage(ImageBuffer image, int x, int y)
    {
        g.drawBitmap(GraphicImpl.getBuffer(image), x, y, paint);
    }

    @Override
    public void drawImage(ImageBuffer image, Transform transform, int x, int y)
    {
        if (lastTransform != transform)
        {
            lastTransform = transform;
            g.scale((float) transform.getScaleX(), (float) transform.getScaleY());
        }
        g.drawBitmap(GraphicImpl.getBuffer(image), x, y, paint);
    }

    @Override
    public void drawImage(ImageBuffer image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2)
    {
        final Rect src = new Rect(dx1, dy1, dx2, dy2);
        final Rect dest = new Rect(sx1, sy1, sx2, sy2);
        g.drawBitmap(GraphicImpl.getBuffer(image), src, dest, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, boolean fill)
    {
        // TODO: Fill ?
        if (fill)
        {
            g.drawRect(x, y, x + width, y + height, paint);
        }
        else
        {
            g.drawRect(x, y, x + width, y + height, paint);
        }
    }

    @Override
    public void drawGradient(int x, int y, int width, int height)
    {
        // TODO: Gradient
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        g.drawLine(x1, y1, x2, y2, paint);
    }

    @Override
    public void drawOval(int x, int y, int width, int height, boolean fill)
    {
        g.drawCircle(x, y, width * height, paint);
    }

    @Override
    public void setColor(ColorRgba color)
    {
        paint.setColor(color.getRgba());
    }

    @Override
    public void setColorGradient(GradientColor gc)
    {
        // TODO: Gradient
    }

    @Override
    public <G> void setGraphic(G graphic)
    {
        g = (Canvas) graphic;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <G> G getGraphic()
    {
        return (G) g;
    }

    @Override
    public ColorRgba getColor()
    {
        return new ColorRgba(paint.getColor());
    }
}
