package com.ncec.companion.service.business.timetable;

import com.ncec.companion.exception.BusinessLogicException;
import com.ncec.companion.model.dto.LessonDto;
import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.service.crud.lesson.LessonCrudService;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimetableValidationServiceImpl implements TimetableValidationService {
    private final LessonCrudService lessonCrudService;
    private final ProfessorCrudService professorCrudService;

    @Override
    public void validate(LessonDto dto) {
        validateProfessor(dto);
        validateLocation(dto);
        dto.getGroups().forEach(groupId -> validateGroup(groupId, dto));
    }

    private void validateGroup(Integer groupId, LessonDto dto) {
        for (LessonDto existing : lessonCrudService.findLessonsByGroup(groupId)) {
            if (existing.getDay() == dto.getDay()
                    && TimeUtils.lessonsOverlap(existing.getStartTime(), existing.getDuration(), dto.getStartTime(), dto.getDuration())
                    && (dto.getId() == null || !dto.getId().equals(existing.getId()))) {
                throw new BusinessLogicException("Cannot create this lesson, " +
                        "because this lesson overlaps with " +
                        "other lesson of one of the groups," +
                        "assigned to this lesson");
            }
        }
    }

    private void validateProfessor(LessonDto dto) {
        ProfessorDto professorDto = professorCrudService.findById(dto.getProfessor());
        Set<Integer> professorSubjects = professorDto.getSubjects();

        if (!professorSubjects.contains(dto.getSubject())) {
            throw new BusinessLogicException("This professor does not teach this subject at the moment");
        }

        for (LessonDto existing : lessonCrudService.findLessonsByProfessor(professorDto.getId())) {
            if (existing.getDay() == dto.getDay()
                    && TimeUtils.lessonsOverlap(existing.getStartTime(), existing.getDuration(), dto.getStartTime(), dto.getDuration())
                    && (dto.getId() == null || !dto.getId().equals(existing.getId()))) {
                throw new BusinessLogicException("Cannot create this lesson, " +
                        "because this lesson overlaps with " +
                        "other lesson, that is" +
                        "assigned to this professor");
            }
        }
    }

    private void validateLocation(LessonDto dto) {
        for (LessonDto existing : lessonCrudService.findAll()) {
            if (existing.getDay() == dto.getDay()
                    && existing.getLocation().equals(dto.getLocation())
                    && existing.getAuditory().equals(dto.getAuditory())
                    && TimeUtils.lessonsOverlap(existing.getStartTime(), existing.getDuration(), dto.getStartTime(), dto.getDuration())
                    && (dto.getId() == null || !dto.getId().equals(existing.getId()))) {
                throw new BusinessLogicException("Cannot create this lesson, " +
                        "because auditory, where this lessons takes place " +
                        "is occupied at that moment of time.");
            }
        }
    }
}
