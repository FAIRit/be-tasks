package pl.antonina.tasks.child;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

import static pl.antonina.tasks.util.LocationUtils.getLocation;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping("/byParent")
    public List<ChildView> getChildrenByParent(@ApiIgnore Principal parentPrincipal) {
        return childService.getChildrenByParent(parentPrincipal);
    }

    @GetMapping
    public ChildView getChild(@ApiIgnore Principal childPrincipal) {
        return childService.getChild(childPrincipal);
    }

    @GetMapping("/{childId}")
    public ChildView getChild(@ApiIgnore Principal parentPrincipal, @PathVariable long childId) {
        return childService.getChild(parentPrincipal, childId);
    }

    @PostMapping
    public ResponseEntity<Void> addChild(@ApiIgnore Principal parentPrincipal, @Validated @RequestBody ChildData childData) {
        long childId = childService.addChild(parentPrincipal, childData);
        return ResponseEntity.created(getLocation(childId)).build();
    }

    @PutMapping("/{childId}")
    public void updateChild(@ApiIgnore Principal parentPrincipal, @PathVariable long childId, @Validated @RequestBody ChildData childData) {
        childService.updateChild(parentPrincipal, childId, childData);
    }

    @DeleteMapping("/{childId}")
    public void deleteChild(@ApiIgnore Principal parentPrincipal, @PathVariable long childId) {
        childService.deleteChild(parentPrincipal, childId);
    }
}