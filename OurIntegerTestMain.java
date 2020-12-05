import java.math.BigInteger;
import java.util.HashSet;

class OurIntegerTestMain {
    public static void main(String[] args) {
        OurInteger a = new OurInteger(0);
        BigInteger b = BigInteger.valueOf(0);
        long start = System.currentTimeMillis();
//        long b;
        for (int i = 0; i < 1000000000; i++) {
            a.testBit((i % 126));
//            b = (long) 1;
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            b.testBit((i % 126));
        }
        System.out.println(System.currentTimeMillis() - start);
    }
    
    static void printLong(long l) {
        StringBuilder s = new StringBuilder();
        for (int i = 63; i > -1; i--) {
            if ((l & ((long) 1 << i)) != 0) {
                s.append("1");
            } else {
                s.append("0");
            }
        }
        System.out.println(s);
    }
}
