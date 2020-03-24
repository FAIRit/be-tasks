package pl.antonina.tasks.parent;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

public interface ParentService {

    ParentView getParent(Principal parentPrincipal);

    @Transactional
    void addParent(ParentData parentData);

    @Transactional
    void updateParent(ParentData parentData, Principal parentPrincipal);

    @Transactional
    void deleteParent(Principal parentPrincipal);
}