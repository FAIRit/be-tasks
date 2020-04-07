package pl.antonina.tasks.task;

class TaskCreator {

    static TaskData createTaskData() {
        TaskData taskData = new TaskData();
        String name = "Pościelenie łóźka";
        String description = "Przed śniadaniem";
        Integer points = 10;
        taskData.setName(name);
        taskData.setDescription(description);
        taskData.setPoints(points);
        return taskData;
    }

    static TaskData createNewTaskData() {
        TaskData taskData = new TaskData();
        String name = "Spacer z psem";
        String description = "Przed kolacją";
        Integer points = 12;
        taskData.setName(name);
        taskData.setDescription(description);
        taskData.setPoints(points);
        return taskData;
    }
}
