package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;

@Service
public class ParentService {

    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    Parent getParent(Long id) {
        return parentRepository.findById(id).orElseThrow();
    }

    void addParent(ParentData parentData) {
        Parent parent = new Parent();
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parentRepository.save(parent);
    }

    void updateParent(Long id, ParentData parentData) {
        Parent parent = getParent(id);
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
        parentRepository.save(parent);
    }

    void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
}