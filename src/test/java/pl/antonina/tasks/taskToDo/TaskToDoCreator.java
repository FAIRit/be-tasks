package pl.antonina.tasks.taskToDo;

import java.time.LocalDate;

public class TaskToDoCreator {

    static TaskToDoData createTaskToDoData() {
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(LocalDate.of(2020, 03, 03));
        return taskToDoData;
    }

    static TaskToDoData createNewTaskToDo() {
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(LocalDate.of(2019, 02, 02));
        return taskToDoData;
    }
}