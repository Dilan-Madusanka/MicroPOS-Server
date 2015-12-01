package ow.micropos.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ow.micropos.server.model.Permission;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.Menu;
import ow.micropos.server.model.View;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.service.AuthService;
import ow.micropos.server.service.MenuService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Autowired MenuService menuService;
    @Autowired AuthService authService;

    @JsonView(value = View.Menu.class)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Menu getMenu(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return menuService.getMenu();

    }

    @JsonView(value = View.CategoryWithMenuItem.class)
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getCategories(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return menuService.getCategories();

    }

    @JsonView(value = View.ModifierGroupWithModifier.class)
    @RequestMapping(value = "/modifierGroups", method = RequestMethod.GET)
    public List<ModifierGroup> getModifierGroups(
            HttpServletRequest request
    ) {

        authService.authorize(request, Permission.GET_MENU);

        return menuService.getModifierGroups();

    }


}
