package com.app.coursecenter.service.enrollmentService;


import com.app.coursecenter.entity.Course;
import com.app.coursecenter.exception.AuthenticationException;
import com.app.coursecenter.mapper.CourseMapper;
import com.app.coursecenter.repository.CourseRepository;
import com.app.coursecenter.repository.CourseReservationRepository;
import com.app.coursecenter.response.CourseResponse;
import com.app.coursecenter.service.CourseReservationCommandProducer;
import com.app.coursecenter.util.FindAuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final CourseReservationCommandProducer producer;
    private final CourseReservationRepository courseReservationRepository;
    private final CourseMapper courseMapper;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final CourseRepository courseRepository;

    public ReservationServiceImpl(CourseReservationCommandProducer producer, CourseReservationRepository courseReservationRepository, CourseMapper courseMapper, FindAuthenticatedUser findAuthenticatedUser, CourseRepository courseRepository) {
        this.producer = producer;
        this.courseReservationRepository = courseReservationRepository;
        this.courseMapper = courseMapper;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.courseRepository = courseRepository;
    }


    @Transactional
    @Override
    public List<CourseResponse> getRandomNonEnrolledCourses(int count) {

        Set<Long> enrolledCoursesIds = courseReservationRepository
                .findCoursesByUserId(getCurrentUserId())
                .stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        List<Long> allCourseIds = courseRepository.findAllIds();

        allCourseIds.removeAll(enrolledCoursesIds);

        Collections.shuffle(allCourseIds);
        List<Long> selectedIds = allCourseIds.stream()
                .limit(count)
                .collect(Collectors.toList());

        // Fetch full course objects - O(k) where k = count
        return courseRepository.findAllById(selectedIds).stream().map(courseMapper::courseToCourseResponse).toList();
    }

    @Override
    public void requestCourseReservation(Long courseId) {
        producer.sendReserveCourseCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }

    @Override
    public void cancelCourseReservation(Long courseId) {
        producer.sendCancelReservationCommand(getCurrentUserId(), getCurrentUserEmail(), courseId);
    }


    @Transactional
    @Override
    public List<CourseResponse> getEnrolledCourses() {
        return getCurrentUserId() != null ? courseReservationRepository.findCoursesByUserId(getCurrentUserId()).stream().
                map(courseMapper::courseToCourseResponse).toList() : new ArrayList<>();
    }


    private Long getCurrentUserId() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getId();
        } catch (AccessDeniedException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    private String getCurrentUserEmail() {
        try {
            return findAuthenticatedUser.getAuthenticatedUser().getEmail();
        } catch (AccessDeniedException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }


}
