package pl.antonina.tasks.parent;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

import static pl.antonina.tasks.util.LocationUtils.getLocation;

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
    public ResponseEntity<Void> addParent(@Validated @RequestBody ParentData parentData) {
        long parentId = parentService.addParent(parentData);
        return ResponseEntity.created(getLocation(parentId)).build();
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