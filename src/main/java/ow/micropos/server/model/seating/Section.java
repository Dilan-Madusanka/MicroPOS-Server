package ow.micropos.server.model.seating;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ow.micropos.server.model.View;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Section {

    @Id
    @GeneratedValue
    @JsonView(View.Section.class)
    Long id;

    @JsonView(View.SectionAll.class)
    Date date;

    @JsonView(View.Section.class)
    String name;

    @JsonView(View.Section.class)
    String tag;

    @JsonView(View.Section.class)
    byte rows;

    @JsonView(View.Section.class)
    byte cols;

    @JsonView(View.SectionWithSeat.class)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "section")
    List<Seat> seats;

}