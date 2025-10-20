package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.mapper.StudentMapper;
import com.app.coursecenter.repository.StudentRepository;
import com.app.coursecenter.request.PasswordUpdateRequest;
import com.app.coursecenter.service.CourseReservationCommandProducer;
import com.app.coursecenter.util.FindAuthenticatedStudent;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper, PasswordEncoder passwordEncoder, FindAuthenticatedStudent findAuthenticatedStudent, CourseReservationCommandProducer producer) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.findAuthenticatedStudent = findAuthenticatedStudent;
        this.producer = producer;
    }

    public void requestCourseReservation(Long courseId) {
        producer.sendReserveCourseCommand(getCurrentUserId(),getCurrentUserEmail() ,courseId);
    }

    private Long getCurrentUserId() {
        try {
            return findAuthenticatedStudent.getAuthenticatedStudent().getId();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentUserEmail() {
        try {
            return studentRepository.findById(findAuthenticatedStudent.getAuthenticatedStudent().getId()).get().getEmail();
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    public void cancelCourseReservation(Long reservationId) {
        //producer.sendCancelReservationCommand(reservationId);
    }

    // prevent access if the user is using postman for example without using an actual student profile
    @Override
    @Transactional(readOnly = true)
    public StudentDto getStudentInfo() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Authentication Required");
        }

        Student student = (Student) authentication.getPrincipal();

        return studentMapper.map(student);
    }

    @Override
    public void deleteStudent() throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Authentication Required");
        }

        // ? whenever i see casting i think slowww code

        Student student = (Student) authentication.getPrincipal();

        if (isLastAdmin(student)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin cannot delete itself");
        }

        studentRepository.delete(student);
    }

    @Override
    public void updatePassword(PasswordUpdateRequest passwordUpdateRequest) throws AccessDeniedException {
        Student student = findAuthenticatedStudent.getAuthenticatedStudent();

        if (!isOldPasswordCorrect(student.getPassword(), passwordUpdateRequest.getOldPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old Password Incorrect");
        }

        if (!isNewPasswordConfirmed(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Passwords don't match");
        }

        if (!isNewPasswordDifferent(student.getPassword(), passwordUpdateRequest.getNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old and new passwords must be different");
        }

        student.setPassword(passwordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        studentRepository.save(student);
    }

    private boolean isOldPasswordCorrect(String currentPassword, String oldPassword) {
        return passwordEncoder.matches(currentPassword, oldPassword);
    }

    private boolean isNewPasswordConfirmed(String newPassword, String newPasswordConfirmation) {
        return newPassword.equals(newPasswordConfirmation);
    }

    private boolean isNewPasswordDifferent(String oldPassword, String newPassword) {
        return !newPassword.equals(oldPassword);
    }

    private boolean isLastAdmin(Student student) {
        boolean isAdmin = student.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            long adminCount = studentRepository.countAdminStudents();
            return adminCount <= 1;
        }
        return false;
    }
}
