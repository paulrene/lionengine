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
package com.b3dgs.lionengine.editor.map.sheet.project;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.editor.map.sheet.editor.SheetsEditDialog;
import com.b3dgs.lionengine.editor.project.ProjectModel;

/**
 * Edit a sheet in the selected folder.
 */
public final class SheetsEditHandler
{
    /**
     * Execute the handler.
     * 
     * @param parent The shell parent.
     */
    public static void executeHandler(Shell parent)
    {
        final Media selection = ProjectModel.INSTANCE.getSelection();
        final SheetsEditDialog dialog = new SheetsEditDialog(parent, selection);
        dialog.open();
    }

    /**
     * Create handler.
     */
    public SheetsEditHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     * 
     * @param parent The shell parent.
     */
    @Execute
    public void execute(Shell parent)
    {
        executeHandler(parent);
    }
}
