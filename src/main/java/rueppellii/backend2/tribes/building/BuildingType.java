package rueppellii.backend2.tribes.building;

public enum BuildingType {

    TOWNHALL {
        public Building buildBuilding() {
                return new TownHall();
        }
    }, FARM {
        public Building buildBuilding() {
            return new Farm();
        }
    }, MINE {
        public Building buildBuilding() {
            return new Mine();
        }
    }, BARRACKS {
        public Building buildBuilding() {
            return new Barracks();
        }
    };

    public Building buildBuilding() {
        return null;
    }
}
