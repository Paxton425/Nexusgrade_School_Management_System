package com.nexusgrade.app.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String employeeId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Title title;
    private String profileImage;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private Instructor instructor;
    @OneToMany(mappedBy = "performedBy")
    List<ActivityLog> activityLog;

    public enum Gender { MALE, FEMALE }
    public enum Role { INSTRUCTOR, CLERK, INSTRUCTOR_AND_ADMIN, ADMIN, DEVELOPER }
    public enum Title {
        MR("Mr."),
        MS("Ms."),
        MRS("Mrs."),
        DR("Dr."),
        COACH("Coach"),
        MX("Mx.");

        private final String label;

        Title(String label) {
            this.label = label;
        }

        public String getLabel() {
            return this.label;
        }
    }

    // --- Getters & Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    @Nullable
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(@Nullable Instructor instructor) { this.instructor = instructor; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<ActivityLog> getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(List<ActivityLog> activityLog) {
        this.activityLog = activityLog;
    }
}