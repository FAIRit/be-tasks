package pl.antonina.tasks.parent;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping
    public ParentView getParent(@ApiIgnore Principal principal) {
        return parentService.getParent(principal);
    }

    @PostMapping
    public void addParent(@RequestBody ParentData parentData) {
        parentService.addParent(parentData);
    }

    @PutMapping
    public void updateParent(@RequestBody ParentData parentData, @ApiIgnore Principal principal) {
        parentService.updateParent(parentData, principal);
    }

    @DeleteMapping
    public void deleteParent(@ApiIgnore Principal principal) {
        parentService.deleteParent(principal);
    }
}