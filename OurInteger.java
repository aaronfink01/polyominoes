import java.lang.Math;
import java.math.BigInteger;

class OurInteger {
    long smallerPlace, largerPlace;
    static long one = 1;
    static long _2to63 = (1l << 63);
    
    public OurInteger(long val) {
        largerPlace = 0;
        smallerPlace = val;
    }
    
    public OurInteger(long s, long l) {
        smallerPlace = s;
        largerPlace = l;
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof OurInteger)) {
            return false;
        }
        return smallerPlace == ((OurInteger) o).smallerPlace && largerPlace == ((OurInteger) o).largerPlace;
    }
    
    public int hashCode() {
        return (int) (smallerPlace + 31 * largerPlace);
    }
    
    public int intValue() {
        return (int) smallerPlace;
    }
    
//    public BigInteger bigIntegerValue() {
//        return new BigInteger(Long.toString(smallerPlace)).add(new BigInteger(Long.toString(largerPlace)).shiftLeft(64));
//    }
    
    public boolean testBit(int place) {
        if (place < 64) {
            return (smallerPlace & (one << place)) != 0l;
        } else {
            return (largerPlace & (one << place)) != 0l; // place was place - 64 but this doesn't matter
        }
    }
    
    public OurInteger setBit(int place) {
        // We originally checked testBit first, but that doesn't seem necessary
        if (place < 64) {
            return new OurInteger(smallerPlace | (one << place), largerPlace);
        } else {
            return new OurInteger(smallerPlace, largerPlace | (one << (place - 64)));
        }
    }
    
    public int compareTo(OurInteger other) {
        if (greater(largerPlace, other.largerPlace)) {
            return 1;
        }
        if (greater(other.largerPlace, largerPlace)) {
            return -1;
        }
        if (greater(smallerPlace, other.smallerPlace)) {
            return 1;
        }
        if (greater(other.smallerPlace, smallerPlace)) {
            return -1;
        }
        return 0;
    }
    
    public static boolean greater(long a, long b) {
        if ((a & _2to63) == (b & _2to63)) {
            return a > b;
        }
        return (a & _2to63) != 0;
    }
    
    public OurInteger shiftRight(int places) {
        if (places < 64) {
            long newLargerPlace = largerPlace >>> places;
            long newSmallerPlace = (smallerPlace >>> places) | (largerPlace % (one << places)) << (64 - places); //eh
            return new OurInteger(newSmallerPlace, newLargerPlace);
        }
        long newLargerPlace = 0;
        long newSmallerPlace = largerPlace >>> (places - 64);
        return new OurInteger(newSmallerPlace, newLargerPlace);
    }
    
    public OurInteger shiftLeft(int places) {
        if (places == 0) {
            return new OurInteger(smallerPlace, largerPlace);
        }
        if (places < 64) {
            long newSmallerPlace = smallerPlace << places;
            long newLargerPlace = (largerPlace << places) + ((smallerPlace & ~((one << (64 - places)) - 1)) >>> (64 - places));
            return new OurInteger(newSmallerPlace, newLargerPlace);
        }
        long newSmallerPlace = 0;
        long newLargerPlace = smallerPlace << (places - 64);
        return new OurInteger(newSmallerPlace, newLargerPlace);
    }
    
    public OurInteger mod(int twoPower) {
        if (twoPower < 63) {
            return new OurInteger(smallerPlace & ((one << twoPower) - 1), 0);
        }
        if (twoPower > 63 && twoPower < 128) {
            long newLargerPlace = largerPlace & ((one << (twoPower - 64)) - 1);
            return new OurInteger(smallerPlace, newLargerPlace);
        }
        if (twoPower > 127) {
            return new OurInteger(smallerPlace, largerPlace);
        }
        return new OurInteger(smallerPlace & ~_2to63, 0);
    }
    
    public OurInteger add(OurInteger other) {
        long newSmallerPlace = 0;
        long newLargerPlace = largerPlace + other.largerPlace;
        if ((!testBit(63)) && (!other.testBit(63))) {
            newSmallerPlace = smallerPlace + other.smallerPlace;
        } else if (testBit(63) && other.testBit(63)) {
            newLargerPlace += 1;
            newSmallerPlace = (smallerPlace ^ _2to63) + (other.smallerPlace ^ _2to63);
        } else if (testBit(63)) {
            newSmallerPlace = (smallerPlace ^ _2to63) + other.smallerPlace;
            if ((newSmallerPlace & _2to63) != 0) {
                newSmallerPlace = newSmallerPlace ^ _2to63;
                newLargerPlace += 1;
            } else {
                newSmallerPlace = newSmallerPlace | _2to63;
            }
        } else {
            newSmallerPlace = smallerPlace + (other.smallerPlace ^ _2to63);
            if ((newSmallerPlace & _2to63) != 0) {
                newSmallerPlace = newSmallerPlace ^ _2to63;
                newLargerPlace += 1;
            } else {
                newSmallerPlace = newSmallerPlace | _2to63;
            }
        }
        return new OurInteger(newSmallerPlace, newLargerPlace);
    }
    
    public OurInteger subtract(OurInteger other) {
        OurInteger flippedOther = new OurInteger(~other.smallerPlace, ~other.largerPlace);
        return add(flippedOther).add(new OurInteger(1));
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 127; i > -1; i--) {
            if (testBit(i)) {
                s.append("1");
            } else {
                s.append("0");
            }
        }
        return s.toString();
    }
    
//    public OurInteger add(OurInteger other) {
//        long newLargerPlace = largerPlace + other.largerPlace;
//
//        long newSmallerPlace = smallerPlace + other.smallerPlace;
//        if (newSmallerPlace < 0) {
//            newSmallerPlace =
//        }
//    }
}
