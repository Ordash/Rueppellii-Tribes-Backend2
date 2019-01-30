package rueppellii.backend2.tribes.building;

public class TownHall extends Building {

    public TownHall() {
        super(BuildingType.TOWNHALL);
    }

    @Override
    void buildBuilding() {
        setLevel(1);
        setHP(10);
    }
}
