package com.b3dgs.lionengine.network.purview;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.network.message.NetworkMessage;
import com.b3dgs.lionengine.network.message.NetworkMessageChat;

/**
 * Basic chat implementation.
 */
public abstract class NetworkChat
        implements Networkable, KeyListener
{
    /** Networkable model. */
    private final NetworkableModel networkable;
    /** Message type. */
    private final Enum<?> type;
    /** List of received message. */
    private final ConcurrentLinkedQueue<String> messages;
    /** Current message. */
    private final StringBuilder message;
    /** Writing message. */
    private String display;

    /**
     * Constructor.
     * 
     * @param type The message type enum.
     */
    public NetworkChat(Enum<?> type)
    {
        networkable = new NetworkableModel();
        message = new StringBuilder(16);
        messages = new ConcurrentLinkedQueue<>();
        this.type = type;
        display = "";
    }

    /**
     * Render the chat.
     * 
     * @param g The graphic output.
     */
    public abstract void render(Graphic g);

    /**
     * Get the list of messages.
     * 
     * @return The list of messages.
     */
    public Queue<String> getMessages()
    {
        return messages;
    }

    /**
     * Get the current writing string.
     * 
     * @return The writing string.
     */
    public String getWriting()
    {
        return display;
    }

    /**
     * Get the message string from the network message.
     * 
     * @param message The network message.
     * @return The message string.
     */
    protected abstract String getMessage(NetworkMessageChat message);

    /**
     * Check if the message can be sent.
     * 
     * @param message The message to check.
     * @return <code>true</code> if can be sent, <code>false</code> else.
     */
    protected abstract boolean canSendMessage(String message);

    /**
     * Check if the input char can be added.
     * 
     * @param c The input char.
     * @return <code>true</code> if can be added, <code>false</code> else.
     */
    protected abstract boolean canAddChar(char c);

    /**
     * Add a new message.
     * 
     * @param message The message to add.
     */
    protected void addMessage(String message)
    {
        messages.add(message);
        if (messages.size() > 4)
        {
            messages.remove();
        }
    }

    /*
     * Networkable
     */

    @Override
    public void addNetworkMessage(NetworkMessage message)
    {
        networkable.addNetworkMessage(message);
    }

    @Override
    public void applyMessage(NetworkMessage message)
    {
        if (!(message instanceof NetworkMessageChat))
        {
            return;
        }
        final NetworkMessageChat messageChat = (NetworkMessageChat) message;
        addMessage(getMessage(messageChat));
    }

    @Override
    public Collection<NetworkMessage> getNetworkMessages()
    {
        return networkable.getNetworkMessages();
    }

    @Override
    public void clearNetworkMessages()
    {
        networkable.clearNetworkMessages();
    }

    @Override
    public void setClientId(Byte id)
    {
        networkable.setClientId(id);
    }

    @Override
    public Byte getClientId()
    {
        return networkable.getClientId();
    }

    /*
     * KeyListener
     */

    @Override
    public void keyTyped(KeyEvent e)
    {
        // Nothing to do
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        final char c = e.getKeyChar();
        // Validate message
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (message.length() > 0)
            {
                final String msg = message.toString();
                if (canSendMessage(msg))
                {
                    addNetworkMessage(new NetworkMessageChat(type, getClientId().byteValue(), msg));
                }
                message.delete(0, message.length());
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            if (message.length() > 0)
            {
                message.deleteCharAt(message.length() - 1);
            }
        }
        else if (Character.isDefined(c) && canAddChar(c))
        {
            message.append(c);
        }
        display = message.toString();
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // Nothing to do
    }
}
