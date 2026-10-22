package jp.co.benesse.dcha.util;

import java.util.Calendar;

public final class CalendarUtils {
    private CalendarUtils() {
    }

    public static final boolean isStrictCalendarInfo(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar checkCalendar = Calendar.getInstance();
        checkCalendar.setLenient(true);
        checkCalendar.set(1, year);
        checkCalendar.set(2, month - 1);
        checkCalendar.set(5, date);
        checkCalendar.set(11, hourOfDay);
        checkCalendar.set(12, minute);
        checkCalendar.set(13, second);
        int calendarYear = checkCalendar.get(1);
        int calendarMonth = checkCalendar.get(2) + 1;
        int calendarDate = checkCalendar.get(5);
        int calendarHourOfDay = checkCalendar.get(11);
        int calendarMinute = checkCalendar.get(12);
        int calendarSecond = checkCalendar.get(13);
        if (year != calendarYear || month != calendarMonth || date != calendarDate || hourOfDay != calendarHourOfDay || minute != calendarMinute || second != calendarSecond) {
            return false;
        }
        return true;
    }
}
