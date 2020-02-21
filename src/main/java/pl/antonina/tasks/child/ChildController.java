package pl.antonina.tasks.child;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents/{parentId}/children")
public class ChildController {

    private ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping
    public List<Child> getChildByParentId(@PathVariable Long parentId){
        return childService.getChildrenByParentId(parentId);
    }

    @GetMapping("/{id}")
    public Child getChild(@PathVariable Long parentId, @PathVariable Long id){
        return childService.getChild(id);
    }

    @PostMapping
    public void addChild(@PathVariable Long parentId, @RequestBody ChildData childData){
        childService.addChild(parentId, childData);
    }

    @PutMapping("/{id}")
    public void updateChile(@PathVariable Long parentId, @PathVariable Long id, @RequestBody ChildData childData){
        childService.updateChild(id, childData);
    }

    @DeleteMapping("/{id}")
    public void deleteChild(@PathVariable Long parentId, @PathVariable Long id){
        childService.deleteChild(id);
    }
}
