package de.hhn.it.vs.distribution.sockets;

/**
 * An object of the class Response represents a result of a procedure call. It
 * may contain a return value OR an exception.
 *
 * @author wnck
 */
public final class Response extends Message {
    /**
     * serial id to mark major changes in this class.
     */
    private static final long serialVersionUID = -5816110583392575010L;

    /**
     * Marks if the response contains an exception.
     */
    private boolean exception;

    /**
     * Holds potentially the exception.
     */
    private Exception exceptionObject;

    /**
     * A reference to the responsible request. This element exists only for debug puroses.
     */
    private Request request;

    /**
     * If the response contains a return value, this is the value.
     */
    private Object returnObject;

    /**
     * Marks if the response returns a void return value.
     */
    private boolean returnVoid;

    /**
     * Constructor, if the method returns just a void.
     *
     * @param request request the response belongs to
     */
    public Response(Request request) {
        super();
        this.request = request;
        exception = false;
        returnVoid = true;
    }

    /**
     * Constructor, if the request failed with an exception.
     *
     * @param request request the response belongs to
     * @param object  exception thrown during request processing
     */
    public Response(Request request, Exception object) {
        this(request);
        exceptionObject = object;
        exception = true;
    }

    /**
     * Constructor, if the request returns a value as a return value.
     *
     * @param request request the response belongs to
     * @param object  return value from the request processing
     */
    public Response(Request request, Object object) {
        this(request);
        returnObject = object;
        exception = false;
        returnVoid = false;
    }

    /**
     * Returns the potential exception
     *
     * @return exception, otherwise null
     */
    public Exception getExceptionObject() {
        return exceptionObject;
    }

    /**
     * Returns the request the response belongs to.
     *
     * @return request, the response belongs to
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Returns the return value as result of the request processing.
     *
     * @return return value
     */
    public Object getReturnObject() {
        return returnObject;
    }

    /**
     * Shows if the response contains an exception.
     *
     * @return true if there is an exception, otherwise false
     */
    public boolean isException() {
        return exception;
    }

    /**
     * Shows if the response contains neither return value nor an exception.
     *
     * @return true if the response contains no further objects, otherwise false
     */
    public boolean isReturnVoid() {
        return returnVoid;
    }

    /**
     * Sets the exception object. Removes potential return value object.
     *
     * @param e exception to be set
     */
    public void setExceptionObject(Exception e) {
        this.exception = true;
        this.returnVoid = false;
        this.exceptionObject = e;
        this.returnObject = null;
    }

    /**
     * Sets the return value object. Removes potential exception object.
     *
     * @param returnObject return value object to be transported by the response
     */
    public void setReturnObject(Object returnObject) {
        this.returnVoid = false;
        this.exception = false;
        this.exceptionObject = null;
        this.returnObject = returnObject;
    }

    @Override
    public String toString() {
        String retval = "[Response] ";
        retval += "id = " + getId() + "; ";
        retval += "mode = ";

        // has an exception
        if (this.exception) {
            retval += "exception";
            return retval;
        }

        // has no exception and no return value
        if (this.returnVoid) {
            retval += "void";
            return retval;
        }

        // has no exception but a return value
        retval += "return value";

        return retval;
    }

}
