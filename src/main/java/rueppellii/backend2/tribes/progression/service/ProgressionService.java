package rueppellii.backend2.tribes.progression.service;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import rueppellii.backend2.tribes.building.service.BuildingService;
import rueppellii.backend2.tribes.building.utility.BuildingType;
import rueppellii.backend2.tribes.gameUtility.timeService.TimeServiceImpl;
import rueppellii.backend2.tribes.kingdom.persistence.model.Kingdom;
import rueppellii.backend2.tribes.building.exception.BuildingNotFoundException;
import rueppellii.backend2.tribes.kingdom.service.KingdomService;
import rueppellii.backend2.tribes.progression.exception.InvalidProgressionRequestException;
import rueppellii.backend2.tribes.progression.persistence.ProgressionModel;
import rueppellii.backend2.tribes.progression.persistence.ProgressionModelRepository;
import rueppellii.backend2.tribes.progression.util.ProgressionDTO;
import rueppellii.backend2.tribes.troop.exception.TroopNotFoundException;
import rueppellii.backend2.tribes.troop.service.TroopService;

import java.util.List;

import static rueppellii.backend2.tribes.progression.util.ProgressionFactory.makeProgressionModel;

@Service
public class ProgressionService {

    private ProgressionModelRepository progressionModelRepository;
    private BuildingService buildingService;
    private TroopService troopService;
    private TimeServiceImpl timeService;
    private KingdomService kingdomService;

    @Autowired
    public ProgressionService(ProgressionModelRepository progressionModelRepository, BuildingService buildingService, TroopService troopService, TimeServiceImpl timeService, KingdomService kingdomService) {
        this.progressionModelRepository = progressionModelRepository;
        this.buildingService = buildingService;
        this.troopService = troopService;
        this.timeService = timeService;
        this.kingdomService = kingdomService;
    }

    public List<ProgressionModel> findAllByKingdom(Kingdom kingdom) {
        return progressionModelRepository.findAllByProgressKingdom(kingdom);
    }

    public void refreshProgression(Kingdom kingdom) throws TroopNotFoundException, BuildingNotFoundException {
        List<ProgressionModel> progressions = findAllByKingdom(kingdom);
        for (ProgressionModel p : progressions) {
            if (timeService.timeIsUp(p)) {
                progress(p, kingdom);
            }
        }
    }

    public void progress(ProgressionModel progressionModel, Kingdom kingdom) throws TroopNotFoundException, BuildingNotFoundException {
        if (progressionModel.getGameObjectId() == null) {
            if (progressionModel.getType().equals("TROOP")) {
                troopService.createTroop(kingdom);
                progressionModelRepository.deleteById(progressionModel.getId());
                return;
            }
            buildingService.createBuilding(progressionModel, kingdom);
            progressionModelRepository.deleteById(progressionModel.getId());
            return;
        }
        if (progressionModel.getType() != null) {
            troopService.upgradeTroop(progressionModel);
            progressionModelRepository.deleteById(progressionModel.getId());
            return;
        }
        buildingService.upgradeBuilding(progressionModel);
        progressionModelRepository.deleteById(progressionModel.getId());
    }

    public void validateProgressionRequest(BindingResult bindingResult, ProgressionDTO progressionDTO) throws InvalidProgressionRequestException {
        if (bindingResult.hasErrors() || progressionDTO.getType() == null) {
            throw new InvalidProgressionRequestException("Missing parameter: type");
        } else if (!EnumUtils.isValidEnum(BuildingType.class, progressionDTO.getType())) {
            throw new InvalidProgressionRequestException("Wrong type!");
        }
    }

    public void generateBuildingCreationModel(Kingdom kingdom, ProgressionDTO progressionDTO) {
        ProgressionModel progressionModel = makeProgressionModel();
        progressionModel.setType(progressionDTO.getType());
        progressionModel.setTimeToProgress(timeService.calculateTimeOfBuildingCreation(kingdom));
        kingdom.getKingdomsProgresses().add(progressionModel);
        kingdomService.save(kingdom);
    }

    public void generateBuildingUpgradeModel(Kingdom kingdom, Long id) {
        ProgressionModel progressionModel = makeProgressionModel();
        progressionModel.setGameObjectId(id);
        progressionModel.setTimeToProgress(timeService.calculateTimeOfBuildingUpgrade(kingdom));
        kingdom.getKingdomsProgresses().add(progressionModel);
        kingdomService.save(kingdom);
    }

    public void generateTroopCreationModel(Kingdom kingdom) {
        ProgressionModel progressionModel = makeProgressionModel();
        progressionModel.setType("TROOP");
        progressionModel.setTimeToProgress(timeService.calculateTimeOfTroopCreation(kingdom));
        kingdom.getKingdomsProgresses().add(progressionModel);
        kingdomService.save(kingdom);
    }

    public void generateTroopUpgradeModel(Kingdom kingdom, Long id) {
        ProgressionModel progressionModel = makeProgressionModel();
        progressionModel.setGameObjectId(id);
        progressionModel.setType("TROOP");
        progressionModel.setTimeToProgress(timeService.calculateTimeOfTroopUpgrade(kingdom));
        kingdom.getKingdomsProgresses().add(progressionModel);
        kingdomService.save(kingdom);
    }
}
