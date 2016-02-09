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
import ow.micropos.server.model.enums.SalesOrderType;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.repository.menu.MenuItemRepository;

import java.util.ArrayList;
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

    public boolean printOrder2(SalesOrder curr, boolean hasPrev) {

        boolean printed = false;

        for (String printer : getPrinters()) {

            SalesOrder order = prepareForPrinter(curr, printer);

            // Order does not satisfy conditions to print to printer
            if (order == null)
                continue;

            // (!) - Do not print TAKE OUT orders
            if (printer.contains("!") && curr.hasType(SalesOrderType.TAKEOUT))
                continue;

            // (@) - Do not print DINE IN orders
            if (printer.contains("@") && curr.hasType(SalesOrderType.DINEIN))
                continue;

            // (#) - Print entire order
            boolean requestsOnly = !printer.contains("#");
            pd.requestPrint(printer, builder.order(order, requestsOnly, hasPrev));

            printed = true;

        }

        return printed;

    }

    @Deprecated
    // PrintJob builder does not re-filter entries by printer.
    public boolean printOrder(SalesOrder curr, boolean hasPrev) {

        // Collect all relevant printers
        Set<String> uniquePrinters = new HashSet<>();

        switch (curr.getStatus()) {

            // Void requests must be sent to all printers.
            case REQUEST_VOID:
                for (ProductEntry pe : curr.getProductEntries()) {
                    long id = pe.getMenuItem().getId();
                    List<String> printers = miRepo.findOne(id).getPrinters();
                    uniquePrinters.addAll(printers);
                }
                break;

            // Print to relevant printers for all others
            case OPEN:
            case REQUEST_OPEN:
            case REQUEST_CLOSE:
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
                break;


            case VOID:
            case CLOSED:
            default:
                // Skip other statuses.

        }

        // No new requests to print
        if (uniquePrinters.isEmpty())
            return false;

        // Print to each relevant printer
        for (String printer : uniquePrinters) {

            // (!) - Do not print TAKE OUT orders
            if (printer.contains("!") && curr.hasType(SalesOrderType.TAKEOUT))
                continue;

            // (@) - Do not print DINE IN orders
            if (printer.contains("@") && curr.hasType(SalesOrderType.DINEIN))
                continue;

            // (#) - Print entire order
            boolean requestsOnly = !printer.contains("#");
            pd.requestPrint(printer, builder.order(curr, requestsOnly, hasPrev));
        }

        return true;
    }

    public List<String> getPrinters() {
        return new ArrayList<>(pd.getPrinters().keySet());
    }

    private SalesOrder prepareForPrinter(SalesOrder order, String printer) {

        if (!order.hasStatuses(
                SalesOrderStatus.OPEN, SalesOrderStatus.REQUEST_OPEN,
                SalesOrderStatus.REQUEST_CLOSE, SalesOrderStatus.REQUEST_VOID))
            return null;

        List<ProductEntry> productEntries = new ArrayList<>();

        for (ProductEntry pe : order.getProductEntries()) {
            long peId = pe.getMenuItem().getId();
            List<String> printers = miRepo.findOne(peId).getPrinters();
            if (printers.contains(printer))
                productEntries.add(pe);
        }

        if (productEntries.isEmpty())
            return null;

        SalesOrder so = new SalesOrder();
        so.setId(order.getId());
        so.setProductEntries(productEntries);
        so.setCookTime(order.getCookTime());
        so.setCustomer(order.getCustomer());
        so.setEmployee(order.getEmployee());
        so.setSeat(order.getSeat());
        so.setType(order.getType());
        so.setStatus(order.getStatus());
        so.setGratuityPercent(order.getGratuityPercent());
        so.setTaxPercent(order.getTaxPercent());
        so.setDate(order.getDate());

        return so;


    }

}
