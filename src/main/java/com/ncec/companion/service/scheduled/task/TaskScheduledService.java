package com.ncec.companion.service.scheduled.task;

public interface TaskScheduledService {
    void notifyAboutDeadlines();

    void risePriority();

    void createTasksFromLessons();
}
