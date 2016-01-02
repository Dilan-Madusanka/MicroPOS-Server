package email.com.gmail.ttsai0509.math;

import java.math.BigDecimal;

public final class BigDecimalUtils {

    private BigDecimalUtils() {}

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    public static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public static BigDecimal asQuantity(BigDecimal bd) {
        if (isIntValue(bd))
            return bd.setScale(0, BigDecimal.ROUND_UNNECESSARY);
        else
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal asDollars(BigDecimal bd) {
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal asPercent(BigDecimal decimal) {
        return asQuantity(decimal.multiply(HUNDRED));
    }

    private static boolean isIntValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }

}
