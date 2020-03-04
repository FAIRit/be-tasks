package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;
import pl.antonina.tasks.user.UserService;
import pl.antonina.tasks.user.UserType;

import java.security.Principal;

@Service
class ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LoggedUserService loggedUserService;

    public ParentService(ParentRepository parentRepository, ParentMapper parentMapper, UserRepository userRepository, UserService userService, LoggedUserService loggedUserService) {
        this.parentRepository = parentRepository;
        this.parentMapper = parentMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.loggedUserService = loggedUserService;
    }

    ParentView getParent(Principal principal) {
        Parent parent = loggedUserService.getParent(principal);
        return parentMapper.mapParentView(parent);
    }

    @Transactional
    void addParent(ParentData parentData) {
        Parent parent = new Parent();
        User user = userService.addUser(UserType.PARENT, parentData.getUserData());
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    @Transactional
    void updateParent(ParentData parentData, Principal principal) {
        Parent parent = loggedUserService.getParent(principal);
        User user = userService.updateUser(parent.getUser(), parentData.getUserData());
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    @Transactional
    void deleteParent(Principal principal) {
        Parent parent = loggedUserService.getParent(principal);
        long userId = parent.getUser().getId();
        long parentId = parent.getId();
        userRepository.deleteById(userId);
        parentRepository.deleteById(parentId);
    }
}