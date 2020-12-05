import java.util.*;
import java.math.*;

class NewNewListGenerator {
  NewNewListGenerator() {}

  static boolean pairInPolygon(int[] pairs, int pair) {
    for(int pairIndex = pairs.length - 1; pairIndex > -1; pairIndex--) {
      if(pairs[pairIndex] == pair) {
        return true;
      }
    }
    return false;
  }

  // Everthing is going to be counterclockwise!!!

  static BigInteger listMaker(int[] pairs, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    int threeUpperBound = polygonSize[1] + 1;
    int twoUpperBound = polygonSize[0] + 1;
    int value = 6;
    int finalTwoValue = (int)Math.pow(2, twoUpperBound - 1);
    for(int threePower = 1; threePower < threeUpperBound; threePower++) {
      for(int twoPower = 1; twoPower < twoUpperBound; twoPower++) {
        if(pairInPolygon(pairs, value)) {
          result = result.setBit((twoPower - 1) + (threePower - 1) * polygonSize[0]);
        }
        value *= 2;
      }
      value *= 3;
      value /= finalTwoValue;
    }
    return result;
  }

  static boolean[] nextBits(BigInteger polygon, int[] polygonSize, int x, int y, boolean[] candidates) {
    boolean[] results = new boolean[8];
    results[0] = ninetyDegrees(polygon, polygonSize, x, y, candidates[0]);
    results[1] = ninetyDegreesAndFlip(polygon, polygonSize, x, y, candidates[0]);
    results[2] = hundredEighty(polygon, polygonSize, x, y, candidates[0]);
    results[3] = hundredEightyAndFlip(polygon, polygonSize, x, y, candidates[0]);
    results[4] = twoSeventy(polygon, polygonSize, x, y, candidates[0]);
    results[5] = twoSeventyAndFlip(polygon, polygonSize, x, y, candidates[0]);
    results[6] = flip(polygon, polygonSize, x, y, candidates[0]);
    results[7] = doNothing(polygon, polygonSize, x, y, candidates[0]);
    return results;
  }

  static boolean ninetyDegrees(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit(x + (polygonSize[1] - y - 1) * polygonSize[0]);
  }

  static boolean ninetyDegreesAndFlip(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit(x + y * polygonSize[0]);
  }

  static boolean hundredEighty(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit((polygonSize[0] - x) + (polygonSize[1] - y) * polygonSize[0]);
  }

  static boolean hundredEightyAndFlip(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit(x + (polygonSize[1] - y - 1) * polygonSize[0]);
  }

  static boolean twoSeventy(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit((polygonSize[0] - x - 1) + y * polygonSize[0]);
  }

  static boolean twoSeventyAndFlip(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit((polygonSize[0] - x - 1) + (polygonSize[1] - y - 1) * polygonSize[0]);
  }

  static boolean flip(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit((polygonSize[0] - x - 1) + y * polygonSize[0]);
  }

  static boolean doNothing(BigInteger polygon, int[] polygonSize, int x, int y, boolean candidate) {
    if(! candidate) {
      return false;
    }
    return polygon.testBit(x + y * polygonSize[0]);
  }
}
