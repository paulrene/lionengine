package com.b3dgs.lionengine.swing;

/**
 * Combo item class.
 */
public final class ComboItem
        implements CanEnable
{
    /** Object reference. */
    private final Object obj;
    /** Enabled flag. */
    private boolean isEnable;

    /**
     * Constructor.
     * 
     * @param obj The object reference.
     * @param isEnable The enabled flag.
     */
    public ComboItem(Object obj, boolean isEnable)
    {
        this.obj = obj;
        this.isEnable = isEnable;
    }

    /**
     * Constructor.
     * 
     * @param obj The object reference.
     */
    public ComboItem(Object obj)
    {
        this(obj, true);
    }

    @Override
    public boolean isEnabled()
    {
        return isEnable;
    }

    @Override
    public void setEnabled(boolean isEnable)
    {
        this.isEnable = isEnable;
    }

    /**
     * Get the object.
     * 
     * @return The object.
     */
    public Object getObject()
    {
        return obj;
    }

    @Override
    public String toString()
    {
        return obj.toString();
    }
}
