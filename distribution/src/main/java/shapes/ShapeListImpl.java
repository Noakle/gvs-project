package shapes;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ShapeListImpl implements ShapeList {
  Logger logger = Logger.getLogger(ShapeListImpl.class.getName());
  private int version;
  private ArrayList<Shape> theList;

  public ShapeListImpl() {
    theList = new ArrayList<Shape>();
  }

  public Shape newShape(GraphicalObject g) throws RemoteException {
    version++;
    logger.info("Ah, a new shape ..");
    // create a new shape object with the graphical object
    ShapeImpl ss = new ShapeImpl(g, version);
    // make the shape accessable
    Shape s = (Shape) UnicastRemoteObject.exportObject(ss, 0);
    // add the shape to the list of shapes
    theList.add(s);
    return s;
  }

  public ArrayList<Shape> allShapes() throws RemoteException {
    logger.info("here they come ... all shapes ...");
    return theList;
  }

  public int getVersion() throws RemoteException {
    return version;
  }
}

