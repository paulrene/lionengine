package com.b3dgs.lionengine.game.purview;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.game.CameraGame;

/**
 * Purview representing something which can enter in collision with another. Based on a ray casting collision from a
 * bounding box area.
 */
public interface Collidable
{
    /**
     * Update collision with specified area.
     * 
     * @param x The horizontal offset from entity.
     * @param y The vertical offset from entity.
     * @param width The entity collision width.
     * @param height The entity collision height.
     */
    void updateCollision(int x, int y, int width, int height);

    /**
     * Check if the entity entered in collision with another one.
     * 
     * @param entity The opponent.
     * @return <code>true</code> if collide, <code>false</code> else.
     */
    boolean collide(Collidable entity);

    /**
     * Check if the entity entered in collision with a specified area.
     * 
     * @param area The area to check.
     * @return <code>true</code> if collide, <code>false</code> else.
     */
    boolean collide(Rectangle2D area);

    /**
     * Render collision bounding box.
     * 
     * @param g The graphic output.
     * @param camera The camera reference.
     */
    void renderCollision(Graphic g, CameraGame camera);

    /**
     * Get collision representation.
     * 
     * @return The collision representation.
     */
    Rectangle2D getCollision();

    /**
     * Get collision ray cast.
     * 
     * @return The collision ray cast.
     */
    Line2D getCollisionRay();
}
