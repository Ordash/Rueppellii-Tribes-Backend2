package rueppellii.backend2.tribes.building.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import rueppellii.backend2.tribes.building.utility.BuildingType;
import rueppellii.backend2.tribes.kingdom.persistence.model.Kingdom;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Building_Type")
@Table(name = "buildings")
public abstract class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    @Enumerated(EnumType.STRING)
    private BuildingType type;

    private Integer level;
    private Integer HP;
    private Timestamp started_at;
    private Timestamp finished_at;

    @JsonBackReference
    @ManyToOne
    @JoinTable(name = "kingdom_buildings", joinColumns = {
            @JoinColumn(name = "building_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "kingdom_id", referencedColumnName = "id")})
    private Kingdom buildingsKingdom;

    public Building() {
        this.level = 1;
        this.started_at = new Timestamp(System.currentTimeMillis());
        this.finished_at = new Timestamp(getStarted_at().getTime() + 5L);
    }
}