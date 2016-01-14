package ow.micropos.server.service;

import email.com.gmail.ttsai0509.print.dispatcher.PrinterDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.custom.WokPrintJobBuilder;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.repository.menu.MenuItemRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @Autowired ObjectViewMapper mapper;
    @Autowired MenuItemRepository miRepo;
    @Autowired PrinterDispatcher pd;
    @Autowired WokPrintJobBuilder builder;

    public boolean printOrder(SalesOrder curr) {

        // Collect all relevant printers
        Set<String> uniquePrinters = new HashSet<>();

        if (curr.hasStatus(SalesOrderStatus.REQUEST_VOID)) {
            // Void requests must be sent to all printers.

            for (ProductEntry pe : curr.getProductEntries()) {
                long id = pe.getMenuItem().getId();
                List<String> printers = miRepo.findOne(id).getPrinters();
                uniquePrinters.addAll(printers);
            }

        } else if (curr.hasStatus(SalesOrderStatus.VOID)) {
            // Skip void sales orders

        } else if (curr.hasStatus(SalesOrderStatus.CLOSED)) {
            // Skip closed sales orders

        } else {
            // Print to relevant printers for all others

            for (ProductEntry pe : curr.getProductEntries()) {
                if (pe.hasStatus(ProductEntryStatus.REQUEST_SENT)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_VOID)
                        || pe.hasStatus(ProductEntryStatus.REQUEST_EDIT)) {

                    // Menu Items need to be reattached to the context to determine printers.
                    long id = pe.getMenuItem().getId();
                    List<String> printers = miRepo.findOne(id).getPrinters();
                    uniquePrinters.addAll(printers);
                }
            }
        }

        // No new requests to print
        if (uniquePrinters.isEmpty())
            return false;

        // Print to each relevant printer
        for (String printer : uniquePrinters) {
            boolean printChanges = printer.charAt(0) == '@';
            pd.requestPrint(printer, builder.order(curr, printChanges));
        }

        return true;
    }

}
