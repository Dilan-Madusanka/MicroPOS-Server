package ow.micropos.server.service;

import email.com.gmail.ttsai0509.escpos.ESCPos;
import email.com.gmail.ttsai0509.escpos.PrinterDispatcher;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintService {

    @Autowired ObjectViewMapper mapper;
    @Autowired
    @Qualifier(value = "printerDispatcher")
    PrinterDispatcher pd;

    private static final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy hh:mm a");

    public boolean workOrder(SalesOrder o) {

        List<ProductEntry> workEntries = o.getProductEntries().stream()
                .filter(pe -> pe.hasStatus(ProductEntryStatus.REQUEST_SENT))
                .collect(Collectors.toList());

        ByteArrayOutputStream raw = new ByteArrayOutputStream();

        try {

            ESCPos.Commander commander = ESCPos.command(raw);
            commander.print(ESCPos.INITIALIZE);

            commander.print(ESCPos.ALIGN_CENTER)
                    .print(ESCPos.FONT_DWDH_EMPH)
                    .print("Order #" + o.getId())
                    .print(ESCPos.FEED_N)
                    .print(2)
                    .print(ESCPos.ALIGN_CENTER)
                    .print(ESCPos.FONT_DWDH)
                    .print(date.format(o.getDate()))
                    .print(ESCPos.FEED_N)
                    .print(2)
                    .print(ESCPos.ALIGN_CENTER)
                    .print(ESCPos.FONT_DWDH)
                    .print("---------------------")
                    .print(ESCPos.FEED_N)
                    .print(2);

            workEntries.stream()
                    .map(pe -> pe.getQuantity().setScale(0, BigDecimal.ROUND_UNNECESSARY).toString()
                            + " " + pe.getMenuItem().getTag()
                            + " " + pe.getMenuItem().getName())
                    .forEach(line -> {
                        try {
                            commander.print(ESCPos.ALIGN_LEFT)
                                    .print(ESCPos.FONT_DWDH_EMPH)
                                    .print(line)
                                    .print(ESCPos.FEED_N)
                                    .print(2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            commander.print(ESCPos.FEED_N)
                    .print(7)
                    .print(ESCPos.FULL_CUT);

            pd.print(raw.toByteArray());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } finally {
            IOUtils.closeQuietly(raw);
        }

    }

    private ESCPos.Commander header(ESCPos.Commander commander) throws IOException {

        return commander
                .print("ORIENTAL WOK")
                .print(ESCPos.FEED_N)
                .print(2)
                .print(ESCPos.FONT_REG)
                .print("6 NORTH BOLTON AVE.")
                .print(ESCPos.FEED)
                .print("ALEXANDRIA, LA 71301")
                .print(ESCPos.FEED)
                .print("(318) 448-8247")
                .print(ESCPos.FEED_N)
                .print(2);

    }

    private ESCPos.Commander footer(ESCPos.Commander commander) throws IOException {

        return commander
                .print(ESCPos.FEED_N)
                .print(2)
                .print(Long.toString(new Date().getTime()))
                .print(ESCPos.FEED_N)
                .print(2);

    }

}
