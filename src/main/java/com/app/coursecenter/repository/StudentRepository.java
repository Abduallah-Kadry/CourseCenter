package com.app.coursecenter.repository;

import com.app.coursecenter.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    // the current logic is to assign the first user as the admin and 2nd to infinity to be role student

    @Query("SELECT COUNT(s) from Student s JOIN s.authorities a where a.authority = 'ROLE_ADMIN' ")
    long countAdminStudents();
}
