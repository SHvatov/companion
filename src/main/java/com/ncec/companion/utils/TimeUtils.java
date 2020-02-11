package com.ncec.companion.utils;

import com.ncec.companion.exception.BusinessLogicException;

import java.util.Date;

public final class TimeUtils {
    public static final int MILLISECONDS_IN_DAY = 86400000;

    private TimeUtils() {
    }

    public static boolean lessonsOverlap(Integer startA, Integer durationA, Integer startB, Integer durationB) {
        // (StartA <= EndB)  and  (EndA >= StartB) - events' time ranges overlap
        return (startA <= startB + durationB) && (startA + durationA >= startB);
    }

    public static void validateTimePeriod(Date begin, Date end) {
        Date currentDate = new Date();
        if (begin.after(end)) {
            throw new BusinessLogicException("Start of the period cannot be after the end of the period");
        } else if (begin.equals(end)) {
            throw new BusinessLogicException("Start of the period cannot be at the same time as the end of the period");
        } else if (begin.before(currentDate)) {
            throw new BusinessLogicException("Start of the period cannot be before current moment of the time");
        }
    }
}
