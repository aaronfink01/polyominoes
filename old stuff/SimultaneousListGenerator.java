import java.util.*;
import java.math.*;

class SimultaneousListGenerator {
  SimultaneousListGenerator() {}

  static boolean pairInPolygon(int[] pairs, int pair) {
    for(int pairIndex = pairs.length - 1; pairIndex > -1; pairIndex--) {
      if(pairs[pairIndex] == pair) {
        return true;
      }
    }
    return false;
  }

  // Everthing is going to be counterclockwise!!!

  static BigInteger makeInvariate(int[] pairs, int width, int height) {
    BigInteger[] possibilities = {BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0), BigInteger.valueOf(0)};
    // for(int possibilityIndex = 0; possibilityIndex < 8; possibilityIndex++)
    //     possibilities[possibilityIndex] = BigInteger.valueOf(0);
    // }
    int threeUpperBound = height + 1;
    int twoUpperBound = width + 1;
    int value = 6;
    int finalTwoValue = (int)Math.pow(2, twoUpperBound - 1);
    for(int threePower = 1; threePower < threeUpperBound; threePower++) {
      for(int twoPower = 1; twoPower < twoUpperBound; twoPower++) {
        if(pairInPolygon(pairs, value)) {
          // If our polygons were ever of size 32, we would need to shift by more than 8 bits.
          possibilities[0] = possibilities[0].setBit(8 + twoPower - 1 + (threePower - 1) * width);
          possibilities[1] = possibilities[1].setBit(8 - threePower + twoPower * height);
          possibilities[2] = possibilities[2].setBit(8 + threePower - 1 + (width - twoPower) * height);
          possibilities[3] = possibilities[3].setBit(8 - twoPower + width * (height - threePower + 1));
          possibilities[4] = possibilities[4].setBit(8 - twoPower + threePower * width);
          possibilities[5] = possibilities[5].setBit(8 + twoPower - 1 + (height - threePower) * width);
          possibilities[6] = possibilities[6].setBit(8 + threePower - 1 + (twoPower - 1) * height);
          possibilities[7] = possibilities[7].setBit(8 - threePower + (width - twoPower + 1) * height);
        }
        value *= 2;
      }
      value *= 3;
      value /= finalTwoValue;
    }


    possibilities[0] = possibilities[0].add(BigInteger.valueOf(16 * width + height));
    possibilities[1] = possibilities[1].add(BigInteger.valueOf(16 * height + width));
    possibilities[2] = possibilities[2].add(BigInteger.valueOf(16 * height + width));
    possibilities[3] = possibilities[3].add(BigInteger.valueOf(16 * width + height));
    possibilities[4] = possibilities[4].add(BigInteger.valueOf(16 * width + height));
    possibilities[5] = possibilities[5].add(BigInteger.valueOf(16 * width + height));
    possibilities[6] = possibilities[6].add(BigInteger.valueOf(16 * height + width));
    possibilities[7] = possibilities[7].add(BigInteger.valueOf(16 * height + width));

    BigInteger result = possibilities[0];
    for(int possibilityIndex = possibilities.length - 1; possibilityIndex > 0; possibilityIndex--) {
      // System.out.println(possibilities[possibilityIndex]);
      if(possibilities[possibilityIndex].compareTo(result) == -1) {
        result = possibilities[possibilityIndex];
      }
    }

    return result;
  }
}
