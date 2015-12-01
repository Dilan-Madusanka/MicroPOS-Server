package email.com.gmail.ttsai0509.utils;

import java.math.BigDecimal;
import java.util.Random;

public final class Rdm {

    private static final Random random = new Random();

    public static final String A = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String N = "0123456789";
    public static final String AN = A + N;
    public static final String S = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~'";
    public static final String ALL = AN + S + " ";

    private Rdm() {
        throw new RuntimeException(this.getClass().getSimpleName() + " is a utility class.");
    }

    public static BigDecimal aBigDecimal(Number min, Number max, Number scale) {
        BigDecimal bdMin = new BigDecimal(min.toString());
        BigDecimal bdMax = new BigDecimal(max.toString());
        int intScale = scale.intValue();
        BigDecimal bdRand = bdMin.add(new BigDecimal(random.nextDouble()).multiply(bdMax.subtract(bdMin)));
        return bdRand.setScale(intScale, BigDecimal.ROUND_HALF_UP);
    }

    public static int anInteger(Number min, Number max) {
        int intMin = min.intValue();
        int intMax = max.intValue();
        return intMin + random.nextInt(intMax - intMin);
    }

    public static int anInteger() {
        return anInteger(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static byte aByte(Number min, Number max) {
        byte byteMin = min.byteValue();
        byte byteMax = max.byteValue();
        return (byte) (byteMin + random.nextInt(byteMax - byteMin));
    }

    public static byte aByte() {
        return aByte(Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public static Boolean aBoolean() {
        return random.nextBoolean();
    }

    public static String aString(String charSet, Number length) {
        charSet = (charSet == null || charSet.length() == 0) ? ALL : charSet;
        StringBuilder sb = new StringBuilder();
        int intLength = length.intValue();
        for (int i = 0; i < intLength; i++)
            sb.append(charSet.charAt(random.nextInt(charSet.length())));
        return sb.toString();
    }

    public static Character aCharacter(String charSet) {
        charSet = (charSet == null || charSet.length() == 0) ? ALL : charSet;
        return charSet.charAt(random.nextInt(charSet.length()));
    }

    public static String alpha(Number length) {
        return aString(A, length);
    }

    public static String numeric(Number length) {
        return aString(N, length);
    }

    public static String alphanumeric(Number length) {
        return aString(AN, length);
    }

    public static String symbol(Number length) {
        return aString(S, length);
    }

    public static String all(Number length) {
        return aString(ALL, length);
    }

}
