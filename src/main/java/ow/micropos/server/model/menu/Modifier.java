package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.enums.ModifierType;
import ow.micropos.server.model.orders.ProductEntry;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Modifier {

    @Id
    @GeneratedValue
    @JsonView(View.Modifier.class)
    Long id;

    @JsonView(View.ModifierAll.class)
    Date date;

    @JsonView(View.ModifierAll.class)
    boolean archived;

    @JsonView(View.ModifierAll.class)
    Date archiveDate;

    @JsonView(View.Modifier.class)
    String name;

    @JsonView(View.Modifier.class)
    String tag;

    @JsonView(View.Modifier.class)
    BigDecimal price;

    @Enumerated(EnumType.ORDINAL)
    @JsonView(View.Modifier.class)
    ModifierType type;

    @JsonView(View.ModifierWithModifierGroup.class)
    @ManyToOne(fetch = FetchType.LAZY)
    ModifierGroup modifierGroup;

    @JsonView(View.ModifierWithProductEntry.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modifiers")
    List<ProductEntry> productEntries;

}