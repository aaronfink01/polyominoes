import java.math.*;
import java.util.*;
import java.io.Serializable;

class Polygon implements Serializable {
    OurInteger invariate;

    public Polygon(OurInteger i) {
//        invariate = i;
    }
    
    public boolean equals(Object o) {
        if (! (o instanceof Polygon)) {
            return false;
        }
        Polygon polygon = (Polygon) o;
        return invariate.equals(polygon.invariate);
    }
    
    public int hashCode() {
        return invariate.hashCode();
    }
}
