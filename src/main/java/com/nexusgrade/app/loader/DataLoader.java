package com.nexusgrade.app.loader;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired private UserRepository userRepository;
    @Autowired private InstructorRepository instructorRepository;
    @Autowired private StudentRepository studentRepo;
    @Autowired private SubjectRepository subjectRepo;
    @Autowired private AssessmentRepository assessmentRepo;
    @Autowired private ResultRepository achievementRepo;
    @Autowired private ClassRepository classRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public void insertSampleData() {

        Logger logger = LoggerFactory.getLogger(DataLoader.class);

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

    @Override
    public void run(String... args) throws Exception {
        //insertSampleData();
    }
}
