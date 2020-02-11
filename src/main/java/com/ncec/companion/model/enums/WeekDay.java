package com.ncec.companion.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum WeekDay {
    SUNDAY(1),
    MONDAY(2),
    TUESDAY(3),
    WEDNESDAY(4),
    THURSDAY(5),
    FRIDAY(6),
    SATURDAY(7);

    private static final Map<Integer, WeekDay> BY_CALENDAR_VALUE = new HashMap<>();

    static {
        for (WeekDay w : values()) {
            BY_CALENDAR_VALUE.put(w.calendarValue, w);
        }
    }

    public final int calendarValue;

    @JsonCreator
    public static WeekDay valueOfCalendar(int value) {
        return BY_CALENDAR_VALUE.get(value);
    }

    @JsonValue
    public Integer toValue() {
        for (Map.Entry<Integer, WeekDay> entry : BY_CALENDAR_VALUE.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }
        return null;
    }
}
