/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.game.strategy.ability.entity;

import java.util.Iterator;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.example.game.strategy.ability.ResourceType;
import com.b3dgs.lionengine.game.Bar;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.CoordTile;
import com.b3dgs.lionengine.game.Tiled;
import com.b3dgs.lionengine.game.purview.Configurable;
import com.b3dgs.lionengine.game.strategy.ability.extractor.Extractible;
import com.b3dgs.lionengine.game.strategy.ability.extractor.ExtractorModel;
import com.b3dgs.lionengine.game.strategy.ability.extractor.ExtractorServices;
import com.b3dgs.lionengine.game.strategy.ability.extractor.ExtractorUsedServices;
import com.b3dgs.lionengine.game.strategy.ability.producer.ProducerModel;
import com.b3dgs.lionengine.game.strategy.ability.producer.ProducerServices;
import com.b3dgs.lionengine.game.strategy.ability.producer.ProducerUsedServices;
import com.b3dgs.lionengine.game.strategy.entity.EntityNotFoundException;

/**
 * Worker unit implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class UnitWorker
        extends Unit
        implements ProducerUsedServices<Entity, ProductionCost, ProducibleEntity>,
        ProducerServices<Entity, ProductionCost, ProducibleEntity>, ExtractorUsedServices, ExtractorServices
{
    /** Producer model. */
    private final ProducerModel<Entity, ProductionCost, ProducibleEntity> producer;
    /** Factory reference. */
    private final FactoryEntity factory;
    /** Handler reference. */
    private final HandlerEntity handler;
    /** Extractor model. */
    private final ExtractorModel extractor;
    /** Production step per second. */
    private final int stepsPerSecond;
    /** Extraction speed. */
    private final int extractionSpeed;
    /** Extraction capacity. */
    private final int extractionCapacity;
    /** Drop off speed. */
    private final int dropOffSpeed;
    /** Last town hall. */
    private Entity townHall;
    /** Entity progress bar. */
    private final Bar barProgress;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    protected UnitWorker(SetupEntity setup)
    {
        super(setup);
        factory = setup.factoryEntity;
        handler = setup.handlerEntity;
        producer = new ProducerModel<>(this, setup.handlerEntity, setup.fps);
        extractor = new ExtractorModel(this, setup.fps);
        final Configurable configurable = setup.getConfigurable();
        stepsPerSecond = configurable.getInteger("steps_per_second", "production");
        extractionSpeed = configurable.getInteger("extraction_speed", "extraction");
        extractionCapacity = configurable.getInteger("extraction_capacity", "extraction");
        dropOffSpeed = configurable.getInteger("drop_off_speed", "extraction");
        barProgress = new Bar(0, 0);
        barProgress.setBorderSize(1, 1);
    }

    /*
     * Unit
     */

    @Override
    public void update(double extrp)
    {
        super.update(extrp);
        producer.updateProduction(extrp);
        extractor.updateExtraction(extrp);
    }

    @Override
    public void render(Graphic g, CameraGame camera)
    {
        super.render(g, camera);
        if (isProducing() && getProductionProgress() > 0)
        {
            final int x = camera.getViewpointX(getLocationIntX());
            final int y = camera.getViewpointY(getLocationIntY() + getHeight() + 20);
            barProgress.setLocation(x, y);
            barProgress.setMaximumSize(getWidth(), 4);
            barProgress.setWidthPercent(getProductionProgressPercent());
            barProgress.setColorBackground(ColorRgba.GRAY);
            barProgress.setColorForeground(ColorRgba.RED);
            barProgress.render(g);
        }
    }

    @Override
    public void stop()
    {
        super.stop();
        stopProduction();
        stopExtraction();
    }

    /*
     * Producer user
     */

    @Override
    public boolean canProduce(ProducibleEntity producible)
    {
        return true;
    }

    @Override
    public boolean canBeProduced(ProducibleEntity producible)
    {
        final int dist = getDistanceInTile(producible, true);
        if (!isMoving())
        {
            if (dist > 0)
            {
                setDestination(producible);
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public <EI extends Entity> EI getEntityToProduce(Class<EI> id)
    {
        return factory.create(id);
    }

    @Override
    public int getStepsPerSecond()
    {
        return stepsPerSecond;
    }

    /*
     * Extractor user
     */

    @Override
    public boolean canExtract()
    {
        final Tiled resource = getResourceLocation();
        try
        {
            final Entity entity = handler.getEntityAt(resource);
            // Allow worker to enter inside the gold mine
            setIgnoreId(entity.getId(), true);
            return getDistanceInTile(entity, true) <= 1;
        }
        catch (final EntityNotFoundException entity)
        {
            if (!isDestinationReached())
            {
                return false;
            }
            final ResourceType type = map.getTile(resource).getResourceType();
            switch (type)
            {
                case WOOD:
                    return getDistanceInTile(extractor.getResourceLocation(), false) <= 1;
                default:
                    stopExtraction();
                    break;
            }
            return false;
        }
    }

    @Override
    public boolean canCarry()
    {
        setIgnoreId(townHall.getId(), true);
        return getDistanceInTile(townHall, false) < 1;
    }

    @Override
    public int getExtractionCapacity()
    {
        return extractionCapacity;
    }

    @Override
    public int getExtractionSpeed()
    {
        return extractionSpeed;
    }

    @Override
    public int getDropOffSpeed()
    {
        return dropOffSpeed;
    }

    /*
     * Producer services
     */

    @Override
    public void addToProductionQueue(ProducibleEntity producible)
    {
        producer.addToProductionQueue(producible);
    }

    @Override
    public void updateProduction(double extrp)
    {
        producer.updateProduction(extrp);
    }

    @Override
    public void skipProduction()
    {
        producer.skipProduction();
    }

    @Override
    public void stopProduction()
    {
        producer.stopProduction();
    }

    @Override
    public double getProductionProgress()
    {
        return producer.getProductionProgress();
    }

    @Override
    public int getProductionProgressPercent()
    {
        return producer.getProductionProgressPercent();
    }

    @Override
    public Class<? extends Entity> getProducingElement()
    {
        return producer.getProducingElement();
    }

    @Override
    public Iterator<ProducibleEntity> getProductionIterator()
    {
        return producer.getProductionIterator();
    }

    @Override
    public int getQueueLength()
    {
        return producer.getQueueLength();
    }

    @Override
    public boolean isProducing()
    {
        return producer.isProducing();
    }

    /*
     * Extractor services
     */

    @Override
    public void updateExtraction(double extrp)
    {
        extractor.updateExtraction(extrp);
    }

    @Override
    public void setResource(Extractible entity)
    {
        extractor.setResource(entity);
    }

    @Override
    public void setResource(Enum<?> type, int tx, int ty, int tw, int th)
    {
        extractor.setResource(type, tx, ty, tw, th);
    }

    @Override
    public Tiled getResourceLocation()
    {
        return extractor.getResourceLocation();
    }

    @Override
    public Enum<?> getResourceType()
    {
        return extractor.getResourceType();
    }

    @Override
    public void startExtraction()
    {
        extractor.startExtraction();
    }

    @Override
    public void stopExtraction()
    {
        extractor.stopExtraction();
    }

    @Override
    public boolean isExtracting()
    {
        return extractor.isExtracting();
    }

    /*
     * Producer listener
     */

    @Override
    public void notifyCanNotProduce(ProducibleEntity element)
    {
        // Nothing to do
    }

    @Override
    public void notifyStartProduction(ProducibleEntity element, Entity entity)
    {
        setActive(false);
        setVisible(false);
        setSelection(false);
    }

    @Override
    public void notifyProducing(ProducibleEntity element, Entity entity)
    {
        if (entity instanceof Building)
        {
            if (getProductionProgress() < 100)
            {
                entity.setFrame(1);
            }
        }
    }

    @Override
    public void notifyProduced(ProducibleEntity element, Entity entity)
    {
        entity.setFrame(2);
        final CoordTile coord = map.getFreeTileAround(this, 64);
        setLocation(coord.getX(), coord.getY());
        setActive(true);
        setVisible(true);
    }

    /*
     * Extractor listener
     */

    @Override
    public void notifyStartGoToRessources(Enum<?> type, Tiled resourceLocation)
    {
        setDestination(resourceLocation);
    }

    @Override
    public void notifyStartExtraction(Enum<?> type, Tiled resourceLocation)
    {
        setVisible(false);
        setActive(false);
        setSelection(false);
    }

    @Override
    public void notifyExtracted(Enum<?> type, int currentQuantity)
    {
        clearIgnoredId();
    }

    @Override
    public void notifyStartCarry(Enum<?> type, int totalQuantity)
    {
        setVisible(true);
        setActive(true);
        try
        {
            townHall = handler.getClosestEntity(this, Warehouse.class, true);
            final CoordTile out = map.getClosestAvailableTile(this, 16, townHall);
            if (out != null)
            {
                setLocation(out.getX(), out.getY());
            }
            setDestination(townHall);
        }
        catch (final EntityNotFoundException exception)
        {
            stopExtraction();
        }
    }

    @Override
    public void notifyStartDropOff(Enum<?> type, int totalQuantity)
    {
        setVisible(false);
        setActive(false);
    }

    @Override
    public void notifyDroppedOff(Enum<?> type, int droppedQuantity)
    {
        final CoordTile out = map.getClosestAvailableTile(this, 16, getResourceLocation());
        if (out != null)
        {
            setLocation(out.getX(), out.getY());
        }
        setVisible(true);
        setActive(true);
        clearIgnoredId();
        // Can retrieve resources here (using type and droppedQuantity)
    }
}
