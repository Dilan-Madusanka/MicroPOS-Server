package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class ModifierGroup {

    @Id
    @GeneratedValue
    @JsonView(View.ModifierGroup.class)
    Long id;

    @JsonView(View.ModifierGroupAll.class)
    Date date;

    @JsonView(View.ModifierGroup.class)
    String name;

    @JsonView(View.ModifierGroup.class)
    String tag;

    @JsonView(View.ModifierGroupWithModifier.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modifierGroup")
    List<Modifier> modifiers;

}
