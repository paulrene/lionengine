package com.b3dgs.lionengine.example.d_rts.d_ability;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Sequence;
import com.b3dgs.lionengine.Text;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.BuildingProducer;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.Entity;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.FactoryEntity;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.GoldMine;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.UnitAttacker;
import com.b3dgs.lionengine.example.d_rts.d_ability.entity.UnitWorker;
import com.b3dgs.lionengine.file.FileReading;
import com.b3dgs.lionengine.file.FileWriting;
import com.b3dgs.lionengine.game.TextGame;
import com.b3dgs.lionengine.game.rts.CameraRts;
import com.b3dgs.lionengine.game.rts.CursorRts;
import com.b3dgs.lionengine.game.rts.WorldRts;
import com.b3dgs.lionengine.input.Mouse;

/**
 * World implementation using WorldRts.
 */
final class World
        extends WorldRts
{
    /** Text reference. */
    private final TextGame text;
    /** Map reference. */
    private final Map map;
    /** Camera reference. */
    private final CameraRts camera;
    /** Cursor reference. */
    private final CursorRts cursor;
    /** Control panel reference. */
    private final ControlPanel controlPanel;
    /** Entity factory. */
    private final FactoryEntity factoryEntity;
    /** Production data. */
    private final FactoryProduction factoryProduction;
    /** Entity handler. */
    private final HandlerEntity handlerEntity;
    /** Arrows handler. */
    private final HandlerProjectile handlerProjectile;
    /** Context. */
    private final Context context;

    /**
     * Default constructor.
     * 
     * @param sequence The sequence reference.
     */
    World(Sequence sequence)
    {
        super(sequence);
        text = new TextGame(Font.SERIF, 10, Text.NORMAL);
        map = new Map();
        camera = new CameraRts(map);
        cursor = new CursorRts(internal, map, Media.get("cursor.png"));
        controlPanel = new ControlPanel();
        factoryProduction = new FactoryProduction();
        handlerEntity = new HandlerEntity(controlPanel, map, text);
        handlerProjectile = new HandlerProjectile(handlerEntity);
        context = new Context(map, handlerEntity, handlerProjectile, display.getRate());
        factoryEntity = context.factoryEntity;
        context.assignContext();
    }

    /**
     * Create an entity from its type.
     * 
     * @param type The entity type.
     * @param tx The horizontal location.
     * @param ty The vertical location.
     * @return The entity instance.
     */
    private Entity createEntity(TypeEntity type, int tx, int ty)
    {
        final Entity entity = factoryEntity.createEntity(type);
        entity.setPlayerId(0);
        entity.setLocation(tx, ty);
        handlerEntity.add(entity);
        return entity;
    }

    /*
     * World
     */

    @Override
    public void update(double extrp)
    {
        camera.update(keyboard);
        text.update(camera);
        cursor.update(extrp, camera, mouse, true);
        controlPanel.update(extrp, camera, cursor, keyboard);
        handlerEntity.update(extrp, camera, cursor);
        handlerProjectile.update(extrp);
    }

    @Override
    public void render(Graphic g)
    {
        map.render(g, camera);
        handlerEntity.render(g, camera, cursor);
        handlerProjectile.render(g, camera);
        controlPanel.renderCursorSelection(g, camera);
        cursor.render(g);
    }

    @Override
    protected void loaded()
    {
        camera.setView(0, 0, width, height);
        camera.setSensibility(30, 30);
        camera.setBorders(map);
        camera.setLocation(map, 25, 3);

        controlPanel.setClickableArea(camera);
        controlPanel.setClickSelection(Mouse.LEFT);
        controlPanel.setSelectionColor(Color.GREEN);

        handlerEntity.createLayers(map);
        handlerEntity.setClickAssignment(Mouse.RIGHT);

        final GoldMine goldMine = (GoldMine) createEntity(TypeEntity.gold_mine, 30, 13);

        UnitWorker peon = (UnitWorker) createEntity(TypeEntity.peon, 33, 6);
        peon.setResource(goldMine);
        peon.startExtraction();

        peon = (UnitWorker) createEntity(TypeEntity.peon, 35, 12);
        peon.addToProductionQueue(factoryProduction.createProducible(TypeEntity.barracks_orc, 30, 4));
        peon.addToProductionQueue(factoryProduction.createProducible(TypeEntity.farm_orc, 40, 10));

        final UnitAttacker grunt = (UnitAttacker) createEntity(TypeEntity.grunt, 42, 9);
        final UnitAttacker spearman = (UnitAttacker) createEntity(TypeEntity.spearman, 39, 4);
        spearman.attack(grunt);
        grunt.attack(spearman);

        final BuildingProducer townHall = (BuildingProducer) createEntity(TypeEntity.townhall_orc, 35, 7);
        townHall.setFrame(2);
        townHall.addToProductionQueue(factoryProduction.createProducible(TypeEntity.peon));
    }

    @Override
    protected void saving(FileWriting file) throws IOException
    {
        map.save(file);
    }

    @Override
    protected void loading(FileReading file) throws IOException
    {
        map.load(file);
    }
}
