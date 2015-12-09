package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.model.menu.Category;
import ow.micropos.server.model.menu.Menu;
import ow.micropos.server.model.menu.Modifier;
import ow.micropos.server.model.menu.ModifierGroup;
import ow.micropos.server.repository.menu.CategoryRepository;
import ow.micropos.server.repository.menu.ModifierGroupRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired CategoryRepository catRepo;
    @Autowired ModifierGroupRepository mgRepo;

    @Transactional(readOnly = true)
    public Menu getMenu() {

        List<Category> categories = catRepo.findAll();
        if (categories == null)
            throw new InternalServerErrorException("Error retrieving categories.");

        categories.forEach(this::filterArchivedMenuItems);

        List<ModifierGroup> modifierGroups = mgRepo.findAll();
        if (modifierGroups == null)
            throw new InternalServerErrorException("Error retrieving modifier groups.");

        modifierGroups.forEach(this::filterArchivedModifiers);

        return new Menu(categories, modifierGroups);

    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {

        List<Category> categories = catRepo.findAll();
        if (categories == null)
            throw new InternalServerErrorException("Error retrieving categories.");

        categories.forEach(this::filterArchivedMenuItems);

        return categories;

    }

    @Transactional(readOnly = true)
    public List<ModifierGroup> getModifierGroups() {

        List<ModifierGroup> modifierGroups = mgRepo.findAll();
        if (modifierGroups == null)
            throw new InternalServerErrorException("Error retrieving modifier groups.");

        modifierGroups.forEach(this::filterArchivedModifiers);

        return modifierGroups;

    }

    /******************************************************************
     *                                                                *
     * Filtering Methods
     *                                                                *
     ******************************************************************/

    private void filterArchivedModifiers(ModifierGroup group) {
        group.setModifiers(
                group.getModifiers()
                        .stream()
                        .filter(modifier -> !modifier.isArchived())
                        .collect(Collectors.toList())
        );
    }

    private void filterArchivedMenuItems(Category category) {
        category.setMenuItems(
                category.getMenuItems()
                        .stream()
                        .filter(menuItem -> !menuItem.isArchived())
                        .collect(Collectors.toList())
        );
    }

}
