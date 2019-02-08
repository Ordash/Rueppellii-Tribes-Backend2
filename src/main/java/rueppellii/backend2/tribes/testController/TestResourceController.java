package rueppellii.backend2.tribes.testController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rueppellii.backend2.tribes.building.utility.BuildingDTO;
import rueppellii.backend2.tribes.kingdom.persistence.model.Kingdom;
import rueppellii.backend2.tribes.kingdom.service.KingdomService;
import rueppellii.backend2.tribes.gameUtility.purchaseService.PurchaseService;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestResourceController {

    private PurchaseService purchaseService;
    private KingdomService kingdomService;

    @Autowired
    public TestResourceController(PurchaseService purchaseService, KingdomService kingdomService) {
        this.purchaseService = purchaseService;
        this.kingdomService = kingdomService;
    }

    @GetMapping("/kingdom/gold")
    public Integer showResource(Principal principal) throws Exception {
        Kingdom kingdom = kingdomService.findKingdomByPrincipal(principal);
        return purchaseService.getKingdomsGoldAmount(kingdom.getId());
    }

    @PostMapping("/kingdom/troop/build")
    public Kingdom makeTroop(Principal principal) throws Exception {
        Kingdom kingdom = kingdomService.findKingdomByPrincipal(principal);
        purchaseService.buyTroop(kingdom.getId());
        return kingdom;
    }

    @PostMapping("/kingdom/troop/upgrade")
    public Kingdom upgradeTroop(Principal principal, @RequestBody IdDTO idDTO) throws Exception {
        Kingdom kingdom = kingdomService.findKingdomByPrincipal(principal);
        purchaseService.upgradeTroop(kingdom.getId(), idDTO.getId());
        return kingdom;
    }

    @PostMapping("/kingdom/building/build")
    public Kingdom makeBuilding(Principal principal, @RequestBody BuildingDTO buildingDTO) throws Exception {
        Kingdom kingdom = kingdomService.findKingdomByPrincipal(principal);
        purchaseService.buyBuilding(kingdom.getId(), buildingDTO);
        return kingdom;
    }

    @PostMapping("/kingdom/building/upgrade")
    public Kingdom upgradeBuilding(Principal principal, @RequestBody IdDTO idDTO) throws Exception {
        Kingdom kingdom = kingdomService.findKingdomByPrincipal(principal);
        purchaseService.upgradeBuilding(kingdom.getId(), idDTO.getId());
        return kingdom;
    }
}
