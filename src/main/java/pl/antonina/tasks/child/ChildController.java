package pl.antonina.tasks.child;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping
    public List<ChildView> getChildByParentId(@RequestParam long parentId){
        return childService.getChildrenByParentId(parentId);
    }

    @GetMapping("/{id}")
    public ChildView getChild(@PathVariable long id){
        return childService.getChild(id);
    }

    @PostMapping
    public void addChild(@RequestParam long parentId, @RequestBody ChildData childData){
        childService.addChild(parentId, childData);
    }

    @PutMapping("/{id}")
    public void updateChild(@PathVariable long id, @RequestBody ChildData childData){
        childService.updateChild(id, childData);
    }

    @DeleteMapping("/{id}")
    public void deleteChild(@PathVariable long id){
        childService.deleteChild(id);
    }
}
