import java.util.*;
import java.math.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

class ArrayVersion {
  static int[] timesCalled = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  static long[] totalTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  static NewListGenerator listGenerator = new NewListGenerator();

  static void printPolygonList(ArrayList<Polygon> polygons) {
    timesCalled[2] += 1;
    //long startTime = System.currentTimeMillis();
    for(Polygon polygon : polygons) {
      for(int pairIndex = polygon.pairs.length - 1; pairIndex > -1; pairIndex--) {
        System.out.print("(" + Integer.toString(polygon.pairs[pairIndex]) + ")     ");
      }
      System.out.print("\n");
    }
    //long endTime = System.currentTimeMillis();
    //totalTime[2] += endTime - startTime;
  }

  static boolean pairInPolygon(int[] pairs, int pair) {
    for(int pairIndex = pairs.length - 1; pairIndex > -1; pairIndex--) {
      if(pairs[pairIndex] == pair) {
        return true;
      }
    }
    return false;
  }

  static ArrayList<Polygon> generateExtensionsAndSuperposition(Polygon polygon) {
    timesCalled[4] += 1;
    //long startTime = System.currentTimeMillis();
    ArrayList<Polygon> extensions = new ArrayList<Polygon>();
    HashSet<Integer> usedPairs = new HashSet<Integer>(); // make sure we're not adding a new square to the same place twice
    for(int pairIndex = polygon.pairs.length - 1; pairIndex > -1; pairIndex--) {
      if(! pairInPolygon(polygon.pairs, polygon.pairs[pairIndex] / 2)) {
        if(! usedPairs.contains(polygon.pairs[pairIndex] / 2)) {
          int[] newPairs = new int[polygon.pairs.length + 1];
          for(int newPairIndex = newPairs.length - 1; newPairIndex > 0; newPairIndex--) {
            newPairs[newPairIndex] = polygon.pairs[newPairIndex - 1];
          }
          usedPairs.add(polygon.pairs[pairIndex] / 2);
          newPairs[0] = polygon.pairs[pairIndex] / 2;
          newPairs = superposition(newPairs);
          int newWidth = polygon.width;
          if(polygon.pairs[pairIndex] % 4 != 0) {
            newWidth++;
          }
          BigInteger invariate = invariate(newPairs, newWidth, polygon.height);
          extensions.add(new Polygon(newPairs, invariate, newWidth, polygon.height));
        }
      }
      if(! pairInPolygon(polygon.pairs, polygon.pairs[pairIndex] * 2)) {
        if(! usedPairs.contains(polygon.pairs[pairIndex] * 2)) {
          int[] newPairs = new int[polygon.pairs.length + 1];
          for(int newPairIndex = newPairs.length - 1; newPairIndex > 0; newPairIndex--) {
            newPairs[newPairIndex] = polygon.pairs[newPairIndex - 1];
          }
          usedPairs.add(polygon.pairs[pairIndex] * 2);
          newPairs[0] = polygon.pairs[pairIndex] * 2;
          newPairs = superposition(newPairs);
          int newWidth = polygon.width;
          if(polygon.pairs[pairIndex] % Math.pow(2, polygon.width) == 0) {
            newWidth++;
          }
          BigInteger invariate = invariate(newPairs, newWidth, polygon.height);
          extensions.add(new Polygon(newPairs, invariate, newWidth, polygon.height));
        }
      }
      if(! pairInPolygon(polygon.pairs, polygon.pairs[pairIndex] / 3)) {
        if(! usedPairs.contains(polygon.pairs[pairIndex] / 3)) {
          int[] newPairs = new int[polygon.pairs.length + 1];
          for(int newPairIndex = newPairs.length - 1; newPairIndex > 0; newPairIndex--) {
            newPairs[newPairIndex] = polygon.pairs[newPairIndex - 1];
          }
          usedPairs.add(polygon.pairs[pairIndex] / 3);
          newPairs[0] = polygon.pairs[pairIndex] / 3;
          newPairs = superposition(newPairs);
          int newHeight = polygon.height;
          if(polygon.pairs[pairIndex] % 9 != 0) {
            newHeight++;
          }
          BigInteger invariate = invariate(newPairs, polygon.width, newHeight);
          extensions.add(new Polygon(newPairs, invariate, polygon.width, newHeight));
        }
      }
      if(! pairInPolygon(polygon.pairs, polygon.pairs[pairIndex] * 3)) {
        if(! usedPairs.contains(polygon.pairs[pairIndex] * 3)) {
          int[] newPairs = new int[polygon.pairs.length + 1];
          for(int newPairIndex = newPairs.length - 1; newPairIndex > 0; newPairIndex--) {
            newPairs[newPairIndex] = polygon.pairs[newPairIndex - 1];
          }
          usedPairs.add(polygon.pairs[pairIndex] * 3);
          newPairs[0] = polygon.pairs[pairIndex] * 3;
          newPairs = superposition(newPairs);
          int newHeight = polygon.height;
          if(polygon.pairs[pairIndex] % Math.pow(3, polygon.height) == 0) {
            newHeight++;
          }
          BigInteger invariate = invariate(newPairs, polygon.width, newHeight);
          extensions.add(new Polygon(newPairs, invariate, polygon.width, newHeight));
        }
      }
    }
    //long endTime = System.currentTimeMillis();
    //totalTime[4] += endTime - startTime;
    return extensions;
  }

  static int[] superposition(int[] originalPairs) {
    timesCalled[5] += 1;
    //long startTime = System.currentTimeMillis();
    boolean tooLeft = false;
    boolean tooDown = false;
    for(int pairIndex = originalPairs.length - 1; pairIndex > -1; pairIndex--) {
      if(originalPairs[pairIndex] % 2 != 0) {
        tooLeft = true;
      }
      if(originalPairs[pairIndex] % 3 != 0) {
        tooDown = true;
      }
      if(tooLeft || tooDown) {
        break;
      }
    }

    if(tooLeft) {
      for(int pairIndex = originalPairs.length - 1; pairIndex > -1; pairIndex--) {
        originalPairs[pairIndex] *= 2;
      }
      return originalPairs;
    }

    if(tooDown) {
      for(int pairIndex = originalPairs.length - 1; pairIndex > -1; pairIndex--) {
        originalPairs[pairIndex] *= 3;
      }
      return originalPairs;
    }
    //long endTime = System.currentTimeMillis();
    //totalTime[5] += endTime - startTime;
    return originalPairs;
  }

  static BigInteger binarize(boolean[] polygon, int multipliedSize, int width, int height) {
    BigInteger result = BigInteger.valueOf(0);
    for(int index = multipliedSize - 1; index >= 0; index--) {
      if(polygon[index]) {
        result = result.setBit(index);
      }
    }
    // If our polygons were ever of size 32, we would need to shift by more than 4 bits.
    result = result.shiftLeft(4).add(BigInteger.valueOf(width)).shiftLeft(4).add(BigInteger.valueOf(height));
    return result;
  }

  static BigInteger invariate(int[] pairs, int width, int height) {
    int[] polygonSize = new int[] {width, height};
    int multipliedSize = width * height;

    BigInteger[] possibilities = new BigInteger[8];
    BigInteger base = listGenerator.listMaker(pairs, polygonSize, multipliedSize);
    possibilities[0] = base.shiftLeft(4).add(BigInteger.valueOf(width)).shiftLeft(4).add(BigInteger.valueOf(height));
    possibilities[1] = listGenerator.flip(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(width)).shiftLeft(4).add(BigInteger.valueOf(height));
    possibilities[2] = listGenerator.ninetyDegrees(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(height)).shiftLeft(4).add(BigInteger.valueOf(width));
    possibilities[3] = listGenerator.ninetyDegreesAndFlip(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(height)).shiftLeft(4).add(BigInteger.valueOf(width));
    possibilities[4] = listGenerator.hundredEighty(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(width)).shiftLeft(4).add(BigInteger.valueOf(height));
    possibilities[5] = listGenerator.hundredEightyAndFlip(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(width)).shiftLeft(4).add(BigInteger.valueOf(height));
    possibilities[6] = listGenerator.twoSeventy(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(height)).shiftLeft(4).add(BigInteger.valueOf(width));
    possibilities[7] = listGenerator.twoSeventyAndFlip(base, polygonSize, multipliedSize).shiftLeft(4).add(BigInteger.valueOf(height)).shiftLeft(4).add(BigInteger.valueOf(width));

    BigInteger result = possibilities[0];
    for(int possibilityIndex = possibilities.length - 1; possibilityIndex > 0; possibilityIndex--) {
      if(possibilities[possibilityIndex].compareTo(result) == -1) {
        result = possibilities[possibilityIndex];
      }
    }

    return result;
  }

  static ArrayList<Polygon> generateExtensionsFromList(ArrayList<Polygon> polygons) {
    timesCalled[18] += 1;
    //long startTime = System.currentTimeMillis();
    int extended = 0;
    ArrayList<Polygon> extensions = new ArrayList<Polygon>();
    HashSet<BigInteger> invariates = new HashSet<BigInteger>();
    int polygonArea = polygons.get(0).pairs.length;
    System.out.println("Starting to generate extensions of size " + Integer.toString(polygonArea + 1) + ".");
    for(Polygon polygon : polygons) {
      extended++;
      //polygons.remove(index);
      ArrayList<Polygon> newExtensions = generateExtensionsAndSuperposition(polygon);
      for(Polygon extension : newExtensions) {
        if(! invariates.contains(extension.invariate)) {
          extensions.add(extension);
          invariates.add(extension.invariate);
        }
      }
      if(extended % 3000 == 0) {
        System.out.println(extended);
      }
    }
    //long endTime = System.currentTimeMillis();
    //totalTime[18] += endTime - startTime;
    return extensions;
  }

  public static void writeObjectToFile(Object serObj) {
    try {
      FileOutputStream fileOut = new FileOutputStream("data/polygon");
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(serObj);
      objectOut.close();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public static Object readObjectFromFile(String filePath) {
    try {
      FileInputStream fileIn = new FileInputStream(filePath);
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      Object result = objectIn.readObject();
      objectIn.close();
      return result;
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) {
    int size = 16;

    ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    int[] monominoePairs = new int[] {6};
    Polygon monominoe = new Polygon(monominoePairs, invariate(monominoePairs, 1, 1), 1, 1);
    polygons.add(monominoe);

    long startTime = System.currentTimeMillis();
    for(int round = 0; round < size - 1; round++) {
      polygons = generateExtensionsFromList(polygons);
      System.gc();
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Total Time: " + Long.toString((endTime - startTime) / 1000) + " seconds.");
    System.out.println("Total Memory: " + Long.toString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes.");
    System.out.println("Result: " + Integer.toString(polygons.size()));


     // for(int index = 0; index < totalTime.length - 1; index++) {
     //   if(timesCalled[index] != 0) {
     //     System.out.println("Function " + long.toString(index) + " had an average time of " + long.toString(totalTime[index] / timesCalled[index]) + " milliseconds and a total time of " + long.toString(Math.round(totalTime[index] / 1000)) + " seconds.");
     //   }
     // }
  }
}
