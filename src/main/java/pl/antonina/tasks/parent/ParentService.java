package pl.antonina.tasks.parent;

import org.springframework.stereotype.Service;

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
        Parent parent = new Parent();
        mapParent(parentData, parent);
        parentRepository.save(parent);
    }

    void updateParent(long id, ParentData parentData) {
        Parent parent = parentRepository.findById(id).orElseThrow();
        mapParent(parentData, parent);
        parentRepository.save(parent);
    }

    void deleteParent(long id) {
        parentRepository.deleteById(id);
    }

    private void mapParent(ParentData parentData, Parent parent){
        parent.setName(parentData.getName());
        parent.setGender(parentData.getGender());
    }
}