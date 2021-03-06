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
package com.b3dgs.lionengine.core.awt.swing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Disactivable combo box implementation.
 */
public final class DisactivableComboBox extends JLabel implements ListCellRenderer
{
    /** Serial uid. */
    private static final long serialVersionUID = -5285387364457225845L;

    /**
     * Create a disactivable combo box.
     */
    public DisactivableComboBox()
    {
        setOpaque(true);
        setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    /*
     * ListCellRenderer
     */

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (!((CanEnable) value).isEnabled())
        {
            setBackground(list.getBackground());
            setForeground(UIManager.getColor("Label.disabledForeground"));
        }
        setFont(list.getFont());
        setText(value.toString());
        return this;
    }
}
