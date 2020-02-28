package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final ChildMapper childMapper;
    private final UserRepository userRepository;

    public ChildService(ChildRepository childRepository, ParentRepository parentRepository, ChildMapper childMapper, UserRepository userRepository) {
        this.childRepository = childRepository;
        this.parentRepository = parentRepository;
        this.childMapper = childMapper;
        this.userRepository = userRepository;
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
        String email = childData.getUserData().getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        Parent parent = parentRepository.findById(parentId).orElseThrow();
        User user = new User();
        user.setEmail(email);
        user.setPassword(childData.getUserData().getPassword());

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
        String email = childData.getUserData().getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        Child child = childRepository.findById(id).orElseThrow();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.getUser().setEmail(email);
        child.getUser().setPassword(childData.getUserData().getPassword());
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