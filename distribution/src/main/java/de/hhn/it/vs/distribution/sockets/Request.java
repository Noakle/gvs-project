package de.hhn.it.vs.distribution.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.helper.CheckingHelper;

import java.util.HashMap;

/**
 * Generic request  class. It transports the service request as well as the parameters.
 *
 * @author wnck
 */
public class Request extends Message {
    /**
     * HashMap to hold the request parameters.
     */
    private HashMap<String, Object> parameters;
    /**
     * name of the method to be called.
     */
    private String methodToCall;

    /**
     * Constructor with methodToCall for the remote call.
     */
    public Request(final String method) throws IllegalParameterException {
        CheckingHelper.assertThatIsNotNull(method, "method");
        this.methodToCall = method;
        parameters = new HashMap<>();
    }

    /**
     * Adds a parameter to the request. The class of the object must be serializable.
     *
     * @param key   name of the parameter
     * @param value object associated with the parameter name. The object must be serializable.
     * @return reference to this to allow concatenation of method calls
     */
    public Request addParameter(String key, Object value) throws IllegalParameterException {
        CheckingHelper.assertThatIsNotNull(key, "key");
        CheckingHelper.assertThatIsSerializable(value, "value");
        parameters.put(key, value);
        return this;
    }

    /**
     * Returns a parameter of the request.
     *
     * @param key name of the parameter
     * @return the associated object if the key exists, otherwise null
     * @throws IllegalParameterException if the key is a null reference
     */
    public Object getParameter(String key) throws IllegalParameterException {
        CheckingHelper.assertThatIsNotNull(key, "key");
        return parameters.get(key);
    }

    /**
     * Returns identifier of the method to be called. This identifier may be the method name, but
     * may also be any other string. The server side has to know the meaning of this String.
     *
     * @return identifier of the method
     */
    public String getMethodToCall() {
        return methodToCall;
    }
}
