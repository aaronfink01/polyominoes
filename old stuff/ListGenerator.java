import java.util.*;

class ListGenerator {
  ListGenerator() {}

  static boolean pairInPolygon(int[] pairs, int pair) {
    for(int pairIndex = pairs.length - 1; pairIndex > -1; pairIndex--) {
      if(pairs[pairIndex] == pair) {
        return true;
      }
    }
    return false;
  }

  // Everthing is going to be counterclockwise!!!

  static boolean[] listMaker(int[] pairs, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    int threeUpperBound = polygonSize[1] + 1;
    int twoUpperBound = polygonSize[0] + 1;
    int value = 6;
    int finalTwoValue = (int)Math.pow(2, twoUpperBound - 1);
    for(int threePower = 1; threePower < threeUpperBound; threePower++) {
      for(int twoPower = 1; twoPower < twoUpperBound; twoPower++) {
        if(pairInPolygon(pairs, value)) {
          result[(twoPower - 1) + (threePower - 1) * polygonSize[0]] = true;
        } else {
          result[(twoPower - 1) + (threePower - 1) * polygonSize[0]] = false;
        }
        value *= 2;
      }
      value *= 3;
      value /= finalTwoValue;
    }
    return result;
  }

  static boolean[] ninetyDegrees(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        result[y + x * polygonSize[1]] = polygon[x + (polygonSize[1] - y - 1) * polygonSize[0]];
      }
    }
    return result;
  }

  static boolean[] ninetyDegreesAndFlip(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        result[y + x * polygonSize[1]] = polygon[x + y * polygonSize[0]];
      }
    }
    return result;
  }

  static boolean[] hundredEighty(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int index = 0; index < multipliedSize; index++) {
      result[index] = polygon[multipliedSize - index - 1];
    }
    return result;
  }

  static boolean[] hundredEightyAndFlip(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int y = 0; y < polygonSize[1]; y++) {
      for(int x = 0; x < polygonSize[0]; x++) {
        result[x + y * polygonSize[0]] = polygon[x + (polygonSize[1] - y - 1) * polygonSize[0]];
      }
    }
    return result;
  }

  static boolean[] twoSeventy(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        result[y + (polygonSize[0] - x - 1) * polygonSize[1]] = polygon[x + y * polygonSize[0]];
      }
    }
    return result;
  }

  static boolean[] twoSeventyAndFlip(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        result[(polygonSize[1] - y - 1) + (polygonSize[0] - x - 1) * polygonSize[1]] = polygon[x + y * polygonSize[0]];
      }
    }
    return result;
  }

  static boolean[] flip(boolean[] polygon, int[] polygonSize, int multipliedSize) {
    boolean[] result = new boolean[multipliedSize];
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        result[(polygonSize[0] - x - 1) + y * polygonSize[0]] = polygon[x + y * polygonSize[0]];
      }
    }
    return result;
  }
}
