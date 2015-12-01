package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.seating.Section;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.SeatingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/seating")
public class SeatingController {

    @Autowired AuthService authService;
    @Autowired SeatingService sService;

    @JsonView(value = View.SectionWithSalesOrder.class)
    @RequestMapping(value = "/sections", method = RequestMethod.GET)
    public List<Section> getSections(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_SECTIONS);

        return sService.getSections(true, true);

    }

    @JsonView(value = View.SectionWithSalesOrder.class)
    @RequestMapping(value = "/sections/{id}", method = RequestMethod.GET)
    public Section getSection(
            HttpServletRequest request,
            @PathVariable(value = "id") long id
    ) {

        authService.authorize(request, Permission.GET_SECTIONS);

        return sService.getSection(id, true, true);

    }


}
