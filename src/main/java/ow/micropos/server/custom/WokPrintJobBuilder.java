package ow.micropos.server.custom;

import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.print.printer.PrintJob;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WokPrintJobBuilder {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a");
    private static final SimpleDateFormat cookFormat = new SimpleDateFormat("hh:mm a");
    private static final Pattern tagPattern = Pattern.compile("([^\\d]*)(\\d+)");

    private final int width;
    private final EscPosBuilder builder;

    public WokPrintJobBuilder(int width) {
        this.width = width;
        this.builder = new EscPosBuilder();
    }

    // State should be one of : OPEN, REQUEST_OPEN, REQUEST_CLOSE, REQUEST_VOID
    public PrintJob order(SalesOrder curr, boolean requestsOnly, boolean hasPrev) {
        return new PrintJob(salesOrder(curr, requestsOnly, hasPrev), true);
    }

    /******************************************************************
     *                                                                *
     * Sales Orders
     *                                                                *
     ******************************************************************/

    private byte[] salesOrder(SalesOrder so, boolean requestsOnly, boolean hasPrev) {

        so.getProductEntries().sort(peComparator::compare);

        switch (so.getStatus()) {
            case REQUEST_OPEN:
                return _newSalesOrder(so, requestsOnly);
            case OPEN:
                return _changedSalesOrder(so, requestsOnly);
            case REQUEST_CLOSE:
                return _closedSalesOrder(so, requestsOnly, hasPrev);
            case REQUEST_VOID:
                return _voidSalesOrder(so);
            default:
                return new byte[0];
        }

    }

    // Prints a SalesOrder with no header information
    private byte[] _newSalesOrder(SalesOrder so, boolean requestsOnly) {
        return reset()
                .initialize()
                .align(EscPosBuilder.Align.CENTER)
                .font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                .optional(so.hasType(SalesOrderType.DINEIN), () -> text("Dine In "))
                .optional(so.hasType(SalesOrderType.TAKEOUT), () -> text("Take Out "))
                .text("Order #" + so.getId())
                .feed(2)
                .font(EscPosBuilder.Font.DWDH)
                .text(dateFormat.format(so.getDate()))
                .feed(2)
                .text("---------------------")
                .feed(1)
                .optional(so.getCookTime() != null,
                        () -> font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                                .text("Cook Time : " + cookFormat.format(so.getCookTime()))
                                .feed(1)
                                .font(EscPosBuilder.Font.DWDH)
                                .text("---------------------")
                                .feed(1)
                                .align(EscPosBuilder.Align.LEFT))
                .feed(1)
                .align(EscPosBuilder.Align.LEFT)
                .forEach(so.getProductEntries(), pe -> productEntry(pe, requestsOnly, false))
                .feed(5)
                .cut(EscPosBuilder.Cut.PART)
                .getBytes();
    }

    // Prints a changed SalesOrder with prefixes notifying changes
    private byte[] _changedSalesOrder(SalesOrder so, boolean requestsOnly) {
        return reset()
                .initialize()
                .align(EscPosBuilder.Align.CENTER)
                .font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                .text("CHANGED ORDER")
                .feed(2)
                .optional(so.hasType(SalesOrderType.DINEIN), () -> text("Dine In "))
                .optional(so.hasType(SalesOrderType.TAKEOUT), () -> text("Take Out "))
                .text("Order #" + so.getId())
                .feed(2)
                .font(EscPosBuilder.Font.DWDH)
                .text(dateFormat.format(so.getDate()))
                .feed(2)
                .text("---------------------")
                .feed(1)
                .optional(so.getCookTime() != null,
                        () -> font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                                .text("Cook Time : " + cookFormat.format(so.getCookTime()))
                                .feed(1)
                                .font(EscPosBuilder.Font.DWDH)
                                .text("---------------------")
                                .feed(1)
                                .align(EscPosBuilder.Align.LEFT))
                .feed(1)
                .align(EscPosBuilder.Align.LEFT)
                .forEach(so.getProductEntries(), pe -> productEntry(pe, requestsOnly, true))
                .feed(5)
                .cut(EscPosBuilder.Cut.PART)
                .getBytes();
    }

    // Prints a void SalesOrder with void prefix on all items
    private byte[] _voidSalesOrder(SalesOrder so) {
        return reset()
                .initialize()
                .align(EscPosBuilder.Align.CENTER)
                .font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                .text("VOID ORDER")
                .feed(2)
                .optional(so.hasType(SalesOrderType.DINEIN), () -> text("Dine In "))
                .optional(so.hasType(SalesOrderType.TAKEOUT), () -> text("Take Out "))
                .text("Order #" + so.getId())
                .feed(2)
                .font(EscPosBuilder.Font.DWDH)
                .text(dateFormat.format(so.getDate()))
                .feed(2)
                .text("---------------------")
                .feed(2)
                .align(EscPosBuilder.Align.LEFT)
                .forEach(so.getProductEntries(), this::forceVoidProductEntry)
                .feed(5)
                .cut(EscPosBuilder.Cut.PART)
                .getBytes();
    }

    // Prints a closing SalesOrder (notify change if already exists, otherwise print as new order)
    private byte[] _closedSalesOrder(SalesOrder so, boolean requestsOnly, boolean hasPrev) {
        return hasPrev ? _changedSalesOrder(so, requestsOnly) : _newSalesOrder(so, requestsOnly);
    }

    /******************************************************************
     *                                                                *
     * Product Entries
     *                                                                *
     ******************************************************************/

    private static final Comparator<String> tagComparator = (o1, o2) -> {
        Matcher matcher1 = tagPattern.matcher(o1);
        Matcher matcher2 = tagPattern.matcher(o2);

        if (matcher1.find() && matcher2.find()) {

            String o1Txt = matcher1.group(1);
            String o2Txt = matcher2.group(1);
            if (!o1Txt.equals(o2Txt)) {
                return o1Txt.compareTo(o2Txt);
            }

            String o1Val = matcher1.group(2);
            String o2Val = matcher2.group(2);
            try {
                return Integer.parseInt(o1Val) - Integer.parseInt(o2Val);
            } catch (NumberFormatException e) {
                return o1.compareTo(o2);
            }

        } else {
            return o1.compareTo(o2);
        }
    };

    private static Comparator<ProductEntry> peComparator = (o1, o2) -> {
        if (getRank(o1) != getRank(o2))
            return getRank(o1) - getRank(o2);
        else if (o1.getMenuItem().getWeight() != o2.getMenuItem().getWeight())
            return o1.getMenuItem().getWeight() - o2.getMenuItem().getWeight();
        else
            return tagComparator.compare(o1.getMenuItem().getTag(), o2.getMenuItem().getTag());
    };

    private static int getRank(ProductEntry pe) {
        switch (pe.getStatus()) {
            case REQUEST_VOID:
                return 1;
            case REQUEST_EDIT:
                return 2;
            case REQUEST_SENT:
                return 3;
            case SENT:
                return 4;
            default:
                return 5;
        }
    }

    // Adds void prefix to all ProductEntries, since status transition has not yet occurred
    private void forceVoidProductEntry(ProductEntry pe) {

        font(EscPosBuilder.Font.DWDH_EMPHASIZED);
        String mainLine = "*V ";

        mainLine += pe.getQuantity().setScale(0, BigDecimal.ROUND_FLOOR).toString();
        mainLine += " ";
        mainLine += pe.getMenuItem().getTag();
        mainLine += " ";
        mainLine += pe.getMenuItem().getName();

        text(mainLine.substring(0, Math.min(width, mainLine.length())))
                .feed(1)
                .forEach(pe.getModifiers(), mod -> {
                    String mainline = "      " + mod.getTag() + " " + mod.getName();
                    text(mainline.substring(0, Math.min(width, mainline.length()))).feed(1);
                });
    }

    @SuppressWarnings("ConstantConditions")
    private void productEntry(ProductEntry pe, boolean requestsOnly, boolean addedPrefix) {

        ProductEntryStatus status = pe.getStatus();
        String mainLine = "";

        if (status == ProductEntryStatus.SENT && !requestsOnly) {
            font(EscPosBuilder.Font.DWDH);
            mainLine += "   ";

        } else if (status == ProductEntryStatus.REQUEST_VOID) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*V ";

        } else if (status == ProductEntryStatus.REQUEST_EDIT) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*E ";

        } else if (status == ProductEntryStatus.REQUEST_SENT && addedPrefix) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*A ";

        } else if (status == ProductEntryStatus.REQUEST_SENT && !addedPrefix) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "   ";

        } else {
            return;

        }

        mainLine += pe.getQuantity().setScale(0, BigDecimal.ROUND_FLOOR).toString();
        mainLine += " ";
        mainLine += pe.getMenuItem().getTag();
        mainLine += " ";
        mainLine += pe.getMenuItem().getName();

        text(mainLine.substring(0, Math.min(width, mainLine.length())))
                .feed(1)
                .forEach(pe.getModifiers(), mod -> {
                    String mainline = "      " + mod.getTag() + " " + mod.getName();
                    text(mainline.substring(0, Math.min(width, mainline.length()))).feed(1);
                });
    }

    /******************************************************************
     *                                                                *
     * Convenience
     *                                                                *
     ******************************************************************/

    private interface Action {
        void invoke();
    }

    private interface Action1<T> {
        void invoke(T item);
    }

    private WokPrintJobBuilder optional(boolean condition, Action action) {
        if (condition)
            action.invoke();
        return this;
    }

    private WokPrintJobBuilder total(String desc, int total) {
        return total(desc, Integer.toString(total));
    }

    private WokPrintJobBuilder total(String desc, String total) {

        int dWidth = width - total.length() - 1;

        builder.text(String.format("%-" + dWidth + "." + dWidth + "s", desc))
                .text(" ")
                .text(total)
                .feed(1);

        return this;
    }

    private WokPrintJobBuilder text(String text) {
        builder.text(text);
        return this;
    }

    private WokPrintJobBuilder feed() {
        builder.feed(1);
        return this;
    }

    private WokPrintJobBuilder feed(int lines) {
        builder.feed(lines);
        return this;
    }

    private WokPrintJobBuilder font(EscPosBuilder.Font font) {
        builder.font(font);
        return this;
    }

    private WokPrintJobBuilder align(EscPosBuilder.Align align) {
        builder.align(align);
        return this;
    }

    private <T> WokPrintJobBuilder forEach(Collection<T> items, Action1<T> action) {
        items.forEach(action::invoke);
        return this;
    }

    private WokPrintJobBuilder reset() {
        builder.reset();
        return this;
    }

    private WokPrintJobBuilder initialize() {
        builder.initialize();
        return this;
    }

    private WokPrintJobBuilder cut(EscPosBuilder.Cut cut) {
        builder.cut(cut);
        return this;
    }

    private byte[] getBytes() {
        return builder.getBytes();
    }

}
