package rueppellii.backend2.tribes.resource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rueppellii.backend2.tribes.kingdom.persistence.model.Kingdom;
import rueppellii.backend2.tribes.resource.presistence.model.Resource;
import rueppellii.backend2.tribes.resource.presistence.repository.ResourceRepository;
import rueppellii.backend2.tribes.resource.utility.ResourceType;
import rueppellii.backend2.tribes.resource.exception.NoResourceException;

import java.util.ArrayList;
import java.util.List;

import static rueppellii.backend2.tribes.resource.utility.ResourceFactory.makeResource;

@Service
public class ResourceService {
    private ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource provideResource(ResourceType type) {
        return type.produceResource();
    }

    public List<Resource> findAllResourcesInKingdom(Kingdom kingdom) {
        return kingdom.getKingdomsResources();
    }

    public ResponseEntity saveResource(Resource resource) {
        if ((resource.getType() != null) && validateType(resource)) {
            resourceRepository.save(resource);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public boolean validateType(Resource resource) {
        return resource.getType() == ResourceType.FOOD || resource.getType() == ResourceType.GOLD;
    }

    public Resource returnResource(ResourceType type, Long id) throws NoResourceException {
        return resourceRepository.findByTypeAndResourcesKingdom_Id(type, id).orElseThrow(() -> new NoResourceException("No resource found!"));
    }

    public void minusGoldAmount(Integer gold, Long kingdomId) throws NoResourceException {
        Resource resource = returnResource(ResourceType.GOLD, kingdomId);
        resource.setAmount(resource.getAmount() - gold);
        saveResource(resource);
    }

    public void plusGoldAmount(Integer gold, Long kingdomId) throws NoResourceException {
        Resource resource = returnResource(ResourceType.GOLD, kingdomId);
        resource.setAmount(resource.getAmount() + gold);
        saveResource(resource);
    }

    public static List<Resource> starterKit(){
        List<Resource> starterResources = new ArrayList<>();
        for (ResourceType t : ResourceType.values()) {
            starterResources.add(makeResource(t));
        }
        return starterResources;
    }
}
