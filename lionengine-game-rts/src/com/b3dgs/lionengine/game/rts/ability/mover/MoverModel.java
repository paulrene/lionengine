package com.b3dgs.lionengine.game.rts.ability.mover;

import java.util.Set;

import com.b3dgs.lionengine.game.Orientation;
import com.b3dgs.lionengine.game.Tiled;
import com.b3dgs.lionengine.game.pathfinding.Pathfindable;
import com.b3dgs.lionengine.game.pathfinding.PathfindableModel;
import com.b3dgs.lionengine.game.rts.ability.AbilityModel;
import com.b3dgs.lionengine.game.rts.map.MapTileRts;

/**
 * Default and abstract mover model implementation.
 */
public class MoverModel
        extends AbilityModel<MoverListener, MoverUsedServices>
        implements MoverServices, MoverListener
{
    /** Pathfindable purview. */
    private final Pathfindable pathfindable;
    /** Already arrived flag. */
    private boolean alreadyArrived;

    /**
     * Create a mover model.
     * 
     * @param user The user reference.
     * @param map The map reference.
     */
    public MoverModel(MoverUsedServices user, MapTileRts<?, ?> map)
    {
        super(user);
        pathfindable = new PathfindableModel(map, user, user.getId());
        alreadyArrived = true;
    }

    /*
     * Mover services
     */

    @Override
    public Orientation getMovementOrientation()
    {
        final double mx = getMoveX();
        final double my = getMoveY();

        if (my < 0.0 && mx == 0.0)
        {
            return Orientation.SOUTH;
        }
        else if (my < 0.0 && mx > 0.0)
        {
            return Orientation.SOUTH_EAST;
        }
        else if (my == 0.0 && mx > 0.0)
        {
            return Orientation.EAST;
        }
        else if (my > 0.0 && mx > 0.0)
        {
            return Orientation.NORTH_EAST;
        }
        else if (my > 0.0 && mx == 0.0)
        {
            return Orientation.NORTH;
        }
        else if (my > 0.0 && mx < 0.0)
        {
            return Orientation.NORTH_WEST;
        }
        else if (my == 0.0 && mx < 0.0)
        {
            return Orientation.WEST;
        }
        else if (my < 0.0 && mx < 0.0)
        {
            return Orientation.SOUTH_WEST;
        }
        return user.getOrientation();
    }

    @Override
    public void setDestination(Tiled tiled)
    {
        setDestination(tiled.getLocationInTileX() + tiled.getWidthInTile() / 2,
                tiled.getLocationInTileY() + tiled.getHeightInTile() / 2);
    }

    @Override
    public void pointTo(int dtx, int dty)
    {
        if (getLocationInTileX() == dtx && getLocationInTileY() > dty)
        {
            user.setOrientation(Orientation.SOUTH);
        }
        else if (getLocationInTileX() > dtx && getLocationInTileY() > dty)
        {
            user.setOrientation(Orientation.SOUTH_WEST);
        }
        else if (getLocationInTileX() > dtx && getLocationInTileY() == dty)
        {
            user.setOrientation(Orientation.WEST);
        }
        else if (getLocationInTileX() > dtx && getLocationInTileY() < dty)
        {
            user.setOrientation(Orientation.NORTH_WEST);
        }
        else if (getLocationInTileX() == dtx && getLocationInTileY() < dty)
        {
            user.setOrientation(Orientation.NORTH);
        }
        else if (getLocationInTileX() < dtx && getLocationInTileY() < dty)
        {
            user.setOrientation(Orientation.NORTH_EAST);
        }
        else if (getLocationInTileX() < dtx && getLocationInTileY() == dty)
        {
            user.setOrientation(Orientation.EAST);
        }
        else if (getLocationInTileX() < dtx && getLocationInTileY() > dty)
        {
            user.setOrientation(Orientation.SOUTH_EAST);
        }
    }

    @Override
    public void pointTo(Tiled tiled)
    {
        pointTo(tiled.getLocationInTileX(), tiled.getLocationInTileY());
    }

    @Override
    public void setDestination(double extrp, double dx, double dy)
    {
        pathfindable.setDestination(extrp, dx, dy);
    }

    @Override
    public boolean setDestination(int dtx, int dty)
    {
        final boolean destinationFound = pathfindable.setDestination(dtx, dty);

        if (destinationFound)
        {
            alreadyArrived = false;
            pointTo(dtx, dty);
            notifyStartMove();
        }
        return destinationFound;
    }

    @Override
    public boolean isPathAvailable(int dtx, int dty)
    {
        return pathfindable.isPathAvailable(dtx, dty);
    }

    @Override
    public void setLocation(int dtx, int dty)
    {
        pathfindable.setLocation(dtx, dty);
    }

    @Override
    public void setIgnoreId(Integer id, boolean state)
    {
        pathfindable.setIgnoreId(id, state);
    }

    @Override
    public boolean isIgnoredId(Integer id)
    {
        return pathfindable.isIgnoredId(id);
    }

    @Override
    public void clearIgnoredId()
    {
        pathfindable.clearIgnoredId();
    }

    @Override
    public void setSharedPathIds(Set<Integer> ids)
    {
        pathfindable.setSharedPathIds(ids);
    }

    @Override
    public void clearSharedPathIds()
    {
        pathfindable.clearSharedPathIds();
    }

    @Override
    public void updateMoves(double extrp)
    {
        pathfindable.updateMoves(extrp);

        if (pathfindable.isMoving())
        {
            user.setOrientation(getMovementOrientation());
            alreadyArrived = false;
            notifyMoving();
        }

        if (!alreadyArrived && pathfindable.isDestinationReached())
        {
            alreadyArrived = true;
            notifyArrived();
        }
    }

    @Override
    public void stopMoves()
    {
        pathfindable.stopMoves();
    }

    @Override
    public void setSpeed(double speedX, double speedY)
    {
        pathfindable.setSpeed(speedX, speedY);
    }

    @Override
    public boolean isMoving()
    {
        return pathfindable.isMoving();
    }

    @Override
    public boolean isDestinationReached()
    {
        return pathfindable.isDestinationReached();
    }

    @Override
    public double getSpeedX()
    {
        return pathfindable.getSpeedX();
    }

    @Override
    public double getSpeedY()
    {
        return pathfindable.getSpeedY();
    }

    @Override
    public double getMoveX()
    {
        return pathfindable.getMoveX();
    }

    @Override
    public double getMoveY()
    {
        return pathfindable.getMoveY();
    }

    @Override
    public int getLocationInTileX()
    {
        return pathfindable.getLocationInTileX();
    }

    @Override
    public int getLocationInTileY()
    {
        return pathfindable.getLocationInTileY();
    }

    /*
     * Mover listener
     */

    @Override
    public void notifyStartMove()
    {
        for (final MoverListener listener : listeners)
        {
            listener.notifyStartMove();
        }
    }

    @Override
    public void notifyMoving()
    {
        for (final MoverListener listener : listeners)
        {
            listener.notifyMoving();
        }
    }

    @Override
    public void notifyArrived()
    {
        for (final MoverListener listener : listeners)
        {
            listener.notifyArrived();
        }
    }
}
