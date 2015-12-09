package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.exception.ResourceNotFoundException;
import ow.micropos.server.model.enums.SalesOrderStatus;
import ow.micropos.server.model.orders.SalesOrder;
import ow.micropos.server.model.seating.Seat;
import ow.micropos.server.model.seating.Section;
import ow.micropos.server.repository.seating.SeatRepository;
import ow.micropos.server.repository.seating.SectionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/seating")
public class SeatingService {

    @Autowired SeatRepository seatRepo;
    @Autowired SectionRepository sectionRepository;

    @Transactional(readOnly = true)
    public List<Section> getSections(boolean filterArchived, boolean earliestOpen) {

        List<Section> sections = sectionRepository.findAll();

        if (sections == null)
            throw new InternalServerErrorException("Section repository error.");

        if (filterArchived)
            sections.stream().forEach(this::filterArchived);

        if (earliestOpen)
            sections.forEach(section -> section.getSeats().forEach(this::filterEarliestOpen));

        return sections;

    }

    @Transactional(readOnly = true)
    public Section getSection(long id, boolean filterArchived, boolean earliestOpen) {

        Section section = sectionRepository.getOne(id);

        if (section == null)
            throw new ResourceNotFoundException(id);

        if (filterArchived)
            filterArchived(section);

        if (earliestOpen)
            section.getSeats().forEach(this::filterEarliestOpen);

        return section;

    }

    /******************************************************************
     *                                                                *
     * Filtering Methods                                              *
     *                                                                *
     ******************************************************************/

    private void filterArchived(Section section) {
        section.setSeats(section.getSeats()
                .stream()
                .filter(seat -> !seat.isArchived())
                .collect(Collectors.toList()));
    }

    private void filterEarliestOpen(Seat seat) {
        SalesOrder earliest = seat.getSalesOrders()
                .stream()
                .filter(so -> so.getStatus() == SalesOrderStatus.OPEN)
                .min((o1, o2) -> o1.getDate().compareTo(o2.getDate()))
                .orElse(null);

        List<SalesOrder> earliestAsList = new ArrayList<>();
        if (earliest != null)
            earliestAsList.add(earliest);

        seat.setSalesOrders(earliestAsList);
    }


}
