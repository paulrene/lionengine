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
package com.b3dgs.lionengine.example.warcraft.launcher;

import com.b3dgs.lionengine.example.warcraft.entity.Entity;
import com.b3dgs.lionengine.example.warcraft.projectile.Projectile;
import com.b3dgs.lionengine.example.warcraft.projectile.ProjectileType;
import com.b3dgs.lionengine.example.warcraft.weapon.Weapon;
import com.b3dgs.lionengine.game.projectile.LauncherProjectileGame;

/**
 * Launcher base implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class LauncherProjectile
        extends LauncherProjectileGame<ProjectileType, Entity, Weapon, Projectile>
{
    /** Type projectile. */
    private final ProjectileType type;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    public LauncherProjectile(SetupLauncher setup)
    {
        super(setup, setup.factoryProjectile, setup.handlerProjectile);
        type = setup.type;
    }

    /*
     * LauncherProjectileGame
     */

    @Override
    protected void launchProjectile(Weapon owner)
    {
        // Nothing to do
    }

    @Override
    protected void launchProjectile(Weapon owner, Entity target)
    {
        final Projectile projectile = addProjectile(type, owner.getAttackDamages(), target, 2.5, 0, 0);
        projectile.setFrame(owner.getFrame());
    }
}