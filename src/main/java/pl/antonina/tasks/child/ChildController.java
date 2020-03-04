package pl.antonina.tasks.child;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping
    public List<ChildView> getChildrenByParent(@ApiIgnore Principal principal) {
        return childService.getChildrenByParent(principal);
    }

    @GetMapping("/{id}")
    public ChildView getChild(@PathVariable long childId) {
        return childService.getChild(childId);
    }

    @PostMapping
    public void addChild(@ApiIgnore Principal principal, @RequestBody ChildData childData) {
        childService.addChild(principal, childData);
    }

    @PutMapping("/{id}")
    public void updateChild(@PathVariable long childId, @RequestBody ChildData childData) {
        childService.updateChild(childId, childData);
    }

    @DeleteMapping("/{id}")
    public void deleteChild(@PathVariable long childId) {
        childService.deleteChild(childId);
    }
}