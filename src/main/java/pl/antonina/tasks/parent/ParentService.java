package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserData;

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
        user.setEmail(parentData.getUserData().getEmail());
        user.setPassword(parentData.getUserData().getPassword());

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
        parent.getUser().setPassword(parentData.getUserData().getPassword());
        parent.getUser().setEmail(parentData.getUserData().getEmail());
        parentRepository.save(parent);
    }

    void updateParentAsUser(long id, UserData userData){
        Parent parent = parentRepository.findById(id).orElseThrow();
        parent.getUser().setPassword(userData.getPassword());
        parent.getUser().setEmail(userData.getEmail());
        parentRepository.save(parent);
    }

    void deleteParent(long id) {
        parentRepository.deleteById(id);
    }
}