package ow.micropos.server.model.reports;


import java.math.BigDecimal;

public class Summary {

    private static final BigDecimal ZERO_DOLLARS = new BigDecimal("0.00");

    public int takeOutCount = 0;
    public int dineInCount = 0;
    public int productCount = 0;
    public int chargeCount = 0;
    public int gratuityCount = 0;
    public int paymentCount = 0;
    public BigDecimal productTotal = ZERO_DOLLARS;
    public BigDecimal chargeTotal = ZERO_DOLLARS;
    public BigDecimal subTotal = ZERO_DOLLARS;
    public BigDecimal taxTotal = ZERO_DOLLARS;
    public BigDecimal gratuityTotal = ZERO_DOLLARS;
    public BigDecimal grandTotal = ZERO_DOLLARS;
    public BigDecimal paymentTotal = ZERO_DOLLARS;

}
