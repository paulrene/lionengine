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
package com.b3dgs.lionengine.anim;

import com.b3dgs.lionengine.LionEngineException;

/**
 * Anim factory. Can create the following elements:
 * <ul>
 * <li>{@link Animation}</li>
 * <li>{@link Animator}</li>
 * </ul>
 * <p>
 * This class is Thread-Safe.
 * </p>
 */
public final class Anim
{
    /**
     * Create an animation, which can be played by an {@link Animator}.
     * 
     * @param name The animation name.
     * @param firstFrame The first frame (included) index to play (superior or equal to {@link Animation#MINIMUM_FRAME}
     *            ).
     * @param lastFrame The last frame (included) index to play (superior or equal to firstFrame).
     * @param speed The animation playing speed (superior or equal to 0.0).
     * @param reverse <code>true</code> to reverse animation play (play it from first to last, and last to first).
     * @param repeat The repeat state (<code>true</code> will play in loop, <code>false</code> will play once only).
     * @return The created animation.
     * @throws LionEngineException If invalid animation.
     */
    public static Animation createAnimation(String name,
                                            int firstFrame,
                                            int lastFrame,
                                            double speed,
                                            boolean reverse,
                                            boolean repeat)
    {
        return new AnimationImpl(name, firstFrame, lastFrame, speed, reverse, repeat);
    }

    /**
     * Create an animator, which will be able to play {@link Animation}.
     * 
     * @return The created animator.
     */
    public static Animator createAnimator()
    {
        return new AnimatorImpl();
    }

    /**
     * Private constructor.
     */
    private Anim()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}
