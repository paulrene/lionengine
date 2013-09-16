package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.player;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.AppLionheart;

/**
 * Render the stats.
 */
public class StatsRenderer
{
    /** Hud sprite. */
    private final SpriteTiled hud;
    /** Heart sprite. */
    private final SpriteTiled heart;
    /** Number font. */
    private final SpriteTiled number;
    /** 1.5 if wide, 1 if not. */
    private final double wide;

    /**
     * Constructor.
     * 
     * @param wide The wide state.
     */
    public StatsRenderer(boolean wide)
    {
        hud = Drawable.loadSpriteTiled(Media.get(AppLionheart.SPRITES_DIR, "hud.png"), 16, 16);
        heart = Drawable.loadSpriteTiled(Media.get(AppLionheart.SPRITES_DIR, "health.png"), 8, 8);
        number = Drawable.loadSpriteTiled(Media.get(AppLionheart.SPRITES_DIR, "numbers.png"), 8, 16);
        this.wide = wide ? 1.5 : 1;
    }

    /**
     * Load the sprites.
     */
    public void load()
    {
        hud.load(false);
        heart.load(false);
        number.load(false);
    }

    /**
     * Render the stats.
     * 
     * @param g The graphic output.
     * @param stats The stats to render.
     */
    public void render(Graphic g, Stats stats)
    {
        renderHeart(g, stats);
        renderTalisment(g, stats);
        renderLife(g, stats);

        // Sword level
        int x = (int) (160 * wide);
        hud.render(g, stats.getSwordLevel() + 1, x, 2);

        // Amulet
        x = (int) (230 * wide);
        if (stats.getAmulet())
        {
            hud.render(g, 1, x, 2);
        }
    }

    /**
     * Render heart.
     * 
     * @param g The graphic output.
     * @param stats The stats to render.
     */
    private void renderHeart(Graphic g, Stats stats)
    {
        for (int i = 0; i < 8; i++)
        {
            final int x = i % 4 * 9 + 1;
            final int y = (int) Math.floor(i / 4) * 9 + 1;
            if (i < stats.getHeart())
            {
                heart.render(g, 0, x, y);
            }
            else if (i < stats.getHeartMax())
            {
                heart.render(g, 1, x, y);
            }
            else
            {
                heart.render(g, 2, x, y);
            }
        }
    }

    /**
     * Render talisment.
     * 
     * @param g The graphic output.
     * @param stats The stats to render.
     */
    private void renderTalisment(Graphic g, Stats stats)
    {
        final int x = (int) (60 * wide);
        hud.render(g, 0, x, 2);
        final int talisments = stats.getTalisment();
        if (talisments < 10)
        {
            number.render(g, 0, x + 20, 2);
            number.render(g, talisments + 1, x + 28, 2);
        }
        else if (talisments < 100)
        {
            number.render(g, talisments / 10 + 1, x + 20, 2);
            number.render(g, talisments % 10 + 1, x + 28, 2);
        }
    }

    /**
     * Render life.
     * 
     * @param g The graphic output.
     * @param stats The stats to render.
     */
    private void renderLife(Graphic g, Stats stats)
    {
        final int x = wide == 1.0 ? 280 : (int) (280 * wide * 1.03);
        hud.render(g, 6, x, 2);
        final int lifes = stats.getLife();
        if (lifes < 10)
        {
            number.render(g, 0, x + 20, 2);
            number.render(g, lifes + 1, x + 28, 2);
        }
        else if (lifes < 100)
        {
            number.render(g, lifes / 10 + 1, x + 20, 2);
            number.render(g, lifes % 10 + 1, x + 28, 2);
        }
    }
}