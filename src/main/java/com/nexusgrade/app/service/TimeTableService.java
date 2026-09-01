package com.nexusgrade.app.service;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.TimeTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;

    private static final Logger logger = LoggerFactory.getLogger(TimeTableService.class);

    public static final List<String> DAYS = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

    /**
     * Builds a structured matrix map: Map<DayName, List<TimeSlot>>
     * Every day list is guaranteed to match the exact size and index order of timeSlots.
     */
    public Map<String, List<TimeSlot>> buildTimeTable(TimeTable timeTable, List<String> timeSlots) {
        // Initialize independent lists for each day
        Map<String, List<TimeSlot>> grid = new LinkedHashMap<>();
        for (String day : DAYS) {
            grid.put(day, new ArrayList<>(Collections.nCopies(timeSlots.size(), null)));
        }

        if (timeTable == null || timeTable.getTimeTablePeriods() == null) {
            return grid;
        }

        List<TimeTablePeriod> periods = timeTable.getTimeTablePeriods();

        for (TimeTablePeriod period : periods) {
            String dayName = getDayName(period.getDayOfWeek());
            if (dayName == null) continue;

            String timeFormatted = period.getStartTime().toString() + " - " + period.getEndTime().toString();
            int slotIndex = timeSlots.indexOf(timeFormatted);

            if (slotIndex != -1) {
                TimeSlot slot = mapToTimeSlot(period, timeFormatted);
                grid.get(dayName).set(slotIndex, slot);
            }
        }

        return grid;
    }

    private TimeSlot mapToTimeSlot(TimeTablePeriod period, String timeFormatted) {
        String room = null;
        String teacher = null;
        String subject = null;

        if (period.getSessionType() == TimeTablePeriod.SessionType.CLASS) {
            Instructor instructor = period.getInstructor();
            if (instructor != null && instructor.getUser() != null) {
                User user = instructor.getUser();
                String title = (user.getTitle() != null) ? user.getTitle().getLabel() : "";
                teacher = (title + " " + user.getLastName()).trim();
            }

            if (period.getSubject() != null) {
                subject = period.getSubject().getSubjectCode();
            }

            SchoolClass sClass = period.getClassTimeTable().getSchoolClass();
            room = "Class " + (sClass != null ? sClass.getGrade()+"-"+sClass.getTitle() : "N/A");
        } else {
            // BREAK or LUNCH
            subject = period.getSessionType().name(); // "BREAK" or "LUNCH"
        }

        String colorHex = (period.getColor() != null) ? period.getColor().getHexCode() : "#4A5568";

        return new TimeSlot(timeFormatted, subject, teacher, room, colorHex);
    }

    public List<String> getTimeSlots(UUID timeTableId) {
        if (timeTableId == null) return Collections.emptyList();

        List<Object[]> rawSlots = timeTableRepository.getTimeSlots(timeTableId);
        return rawSlots.stream()
                .map(row -> row[0].toString() + " - " + row[1].toString())
                .distinct()
                .toList();
    }

    public List<String> getDays() {
        return DAYS;
    }

    public Map<String, Integer> getStats(UUID timeTableId) {
        if (timeTableId == null) {
            return Map.of("totalPeriods", 0, "subjectsCount", 0);
        }
        try {
            return Map.of(
                    "totalPeriods", Optional.ofNullable(timeTableRepository.getTotalTimetablePeriods(timeTableId)).orElse(0),
                    "subjectsCount", Optional.ofNullable(timeTableRepository.getTimeTableSubjectsCount(timeTableId)).orElse(0)
            );
        } catch (Exception e) {
            logger.error("Error fetching timetable stats for ID: {}", timeTableId, e);
            return Map.of("totalPeriods", 0, "subjectsCount", 0);
        }
    }

    private String getDayName(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            default -> null;
        };
    }

    // DTO for Thymeleaf rendering
    public static class TimeSlot {
        private final String time;
        private final String subject;
        private final String teacher;
        private final String room;
        private final String color;

        public TimeSlot(String time, String subject, String teacher, String room, String color) {
            this.time = time;
            this.subject = subject;
            this.teacher = teacher;
            this.room = room;
            this.color = color;
        }

        public String getTime() { return time; }
        public String getSubject() { return subject; }
        public String getTeacher() { return teacher; }
        public String getRoom() { return room; }
        public String getColor() { return color; }

        public boolean isBreak() {
            return subject != null && (subject.equalsIgnoreCase("BREAK") || subject.equalsIgnoreCase("LUNCH"));
        }
    }
}