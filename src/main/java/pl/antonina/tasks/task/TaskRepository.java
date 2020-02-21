package pl.antonina.tasks.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByParentIdOrderByNameAsc(Long parentId);
    List<Task> findByParentIdAndNameContains(Long parentId, String name);
}
