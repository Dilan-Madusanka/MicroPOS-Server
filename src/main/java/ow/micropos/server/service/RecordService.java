package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.model.records.SalesOrderRecord;
import ow.micropos.server.repository.records.SalesOrderRecordRepository;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    @Autowired SalesOrderRecordRepository sorRepo;

    @Transactional(readOnly = true)
    public List<SalesOrderRecord> findSalesOrderRecords() {
        return sorRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<SalesOrderRecord> findSalesOrderRecords(@Nonnull Date start, @Nonnull Date end) {
        return sorRepo.findByDateBetween(start, end);
    }

}
