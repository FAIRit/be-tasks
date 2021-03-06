package pl.antonina.tasks.security;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.UserNotExistsException;

import java.security.Principal;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    public LoggedUserServiceImpl(ParentRepository parentRepository, ChildRepository childRepository) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
    }

    @Override
    public Parent getParent(Principal parentPrincipal) {
        String email = parentPrincipal.getName();
        return parentRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotExistsException("User with email=" + email + " doesn't exist."));
    }

    @Override
    public Child getChild(Principal childPrincipal) {
        String email = childPrincipal.getName();
        return childRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotExistsException("User with email=" + email + " doesn't exist."));
    }
}