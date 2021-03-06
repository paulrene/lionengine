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
package com.b3dgs.lionengine.editor.widget;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.editor.project.Project;
import com.b3dgs.lionengine.editor.project.ProjectModel;
import com.b3dgs.lionengine.editor.utility.control.UtilButton;
import com.b3dgs.lionengine.editor.utility.dialog.UtilDialog;

/**
 * Widget that allows to browse a project media and get its path.
 */
public class BrowseWidget
{
    /** Listeners. */
    private final Collection<BrowseWidgetListener> listeners = new HashSet<>();
    /** Text location value. */
    private final Text location;
    /** Selected media. */
    private Media media;

    /**
     * Create the widget.
     * 
     * @param parent The parent composite.
     * @param label The browse label text.
     */
    public BrowseWidget(Composite parent, String label)
    {
        this(parent, label, null, null, true);
    }

    /**
     * Create the widget.
     * 
     * @param parent The parent composite.
     * @param label The browse label text.
     * @param filter The file filter description.
     * @param open <code>true</code> to open, <code>false</code> to save.
     */
    public BrowseWidget(Composite parent, String label, String filter, boolean open)
    {
        this(parent, label, filter, null, open);
    }

    /**
     * Create the widget.
     * 
     * @param parent The parent composite.
     * @param label The browse label text.
     * @param filter The file filter description.
     * @param types The file types.
     * @param open <code>true</code> to open, <code>false</code> to save.
     */
    public BrowseWidget(Composite parent, String label, String filter, String[] types, boolean open)
    {
        final Composite area = new Composite(parent, SWT.NONE);
        area.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        area.setLayout(new GridLayout(3, false));
        final Label locationLabel = new Label(area, SWT.NONE);
        locationLabel.setText(label);

        location = new Text(area, SWT.BORDER);
        location.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        location.setEditable(false);
        location.addDisposeListener(event -> listeners.clear());

        final Button browse = UtilButton.createBrowse(area);
        UtilButton.setAction(browse, () -> onBrowse(location, filter, types, open));
    }

    /**
     * Add a browse listener.
     * 
     * @param listener The listener reference.
     */
    public void addListener(BrowseWidgetListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Remove a browse listener.
     * 
     * @param listener The listener reference.
     */
    public void removeListener(BrowseWidgetListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Set the text location.
     * 
     * @param path The path value.
     */
    public void setLocation(String path)
    {
        media = Medias.create(path);
        if (location != null && !location.isDisposed())
        {
            location.setText(path);
            for (final BrowseWidgetListener listener : listeners)
            {
                listener.notifyMediaSelected(media);
            }
        }
    }

    /**
     * Get the selected media.
     * 
     * @return The selected media, <code>null</code> if none.
     */
    public Media getMedia()
    {
        return media;
    }

    /**
     * Browse the location.
     * 
     * @param location The location text reference.
     * @param filter The file filter description.
     * @param types The file types.
     * @param open <code>true</code> to open, <code>false</code> to save.
     */
    private void onBrowse(Text location, String filter, String[] types, boolean open)
    {
        final Project project = ProjectModel.INSTANCE.getProject();
        final String initialPath = project.getResourcesPath().getAbsolutePath();
        final File file;
        if (filter == null)
        {
            file = UtilDialog.selectResourceFolder(location.getShell(), initialPath);
        }
        else if (types == null)
        {
            file = UtilDialog.selectResourceXml(location.getShell(), initialPath, open, filter);
        }
        else
        {
            file = UtilDialog.selectResourceFile(location.getShell(), initialPath, open, new String[]
            {
                filter
            }, types);
        }
        if (file != null)
        {
            media = project.getResourceMedia(file);
            location.setText(media.getPath());

            for (final BrowseWidgetListener listener : listeners)
            {
                listener.notifyMediaSelected(media);
            }
        }
    }

    /**
     * Listen to {@link BrowseWidgetListener} events.
     */
    @FunctionalInterface
    public interface BrowseWidgetListener
    {
        /**
         * Called when a media has been selected.
         * 
         * @param media The selected media.
         */
        void notifyMediaSelected(Media media);
    }
}
