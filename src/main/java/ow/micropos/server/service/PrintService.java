package ow.micropos.server.service;

import email.com.gmail.ttsai0509.escpos.ESCPos;
import email.com.gmail.ttsai0509.escpos.PrintJob;
import email.com.gmail.ttsai0509.escpos.dispatcher.PrinterDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @Autowired ObjectViewMapper mapper;
    @Autowired MenuItemRepository miRepo;
    @Autowired PrinterDispatcher pd;

    // TODO : Fix hardcoded columns.
    private final MicroPOSCommander cmd = new MicroPOSCommander(21);

    @SuppressWarnings("Convert2streamapi")
    public boolean printOrder(SalesOrder o) {

        // Collect all relevant printers
        Set<String> uniquePrinters = new HashSet<>();
        for (ProductEntry pe : o.getProductEntries()) {

            // Only these requests get printed
            if (pe.hasStatus(ProductEntryStatus.REQUEST_EDIT)
                    || pe.hasStatus(ProductEntryStatus.REQUEST_SENT)
                    || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)) {

                // Menu Items need to be reattached to the context to determine printers.
                // Client-side sales order may not (should not) know about server printers.
                long id = pe.getMenuItem().getId();
                List<String> printers = miRepo.findOne(id).getPrinters();
                uniquePrinters.addAll(printers);
            }
        }

        // No new requests to print
        if (uniquePrinters.isEmpty())
            return false;

        // Print to each relevant printer
        for (String printer : uniquePrinters) {
            try {
                pd.requestPrint(new PrintJob(
                        printer,
                        cmd.begin().salesOrder(printer, o).end()
                ));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private class MicroPOSCommander {

        private final SimpleDateFormat date = new SimpleDateFormat("MM/dd/yy hh:mm a");

        private final int width;
        private final ByteArrayOutputStream out;
        private final ESCPos.Commander commander;

        public MicroPOSCommander(int width) {
            this.width = width;
            this.out = new ByteArrayOutputStream();
            this.commander = new ESCPos.Commander(out);
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

        public MicroPOSCommander begin() throws IOException {
            out.reset();
            return print(ESCPos.INITIALIZE);
        }

        public byte[] end() throws IOException {

            print(ESCPos.FEED_N)
                    .print(4)
                    .print(ESCPos.PART_CUT);

            byte[] result = out.toByteArray();
            out.reset();

            return result;
        }

        public MicroPOSCommander salesOrder(String printer, SalesOrder so) throws IOException {

            List<ProductEntry> toPrint = so.getProductEntries().stream()
                    .filter(pe -> {
                        MenuItem mi = miRepo.findOne(pe.getMenuItem().getId());
                        return mi.getPrinters().contains(printer) &&
                                (pe.hasStatus(ProductEntryStatus.REQUEST_SENT)
                                        || pe.hasStatus(ProductEntryStatus.REQUEST_EDIT)
                                        || pe.hasStatus(ProductEntryStatus.REQUEST_VOID));
                    })
                    .collect(Collectors.toList());

            if (!toPrint.isEmpty()) {
                print(ESCPos.ALIGN_CENTER)
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

            return print(ESCPos.FEED_N).print(2);

        }

        private MicroPOSCommander productEntry(ProductEntry pe) throws IOException {

            String mainLine = "";

            switch (pe.getStatus()) {
                case REQUEST_SENT:
                    mainLine += "   ";
                    break;
                case REQUEST_VOID:
                    mainLine += "*V ";
                    break;
                case REQUEST_EDIT:
                    mainLine += "*E ";
                    break;
                default:
                    log.warn("Unexpected Product Entry " + pe.toString());
                    return this;
            }

            mainLine += pe.getQuantity().setScale(0, BigDecimal.ROUND_FLOOR).toString();
            mainLine += " ";
            mainLine += pe.getMenuItem().getTag();
            mainLine += " ";
            mainLine += pe.getMenuItem().getName();

            print(mainLine.substring(0, Math.min(width, mainLine.length())))
                    .print(ESCPos.FEED);

            for (Modifier mod : pe.getModifiers())
                modifier(mod);

            return this;
        }

        private MicroPOSCommander modifier(Modifier mod) throws IOException {
            String mainline = "      " + mod.getTag() + " " + mod.getName();
            return print(mainline.substring(0, Math.min(width, mainline.length())))
                    .print(ESCPos.FEED);
        }

    }

}
