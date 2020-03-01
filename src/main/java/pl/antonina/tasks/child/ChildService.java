package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;
import pl.antonina.tasks.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final ChildMapper childMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public ChildService(ChildRepository childRepository, ParentRepository parentRepository, ChildMapper childMapper, UserRepository userRepository, UserService userService) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
        this.childMapper = childMapper;
        this.userRepository = userRepository;
        this.userService = userService;
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

    @Transactional
    void addChild(long parentId, ChildData childData) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        User user = userService.addUser(childData.getUserData());
        Child child = new Child();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setParent(parent);
        child.setPoints(0);
        child.setUser(user);
        childRepository.save(child);
    }

    @Transactional
    void updateChild(long id, ChildData childData) {
        Child child = childRepository.findById(id).orElseThrow();
        User user = userService.updateUser(child.getUser(), childData.getUserData());
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setUser(user);
        childRepository.save(child);
    }

    @Transactional
    void deleteChild(long id) {
        Child child = childRepository.findById(id).orElseThrow();
        long userId = child.getUser().getId();
        userRepository.deleteById(userId);
        childRepository.deleteById(id);
    }
}