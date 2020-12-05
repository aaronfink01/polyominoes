import java.util.*;

class GeneratorThread extends Thread {
    public ArrayList<OurInteger> polygons;

    public GeneratorThread(ArrayList<OurInteger> myPolygons) {
        polygons = myPolygons;
    }

    public void run() {
        if(polygons.size() > 0) {
            for(OurInteger polygon : polygons) {
              CurrentVersion.extended++;
              ArrayList<OurInteger> newExtensions = CurrentVersion.generateExtensionsAndSuperposition(polygon);
              for(OurInteger extension : newExtensions) {
                synchronized(CurrentVersion.extensions) {
                    CurrentVersion.extensions.add(extension);
                }
              }
              if(CurrentVersion.extended % 3000 == 0) {
                System.out.println(CurrentVersion.extended);
              }
            }
        }
    }
}
