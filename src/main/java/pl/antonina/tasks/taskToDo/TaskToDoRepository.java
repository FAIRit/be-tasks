package pl.antonina.tasks.taskToDo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskToDoRepository extends JpaRepository<TaskToDo, Long> {

    List<TaskToDo> findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(Long childId, boolean done, boolean expected);
}
