package pl.antonina.tasks.child;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByParentId(Long parentId);

    Optional<Child> findByUserEmail(String email);

    Optional<Child> findByIdAndParentId(Long childId, Long parentId);
}