package pl.antonina.tasks.taskToDo;

import java.time.LocalDate;

class TaskToDoCreator {

    static TaskToDoData createTaskToDoData() {
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(LocalDate.of(2020, 3, 3));
        return taskToDoData;
    }

    static TaskToDoData createNewTaskToDo() {
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(LocalDate.of(2019, 2, 2));
        return taskToDoData;
    }
}