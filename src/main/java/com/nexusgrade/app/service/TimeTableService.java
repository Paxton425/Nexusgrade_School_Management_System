package com.nexusgrade.app.service;

import com.nexusgrade.app.controller.SchoolClassController;
import com.nexusgrade.app.model.SchoolClass;
import com.nexusgrade.app.model.TimeTable;
import com.nexusgrade.app.model.TimeTablePeriod;
import com.nexusgrade.app.repository.TimeTableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimeTableService {

    @Autowired
    TimeTableRepository timeTableRepository;

    Logger logger = LoggerFactory.getLogger(TimeTableService.class);

    public Map<String, List<TimeSlot>> buildTimeTable(TimeTable timeTable){
        try{
            List<TimeTablePeriod> periods = timeTable.getTimeTablePeriods();

            List<TimeSlot> monday , tuesday, wednesday, thursday, friday;
            monday = tuesday = wednesday = thursday = friday =  new ArrayList<>();
            for(TimeTablePeriod p : periods){
                switch (p.getDayOfWeek()){
                    case 1: //Monday
                        monday.add(new TimeSlot(p.getStartTime().toString()+" "+p.getEndTime().toString(),
                                p.getSubject().getName(),
                                p.getInstructor().getUser().getTitle().getLabel().toString()+" "+p.getInstructor().getUser().getLastName(),
                                "Class Room "+p.getColor().name(),
                                p.getColor().getHexCode()));
                        break;
                    case 2: //Tuesday
                        tuesday.add(new TimeSlot(p.getStartTime().toString()+" "+p.getEndTime().toString(),
                                p.getSubject().getName(),
                                p.getInstructor().getUser().getTitle().getLabel().toString()+" "+p.getInstructor().getUser().getLastName(),
                                "Class Room "+p.getColor().name(),
                                p.getColor().getHexCode()));
                        break;
                    case 3: //Monday
                        wednesday.add(new TimeSlot(p.getStartTime().toString()+" "+p.getEndTime().toString(),
                                p.getSubject().getName(),
                                p.getInstructor().getUser().getTitle().getLabel().toString()+" "+p.getInstructor().getUser().getLastName(),
                                "Class Room "+p.getColor().name(),
                                p.getColor().getHexCode()));
                        break;
                    case 4: //Monday
                        thursday.add(new TimeSlot(p.getStartTime().toString()+" "+p.getEndTime().toString(),
                                p.getSubject().getName(),
                                p.getInstructor().getUser().getTitle().getLabel().toString()+" "+p.getInstructor().getUser().getLastName(),
                                "Class Room "+p.getColor().name(),
                                p.getColor().getHexCode()));
                        break;
                    case 5: //Monday
                        friday.add(new TimeSlot(p.getStartTime().toString()+" "+p.getEndTime().toString(),
                                p.getSubject().getName(),
                                p.getInstructor().getUser().getTitle().getLabel().toString()+" "+p.getInstructor().getUser().getLastName(),
                                "Class Room "+p.getColor().name(),
                                p.getColor().getHexCode()));
                        break;
                }
            }
            return Map.of(
                    "Monday", monday,
                    "Tuesday", tuesday,
                    "Wednesday", wednesday,
                    "Thursday", thursday,
                    "Friday", friday
            );
        } catch (Exception e) {
            logger.error("Error building time table!\n{}\n =================== STACK TRACE ================\n", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, List<TimeSlot>> buildTimetable() {
        Map<String, List<TimeSlot>> timetable = new LinkedHashMap<>();

        // Monday
        List<TimeSlot> monday = Arrays.asList(
                new TimeSlot("7:30 - 8:20", "Mathematics", "Mrs. Adebayo", "Block A-204", "#48bb78"),
                new TimeSlot("8:20 - 9:10", "English Language", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("9:10 - 10:00", "Physics", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("10:00 - 10:30", "BREAK", null, null, "#ecc94b"),
                new TimeSlot("10:30 - 11:20", "Chemistry", "Dr. Nwachukwu", "Science Lab 2", "#9f7aea"),
                new TimeSlot("11:20 - 12:10", "History", "Mrs. Okonkwo", "Block A-204", "#f6ad55"),
                new TimeSlot("12:10 - 1:00", "French", "Mme. Dubois", "Block B-102", "#68d391"),
                new TimeSlot("1:00 - 2:00", "LUNCH", null, null, "#ecc94b"),
                new TimeSlot("2:00 - 2:50", "Physical Education", "Coach Musa", "Sports Field", "#fc8181")
        );
        timetable.put("Monday", monday);

        // Tuesday
        List<TimeSlot> tuesday = Arrays.asList(
                new TimeSlot("7:30 - 8:20", "English Language", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("8:20 - 9:10", "Mathematics", "Mrs. Adebayo", "Block A-204", "#48bb78"),
                new TimeSlot("9:10 - 10:00", "Biology", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("10:00 - 10:30", "BREAK", null, null, "#ecc94b"),
                new TimeSlot("10:30 - 11:20", "Geography", "Mrs. Okonkwo", "Block A-204", "#f6ad55"),
                new TimeSlot("11:20 - 12:10", "Physics", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("12:10 - 1:00", "Literature", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("1:00 - 2:00", "LUNCH", null, null, "#ecc94b"),
                new TimeSlot("2:00 - 2:50", "ICT", "Mr. Adeyemi", "Computer Lab", "#9f7aea")
        );
        timetable.put("Tuesday", tuesday);

        // Wednesday
        List<TimeSlot> wednesday = Arrays.asList(
                new TimeSlot("7:30 - 8:20", "Chemistry", "Dr. Nwachukwu", "Science Lab 2", "#9f7aea"),
                new TimeSlot("8:20 - 9:10", "Mathematics", "Mrs. Adebayo", "Block A-204", "#48bb78"),
                new TimeSlot("9:10 - 10:00", "English Language", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("10:00 - 10:30", "BREAK", null, null, "#ecc94b"),
                new TimeSlot("10:30 - 11:20", "History", "Mrs. Okonkwo", "Block A-204", "#f6ad55"),
                new TimeSlot("11:20 - 12:10", "French", "Mme. Dubois", "Block B-102", "#68d391"),
                new TimeSlot("12:10 - 1:00", "Biology", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("1:00 - 2:00", "LUNCH", null, null, "#ecc94b"),
                new TimeSlot("2:00 - 2:50", "Agriculture", "Mr. Okafor", "Farm Area", "#68d391")
        );
        timetable.put("Wednesday", wednesday);

        // Thursday
        List<TimeSlot> thursday = Arrays.asList(
                new TimeSlot("7:30 - 8:20", "Physics", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("8:20 - 9:10", "English Language", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("9:10 - 10:00", "Mathematics", "Mrs. Adebayo", "Block A-204", "#48bb78"),
                new TimeSlot("10:00 - 10:30", "BREAK", null, null, "#ecc94b"),
                new TimeSlot("10:30 - 11:20", "Geography", "Mrs. Okonkwo", "Block A-204", "#f6ad55"),
                new TimeSlot("11:20 - 12:10", "Literature", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("12:10 - 1:00", "ICT", "Mr. Adeyemi", "Computer Lab", "#9f7aea"),
                new TimeSlot("1:00 - 2:00", "LUNCH", null, null, "#ecc94b"),
                new TimeSlot("2:00 - 2:50", "Physical Education", "Coach Musa", "Sports Field", "#fc8181")
        );
        timetable.put("Thursday", thursday);

        // Friday
        List<TimeSlot> friday = Arrays.asList(
                new TimeSlot("7:30 - 8:20", "Mathematics", "Mrs. Adebayo", "Block A-204", "#48bb78"),
                new TimeSlot("8:20 - 9:10", "Chemistry", "Dr. Nwachukwu", "Science Lab 2", "#9f7aea"),
                new TimeSlot("9:10 - 10:00", "English Language", "Mr. Thompson", "Block A-204", "#63b3ed"),
                new TimeSlot("10:00 - 10:30", "BREAK", null, null, "#ecc94b"),
                new TimeSlot("10:30 - 11:20", "Biology", "Dr. Nwachukwu", "Science Lab 1", "#fc8181"),
                new TimeSlot("11:20 - 12:10", "History", "Mrs. Okonkwo", "Block A-204", "#f6ad55"),
                new TimeSlot("12:10 - 1:00", "French", "Mme. Dubois", "Block B-102", "#68d391"),
                new TimeSlot("1:00 - 2:00", "LUNCH", null, null, "#ecc94b"),
                new TimeSlot("2:00 - 2:50", "Religious Studies", "Pastor Adeyemi", "Block A-204", "#9f7aea")
        );
        timetable.put("Friday", friday);

        return timetable;
    }

    // Inner class for TimeSlot
    public static class TimeSlot {
        private String time;
        private String subject;
        private String teacher;
        private String room;
        private String color;

        public TimeSlot(String time, String subject, String teacher, String room, String color) {
            this.time = time;
            this.subject = subject;
            this.teacher = teacher;
            this.room = room;
            this.color = color;
        }

        // Getters
        public String getTime() { return time; }
        public String getSubject() { return subject; }
        public String getTeacher() { return teacher; }
        public String getRoom() { return room; }
        public String getColor() { return color; }

        // Check if this is a break/lunch period
        public boolean isBreak() {
            return subject != null && (subject.equals("BREAK") || subject.equals("LUNCH"));
        }
    }

    // Time slots for rows
    public List<String> getTimeSlots(){
        return Arrays.asList(
                "7:30 - 8:20",
                "8:20 - 9:10",
                "9:10 - 10:00",
                "10:00 - 10:30",  // Break
                "10:30 - 11:20",
                "11:20 - 12:10",
                "12:10 - 1:00",
                "1:00 - 2:00",    // Lunch
                "2:00 - 2:50"
        );
    }
    public List<String> getDays(){
        return Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
    }

    public Map<String, Integer> getStats(UUID timeTableId){
        try{
            return Map.of(
                    "totalPeriods", timeTableRepository.getTotalTimetablePeriods(timeTableId),
                    "subjectsCount", timeTableRepository.getTimeTableSubjectsCount(timeTableId)
            );
        } catch (Exception e) {
            logger.error("Error trying to return stats\n{}\n =================== STACK TRACE ================\n", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
