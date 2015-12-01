package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;

import java.util.List;

@Data
public class Menu {

    public Menu() {}

    public Menu(List<Category> categories, List<ModifierGroup> modifierGroups) {
        this.categories = categories;
        this.modifierGroups = modifierGroups;
    }

    @JsonView(View.Menu.class)
    List<Category> categories;

    @JsonView(View.Menu.class)
    List<ModifierGroup> modifierGroups;

}
