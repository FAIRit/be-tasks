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

    Child getChild(long id) {
        return childRepository.findById(id).orElseThrow();
    }

    List<Child> getChildrenByParentId(long parentId) {
        return childRepository.findByParentId(parentId);
    }

    void addChild(long parentId, ChildData childData) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        Child child = new Child();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setParent(parent);
        child.setPoints(0);
        childRepository.save(child);
    }

    void updateChild(long id, ChildData childData) {
        Child child = getChild(id);
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        childRepository.save(child);
    }

    void deleteChild(long id) {
        childRepository.deleteById(id);
    }
}