import java.util.*;
import java.math.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

class CurrentVersion {
  static int[] timesCalled = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  static long[] totalTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  static HashSet<OurInteger> extensions;
  static int extended;

  static OurInteger makeInvariate(OurInteger invariate, int width, int height) {
    OurInteger[] possibilities = {new OurInteger(32 * width + height),
                                  new OurInteger(32 * height + width),
                                  new OurInteger(32 * height + width),
                                  new OurInteger(32 * width + height),
                                  new OurInteger(32 * width + height),
                                  new OurInteger(32 * width + height),
                                  new OurInteger(32 * height + width),
                                  new OurInteger(32 * height + width)};

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        if(invariate.testBit(10 + x + y * width)) {
          // If our polygons were ever of size 32, we would need to shift by more than 8 bits.
          possibilities[0] = possibilities[0].setBit(10 + x + (y) * width);
          possibilities[1] = possibilities[1].setBit(10 - y - 1 + (x + 1) * height);
          possibilities[2] = possibilities[2].setBit(10 + y + (width - x - 1) * height);
          possibilities[3] = possibilities[3].setBit(10 - x - 1 + width * (height - y));
          possibilities[4] = possibilities[4].setBit(10 - x - 1 + (y + 1) * width);
          possibilities[5] = possibilities[5].setBit(10 + x + (height - y - 1) * width);
          possibilities[6] = possibilities[6].setBit(10 + y + x * height);
          possibilities[7] = possibilities[7].setBit(10 - y - 1 + (width - x) * height);
        }
      }
    }

    OurInteger result = possibilities[0];
    for(int possibilityIndex = possibilities.length - 1; possibilityIndex > 0; possibilityIndex--) {
      // System.out.println(possibilities[possibilityIndex]);
      if(possibilities[possibilityIndex].compareTo(result) == -1) {
        result = possibilities[possibilityIndex];
      }
    }
    return result;
  }

  static ArrayList<OurInteger> generateExtensionsAndSuperposition(OurInteger polygon) {
    timesCalled[4] += 1;
    //long startTime = System.currentTimeMillis();
//    int width = (polygon.invariate.intValue() >>> 5) % 32;
//    int width =  (( ( (int)polygon.invariate.smallerPlace) >>> 5) % 32);
//    int height = ( polygon.invariate.intValue() % 32);
      int width = polygon.mod(10).shiftRight(5).intValue();
      int height = polygon.mod(5).intValue();
    ArrayList<OurInteger> extensions = new ArrayList<OurInteger>();
    HashSet<Integer> usedPairs = new HashSet<Integer>(); // make sure we're not adding a new square to the same place twice
    for(int pairIndex = width * height - 1; pairIndex > -1; pairIndex--) {
      if(polygon.testBit(10 + pairIndex)) {
        if(pairIndex % width != 0) {
          if(! polygon.testBit(10 + pairIndex - 1)) {
            if(! usedPairs.contains(pairIndex - 1)) {
              usedPairs.add(pairIndex - 1);
              extensions.add(makeInvariate(polygon.setBit(10 + pairIndex - 1), width, height));
            }
          }
        } else {
          OurInteger invariate = polygon;
          for(int groupNumber = 0; groupNumber < height; groupNumber++) {
            OurInteger preShift = invariate.mod(width * groupNumber + 10 + groupNumber);
            invariate = invariate.shiftLeft(1).subtract(preShift); // came from (invariate - preShift) * 2 + preShift
          }
          invariate = invariate.setBit(10 + pairIndex * (width + 1) / width);
          invariate = makeInvariate(invariate, width + 1, height);
          extensions.add(invariate);
        }

        if((pairIndex + 1) % width != 0) {
          if(! polygon.testBit(10 + pairIndex + 1)) {
            if(! usedPairs.contains(pairIndex + 1)) {
              usedPairs.add(pairIndex + 1);
              extensions.add(makeInvariate(polygon.setBit(10 + pairIndex + 1), width, height));
            }
          }
        } else {
          OurInteger invariate = polygon;
          for(int groupNumber = 1; groupNumber < height; groupNumber++) {
            OurInteger preShift = invariate.mod(width * groupNumber + groupNumber - 1 + 10);
            invariate = invariate.shiftLeft(1).subtract(preShift); // came from (invariate - preShift) * 2 + preShift
          }
          invariate = invariate.setBit(10 + (pairIndex * (width + 1) + 1) / width);
          invariate = makeInvariate(invariate, width + 1, height);
          extensions.add(invariate);
        }

        if(pairIndex >= width) {
          if(! polygon.testBit(10 + pairIndex - width)) {
            if(! usedPairs.contains(pairIndex - width)) {
              usedPairs.add(pairIndex - width);
              extensions.add(makeInvariate(polygon.setBit(10 + pairIndex - width), width, height));
            }
          }
        } else {
          extensions.add(makeInvariate(polygon.shiftRight(10).shiftLeft(width).setBit(pairIndex).shiftLeft(10).add(polygon.mod(10)), width, height + 1));
        }

        if(pairIndex < width * (height - 1)) {
          if(! polygon.testBit(10 + pairIndex + width)) {
            if(! usedPairs.contains(pairIndex + width)) {
              usedPairs.add(pairIndex + width);
              extensions.add(makeInvariate(polygon.setBit(10 + pairIndex + width), width, height));
            }
          }
        } else {
          extensions.add(makeInvariate(polygon.setBit(10 + pairIndex + width), width, height + 1));
        }
      }
    }
    //long endTime = System.currentTimeMillis();
    //totalTime[4] += endTime - startTime;
    return extensions;
  }

  static ArrayList<OurInteger> generateExtensionsFromList(ArrayList<OurInteger> polygons, int threads) {
    timesCalled[18] += 1;
    //long startTime = System.currentTimeMillis();


    extensions = new HashSet<OurInteger>();
    extended = 0;

    GeneratorThread[] generators = new GeneratorThread[threads];
    int polygonsPerThread = (int)Math.floor(polygons.size() / threads);
    for(int threadIndex = 0; threadIndex < threads; threadIndex++) {
        ArrayList<OurInteger> polygonsForThisThread;
        if(threadIndex < threads - 1) {
            polygonsForThisThread = new ArrayList<OurInteger>(polygons.subList(threadIndex * polygonsPerThread, (threadIndex + 1) * polygonsPerThread));
        } else {
            polygonsForThisThread = new ArrayList<OurInteger>(polygons.subList(threadIndex * polygonsPerThread, polygons.size()));
        }
        generators[threadIndex] = new GeneratorThread(polygonsForThisThread);
        generators[threadIndex].start();
    }

    for(int threadIndex = 0; threadIndex < threads; threadIndex++) {
        try {
            generators[threadIndex].join();
        } catch(Exception e) {
            System.out.println("There was an exception in a GeneratorThread.");
        }
    }

    //long endTime = System.currentTimeMillis();
    //totalTime[18] += endTime - startTime;
    return new ArrayList<>(extensions);
  }

  public static void main(String[] args) {
    int size = 15;
    int threads = 6;

    ArrayList<OurInteger> polygons = new ArrayList<OurInteger>();
    OurInteger monominoe = new OurInteger(1057);
    polygons.add(monominoe);

    long startTime = System.currentTimeMillis();
    for(int round = 0; round < size - 1; round++) {
      System.out.println("Starting to generate extensions of size " + Integer.toString(round + 2) + ".");
      polygons = generateExtensionsFromList(polygons, threads);
      System.gc();
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total Time: " + Long.toString((endTime - startTime) / 1000) + " seconds.");
    System.out.println("Total Memory: " + Long.toString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes.");
    System.out.println("Result: " + Integer.toString(polygons.size()));
      
//    printPolygonList(polygons);



    // startTime = System.currentTimeMillis();
    // writeObjectToFile(polygons);
    // endTime = System.currentTimeMillis();
    // System.out.println("Write Time: " + ((endTime - startTime) / 1000) + " seconds.");
    // polygons = (ArrayList<Polygon>)readObjectFromFile("data/polygon");
    // startTime = System.currentTimeMillis();
    // System.out.println("Read Time: " + ((startTime - endTime) / 1000) + " seconds.");
    //


     // for(int index = 0; index < totalTime.length - 1; index++) {
     //   if(timesCalled[index] != 0) {
     //     System.out.println("Function " + long.toString(index) + " had an average time of " + long.toString(totalTime[index] / timesCalled[index]) + " milliseconds and a total time of " + long.toString(Math.round(totalTime[index] / 1000)) + " seconds.");
     //   }
     // }
      
  }
    
    //  static void printPolygonList(ArrayList<Polygon> polygons) {
    //    timesCalled[2] += 1;
    //    //long startTime = System.currentTimeMillis();
    //    for(Polygon polygon : polygons) {
    //      String polygonString = polygon.invariate.toString(2).substring(0, polygon.width * polygon.height);
    //      polygonString = "0".repeat(polygon.width * polygon.height - polygonString.length()) + polygonString;
    //      for(int row = 0; row < polygon.height; row++) {
    //        StringBuilder string = new StringBuilder();
    //        string.append(polygonString.substring(row * polygon.width, row * (polygon.width) + polygon.width));
    //        System.out.println(string.reverse());
    //      }
    //
    //      System.out.println(polygon.invariate.toString(2));
    //      System.out.print("\n");
    //    }
    //     //long endTime = System.currentTimeMillis();
    //     //totalTime[2] += endTime - startTime;
    //  }

    // public static void writeObjectToFile(Object serObj) {
    //   try {
    //     FileOutputStream fileOut = new FileOutputStream("data/polygon");
    //     ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
    //     objectOut.writeObject(serObj);
    //     objectOut.close();
    //   } catch(Exception ex) {
    //     ex.printStackTrace();
    //   }
    // }
    //
    // public static Object readObjectFromFile(String filePath) {
    //   try {
    //     FileInputStream fileIn = new FileInputStream(filePath);
    //     ObjectInputStream objectIn = new ObjectInputStream(fileIn);
    //     Object result = objectIn.readObject();
    //     objectIn.close();
    //     return result;
    //   } catch(Exception ex) {
    //     ex.printStackTrace();
    //   }
    //   return null;
    // }
}
