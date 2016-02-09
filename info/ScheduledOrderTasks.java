package ow.micropos.server.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.auth.Position;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.enums.ProductEntryStatus;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.service.OrderService;
import ow.micropos.server.service.SalesOrderService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledOrderTasks {

    @Autowired OrderService orderService;
    @Autowired SalesOrderService soService;

    private final Employee scheduler = new Employee();

    {
        // Order Service requires employee for validation.
        Position pos = new Position();
        pos.setPermissions(Arrays.asList(Permission.CREATE_PRODUCT_ENTRY));
        scheduler.setPositions(Arrays.asList(pos));
    }

    @Scheduled(fixedRate = 60000)
    @Transactional(readOnly = false)
    public void sendHeldProductEntries() {

        System.out.println("Checked Held Product Entries.");

        List<SalesOrder> orders = soService.findSalesOrders(null, null);
        if (orders != null && !orders.isEmpty()) {

            Date date = new Date();

            orders.stream()
                    .filter(so -> hasExpiredHeldOrders(date, so))
                    .forEach(so -> orderService.order(scheduler, so));
        }

    }

    private boolean hasExpiredHeldOrders(Date now, SalesOrder so) {
        boolean hasHeld = false;
        for (ProductEntry pe : so.getProductEntries()) {
            if (pe.hasStatus(ProductEntryStatus.HOLD) && now.after(pe.getCookTime())) {
                pe.setStatus(ProductEntryStatus.REQUEST_SENT);
                hasHeld = true;
            }
        }
        return hasHeld;
    }

}
