package pl.antonina.tasks.parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ParentService {

    private final ParentRepository parentRepository;

    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }


    public Parent getParent(Long id){
        return parentRepository.findById(id).orElseThrow();
    }

    public void addParent(ParentData parentData){
        Parent parent = new Parent();
        parent.setName(parentData.getName());
        parentRepository.save(parent);
    }

    public Parent updateParent(Long id, ParentData parentData){
        Parent parent = getParent(id);
        parent.setName(parentData.getName());
        parentRepository.save(parent);
        return parent;
    }

    public void deleteParent(Long id){
        parentRepository.deleteById(id);
    }
}
