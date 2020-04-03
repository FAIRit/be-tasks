package pl.antonina.tasks.child;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface ChildService {

    ChildView getChild(Principal parentPrincipal, long childId);

    ChildView getChild(Principal childPrincipal);

    List<ChildView> getChildrenByParent(Principal parentPrincipal);

    @Transactional
    long addChild(Principal parentPrincipal, ChildData childData);

    @Transactional
    void updateChild(Principal parentPrincipal, long childId, ChildData childData);

    @Transactional
    void deleteChild(Principal parentPrincipal, long childId);
}