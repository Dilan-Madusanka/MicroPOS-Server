package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.View;
import ow.micropos.server.model.menu.MenuItem;
import ow.micropos.server.repository.menu.MenuItemRepository;
import ow.micropos.server.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/database")
public class DatabaseController {

    @Autowired AuthService authService;
    @Autowired MenuItemRepository miRepo;

    @JsonView(value = View.MenuItemEdit.class)
    @RequestMapping(value = "/menuItems", method = RequestMethod.GET)
    public List<MenuItem> getMenuItems(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return miRepo.findAll();

    }

}
