package shapes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Shape extends Remote {
  // get version number of this object
  int getVersion() throws RemoteException;

  // get the graphical object from the shape
  GraphicalObject getAllState() throws RemoteException;
}

