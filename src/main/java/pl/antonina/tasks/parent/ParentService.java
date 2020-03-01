package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;
import pl.antonina.tasks.user.UserService;

@Service
class ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    public ParentService(ParentRepository parentRepository, ParentMapper parentMapper, UserRepository userRepository, UserService userService) {
        this.parentRepository = parentRepository;
        this.parentMapper = parentMapper;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    ParentView getParent(long id) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        return parentMapper.mapParentView(parent);
    }

    @Transactional
    void addParent(ParentData parentData) {
        Parent parent = new Parent();
        User user = userService.addUser(parentData.getUserData());
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    @Transactional
    void updateParent(long id, ParentData parentData) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        User user = userService.updateUser(parent.getUser(), parentData.getUserData());
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    @Transactional
    void deleteParent(long id) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        long userId = parent.getUser().getId();
        userRepository.deleteById(userId);
        parentRepository.deleteById(id);
    }
}