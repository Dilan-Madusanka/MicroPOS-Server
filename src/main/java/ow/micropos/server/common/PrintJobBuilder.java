package ow.micropos.server.common;

import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.print.printer.PrintJob;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;

public class PrintJobBuilder {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a");
    private static final SimpleDateFormat cookFormat = new SimpleDateFormat("hh:mm a");

    private final int width;
    private final EscPosBuilder builder;

    public PrintJobBuilder(int width) {
        this.width = width;
        this.builder = new EscPosBuilder();
    }

    public PrintJob order(SalesOrder curr, String notification, boolean suppressAddPrefix, boolean voidOverride) {
        byte[] data = reset()
                .initialize()
                .align(EscPosBuilder.Align.CENTER)
                .font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                .text(notification)
                .feed(2)
                .optional(curr.hasType(SalesOrderType.DINEIN), () -> text("Dine In "))
                .optional(curr.hasType(SalesOrderType.TAKEOUT), () -> text("Take Out "))
                .text("#" + curr.getId())
                .feed(2)
                .font(EscPosBuilder.Font.DWDH)
                .text(dateFormat.format(curr.getDate()))
                .feed(2)
                .text("---------------------")
                .feed(1)
                .optional(curr.getCookTime() != null, () -> cookTime(curr))
                .feed(1)
                .align(EscPosBuilder.Align.LEFT)
                .forEach(curr.getProductEntries(), pe -> productEntry(pe, suppressAddPrefix, voidOverride))
                .feed(5)
                .cut(EscPosBuilder.Cut.PART)
                .getBytes();

        return new PrintJob(data, true);

    }

    @SuppressWarnings("ConstantConditions")
    private void productEntry(ProductEntry pe, boolean suppressAddPrefix, boolean voidOverride) {

        String mainLine = "";

        if (pe.hasPrintableStatus() && voidOverride) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*V ";

        } else if (pe.hasStatus(ProductEntryStatus.SENT)) {
            font(EscPosBuilder.Font.DWDH);
            mainLine += "   ";

        } else if (pe.hasStatus(ProductEntryStatus.REQUEST_VOID)) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*V ";

        } else if (pe.hasStatus(ProductEntryStatus.REQUEST_EDIT)) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*C ";

        } else if (pe.hasStatus(ProductEntryStatus.REQUEST_SENT) && suppressAddPrefix) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "   ";

        } else if (pe.hasStatus(ProductEntryStatus.REQUEST_SENT) && !suppressAddPrefix) {
            font(EscPosBuilder.Font.DWDH_EMPHASIZED);
            mainLine += "*A ";

        } else {
            // Everything else should have already been filtered out
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

    private void cookTime(SalesOrder order) {
        font(EscPosBuilder.Font.DWDH_EMPHASIZED)
                .text("Cook Time : " + cookFormat.format(order.getCookTime()))
                .feed(1)
                .font(EscPosBuilder.Font.DWDH)
                .text("---------------------")
                .feed(1)
                .align(EscPosBuilder.Align.LEFT);
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

    private PrintJobBuilder optional(boolean condition, Action action) {
        if (condition)
            action.invoke();
        return this;
    }

    private PrintJobBuilder total(String desc, int total) {
        return total(desc, Integer.toString(total));
    }

    private PrintJobBuilder total(String desc, String total) {

        int dWidth = width - total.length() - 1;

        builder.text(String.format("%-" + dWidth + "." + dWidth + "s", desc))
                .text(" ")
                .text(total)
                .feed(1);

        return this;
    }

    private PrintJobBuilder text(String text) {
        builder.text(text);
        return this;
    }

    private PrintJobBuilder feed() {
        builder.feed(1);
        return this;
    }

    private PrintJobBuilder feed(int lines) {
        builder.feed(lines);
        return this;
    }

    private PrintJobBuilder font(EscPosBuilder.Font font) {
        builder.font(font);
        return this;
    }

    private PrintJobBuilder align(EscPosBuilder.Align align) {
        builder.align(align);
        return this;
    }

    private <T> PrintJobBuilder forEach(Collection<T> items, Action1<T> action) {
        items.forEach(action::invoke);
        return this;
    }

    private PrintJobBuilder reset() {
        builder.reset();
        return this;
    }

    private PrintJobBuilder initialize() {
        builder.initialize();
        return this;
    }

    private PrintJobBuilder cut(EscPosBuilder.Cut cut) {
        builder.cut(cut);
        return this;
    }

    private byte[] getBytes() {
        return builder.getBytes();
    }

}
