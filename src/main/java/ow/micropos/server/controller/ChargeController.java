package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.charge.Charge;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.ChargeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/charges")
public class ChargeController {

    @Autowired AuthService authService;
    @Autowired ChargeService cService;

    @JsonView(value = View.Charge.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Charge> getCharges(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_CHARGES);

        return cService.getCharges();

    }

}
