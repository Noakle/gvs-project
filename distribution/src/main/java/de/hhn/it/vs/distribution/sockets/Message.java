package de.hhn.it.vs.distribution.sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class Message implements Serializable {

    /** id of the message. This ID is unique for the sender. */
    private int id;
    /** static counter to generate unique ids within the same process. */
    private static int idcounter;

    private static final transient Logger logger = LoggerFactory.getLogger(Message.class.getName());

    /**
     * Constructor asigning unique message number to id.
     */
    public Message() {
        super();
        id = ++idcounter;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String retval = "[Message] ";
        retval += "id = " + id;
        return retval;
    }

}
