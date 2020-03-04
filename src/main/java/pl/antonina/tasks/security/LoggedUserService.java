package pl.antonina.tasks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.security.Principal;

@Service
public class LoggedUserService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    public LoggedUserService(ParentRepository parentRepository, ChildRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }

    public Parent getParent(Principal principal) {
        String email = principal.getName();
        return parentRepository.findByUserEmail(email).orElseThrow();
    }

    public Child getChild(Principal principal){
        String email = principal.getName();
        return childRepository.findByUserEmail(email).orElseThrow();
    }
}