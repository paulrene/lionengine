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
package com.b3dgs.lionengine.example.warcraft.skill.human;

import com.b3dgs.lionengine.example.warcraft.skill.SetupSkill;
import com.b3dgs.lionengine.example.warcraft.skill.SkillBuild;
import com.b3dgs.lionengine.example.warcraft.skill.SkillType;

/**
 * Buildings standard human skill implementation.
 */
final class BuildHuman
        extends SkillBuild
{
    /**
     * Constructor.
     * 
     * @param setup The setup skill reference.
     */
    BuildHuman(SetupSkill setup)
    {
        super(SkillType.building_standard_human, setup);
    }
}