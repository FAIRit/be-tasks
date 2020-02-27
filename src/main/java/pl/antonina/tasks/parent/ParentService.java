package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.user.User;

@Service
class ParentService {

    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    public ParentService(ParentRepository parentRepository, ParentMapper parentMapper) {
        this.parentRepository = parentRepository;
        this.parentMapper = parentMapper;
    }

    ParentView getParent(long id) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        return parentMapper.mapParentView(parent);
    }

    void addParent(ParentData parentData) {
        User user = new User();
        user.setEmail(parentData.getEmail());
        user.setPassword(parentData.getPassword());

        Parent parent = new Parent();
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parent.setUser(user);
        parentRepository.save(parent);
    }

    void updateParent(long id, ParentData parentData) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parentRepository.save(parent);
    }

    void deleteParent(long id) {
        parentRepository.deleteById(id);
    }
}