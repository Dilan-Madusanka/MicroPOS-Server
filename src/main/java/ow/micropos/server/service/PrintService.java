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
import java.util.stream.Collectors;

@Service
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    @Autowired ObjectViewMapper mapper;
    @Autowired MenuItemRepository miRepo;
    @Autowired PrinterDispatcher pd;
    @Autowired WokPrintJobBuilder builder;

    public List<String> getPrinters() {
        return new ArrayList<>(pd.getPrinters().keySet());
    }

    public List<String> getPrintersFor(ProductEntry pe) {
        long peId = pe.getMenuItem().getId();
        return miRepo.findOne(peId).getPrinters();
    }

    @SuppressWarnings("Convert2MethodRef")
    public boolean printOrder(SalesOrder curr, boolean hasPrev) {

        boolean printed = false;

        for (String printer : getPrinters()) {

            String notification;
            boolean suppressAddPrefix;

            // ! printers do not print TAKEOUT
            if (curr.hasType(SalesOrderType.TAKEOUT) && printer.contains("!")) {
                continue;

                // @ printers do not print DINEIN
            } else if (curr.hasType(SalesOrderType.DINEIN) && printer.contains("@")) {
                continue;

                // REQUEST_OPEN orders with previous instance are being reopened (can't be changed)
            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_OPEN) && hasPrev) {
                continue;

                // REQUEST_OPEN orders without previous instances are new orders
            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_OPEN) && !hasPrev) {
                notification = "NEW ORDER";
                suppressAddPrefix = true;

                // REQUEST_CLOSE orders without previous instances are new orders that are also being closed
            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_CLOSE) && !hasPrev) {
                notification = "NEW ORDER";
                suppressAddPrefix = true;

                // REQUEST_CLOSE orders with previous instances are closing orders with possible changes
            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_CLOSE) && hasPrev) {
                notification = "CHANGED ORDER";
                suppressAddPrefix = false;

                // OPEN orders indicate a previous order has changed
            } else if (curr.hasStatus(SalesOrderStatus.OPEN)) {
                notification = "CHANGED ORDER";
                suppressAddPrefix = false;

                // REQUEST_VOID orders means all items should be voided
            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_VOID)) {
                notification = "VOID ORDER";
                suppressAddPrefix = false;

                // Everything else is skipped
            } else {
                continue;

            }

            boolean printSent = printer.contains("#");
            List<ProductEntry> printEntries = curr.getProductEntries()
                    .stream()
                    .filter(pe -> getPrintersFor(pe).contains(printer))
                    .filter(pe -> pe.hasPrintableStatus())
                    .filter(pe -> !pe.hasStatus(ProductEntryStatus.SENT) || printSent)
                    .collect(Collectors.toList());

            if (printEntries.isEmpty())
                continue;

            SalesOrder printable = new SalesOrder();
            printable.setId(curr.getId());
            printable.setProductEntries(printEntries);
            printable.setCookTime(curr.getCookTime());
            printable.setCustomer(curr.getCustomer());
            printable.setEmployee(curr.getEmployee());
            printable.setSeat(curr.getSeat());
            printable.setType(curr.getType());
            printable.setStatus(curr.getStatus());
            printable.setGratuityPercent(curr.getGratuityPercent());
            printable.setTaxPercent(curr.getTaxPercent());
            printable.setDate(curr.getDate());
            pd.requestPrint(printer, builder.order(printable, notification, suppressAddPrefix));

            printed = true;

        }

        return printed;

    }

    @Deprecated
    // PrintJob builder printing empty tickets
    public boolean printOrder2(SalesOrder curr, boolean hasPrev) {

        boolean printed = false;

        for (String printer : getPrinters()) {

            if (!curr.hasStatuses(
                    SalesOrderStatus.OPEN, SalesOrderStatus.REQUEST_OPEN,
                    SalesOrderStatus.REQUEST_CLOSE, SalesOrderStatus.REQUEST_VOID))
                continue;

            List<ProductEntry> productEntries = new ArrayList<>();

            for (ProductEntry pe : curr.getProductEntries()) {
                long peId = pe.getMenuItem().getId();
                List<String> printers = miRepo.findOne(peId).getPrinters();
                if (printers.contains(printer))
                    productEntries.add(pe);
            }

            if (productEntries.isEmpty())
                continue;

            SalesOrder so = new SalesOrder();
            so.setId(curr.getId());
            so.setProductEntries(productEntries);
            so.setCookTime(curr.getCookTime());
            so.setCustomer(curr.getCustomer());
            so.setEmployee(curr.getEmployee());
            so.setSeat(curr.getSeat());
            so.setType(curr.getType());
            so.setStatus(curr.getStatus());
            so.setGratuityPercent(curr.getGratuityPercent());
            so.setTaxPercent(curr.getTaxPercent());
            so.setDate(curr.getDate());

            // (!) - Do not print TAKE OUT orders
            if (printer.contains("!") && curr.hasType(SalesOrderType.TAKEOUT))
                continue;

            // (@) - Do not print DINE IN orders
            if (printer.contains("@") && curr.hasType(SalesOrderType.DINEIN))
                continue;

            // (#) - Print entire order
            boolean requestsOnly = !printer.contains("#");
            pd.requestPrint(printer, builder.order(so, requestsOnly, hasPrev));

            printed = true;

        }

        return printed;

    }

    @Deprecated
    // PrintJob builder does not re-filter entries by printer.
    public boolean printOrder1(SalesOrder curr, boolean hasPrev) {

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

}
