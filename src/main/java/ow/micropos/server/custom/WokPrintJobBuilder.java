package ow.micropos.server.custom;

import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.print.printer.PrintJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class WokPrintJobBuilder {

    private static final Logger log = LoggerFactory.getLogger(WokPrintJobBuilder.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy hh:mm a");

    private final int width;
    private final EscPosBuilder builder;

    public WokPrintJobBuilder(int width) {
        this.width = width;
        this.builder = new EscPosBuilder();
    }

    public PrintJob order(SalesOrder so, boolean changesOnly) {
        return new PrintJob(salesOrder(so, changesOnly), true);
    }


    /******************************************************************
     *                                                                *
     * Sales Orders
     *                                                                *
     ******************************************************************/

    private byte[] salesOrder(SalesOrder so, boolean changesOnly) {

        builder.reset()
                .initialize()
                .align(EscPosBuilder.Align.CENTER)
                .font(EscPosBuilder.Font.DWDH_EMPHASIZED);


        if (so.hasStatus(SalesOrderStatus.REQUEST_VOID)) {
            // Notify void order
            builder.text("VOID ORDER").feed(2);

        } else if (!so.hasStatus(SalesOrderStatus.REQUEST_OPEN)) {
            // Notify changed order if it has already been opened
            builder.text("CHANGED ORDER").feed(2);
        }

        builder.text("Order #" + so.getId())
                .feed(2)
                .font(EscPosBuilder.Font.DWDH)
                .text(dateFormat.format(so.getDate()))
                .feed(2)
                .text("---------------------")
                .feed(2)
                .align(EscPosBuilder.Align.LEFT);

        if (so.hasStatus(SalesOrderStatus.REQUEST_VOID)) {
            // Print entire ticket if REQUEST_VOID
            so.getProductEntries().forEach(pe -> productEntry(pe, false));
        } else {
            so.getProductEntries().forEach(pe -> productEntry(pe, changesOnly));
        }

        return builder.feed(5).cut(EscPosBuilder.Cut.PART).getBytes();

    }

    private void productEntry(ProductEntry pe, boolean changesOnly) {

        String mainLine = "";

        switch (pe.getStatus()) {
            case SENT:
                if (changesOnly) {
                    // Sent items have not been changed
                    return;
                } else {
                    builder.font(EscPosBuilder.Font.DWDH);
                    mainLine += "   ";
                    break;
                }

            case HOLD:
                // Held items do not get printed.
                return;

            case VOID:
                // Void items do not get printed.
                return;

            case REQUEST_SENT:
                builder.font(EscPosBuilder.Font.DWDH_EMPHASIZED);
                mainLine += "   ";
                break;

            case REQUEST_VOID:
                builder.font(EscPosBuilder.Font.DWDH_EMPHASIZED);
                mainLine += "*V ";
                break;

            case REQUEST_EDIT:
                builder.font(EscPosBuilder.Font.DWDH_EMPHASIZED);
                mainLine += "*E ";
                break;

            case REQUEST_HOLD:
                // Hold requests do not get printed.
                return;

            case REQUEST_HOLD_VOID:
                // Held void requests do not get printed.
                return;

            default:
                log.warn("Unexpected Product Entry " + pe.toString());
                return;
        }

        mainLine += pe.getQuantity().setScale(0, BigDecimal.ROUND_FLOOR).toString();
        mainLine += " ";
        mainLine += pe.getMenuItem().getTag();
        mainLine += " ";
        mainLine += pe.getMenuItem().getName();

        builder.text(mainLine.substring(0, Math.min(width, mainLine.length())))
                .feed(1);

        pe.getModifiers().forEach(mod -> {
            String mainline = "      " + mod.getTag() + " " + mod.getName();
            builder.text(mainline.substring(0, Math.min(width, mainline.length())))
                    .feed(1);
        });

    }

}
