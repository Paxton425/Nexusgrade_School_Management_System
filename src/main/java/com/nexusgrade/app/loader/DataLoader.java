package com.nexusgrade.app.loader;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.service.MarkingService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static java.util.Locale.filter;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private StudentRepository studentRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private AssessmentRepository assessmentRepo;
    @Autowired private ClassRepository classRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ActivityLogRepository activityLogRepository;
    @Autowired private AcademicCalendarRepository academicCalendarRepository;
    @Autowired private TimeTableRepository timeTableRepository;
    @Autowired private PeriodsRepository periodsRepository;
    @Autowired private AssessmentScoreRepository assessmentScoreRepository;
    @Autowired private AssessmentMarkRepository assessmentMarkRepository;
    @Autowired private MarkingService markingService;

    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public void insertSampleData() {

        try{
            logger.info("============= CLEARING ALL USERS FROM DATABASE ============");
            userRepository.deleteAll();
            logger.info("============= SEEDING DATABASE ============");
            User user = new User();
            user.setFirstName("Sthandiwe");
            user.setMiddleName("Nokwanda");
            user.setLastName("Zwane");
            user.setEmployeeId("EMP-001");
            user.setGender(User.Gender.FEMALE);
            user.setPhone("+278976543");
            user.setEmail("sthandiwe@email.com");
            user.setPassword(passwordEncoder.encode("Admin@Pass123")); // Hashes the text safely
            user.setRole(User.Role.ADMIN);
            user = userRepository.save(user);

            User user2 = new User();
            user2.setFirstName("Patrick");
            user2.setLastName("Mwelase");
            user2.setEmployeeId("EMP-002");
            user2.setGender(User.Gender.MALE);
            user2.setPhone("+27785123694");
            user2.setEmail("ptmwelase@email.com");
            user2.setPassword(passwordEncoder.encode("Admin2@Pass123")); // Hashes the text safely
            user2.setRole(User.Role.ADMIN);
            user2 = userRepository.save(user2);

            Instructor instructor = new Instructor();
            instructor.setDepartment(Department.HUMANITIES);
            instructor.setTitle(Instructor.Title.HOD);
            instructor.setUser(user);

            Instructor instructor2 = new Instructor();
            instructor2.setDepartment(Department.SCIENCES);
            instructor2.setTitle(Instructor.Title.HOD);
            instructor2.setUser(user2);

            instructorRepository.saveAll(List.of(instructor, instructor2));

            logger.info("============= DATABASE SEEDING COMPLETE ============");
        } catch (Exception e) {
            logger.error("======== DB SEEDING FAILED! =======\n"+e.getMessage());
        }
    }

    private void setActivityPerformers(){
        logger.info("============= POPULATING ACTIVITIES WITH PERFORMERS ============");
        try {
            User performer = userRepository.findByUsername("ptmwelase@email.com").orElseThrow(EntityNotFoundException::new);
            List<ActivityLog> activityLogs = activityLogRepository.findAll();
            for(ActivityLog log : activityLogs){
                log.setPerformedBy(performer);
            }
            activityLogRepository.saveAll(activityLogs);
        } catch (Exception e) {
            logger.error("======== DB SEEDING FAILED! =======\n{}", e.getMessage());
        }
        logger.info("============= ACTIVITIES SEEDING COMPLETE ============");
    }

    void loadAcademicCalender(){
        logger.info("============= SORTING TERMS ============");
        try{
            List<AssessmentScore> assessmentScores = assessmentScoreRepository.findAll();
            List<AcademicCalendar> calendars = academicCalendarRepository.findAll();
            for(AcademicCalendar calendar : calendars)
                for(AssessmentScore assessmentScore : assessmentScores)
                    if(assessmentScore.getAcademicCalendar().getCurrentTerm().equals(calendar.getCurrentTerm()))
                        assessmentScore.setAcademicCalendar(calendar);

            assessmentScoreRepository.saveAll(assessmentScores);
        } catch (Exception e) {
            logger.error("======== DB SEEDING FAILED! =======\n{}", e.getMessage());
        }
        logger.info("============= SORTING COMPLETE ============");
    }

    private void seedTimetable(){
        logger.info("============= SEEDING TIME TABLE SUBJECTS ============");
        try{
            // Create TimeTable data to save in database
            List<TimeTablePeriod> periods = periodsRepository.findAll();
            List<Subject> subjects = subjectRepo.getSubjectsByGrade(10);
            Function<Long, Subject> subjectMatchSupplier = (subjectId) -> subjects.stream()
                    .filter(s -> Objects.equals(s.getId(), subjectId))
                    .findFirst().orElse(null);

            for(TimeTablePeriod p : periods){
                switch (p.getColor()){
                    case BLUE:
                        p.setSubject(subjectMatchSupplier.apply(37L));
                        break;
                    case TEAL:
                        p.setSubject(subjectMatchSupplier.apply(1L));
                        break;
                    case PURPLE:
                        p.setSubject(subjectMatchSupplier.apply(2L));
                        break;
                    case PINK:
                        p.setSubject(subjectMatchSupplier.apply(10L));
                        break;
                    case RED:
                        p.setSubject(subjectMatchSupplier.apply(36L));
                        break;
                }
            }

            periodsRepository.saveAll(periods);
        }catch (Exception e) {
            logger.error("\n======== TIME TABLE SUBJECTS SEEDING FAILED! =======\n{}", e.getMessage());
        }
        logger.info("============= TIME TABLE SUBJECTS INJECTION COMPLETE ============");
    }

    private void injectUserTitles(){
        logger.error("================== INITIALIZING USER TITLE INJECTION ======================");
        try {
            List<User> users = userRepository.findAll();
            for(User user : users){
                if(user.getEmployeeId().equals("EMP-001"))
                    user.setTitle(User.Title.MRS);
                else if(user.getEmployeeId().equals("EMP-002"))
                    user.setTitle(User.Title.MR);
                else
                    throw new EntityNotFoundException("User did not match either EMPLOYEE-ID");
            }

            userRepository.saveAll(users);
        } catch (Exception e) {
            logger.error("================== TITLE INJECTION FAILED ======================");
        }
        logger.error("================== TITLE INJECTION COMPLETE ======================");
    }

    private void setPeriodInstructors(){
        logger.error("================== SETTING PERIOD INSTRUCTORS ======================");
        try {
            List<Instructor> instructors = instructorRepository.findAll();
            List<TimeTablePeriod> periods = periodsRepository.findAll();
            periods.forEach(p -> p.setInstructor(instructors.get(randomInt(instructors.size()))));
            periodsRepository.saveAll(periods);
        } catch (Exception e) {
            logger.error("================== FAILED ======================");
        }
        logger.error("================== SETTING PERIOD INSTRUCTORS COMPLETE ======================");
    }

    private void setColors(){
        logger.error("================== STARTING STUDENT COLOR SETTING ======================");
        try{
            List<Student> students = studentRepo.findAll();
            Color[] colors = Color.values();
            for(Student student : students){
                student.setColor(colors[randomInt(colors.length)]);
            }
            studentRepo.saveAll(students);
        } catch (Exception e) {
            logger.error("================== COLOR SETTING FAILED ======================");
            e.printStackTrace();
        }
        logger.error("================== COLOR SETTING COMPLETE!! ======================");
    }

    private void setStudenyIDS(){
        logger.error("================== STARTING STUDENT ID SETTINGS ======================");
        try{
            List<Student> students = studentRepo.findAll();
            LocalDate today = LocalDate.now();
            for (int i = 0; i < students.size(); i++) {
                // "%tY" extracts the 4-digit year from 'today'
                // "%04d" formats the integer 'i'
                String code = String.format("STU-%tY-%04d", today, (i+1));

                students.get(i).setStudentCode(code);
            }
            studentRepo.saveAll(students);
        } catch (Exception e) {
            logger.error("================== STUDENT IDS SETTING FAILED ======================");
            e.printStackTrace();
        }
        logger.error("================== STUDENT IDS SETTING COMPLETE!! ======================");
    }

    private void markAllScores(){
        logger.error("================== Marking ======================");
        try{
            List<AssessmentScore> allScores = assessmentScoreRepository.findAll();
            markingService.markAllScores(allScores); //Mark and saves scores
        } catch (Exception e) {
            logger.error("================== Marking FAILED ======================");
            e.printStackTrace();
        }
        logger.error("================== Marking COMPLETE!! ======================");
    }

    private int randomInt(int bound){
        return new Random().nextInt(bound);
    }

    @Override
    public void run(String... args) throws Exception {
        //insertSampleData();
        //setActivityPerformers();
        //seedTimetable();
        //injectUserTitles();
        //setPeriodInstructors();
        //setColors();
        //setStudenyIDS();
        //markAllScores();
    }
}
