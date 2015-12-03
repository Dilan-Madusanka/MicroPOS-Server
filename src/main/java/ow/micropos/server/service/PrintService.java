package ow.micropos.server.service;

import email.com.gmail.ttsai0509.escpos.ESCPos;
import email.com.gmail.ttsai0509.print.PrintJob;
import email.com.gmail.ttsai0509.print.PrinterDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.repository.menu.MenuItemRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrintService {

    @Autowired
    ObjectViewMapper mapper;

    @Autowired
    MenuItemRepository miRepo;

    @Autowired
    @Qualifier(value = "printerDispatcher")
    PrinterDispatcher pd;

    private static final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy hh:mm a");
    private final MicroPOSCommander cmd = new MicroPOSCommander();

    public boolean printOrder(SalesOrder o) {

        // Collect all relevant printers
        List<String> uniquePrinters = new ArrayList<>();
        for (ProductEntry pe : o.getProductEntries()) {

            MenuItem mi = miRepo.findOne(pe.getMenuItem().getId());
            List<String> printers = mi.getPrinters();
            for (String printer : printers) {
                if (!uniquePrinters.contains(printer))
                    uniquePrinters.add(printer);
            }
        }

        // Print the sales order to each one
        // (filtering occurs in commander)
        for (String printer : uniquePrinters) {
            try {
                cmd.reset()
                        .salesOrder(printer, o)
                        .print(ESCPos.ALIGN_CENTER)
                        .print(printer)
                        .print(ESCPos.FEED_N)
                        .print(5)
                        .print(ESCPos.PART_CUT);

                pd.requestPrint(new PrintJob(printer, cmd.out.toByteArray()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public class MicroPOSCommander {

        private final ByteArrayOutputStream out;
        private final ESCPos.Commander commander;

        public MicroPOSCommander() {
            this.out = new ByteArrayOutputStream();
            this.commander = new ESCPos.Commander(out);
        }

        public MicroPOSCommander reset() {
            out.reset();
            return this;
        }

        public MicroPOSCommander print(int val) throws IOException {
            commander.print(val);
            return this;
        }

        public MicroPOSCommander print(String text) throws IOException {
            commander.print(text);
            return this;
        }

        public MicroPOSCommander print(byte... cmd) throws IOException {
            commander.print(cmd);
            return this;
        }

        public MicroPOSCommander datetime() throws IOException {
            commander.datetime();
            return this;
        }

        public MicroPOSCommander date() throws IOException {
            commander.date();
            return this;
        }

        public MicroPOSCommander time() throws IOException {
            commander.time();
            return this;
        }

        public MicroPOSCommander header() throws IOException {
            commander
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
            return this;
        }

        public MicroPOSCommander footer() throws IOException {
            commander
                    .print(ESCPos.FEED_N)
                    .print(2)
                    .print(Long.toString(new Date().getTime()))
                    .print(ESCPos.FEED_N)
                    .print(2);
            return this;
        }

        public MicroPOSCommander salesOrder(String printer, SalesOrder so) throws IOException {

            List<ProductEntry> toPrint = so.getProductEntries().stream()
                    .filter(pe -> pe.hasStatus(ProductEntryStatus.REQUEST_SENT) &&
                                    miRepo.findOne(pe.getMenuItem().getId()).getPrinters().contains(printer)
                    )
                    .collect(Collectors.toList());

            if (!toPrint.isEmpty()) {
                print(ESCPos.INITIALIZE)
                        .print(ESCPos.ALIGN_CENTER)
                        .print(ESCPos.FONT_DWDH_EMPH)
                        .print("Order #" + so.getId())
                        .print(ESCPos.FEED_N)
                        .print(2)
                        .print(ESCPos.ALIGN_CENTER)
                        .print(ESCPos.FONT_DWDH)
                        .print(date.format(so.getDate()))
                        .print(ESCPos.FEED_N)
                        .print(2)
                        .print(ESCPos.ALIGN_CENTER)
                        .print(ESCPos.FONT_DWDH)
                        .print("---------------------")
                        .print(ESCPos.FEED_N)
                        .print(2)
                        .print(ESCPos.ALIGN_LEFT);

                for (ProductEntry pe : toPrint)
                    productEntry(pe);
            }

            return this;

        }

        private MicroPOSCommander productEntry(ProductEntry pe) throws IOException {
            print(pe.getQuantity().setScale(0, BigDecimal.ROUND_FLOOR).toString())
                    .print(" ")
                    .print(pe.getMenuItem().getTag())
                    .print(" ")
                    .print(pe.getMenuItem().getName())
                    .print(ESCPos.FEED);

            for (Modifier mod : pe.getModifiers())
                modifier(mod);

            return this;
        }

        private MicroPOSCommander modifier(Modifier mod) throws IOException {
            return print("   ").print(mod.getName());
        }

    }

}
