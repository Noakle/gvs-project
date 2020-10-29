package shapes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ShapeListServer {
  // Constant for registry lookup
  public static final String REGISTRY_NAME = "shapes.shapes.Shape List";

  public static void main(String[] args) throws RemoteException {
    try {
      // Create serving object
      ShapeListImpl aShapeList = new ShapeListImpl();

      // Make object accessible from remote and generate remote reference
      // (a.k.a. stub, proxy)
      ShapeList shapeListProxy = (ShapeList) UnicastRemoteObject.exportObject(
              aShapeList, 0);

      // create a handle to a hopefully running rmiregistry on the local
      // host and bind the remote object's stub in the registry
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(REGISTRY_NAME, shapeListProxy);

      System.out.println("shapes.shapes.ShapeList server ready");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
