package pl.antonina.tasks.taskToDo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskToDoRepository extends JpaRepository<TaskToDo, Long> {

    List<TaskToDo> findByChildIdAndApprovedOrderByExpectedDateDesc(Long childId, boolean approved);

    List<TaskToDo> findByChildIdAndTaskParentIdAndApprovedOrderByExpectedDateDesc(Long childId, long parentId, boolean approved);

    Optional<TaskToDo> findByIdAndTaskParentId(Long taskToDoId, Long parentId);

    Optional<TaskToDo> findByIdAndChildId(Long taskToDoId, Long childId);
}
