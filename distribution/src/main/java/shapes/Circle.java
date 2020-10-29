package shapes;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Simple shapes.shapes.Circle class for demonstration purpose
 */
public class Circle extends GraphicalObject implements Serializable {
  private static Logger logger = Logger.getLogger(Circle.class.getName());
  /**
   * This class represents a circle only for demonstration purpose. This is not usable in any
   * serious environment.
   */
  private static final long serialVersionUID = 8084990708075378109L;
  private int radius;

  public Circle(int radius) {
    this.radius = radius;
    logger.info("Create new circle with radius " + radius);
  }


  @Override
  public String toString() {
    return "shapes.Circle{radius=" + radius + '}';
  }
}
