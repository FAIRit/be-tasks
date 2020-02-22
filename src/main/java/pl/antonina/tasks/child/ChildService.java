package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final ChildMapper childMapper;

    public ChildService(ChildRepository childRepository, ParentRepository parentRepository, ChildMapper childMapper) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
        this.childMapper = childMapper;
    }

    ChildView getChild(long id) {
        Child child = childRepository.findById(id).orElseThrow();
        return childMapper.mapChildView(child);
    }

    List<ChildView> getChildrenByParentId(long parentId) {
        List<Child> childList = childRepository.findByParentId(parentId);
        return childList.stream()
                .map(childMapper::mapChildView)
                .collect(Collectors.toList());
    }

    void addChild(long parentId, ChildData childData) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        Child child = new Child();
        mapChild(childData, child);
        child.setParent(parent);
        child.setPoints(0);
        childRepository.save(child);
    }

    void updateChild(long id, ChildData childData) {
        Child child = childRepository.findById(id).orElseThrow();
        mapChild(childData, child);
        childRepository.save(child);
    }

    void deleteChild(long id) {
        childRepository.deleteById(id);
    }

    private void mapChild(ChildData childData, Child child) {
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
    }
}