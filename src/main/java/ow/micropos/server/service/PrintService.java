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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);
    private static final Pattern tagPattern = Pattern.compile("([^\\d]*)(\\d+)");

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

            String notification;        // Ticket Header
            boolean suppressAddPrefix;  // Hide *A prefix on new orders
            boolean voidOverride;       // Force *V prefix on all entries

            if (curr.hasType(SalesOrderType.TAKEOUT) && printer.contains("!")) {
                // ! printers do not print TAKEOUT
                continue;

            } else if (curr.hasType(SalesOrderType.DINEIN) && printer.contains("@")) {
                // @ printers do not print DINEIN
                continue;

            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_OPEN) && hasPrev) {
                // REQUEST_OPEN orders with previous instance are being reopened (can't be changed)
                continue;

            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_OPEN) && !hasPrev) {
                // REQUEST_OPEN orders without previous instances are new orders
                notification = "NEW ORDER";
                suppressAddPrefix = true;
                voidOverride = false;

            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_CLOSE) && !hasPrev) {
                // REQUEST_CLOSE orders without previous instances are new orders that are also being closed
                notification = "NEW ORDER";
                suppressAddPrefix = true;
                voidOverride = false;

            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_CLOSE) && hasPrev) {
                // REQUEST_CLOSE orders with previous instances are closing orders with possible changes
                notification = "CHANGED ORDER";
                suppressAddPrefix = false;
                voidOverride = false;

                /* >>> HACK - prevent printing unchanged closed orders to @#tg */
                boolean hasPrintableRequest = false;
                for (ProductEntry pe : curr.getProductEntries())
                    if (pe.hasPrintableStatus() && !pe.hasStatus(ProductEntryStatus.SENT))
                        hasPrintableRequest = true;
                if (!hasPrintableRequest)
                    continue;
                // <<< HACK - prevent printing unchanged closed orders to @#tg */

            } else if (curr.hasStatus(SalesOrderStatus.OPEN)) {
                // OPEN orders indicate a previous order has changed
                notification = "CHANGED ORDER";
                suppressAddPrefix = false;
                voidOverride = false;

            } else if (curr.hasStatus(SalesOrderStatus.REQUEST_VOID)) {
                // REQUEST_VOID orders means all items should be voided
                notification = "VOID ORDER";
                suppressAddPrefix = false;
                voidOverride = true;

            } else {
                // Everything else is skipped
                continue;

            }

            boolean printSent = printer.contains("#") || voidOverride;
            List<ProductEntry> printEntries = curr.getProductEntries()
                    .stream()
                    .filter(pe -> getPrintersFor(pe).contains(printer))
                    .filter(pe -> pe.hasPrintableStatus())
                    .filter(pe -> !pe.hasStatus(ProductEntryStatus.SENT) || printSent)
                    .sorted(peComparator)
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
            pd.requestPrint(printer, builder.order(printable, notification, suppressAddPrefix, voidOverride));

            printed = true;

        }

        return printed;

    }

    /**
     * ***************************************************************
     * *
     * Product Entry Sorting
     * *
     * ****************************************************************
     */

    // ProductEntry rank moves changes to the top of the ticket.
    private int getRank(ProductEntry pe) {
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

    // Order tags alphabetically by prefix, then numerically by suffix.
    // If invalid format, order lexicographically.
    // A1 < A5 < A10 < B1 < B5 < B10 < C1 < CA1 < CZ1
    private final Comparator<String> tagComparator = (o1, o2) -> {
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

    // ProductEntries are sorted by Rank, MenuItem Weight, then MenuItem Tag
    private Comparator<ProductEntry> peComparator = (o1, o2) -> {
        if (getRank(o1) != getRank(o2))
            return getRank(o1) - getRank(o2);
        else if (o1.getMenuItem().getWeight() != o2.getMenuItem().getWeight())
            return o1.getMenuItem().getWeight() - o2.getMenuItem().getWeight();
        else
            return tagComparator.compare(o1.getMenuItem().getTag(), o2.getMenuItem().getTag());
    };


}
