package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.util.List;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;

    public ChildService(ChildRepository childRepository, ParentRepository parentRepository) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
    }

    public Child getChild(Long id) {
        return childRepository.findById(id).orElseThrow();
    }

    public List<Child> getChildrenByParentId(Long parentId) {
        return childRepository.findByParentId(parentId);
    }

    public void addChild(ChildData childData) {
        Long parentId = childData.getParentId();
        Parent parent = parentRepository.findById(parentId).orElseThrow();

        Child child = new Child();
        child.setName(childData.getName());
        child.setParent(parent);
        child.setPoints(0);
        childRepository.save(child);
    }

    public Child updateChild(Long id, ChildData childData) {
        Child child = getChild(id);
        child.setName(childData.getName());
        childRepository.save(child);
        return child;
    }

    public void deleteChild(Long id) {
        Child child = getChild(id);
        childRepository.delete(child);
    }
}