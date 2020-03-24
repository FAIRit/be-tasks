package pl.antonina.tasks.child;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;
import pl.antonina.tasks.user.UserService;
import pl.antonina.tasks.user.UserType;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final ChildMapper childMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LoggedUserService loggedUserService;

    public ChildServiceImpl(ChildRepository childRepository,
                            ChildMapper childMapper,
                            UserRepository userRepository,
                            UserService userService,
                            LoggedUserService loggedUserService) {
        this.childRepository = childRepository;
        this.childMapper = childMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
    }

    @Override
    public ChildView getChild(long childId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        return childMapper.mapChildView(child);
    }

    @Override
    public ChildView getChild(Principal childPrincipal) {
        Child child = loggedUserService.getChild(childPrincipal);
        return childMapper.mapChildView(child);
    }

    @Override
    public List<ChildView> getChildrenByParent(Principal parentPrincipal) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        List<Child> childList = childRepository.findByParentId(parent.getId());
        return childList.stream()
                .map(childMapper::mapChildView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addChild(Principal parentPrincipal, ChildData childData) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        User user = userService.addUser(UserType.CHILD, childData.getUserData());
        Child child = new Child();
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setParent(parent);
        child.setPoints(0);
        child.setUser(user);
        childRepository.save(child);
    }

    @Override
    @Transactional
    public void updateChild(long childId, ChildData childData) {
        Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        User user = userService.updateUser(child.getUser(), childData.getUserData());
        child.setName(childData.getName());
        child.setGender(childData.getGender());
        child.setBirthDate(childData.getBirthDate());
        child.setUser(user);
        childRepository.save(child);
    }

    @Override
    @Transactional
    public void deleteChild(long childId) {
        Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        long userId = child.getUser().getId();
        childRepository.deleteById(childId);
        userRepository.deleteById(userId);
    }
}