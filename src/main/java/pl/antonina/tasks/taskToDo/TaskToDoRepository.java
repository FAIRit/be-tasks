package pl.antonina.tasks.taskToDo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskToDoRepository extends JpaRepository<TaskToDo, Long> {
}
