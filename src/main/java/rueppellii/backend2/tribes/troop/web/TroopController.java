package rueppellii.backend2.tribes.troop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import rueppellii.backend2.tribes.gameUtility.timeService.TimeServiceImpl;
import rueppellii.backend2.tribes.progression.persistence.ProgressionModel;
import rueppellii.backend2.tribes.user.persistence.model.ApplicationUser;
import rueppellii.backend2.tribes.user.service.ApplicationUserService;
import rueppellii.backend2.tribes.user.util.ErrorResponse;

import java.security.Principal;

@RestController
@RequestMapping("/api/kingdom/troop")
public class TroopController {

    private ApplicationUserService applicationUserService;
    private TimeServiceImpl timeService;

    @Autowired
    public TroopController(ApplicationUserService applicationUserService, TimeServiceImpl timeService) {
        this.applicationUserService = applicationUserService;
        this.timeService = timeService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void createTroop(Principal principal) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserService.findByPrincipal(principal);
        //TODO: timeService method to check the progression and update/create if time is up

        //TODO: ResourceService will call timeService and refresh the actual resources(applicationUser)

        //TODO: PurchaseService will check if user have sufficient funds for the progression(progressionDTO.getType, applicationUser, actionCode)
        //TODO: Will return Boolean and deduct the amount(the amount is gonna be based on the type of the gameObject, whether if its create or upgrade and the level)

        ProgressionModel progressionModel = new ProgressionModel();
        progressionModel.setObjectToProgress("TROOP");
        progressionModel.setTimeToCreate(timeService.calculateTimeOfTroopCreation(applicationUser));

        progressionModel.setProgressKingdom(applicationUser.getKingdom());
        applicationUser.getKingdom().getKingdomsProgresses().add(progressionModel);
        applicationUserService.save(applicationUser);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void upgradeTroop(@PathVariable Long id, Principal principal) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserService.findByPrincipal(principal);
        //TODO: timeService method to check the progression and update/create if time is up

        //TODO: ResourceService will call timeService and refresh the actual resources(applicationUser)

        //TODO: PurchaseService will check if user have sufficient funds for the progression(progressionDTO.getType, applicationUser, actionCode)
        //TODO: Will return Boolean and deduct the amount(the amount is gonna be based on the type of the gameObject, whether if its create or upgrade and the level)

        ProgressionModel progressionModel = new ProgressionModel();
        progressionModel.setGameObjectId(id);
        progressionModel.setTimeToCreate(timeService.calculateTimeOfTroopUpgrade(applicationUser));

        progressionModel.setProgressKingdom(applicationUser.getKingdom());
        applicationUser.getKingdom().getKingdomsProgresses().add(progressionModel);
        applicationUserService.save(applicationUser);
    }

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse userNotFoundHandler(UsernameNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}