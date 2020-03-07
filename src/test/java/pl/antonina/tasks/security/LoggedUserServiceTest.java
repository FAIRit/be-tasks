package pl.antonina.tasks.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggedUserServiceTest {

    @Mock
    private ParentRepository parentRepository;
    @Mock
    private ChildRepository childRepository;

    private LoggedUserService loggedUserService;

    @BeforeEach
    void beforeEach() {
        loggedUserService = new LoggedUserService(parentRepository, childRepository);
    }

    @Test
    void getParent() {
        final Parent parent = new Parent();
        String email = "test@gmail.com";

        Principal parentPrincipal = mock(Principal.class);
        when(parentPrincipal.getName()).thenReturn(email);
        when(parentRepository.findByUserEmail(email)).thenReturn(Optional.of(parent));

        Parent parentResult = loggedUserService.getParent(parentPrincipal);

        assertThat(parentResult).isEqualTo(parent);
    }

    @Test
    void getChild() {
        final Child child = new Child();
        String email = "test@gmail.com";

        Principal childPrincipal = mock(Principal.class);
        when(childPrincipal.getName()).thenReturn(email);
        when(childRepository.findByUserEmail(email)).thenReturn(Optional.of(child));

        Child childResult = loggedUserService.getChild(childPrincipal);

        assertThat(childResult).isEqualTo(child);
    }
}