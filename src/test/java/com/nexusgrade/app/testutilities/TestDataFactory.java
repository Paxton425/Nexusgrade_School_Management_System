package com.nexusgrade.app.testutilities;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.model.Student.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class TestDataFactory {

    private static Random random = new Random();

    // ===================== MOCK POOLS =========================
    private static final String[] FIRST_NAMES = {"James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda"};
    private static final String[] MIDDLE_NAMES = {"Alexander", "Grace", "David", "Elizabeth", "Edward", "Marie", "Thomas", "Rose"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
    private static final String[] EMAIL_DOMAINS = {"gmail.com", "yahoo.com", "outlook.com", "example.com"};

    // ==================== STUDENT FACTORIES ====================
    public static Student createRandomStudent(SchoolClass schoolClass) {
        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setFirstName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
        student.setLastName(LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
        student.setSchoolClass(schoolClass);
        student.setStatus(Status.values()[random.nextInt(Status.values().length)]);
        student.setGender(Student.Gender.values()[random.nextInt(2)]);

        // FIXED: Resolved 20007 bounds exception and fixed unstable day/month combinations crashing February
        int year = random.nextInt(2007, 2014); // 2007 to 2013 inclusive
        int month = random.nextInt(1, 13);
        int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
        student.setBirthDay(LocalDate.of(year, month, random.nextInt(1, maxDay + 1)));

        return student;
    }

    public static List<Student> createStudentList(int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(createRandomStudent(null));
        }
        return students;
    }

    public static List<Student> createStudentsForClass(SchoolClass schoolClass, int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(createRandomStudent(schoolClass));
        }
        return students;
    }

    // ==================== CLASS SESSION FACTORIES ====================

    public static ClassSession createClassSession(Subject subject, SchoolClass schoolClass, Instructor instructor, List<ClassAttendance> attendance) {
        ClassSession session = new ClassSession();
        session.setId(UUID.randomUUID());
        session.setSessionDate(LocalDate.now());
        session.setStartTime(LocalTime.of(9, 0));
        session.setEndTime(LocalTime.of(10, 0));
        session.setCancelled(false);
        session.setCreatedAt(LocalDateTime.now());
        session.setSubject(subject);
        session.setSchoolClass(schoolClass);
        session.setInstructor(instructor);
        return session;
    }

    // ==================== CLASS ATTENDANCE FACTORIES ====================

    public static ClassAttendance createAttendance(Student student, ClassSession session) {
        return createAttendance(student, session, ClassAttendance.AttendanceStatus.PRESENT);
    }

    public static ClassAttendance createAttendance(Student student, ClassSession session,
                                                   ClassAttendance.AttendanceStatus status) {
        ClassAttendance attendance = new ClassAttendance();
        attendance.setId(random.nextLong() & Long.MAX_VALUE);
        attendance.setStudent(student);
        attendance.setClassSession(session);
        attendance.setStatus(status);
        attendance.setExcused(false);
        attendance.setCreatedAt(LocalDateTime.now());
        attendance.setUpdatedAt(LocalDateTime.now());
        return attendance;
    }

    public static ClassAttendance createAttendanceWithRemarks(Student student, ClassSession session,
                                                              ClassAttendance.AttendanceStatus status,
                                                              String remarks) {
        ClassAttendance attendance = createAttendance(student, session, status);
        attendance.setRemarks(remarks);
        return attendance;
    }

    public static ClassAttendance createExcusedAttendance(Student student, ClassSession session) {
        ClassAttendance attendance = createAttendance(student, session, ClassAttendance.AttendanceStatus.EXCUSED);
        attendance.setExcused(true);
        attendance.setExcuseReason("Doctor's appointment");
        attendance.setExcuseAttachmentUrl("/uploads/doctor-note-123.pdf");
        return attendance;
    }

    public static List<ClassAttendance> createAttendanceList(Student student, ClassSession session, int count) {
        return createAttendanceList(student, session, count, ClassAttendance.AttendanceStatus.PRESENT);
    }

    public static List<ClassAttendance> createAttendanceList(Student student, ClassSession session,
                                                             int count, ClassAttendance.AttendanceStatus status) {
        List<ClassAttendance> attendances = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attendances.add(createAttendance(student, session, status));
        }
        return attendances;
    }

    public static List<ClassAttendance> createMixedAttendanceList(Student student, ClassSession session) {
        return Arrays.asList(
                createAttendance(student, session, ClassAttendance.AttendanceStatus.PRESENT),
                createAttendance(student, session, ClassAttendance.AttendanceStatus.ABSENT),
                createAttendance(student, session, ClassAttendance.AttendanceStatus.LATE),
                createAttendance(student, session, ClassAttendance.AttendanceStatus.EXCUSED),
                createAttendance(student, session, ClassAttendance.AttendanceStatus.LEFT_EARLY)
        );
    }

    // ==================== SCHOOL CLASS FACTORIES ====================

    public static SchoolClass createSchoolClass(String title, int grade, List<Student> students, List<Instructor> instructors, List<Subject> subjects) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId((long) (Math.random() * 1000));
        schoolClass.setTitle(title);
        schoolClass.setGrade(grade);
        schoolClass.setClassYear(LocalDate.now().getYear());
        schoolClass.setStudents(students);
        schoolClass.setInstructors(instructors);
        schoolClass.setSubjects(subjects);
        return schoolClass;
    }

    public static SchoolClass createSchoolClass(int grade, String title) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId((long) (Math.random() * 1000));
        schoolClass.setTitle(title);
        schoolClass.setGrade(grade);
        schoolClass.setClassYear(LocalDate.now().getYear());
        schoolClass.setStudents(createStudentList(10));
        schoolClass.setInstructors(null);
        schoolClass.setSubjects(null);
        return schoolClass;
    }

    public static List<SchoolClass> createSchoolClassesWithStudents(int classesPerGradeCount){
        int cp = (classesPerGradeCount > 0)? classesPerGradeCount : 4;
        List<SchoolClass> SCList = new ArrayList<>();

        for(int i=0; i<4; i++) { //Across all four grades
            for(int j=0; j<cp; j++){
                String title = ((char)('A'+j))+"";
                // FIXED: Actually added the generated classes to the returning list container
                SCList.add(createSchoolClass(title, (i+1), createStudentList(10), createInstructorsList(10), createSubjectList(7)));
            }
        }

        return SCList;
    }

    // ==================== SUBJECT FACTORIES ====================

    public static Subject createSubject(String name, String code, int grade, Department department, List<SchoolClass> classes) {
        Subject subject = new Subject();
        subject.setId((long) (Math.random() * 1000));
        subject.setName(name);
        subject.setSubjectCode(code);
        subject.setGrade(grade);
        subject.setDepartment(department);
        subject.setSchoolClasses(classes);
        return subject;
    }

    public static List<Subject> createSubjectList(int limit){
        int actualLimit = (limit > 0 && limit <= 10) ? limit : 7;
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < actualLimit; i++) {
            subjects.add(createSubject("Subject " + i, "SUBJ" + i, 1, null, null));
        }
        return subjects;
    }

    // ==================== INSTRUCTOR FACTORIES ====================

    public static Instructor createRandomInstructor(Department department, List<SchoolClass> assignedClasses) {
        Instructor instructor = new Instructor();
        instructor.setId(UUID.randomUUID());
        instructor.setDepartment(department);
        instructor.setTitle(Instructor.Title.values()[new Random().nextInt(4)]);
        instructor.setAssignedClasses(assignedClasses);

        User user = new User();
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String middleName = MIDDLE_NAMES[random.nextInt(MIDDLE_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(MIDDLE_NAMES.length)];
        String email = firstName+"."+lastName+EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setGender(User.Gender.values()[new Random().nextInt(2)]);
        instructor.setUser(user);

        return instructor;
    }

    public static List<Instructor> createInstructorsList(int limit){
        List<Instructor> instructors = new ArrayList<>();

        for(int i=0; i<limit; i++)
            instructors.add(createRandomInstructor(
                    Department.values()[random.nextInt(Department.values().length)],
                    null));

        return instructors;
    }

    public static List<Instructor> createInstructorsListForClasses(List<SchoolClass> classes, int clasCountPerInstructor){
        int ccPerInst = (clasCountPerInstructor > 0 && clasCountPerInstructor < classes.size())?
                clasCountPerInstructor : 3;
        int startIndex = random.nextInt((classes.size()-ccPerInst));
        List<Instructor> instructors = new ArrayList<>();
        List<SchoolClass> assignedClasses = new ArrayList<>();

        for(int i=startIndex; i<ccPerInst; i++)
            assignedClasses.add(classes.get(i));

        for(int i=0; i<classes.size(); i++) {
            Instructor instructor = createRandomInstructor(
                    Department.values()[random.nextInt(Department.values().length)],
                    assignedClasses);
            instructors.add(instructor);
        }
        return instructors;
    }
}