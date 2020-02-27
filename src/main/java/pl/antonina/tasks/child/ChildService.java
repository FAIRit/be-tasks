package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
class ChildService {

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

        User user = new User();
        user.setEmail(childData.getEmail());
        user.setPassword(childData.getPassword());

        Child child = new Child();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setParent(parent);
        child.setPoints(0);
        child.setUser(user);
        childRepository.save(child);
    }

    void updateChild(long id, ChildData childData) {
        Child child = childRepository.findById(id).orElseThrow();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        childRepository.save(child);
    }

    void deleteChild(long id) {
        childRepository.deleteById(id);
    }
}