package ow.micropos.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ow.micropos.server.exception.InternalServerErrorException;
import ow.micropos.server.model.menu.*;
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

        // Filter out archived menu items
        for (Category category : categories) {
            List<MenuItem> filtered = category.getMenuItems()
                    .stream()
                    .filter(menuItem -> !menuItem.isArchived())
                    .collect(Collectors.toList());
            category.setMenuItems(filtered);
        }

        List<ModifierGroup> modifierGroups = mgRepo.findAll();
        if (modifierGroups == null)
            throw new InternalServerErrorException("Error retrieving modifier groups.");

        // Filter out archived modifiers
        for (ModifierGroup modifierGroup : modifierGroups) {
            List<Modifier> filtered = modifierGroup.getModifiers()
                    .stream()
                    .filter(modifier -> !modifier.isArchived())
                    .collect(Collectors.toList());
            modifierGroup.setModifiers(filtered);
        }

        return new Menu(categories, modifierGroups);

    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {

        List<Category> categories = catRepo.findAll();
        if (categories == null)
            throw new InternalServerErrorException("Error retrieving categories.");

        // Filter out archived menu items
        for (Category category : categories) {
            List<MenuItem> filtered = category.getMenuItems()
                    .stream()
                    .filter(menuItem -> !menuItem.isArchived())
                    .collect(Collectors.toList());
            category.setMenuItems(filtered);
        }

        return categories;

    }

    @Transactional(readOnly = true)
    public List<ModifierGroup> getModifierGroups() {

        List<ModifierGroup> modifierGroups = mgRepo.findAll();
        if (modifierGroups == null)
            throw new InternalServerErrorException("Error retrieving modifier groups.");

        // Filter out archived modifiers
        for (ModifierGroup modifierGroup : modifierGroups) {
            List<Modifier> filtered = modifierGroup.getModifiers()
                    .stream()
                    .filter(modifier -> !modifier.isArchived())
                    .collect(Collectors.toList());
            modifierGroup.setModifiers(filtered);
        }

        return modifierGroups;

    }

}
