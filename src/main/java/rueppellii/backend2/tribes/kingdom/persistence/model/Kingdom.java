package rueppellii.backend2.tribes.kingdom.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Immutable;
import rueppellii.backend2.tribes.building.persistence.model.*;
import rueppellii.backend2.tribes.resource.presistence.model.Resource;
import rueppellii.backend2.tribes.progression.persistence.ProgressionModel;
import rueppellii.backend2.tribes.resource.presistence.model.Food;
import rueppellii.backend2.tribes.resource.presistence.model.Gold;
import rueppellii.backend2.tribes.troop.persistence.model.Troop;
import rueppellii.backend2.tribes.user.persistence.model.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "kingdoms")
public class Kingdom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @JsonBackReference
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "application_user_user_id")
    private ApplicationUser applicationUser;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "kingdom_troops", joinColumns = {
            @JoinColumn(name = "kingdom_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "troop_id", referencedColumnName = "id")})
    private List<Troop> kingdomsTroops;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "kingdom_resources", joinColumns = {
            @JoinColumn(name = "kingdom_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "resource_id", referencedColumnName = "id")})
    public ImmutableList<Resource> kingdomsResources;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "kingdom_buildings", joinColumns = {
            @JoinColumn(name = "kingdom_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "building_id", referencedColumnName = "id")})
    private List<Building> kingdomsBuildings;

    @JsonManagedReference
    @OneToMany(mappedBy = "progressKingdom", targetEntity = ProgressionModel.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProgressionModel> kingdomsProgresses;

    public Kingdom() {
        //TODO: this should be set by the KindomService/starterKit method
        TownHall townHall = new TownHall();
        Barracks barracks = new Barracks();
        Farm farm = new Farm();
        Mine mine = new Mine();
        Gold gold = new Gold();
        Food food = new Food();
        kingdomsResources = ImmutableList.of(food, gold);
        kingdomsBuildings = new ArrayList<>();
        kingdomsBuildings.add(townHall);
        kingdomsBuildings.add(barracks);
        kingdomsBuildings.add(farm);
        kingdomsBuildings.add(mine);

    }
}
