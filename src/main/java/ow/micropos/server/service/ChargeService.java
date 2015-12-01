package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.model.charge.Charge;
import ow.micropos.server.repository.charge.ChargeRepository;

import java.util.List;

@Service
public class ChargeService {

    @Autowired ChargeRepository cRepo;

    @Transactional(readOnly = true)
    public List<Charge> getCharges() {

        List<Charge> charges = cRepo.findByArchived(false);

        if (charges == null)
            throw new InternalServerErrorException("Error retrieving charges.");

        return charges;

    }

}
