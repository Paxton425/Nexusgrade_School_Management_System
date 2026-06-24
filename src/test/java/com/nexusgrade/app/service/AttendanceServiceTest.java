package com.nexusgrade.app.service;

import com.nexusgrade.app.model.*;
import com.nexusgrade.app.repository.ClassAttendanceRepository;
import com.nexusgrade.app.repository.ClassSessionRepository;
import com.nexusgrade.app.testutilities.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Attendance Service Tests")
class AttendanceServiceTest {

    @Mock
    private ClassAttendanceRepository classAttendanceRepository;

    @Mock
    private ClassSessionRepository classSessionRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    Logger logger = LoggerFactory.getLogger(AttendanceServiceTest.class);

    // Test data
    private SchoolClass testSClass;
    private Student testStudent;
    private Instructor testInstructor;
    private List<Subject> testSubjects;
    private ClassSession testSession;
    private ClassAttendance testAttendance;
    private List<ClassAttendance> testAttendances;

    @BeforeEach
    void setUp() {
        testSClass = TestDataFactory.createSchoolClass(10, "A");
        testStudent = TestDataFactory.createRandomStudent(testSClass);
        testInstructor = TestDataFactory.createRandomInstructor(Department.SCIENCES, null);

        testAttendance = new ClassAttendance();
        testAttendance.setStudent(testStudent);
        testAttendance.setClassSession(testSession);
        testAttendance.setStatus(ClassAttendance.AttendanceStatus.PRESENT);

        testSubjects = TestDataFactory.createSubjectList(10);

        testAttendances = TestDataFactory.createAttendanceList(testStudent, null, 10);

        testSession = TestDataFactory.createClassSession(testSubjects.getFirst(), testSClass, testInstructor, testAttendances);
    }

    @Test
    void markAttendancesTest_ShouldReturnTheSubmittedAttendances() {
        // ✅ Use the injected service (already has mocks)
        // Arrange
        when(classAttendanceRepository.saveAll(testAttendances))
                .thenReturn(testAttendances);

        //Act
        List<ClassAttendance> result = attendanceService.markAttendances(testAttendances);

        //Assert
        assertEquals(testAttendances, result);
        //Verify
        verify(classAttendanceRepository, times(1)).saveAll(testAttendances);
    }

    @Test
    @DisplayName("Attendances for Session/period Test")
    void getSessionAttendanceTest() {
        // Arrange
        when(classAttendanceRepository.findClassAttendancesByClassSession(testSession))
                .thenReturn(testAttendances);

        // Act
        List<ClassAttendance> result = attendanceService.getSessionAttendance(testSession);
        String info = """
               ===================================
               Attendances for Session/period Test
               ====================================
               ---------------Results--------------\n
               """;
        logger.info(info);
        for(ClassAttendance at : result)
            logger.info("Session ID: {}", at.getId());

        // Assert
        assertEquals(testAttendances, result);
        verify(classAttendanceRepository, times(1))
                .findClassAttendancesByClassSession(testSession);
    }


    @Test
    @DisplayName("Attendances by a student test")
    void getStudentAttendance() {
        //Arrange
        when(classAttendanceRepository.findClassAttendancesByStudent(testStudent))
                .thenReturn(testAttendances);

        //Act
        List<ClassAttendance> result = attendanceService.getStudentAttendance(testStudent);
        String info = """
               ===================================
               Attendances by a student test
               ====================================
               ---------------Results--------------\n
               """;
        logger.info(info);
        for(ClassAttendance at : result)
            logger.info("Session ID: {}", at.getId());

        //Assert
        assertEquals(testAttendances, result);
        verify(classAttendanceRepository, times(1))
                .findClassAttendancesByStudent(testStudent);
    }

    // ==================== SCENARIO 1: HAPPY PATH ====================

    @Test
    @DisplayName("Should return attendances when session exists with attendances")
    void getRecentAttendancesForClass_shouldReturnAttendances_whenSessionExists() {
        // Arrange
        Long classId = 10L;
        int limit, expectedSize;
        limit = expectedSize = testAttendances.size(); //Ensure Accurate expectation result will return a list with a size = limit
        testSession.setClassAttendances(testAttendances);

        when(classSessionRepository.findLastSessionByClassId(classId))
                .thenReturn(Optional.of(testSession));

        // Act
        List<ClassAttendance> result = attendanceService
                .getRecentAttendancesForClass(classId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(expectedSize, result.size());
        assertEquals(testAttendances, result);
        verify(classSessionRepository, times(1))
                .findLastSessionByClassId(classId);
    }

    // ==================== SCENARIO 2: NO SESSION ====================

    @Test
    @DisplayName("Should return empty list when no session exists")
    void getRecentAttendancesForClass_shouldReturnEmptyList_whenNoSessionExists() {
        // Arrange
        Long classId = 999L;

        when(classSessionRepository.findLastSessionByClassId(classId))
                .thenReturn(Optional.empty());  // ← No session!

        // Act
        List<ClassAttendance> result = attendanceService
                .getRecentAttendancesForClass(classId, 5);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(classSessionRepository, times(1))
                .findLastSessionByClassId(classId);
    }

    // ==================== SCENARIO 3: SESSION WITH NO ATTENDANCES ====================

    @Test
    @DisplayName("Should return empty list when session exists but has no attendances")
    void getRecentAttendancesForClass_shouldReturnEmptyList_whenNoAttendances() {
        // Arrange
        Long classId = 10L;
        testSession.setClassAttendances(null);  // ← No attendances!

        when(classSessionRepository.findLastSessionByClassId(classId))
                .thenReturn(Optional.of(testSession));

        // Act
        List<ClassAttendance> result = attendanceService
                .getRecentAttendancesForClass(classId, 5);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(classSessionRepository, times(1))
                .findLastSessionByClassId(classId);
    }

    @Test
    @DisplayName("Should return empty list when session has empty attendances list")
    void getRecentAttendancesForClass_shouldReturnEmptyList_whenEmptyAttendances() {
        // Arrange
        Long classId = 10L;
        testSession.setClassAttendances(new ArrayList<>());  // ← Empty list!

        when(classSessionRepository.findLastSessionByClassId(classId))
                .thenReturn(Optional.of(testSession));

        // Act
        List<ClassAttendance> result = attendanceService
                .getRecentAttendancesForClass(classId, 5);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== SCENARIO 4: LIMIT LOGIC ====================

    @ParameterizedTest
    @DisplayName("Should comply to limit logic")
    @CsvSource({
            "5, 5, 5",     // limit 5, have 5, expect 5
            "0, 10, 10",   // limit 0, default 10, expect 10
            "3, 20, 3",    // limit 3, have 20, expect 3
            "10, 5, 5"     // limit 10, have 5, expect 5
    })
    void getRecentAttendancesForClass_shouldRespectLimit(int limit, int totalAttendances, int expectedCount) {
        // Arrange
        Long classId = 10L;
        List<ClassAttendance> attendances = TestDataFactory.createAttendanceList(testStudent, testSession, totalAttendances);
        testSession.setClassAttendances(attendances);

        when(classSessionRepository.findLastSessionByClassId(classId))
                .thenReturn(Optional.of(testSession));

        // Act
        List<ClassAttendance> result = attendanceService
                .getRecentAttendancesForClass(classId, limit);
        String info = """
               ===================================
               Attendances Limit Logic Test
               ====================================
               ---------------Results--------------\n
               """;
        logger.info(info);
        for(ClassAttendance at : result)
            logger.info("Session ID: {}", at.getId());

        // Assert
        assertEquals(expectedCount, result.size());
    }


    @Test
    @DisplayName("Get recent attendances based on limit")
    void getRecentAttendances() {
    }

    @Test
    void autoMarkAbsentStudents() {
    }

    @Test
    void getTodayAttendanceRate() {
    }

    @Test
    void getWeeklyAverage() {
    }

    @Test
    void getMonthlyAverage() {
    }

    @Test
    void getAbsentCountToday() {
    }

    @Test
    void getOverallPerformance() {
    }
}