import java.math.*;
import java.util.*;
import java.io.Serializable;

class Polygon implements Serializable {
  int[] pairs;
  BigInteger invariate;
  int width;
  int height;

  public Polygon(int[] p, BigInteger i, int w, int h) {
    pairs = p;
    invariate = i;
    width = w;
    height = h;
  }
}
