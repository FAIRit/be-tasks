package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserRepository;

@Service
class ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;
    private final UserRepository userRepository;

    public ParentService(ParentRepository parentRepository, ParentMapper parentMapper, UserRepository userRepository) {
        this.parentRepository = parentRepository;
        this.parentMapper = parentMapper;
        this.userRepository = userRepository;
    }

    ParentView getParent(long id) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        return parentMapper.mapParentView(parent);
    }

    void addParent(ParentData parentData) {
        String email = parentData.getUserData().getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(parentData.getUserData().getPassword());

        Parent parent = new Parent();
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    void updateParent(long id, ParentData parentData) {
        String email = parentData.getUserData().getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        Parent parent = parentRepository.findById(id).orElseThrow();
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.getUser().setPassword(parentData.getUserData().getPassword());
        parent.getUser().setEmail(email);
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