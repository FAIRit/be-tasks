package pl.antonina.tasks.security;

import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.parent.Parent;

import java.security.Principal;

public interface LoggedUserService {

    Parent getParent(Principal parentPrincipal);

    Child getChild(Principal childPrincipal);
}