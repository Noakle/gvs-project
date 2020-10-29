package shapes;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ShapeList extends Remote {
  // add a new graphical object to the whiteboard
  Shape newShape(GraphicalObject g) throws RemoteException;

  // get all shapes
  ArrayList<Shape> allShapes() throws RemoteException;

  // get the latest version number
  int getVersion() throws RemoteException;
}
