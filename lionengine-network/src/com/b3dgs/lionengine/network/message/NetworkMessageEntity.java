package com.b3dgs.lionengine.network.message;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.b3dgs.lionengine.utility.UtilityConversion;

/**
 * Standard entity message.
 * 
 * @param <M> The message entity element enum.
 */
public abstract class NetworkMessageEntity<M extends Enum<M>>
        extends NetworkMessage
{
    /** List of actions. */
    private final HashMap<M, Object> actions = new HashMap<>(1);
    /** Entity number. */
    private short entityId;

    /**
     * Constructor (used in decoding case).
     */
    public NetworkMessageEntity()
    {
        super();
        this.entityId = -1;
    }

    /**
     * Constructor (used for the client case).
     * 
     * @param type The message type.
     * @param clientId The client id.
     */
    public NetworkMessageEntity(Enum<?> type, byte clientId)
    {
        super(type, clientId);
        this.entityId = -1;
    }

    /**
     * Constructor (used to identify an entity from the server).
     * 
     * @param type The message type.
     * @param entityId The entity id.
     */
    public NetworkMessageEntity(Enum<?> type, short entityId)
    {
        super(type, (byte) -1);
        this.entityId = entityId;
    }

    /**
     * Constructor (used to identify an entity from the server).
     * 
     * @param type The message type.
     * @param entityId The entity id.
     * @param destId The client destination.
     */
    public NetworkMessageEntity(Enum<?> type, short entityId, byte destId)
    {
        super(type, (byte) -1, destId);
        this.entityId = entityId;
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, boolean value)
    {
        this.actions.put(element, Boolean.valueOf(value));
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, char value)
    {
        this.actions.put(element, Character.valueOf(value));
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, byte value)
    {
        this.actions.put(element, Byte.valueOf(value));
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, short value)
    {
        this.actions.put(element, Short.valueOf(value));
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, int value)
    {
        this.actions.put(element, Integer.valueOf(value));
    }

    /**
     * Add an action.
     * 
     * @param element The action type.
     * @param value The action value.
     */
    public void addAction(M element, double value)
    {
        this.actions.put(element, Double.valueOf(value));
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public boolean getActionBoolean(M element)
    {
        return ((Boolean) this.actions.get(element)).booleanValue();
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public byte getActionByte(M element)
    {
        return ((Byte) this.actions.get(element)).byteValue();
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public char getActionChar(M element)
    {
        return ((Character) this.actions.get(element)).charValue();
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public short getActionShort(M element)
    {
        return ((Short) this.actions.get(element)).shortValue();
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public int getActionInteger(M element)
    {
        return ((Integer) this.actions.get(element)).intValue();
    }

    /**
     * Get the action value.
     * 
     * @param element The action element.
     * @return The action value.
     */
    public double getActionDouble(M element)
    {
        return ((Double) this.actions.get(element)).doubleValue();
    }

    /**
     * Check if the action is contained.
     * 
     * @param element The action to check.
     * @return <code>true</code> if action is contained, <code>false</code> else.
     */
    public boolean hasAction(M element)
    {
        return this.actions.containsKey(element);
    }

    /**
     * Get the entity id (-1 if none).
     * 
     * @return The entity id.
     */
    public short getEntityId()
    {
        return this.entityId;
    }

    /**
     * Retrieve the keys, store its total number in the buffer, and call {@link #encode(ByteArrayOutputStream, Enum) for
     * each key}.
     * 
     * @param buffer The current buffer to write.
     * @throws IOException Exception in case of error.
     */
    @Override
    protected void encode(ByteArrayOutputStream buffer) throws IOException
    {
        buffer.write(UtilityConversion.shortToByteArray(this.entityId));
        final Set<M> keys = this.actions.keySet();

        // Fill the data
        buffer.write((byte) keys.size());
        for (final M key : keys)
        {
            this.encode(buffer, key);
        }
    }

    /**
     * Read the first byte to retrieve the total number of key and call {@link #decode(DataInputStream, int) for each
     * key}.
     * 
     * @param buffer The current buffer to read.
     * @throws IOException Exception in case of error.
     */
    @Override
    protected void decode(DataInputStream buffer) throws IOException
    {
        this.entityId = buffer.readShort();
        final int number = buffer.readByte();
        for (int i = 0; i < number; i++)
        {
            this.decode(buffer, i);
        }
    }

    /**
     * Encode function for the current key.
     * 
     * @param buffer The current buffer to write.
     * @param key The current key.
     * @throws IOException Exception in case of error.
     */
    protected abstract void encode(ByteArrayOutputStream buffer, M key) throws IOException;

    /**
     * Decode function for the current key number.
     * 
     * @param buffer The current buffer to read.
     * @param i The current key number.
     * @throws IOException Exception in case of error.
     */
    protected abstract void decode(DataInputStream buffer, int i) throws IOException;
}
