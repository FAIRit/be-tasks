package pl.antonina.tasks.parent;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    private ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/{id}")
    public Parent getParent(@PathVariable Long id) {
        return parentService.getParent(id);
    }

    @PostMapping
    public void addParent(@RequestBody ParentData parentData) {
        parentService.addParent(parentData);
    }

    @PutMapping("/{id}")
    public void updateParent(@PathVariable Long id, @RequestBody ParentData parentData) {
        parentService.updateParent(id, parentData);
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable Long id){
        parentService.deleteParent(id);
    }
}
