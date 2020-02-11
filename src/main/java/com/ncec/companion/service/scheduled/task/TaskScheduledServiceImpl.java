package com.ncec.companion.service.scheduled.task;

import com.ncec.companion.model.dto.*;
import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.SubjectType;
import com.ncec.companion.model.enums.TaskPriority;
import com.ncec.companion.model.enums.TaskType;
import com.ncec.companion.model.enums.WeekDay;
import com.ncec.companion.service.business.task.ProfessorTaskService;
import com.ncec.companion.service.crud.group.GroupCrudService;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.ptask.ProfessorTaskCrudService;
import com.ncec.companion.service.crud.stask.StudentTaskCrudService;
import com.ncec.companion.service.crud.student.StudentCrudService;
import com.ncec.companion.service.crud.subject.SubjectCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskScheduledServiceImpl implements TaskScheduledService {
	private static final int DAYS_BEFORE_DEADLINE = 3;
	private static final int DAYS_BEFORE_MIDDLE = 7;
	private static final int DAYS_BEFORE_HIGH = 3;
	private final StudentTaskCrudService studentTaskCrudService;
	private final ProfessorTaskCrudService professorTaskCrudService;
	private final LessonCrudService lessonCrudService;
	private final SubjectCrudService subjectCrudService;
	private final StudentCrudService studentCrudService;
	private final UserCrudService userCrudService;
	private final GroupCrudService groupCrudService;
	private final ProfessorTaskService professorTaskService;
	private final EmailSupportService emailSupportService;

	@Override
	@Scheduled(cron = "0 0 1 * * ?") // at 1 am
	public void notifyAboutDeadlines() {
		Set<ProfessorTaskDto> upcoming = professorTaskCrudService.findAllUpcomingTasks(DAYS_BEFORE_DEADLINE);
		for (ProfessorTaskDto professorTask : upcoming) {
			for (Integer taskId : professorTask.getStudentsTasks()) {
				StudentTaskDto task = studentTaskCrudService.findById(taskId);
				StudentDto student = studentCrudService.findById(task.getStudent());
				UserDto user = userCrudService.findById(student.getUser());

				emailSupportService.sendTaskDeadlineNotificationMessage(
						user.getEmail(),
						professorTask.getTitle(),
						professorTask.getDueDate()
				);
			}
		}
	}

	@Override
	@Scheduled(cron = "0 30 1 * * ?") // at 1:30 am
	public void risePriority() {
		// update to middle priority
		Set<ProfessorTaskDto> upcoming = professorTaskCrudService.findAllUpcomingTasks(DAYS_BEFORE_MIDDLE);
		for (ProfessorTaskDto task : upcoming) {
			if (task.getPriority() == TaskPriority.LOW) {
				task.setPriority(TaskPriority.MIDDLE);
			}
		}

		// update to high priority
		upcoming = professorTaskCrudService.findAllUpcomingTasks(DAYS_BEFORE_HIGH);
		for (ProfessorTaskDto task : upcoming) {
			if (task.getPriority() == TaskPriority.MIDDLE) {
				task.setPriority(TaskPriority.HIGH);
			}
		}
	}

	@Override
	@Scheduled(cron = "0 0 2 * * ?") // at 2 am
	public void createTasksFromLessons() {
		Calendar calendar = Calendar.getInstance();
		WeekDay weekDay = WeekDay.valueOfCalendar(calendar.get(Calendar.DAY_OF_WEEK));
		if (weekDay != WeekDay.SUNDAY) {
			Set<LessonDto> lessons = lessonCrudService.findLessonsByWeekDay(weekDay);
			for (LessonDto lesson : lessons) {
				professorTaskService.save(convertLessonToTask(lesson));
			}
		}
	}

	private ProfessorTaskDto convertLessonToTask(LessonDto lessonDto) {
		ProfessorTaskDto professorTaskDto = new ProfessorTaskDto();

		professorTaskDto.setPriority(TaskPriority.LOW);
		professorTaskDto.setSubject(lessonDto.getSubject());

		// today
		Calendar date = Calendar.getInstance();

		// reset hour, minutes, seconds and millis
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		// get lesson end time
		date.add(Calendar.SECOND, lessonDto.getStartTime() + lessonDto.getDuration());
		professorTaskDto.setDueDate(date.getTime());

		SubjectDto subject = subjectCrudService.findById(lessonDto.getSubject());
		TaskType type = subject.getType() == SubjectType.LECTURE ? TaskType.LECTURE : TaskType.PRACTICE;
		professorTaskDto.setType(type);

		professorTaskDto.setTitle(subject.getType() + ": " + subject.getName());
		professorTaskDto.setDescription(String.format(
				"Lesson of %s.%nLocation: %s.%nAuditory: %s.",
				subject.getName(),
				lessonDto.getLocation(),
				lessonDto.getAuditory()
		));
		professorTaskDto.setProfessor(lessonDto.getProfessor());

		Set<Integer> assignees = new HashSet<>();
		lessonDto.getGroups()
				.stream()
				.map(groupCrudService::findById)
				.map(GroupDto::getStudents)
				.forEach(assignees::addAll);
		professorTaskDto.setAssignees(assignees);

		return professorTaskDto;
	}
}
