package com.b3dgs.lionengine.example.d_rts.f_warcraft.skill;

import java.awt.Color;

import com.b3dgs.lionengine.example.d_rts.f_warcraft.Cursor;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.FactoryProduction;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.Map;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.ProducibleEntity;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.entity.UnitWorker;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.type.TypeCursor;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.type.TypeEntity;
import com.b3dgs.lionengine.example.d_rts.f_warcraft.type.TypeSkill;
import com.b3dgs.lionengine.game.purview.Configurable;
import com.b3dgs.lionengine.game.rts.CameraRts;
import com.b3dgs.lionengine.game.rts.ControlPanelModel;
import com.b3dgs.lionengine.game.rts.CursorRts;

/**
 * Skill build implementation.
 */
public abstract class SkillProduceBuilding
        extends Skill
{
    /** Production factory. */
    protected final FactoryProduction factoryProduction;
    /** Entity type to produce. */
    private final TypeEntity entity;
    /** Production width in tile. */
    private final int width;
    /** Production height in tile. */
    private final int height;
    /** The production cost gold. */
    private final int gold;
    /** The production cost wood. */
    private final int wood;
    /** Cursor reference. */
    private final Cursor cursor;
    /** Map reference. */
    private final Map map;
    /** To produce. */
    private ProducibleEntity toProduce;

    /**
     * Constructor.
     * 
     * @param id The skill id.
     * @param setup The setup skill reference.
     * @param entity The entity type to produce.
     * @param cursor The cursor reference.
     * @param map The map reference.
     */
    protected SkillProduceBuilding(TypeSkill id, SetupSkill setup, TypeEntity entity, Cursor cursor, Map map)
    {
        super(id, setup);
        this.cursor = cursor;
        this.map = map;
        this.entity = entity;
        factoryProduction = setup.factoryProduction;
        final Configurable config = factoryProduction.getConfig(entity);
        width = config.getDataInteger("widthInTile", "size");
        height = config.getDataInteger("heightInTile", "size");
        gold = config.getDataInteger("gold", "cost");
        wood = config.getDataInteger("wood", "cost");
        setOrder(true);
    }

    @Override
    public void updateOnMap(double extrp, CameraRts camera, CursorRts cursor)
    {
        super.updateOnMap(extrp, camera, cursor);
        if (isActive())
        {
            if (map.isAreaAvailable(cursor.getLocationInTileX(), cursor.getLocationInTileY(),
                    toProduce.getWidthInTile(), toProduce.getHeightInTile(), owner.getId().intValue()))
            {
                this.cursor.setBoxColor(Color.GREEN);
            }
            else
            {
                this.cursor.setBoxColor(Color.RED);
            }
        }
    }

    @Override
    public void action(ControlPanelModel<?> panel, CursorRts cursor)
    {
        if (!map.isAreaAvailable(cursor.getLocationInTileX(), cursor.getLocationInTileY(), toProduce.getWidthInTile(),
                toProduce.getHeightInTile(), owner.getId().intValue()))
        {
            setActive(true);
            panel.ordered();
            this.cursor.setType(TypeCursor.BOX);
        }
        else if (owner instanceof UnitWorker && toProduce != null)
        {
            toProduce.setLocation(destX, destY);
            ((UnitWorker) owner).addToProductionQueue(toProduce);
        }
    }

    @Override
    public void onClicked(ControlPanelModel<?> panel)
    {
        if (owner instanceof UnitWorker)
        {
            final UnitWorker worker = (UnitWorker) owner;
            final ProducibleEntity producible = factoryProduction.createProducible(entity, 0, 0);
            if (worker.canProduce(producible))
            {
                cursor.setType(TypeCursor.BOX);
                cursor.setBoxColor(Color.GREEN);
                cursor.setBoxSize(width, height);
                toProduce = producible;
            }
            else
            {
                worker.notifyCanNotProduce(producible);
                toProduce = null;
                cursor.setType(TypeCursor.POINTER);
                setActive(false);
                panel.resetOrder();
            }
        }
    }

    @Override
    public String getDescription()
    {
        final StringBuilder description = new StringBuilder(super.getDescription());
        description.append(":     ").append(gold).append("      ").append(wood);
        return description.toString();
    }
}
