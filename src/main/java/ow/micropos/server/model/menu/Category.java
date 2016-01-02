package ow.micropos.server.model.menu;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue
    @JsonView(View.Category.class)
    Long id;

    @JsonView(View.CategoryAll.class)
    Date date;

    @JsonView(View.CategoryAll.class)
    boolean archived;

    @JsonView(View.CategoryAll.class)
    Date archiveDate;

    @JsonView(View.Category.class)
    int weight;

    @JsonView(View.Category.class)
    String name;

    @JsonView(View.Category.class)
    String tag;

    @JsonView(View.CategoryWithMenuItem.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    List<MenuItem> menuItems;

}