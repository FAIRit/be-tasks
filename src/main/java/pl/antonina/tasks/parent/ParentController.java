package pl.antonina.tasks.parent;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/{id}")
    public ParentView getParent(@PathVariable long id) {
        return parentService.getParent(id);
    }

    @PostMapping
    public void addParent(@RequestBody ParentData parentData) {
        parentService.addParent(parentData);
    }

    @PutMapping("/{id}")
    public void updateParent(@PathVariable long id, @RequestBody ParentData parentData) {
        parentService.updateParent(id, parentData);
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable long id){
        parentService.deleteParent(id);
    }
}
