package pl.antonina.tasks.task;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void mapTaskView() {
        Task task = new Task();
        final String name = "Sprzątanie zabawek";
        final String description = "Cały pokój";
        final Integer points = 10;
        task.setName(name);
        task.setPoints(points);
        task.setDescription(description);

        TaskView taskView = taskMapper.mapTaskView(task);

        assertThat(taskView.getName()).isEqualTo(name);
        assertThat(taskView.getDescription()).isEqualTo(description);
        assertThat(taskView.getPoints()).isEqualTo(points);
    }
}