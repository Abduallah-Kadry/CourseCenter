package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.mapper.StudentMapper;
import com.app.coursecenter.repository.StudentRepository;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.service.CourseReservationCommandProducer;
import com.app.coursecenter.util.FindAuthenticatedStudent;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final FindAuthenticatedStudent findAuthenticatedStudent;
    private final CourseReservationCommandProducer producer;

    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentMapper studentMapper,
                              PasswordEncoder passwordEncoder,
                              FindAuthenticatedStudent findAuthenticatedStudent,
                              CourseReservationCommandProducer producer) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.findAuthenticatedStudent = findAuthenticatedStudent;
        this.producer = producer;
    }

    // ---------------- Course Reservation Commands ----------------

    public void requestCourseReservation(Long courseId) {
        producer.sendReserveCourseCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }

    public void cancelCourseReservation(Long courseId) {
        producer.sendCancelReservationCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }

    private Long getCurrentUserId() {
        try {
            return findAuthenticatedStudent.getAuthenticatedStudent().getId();
        } catch (AccessDeniedException e) {
            throw new RuntimeException("Unauthorized access", e);
        }
    }

    private String getCurrentUserEmail() {
        try {
            return findAuthenticatedStudent.getAuthenticatedStudent().getEmail();
        } catch (AccessDeniedException e) {
            throw new RuntimeException("Unauthorized access", e);
        }
    }

    // ---------------- Profile Information ----------------

    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentInfo() throws AccessDeniedException {
        Student student = findAuthenticatedStudent.getAuthenticatedStudent();
        return studentMapper.map(student);
    }

    // ---------------- Account Deletion ----------------

    @Override
    public void deleteStudent() throws AccessDeniedException {
        Student student = findAuthenticatedStudent.getAuthenticatedStudent();

        if (isLastAdmin(student)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot delete itself");
        }

        studentRepository.delete(student);
    }

    // ---------------- Password Update ----------------

    @Override
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        Student student = findAuthenticatedStudent.getAuthenticatedStudent();

        if (!isOldPasswordCorrect(student.getPassword(), passwordUpdateRequest.getOldPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password incorrect");
        }

        if (!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New passwords don't match");
        }

        if (!isNewPasswordDifferent(student.getPassword(), passwordUpdateRequest.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old and new passwords must be different");
        }

        student.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        studentRepository.save(student);
    }

    private boolean isOldPasswordCorrect(String encodedPassword, String oldRawPassword) {
        // ✅ Fix: first argument is raw, second is encoded
        return passwordEncoder.matches(oldRawPassword, encodedPassword);
    }

    private boolean isNewPasswordConfirmed(String newPassword, String confirmation) {
        return newPassword.equals(confirmation);
    }

    private boolean isNewPasswordDifferent(String oldEncodedPassword, String newRawPassword) {
        // compare using matches to avoid comparing encoded hash to raw
        return !passwordEncoder.matches(newRawPassword, oldEncodedPassword);
    }

    private boolean isLastAdmin(Student student) {
        boolean isAdmin = student.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
        if (isAdmin) {
            long adminCount = studentRepository.countAdminStudents();
            return adminCount <= 1;
        }
        return false;
    }
}
