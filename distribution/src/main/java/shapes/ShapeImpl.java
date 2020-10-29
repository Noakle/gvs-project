package shapes;

import java.rmi.RemoteException;

public class ShapeImpl implements Shape {
  private GraphicalObject object;
  private int version;

  public ShapeImpl(final GraphicalObject object2, final int version2) {
    this.object = object2;
    this.version = version2;
  }

  public final int getVersion() throws RemoteException {
    return version;
  }

  public final GraphicalObject getAllState() throws RemoteException {
    return object;
  }

}

