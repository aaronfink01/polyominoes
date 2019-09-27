import java.util.*;
import java.math.*;

class NewListGenerator {
  NewListGenerator() {}

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

  static BigInteger ninetyDegrees(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        if(polygon.testBit(x + (polygonSize[1] - y - 1) * polygonSize[0])) {
          result = result.setBit(y + x * polygonSize[1]);
        }
      }
    }
    return result;
  }

  static BigInteger ninetyDegreesAndFlip(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        if(polygon.testBit(x + y * polygonSize[0])) {
          result = result.setBit(y + x * polygonSize[1]);
        }
      }
    }
    return result;
  }

  static BigInteger hundredEighty(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int index = 0; index < multipliedSize; index++) {
      if(polygon.testBit(multipliedSize - index - 1)) {
        result = result.setBit(index);
      }
    }
    return result;
  }

  static BigInteger hundredEightyAndFlip(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int y = 0; y < polygonSize[1]; y++) {
      for(int x = 0; x < polygonSize[0]; x++) {
        if(polygon.testBit(x + (polygonSize[1] - y - 1) * polygonSize[0])) {
          result = result.setBit(x + y * polygonSize[0]);
        }
      }
    }
    return result;
  }

  static BigInteger twoSeventy(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        if(polygon.testBit(x + y * polygonSize[0])) {
          result = result.setBit(y + (polygonSize[0] - x - 1) * polygonSize[1]);
        }
      }
    }
    return result;
  }

  static BigInteger twoSeventyAndFlip(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        if(polygon.testBit(x + y * polygonSize[0])) {
          result = result.setBit((polygonSize[1] - y - 1) + (polygonSize[0] - x - 1) * polygonSize[1]);
        }
      }
    }
    return result;
  }

  static BigInteger flip(BigInteger polygon, int[] polygonSize, int multipliedSize) {
    BigInteger result = BigInteger.valueOf(0);
    for(int x = 0; x < polygonSize[0]; x++) {
      for(int y = 0; y < polygonSize[1]; y++) {
        if(polygon.testBit(x + y * polygonSize[0])) {
          result = result.setBit((polygonSize[0] - x - 1) + y * polygonSize[0]);
        }
      }
    }
    return result;
  }
}
