package com.b3dgs.lionengine.example.d_rts.e_skills.projectile;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.example.d_rts.e_skills.TypeProjectile;
import com.b3dgs.lionengine.game.SetupEntityGame;
import com.b3dgs.lionengine.game.projectile.FactoryProjectileGame;
import com.b3dgs.lionengine.game.purview.model.ConfigurableModel;

/**
 * Factory projectile implementation.
 */
public final class FactoryProjectile
        extends FactoryProjectileGame<TypeProjectile, Projectile, SetupEntityGame>
{
    /** Directory name from our resources directory containing our entities. */
    private static final String PROJECTILE_PATH = "projectiles";

    /**
     * Constructor.
     */
    public FactoryProjectile()
    {
        super(TypeProjectile.class);
        loadAll(TypeProjectile.values());
    }

    @Override
    protected SetupEntityGame createSetup(TypeProjectile id)
    {
        return new SetupEntityGame(new ConfigurableModel(), Media.get(FactoryProjectile.PROJECTILE_PATH, id + ".xml"),
                false);
    }

    @Override
    public Projectile createProjectile(TypeProjectile type, int id, int frame)
    {
        switch (type)
        {
            case spear:
                return new Spear(getSetup(TypeProjectile.spear), id, frame);
            default:
                throw new LionEngineException("Projectile not found: " + type.name());
        }
    }
}
