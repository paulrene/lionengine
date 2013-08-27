package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity;

import com.b3dgs.lionengine.game.platform.CameraPlatform;
import com.b3dgs.lionengine.game.platform.HandlerEntityPlatform;

/**
 * Handle all entity on the map.
 */
public class HandlerEntity
        extends HandlerEntityPlatform<Entity>
{
    /** The camera reference. */
    private final CameraPlatform camera;
    /** The player reference. */
    private final Valdyn player;

    /**
     * Constructor.
     * 
     * @param camera The camera reference.
     * @param player The player reference.
     */
    public HandlerEntity(CameraPlatform camera, Valdyn player)
    {
        this.camera = camera;
        this.player = player;
    }

    @Override
    protected boolean canUpdateEntity(Entity entity)
    {
        return camera.isVisible(entity);
    }

    @Override
    protected boolean canRenderEntity(Entity entity)
    {
        return camera.isVisible(entity);
    }

    @Override
    protected void updatingEntity(Entity entity)
    {
        if (entity.collide(player))
        {
            entity.hitThat(player);
        }
    }

    @Override
    protected void renderingEntity(Entity entity)
    {
        // Nothing to do
    }

}