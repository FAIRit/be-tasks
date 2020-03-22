package pl.antonina.tasks.parent;

import org.springframework.validation.annotation.Validated;
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
    public ParentView getParent(@ApiIgnore Principal parentPrincipal) {
        return parentService.getParent(parentPrincipal);
    }

    @PostMapping
    public void addParent(@Validated @RequestBody ParentData parentData) {
        parentService.addParent(parentData);
    }

    @PutMapping
    public void updateParent(@Validated @RequestBody ParentData parentData, @ApiIgnore Principal parentPrincipal) {
        parentService.updateParent(parentData, parentPrincipal);
    }

    @DeleteMapping
    public void deleteParent(@ApiIgnore Principal parentPrincipal) {
        parentService.deleteParent(parentPrincipal);
    }
}