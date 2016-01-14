package ow.micropos.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.ObjectViewMapper;
import ow.micropos.server.exception.MicroPosException;
import ow.micropos.server.model.employee.Employee;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.ChargeEntry;
import ow.micropos.server.model.orders.PaymentEntry;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.repository.orders.ChargeEntryRepository;
import ow.micropos.server.repository.orders.PaymentEntryRepository;
import ow.micropos.server.repository.orders.ProductEntryRepository;
import ow.micropos.server.repository.orders.SalesOrderRepository;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SplitService {

    private static final Logger log = LoggerFactory.getLogger(SplitService.class);

    @Autowired ObjectViewMapper mapper;
    @Autowired SalesOrderRepository soRepo;
    @Autowired PaymentEntryRepository payRepo;
    @Autowired ProductEntryRepository prodRepo;
    @Autowired ChargeEntryRepository chargeRepo;
    @Autowired AuthService authService;
    @Autowired SalesOrderService soService;

    @Transactional(readOnly = false)
    public List<Long> splitOrder(Employee employee, List<SalesOrder> currOrders) {

        // Collect previous sales orders
        List<SalesOrder> prevOrders = currOrders.stream()
                .filter(currOrder -> currOrder.getId() != null)
                .map(currOrder -> soRepo.findOne(currOrder.getId()))
                .filter(currOrder -> currOrder != null)
                .collect(Collectors.toList());

        // Validation - SalesOrders and ProductEntries should be sent as OPEN.
        validateOrders(prevOrders, currOrders);

        // Persistence
        currOrders.forEach(soService::saveSalesOrder);

        // NO Transition - New SalesOrders & ProductEntries should already be OPEN.

        return currOrders.stream().map(SalesOrder::getId).collect(Collectors.toList());

    }

    /******************************************************************
     *                                                                *
     *
     *                                                                *
     ******************************************************************/

    // TODO : Validation is not entirely implemented server-side. Client-side validation handled by UI flow.
    private void validateOrders(List<SalesOrder> prevOrders, List<SalesOrder> currOrders) {

        // Only work with OPEN orders.
        if (prevOrders.stream().anyMatch(prevSO -> !prevSO.hasStatus(SalesOrderStatus.OPEN)))
            throw new MicroPosException("All split sales orders should already be open.");
        if (currOrders.stream().anyMatch(currSO -> !currSO.hasStatus(SalesOrderStatus.OPEN)))
            throw new MicroPosException("All split sales orders should already be open.");

        // All previously existing orders should be accounted for and unchanged.
        currOrders.stream()
                .filter(currSO -> currSO.getId() != null)
                .forEach(currSO -> {
                    List<SalesOrder> prevSOs = prevOrders
                            .stream()
                            .filter(prevSO -> Objects.equals(prevSO.getId(), currSO.getId()))
                            .collect(Collectors.toList());

                    if (prevSOs.isEmpty() || prevSOs.size() > 1)
                        throw new MicroPosException("Expected to find a single previous order " + currSO.getId());

                    // TODO : We should technically also check that the payment/charge entries have not changed.
                    if (_hasChanged(prevSOs.get(0), currSO))
                        throw new MicroPosException("Only product entries may change.");
                });

        // Product Entry Validation
        for (SalesOrder prevSO : prevOrders) {
            for (ProductEntry prevPE : prevSO.getProductEntries()) {
                // TODO : To be implemented.
            }
        }
    }

    private boolean _hasChanged(
            @NotNull SalesOrder prevSO,
            @NotNull SalesOrder currSO
    ) {
        ;
        Long[] prevPayIds = prevSO.getPaymentEntries().stream().map(PaymentEntry::getId).sorted().toArray(Long[]::new);
        Long[] prevChargeIds = prevSO.getChargeEntries().stream().map(ChargeEntry::getId).sorted().toArray(Long[]::new);
        Long[] currPayIds = currSO.getPaymentEntries().stream().map(PaymentEntry::getId).sorted().toArray(Long[]::new);
        Long[] currChargeIds = currSO.getChargeEntries().stream().map(ChargeEntry::getId).sorted().toArray(Long[]::new);

        return prevSO.getType() != currSO.getType()
                || !Objects.equals(prevSO.getId(), currSO.getId())
                || prevSO.getTaxPercent().compareTo(currSO.getTaxPercent()) != 0
                || prevSO.getGratuityPercent().compareTo(currSO.getGratuityPercent()) != 0
                || !Arrays.equals(prevPayIds, currPayIds)
                || !Arrays.equals(prevChargeIds, currChargeIds);
    }

}
