package com.app.coursecenter.service.studentservice;

import com.app.coursecenter.dto.StudentDto;
import com.app.coursecenter.entity.Authority;
import com.app.coursecenter.entity.Student;
import com.app.coursecenter.mapper.StudentMapper;
import com.app.coursecenter.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentServiceImplTest")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudentServiceImplTest {
    /*
     1. inject the service using @injectmock
     2. inject all the needed deps of that service using @mock
     3. see if you can setup any objects that this service need
     */

    // we are using the concrete class now but if the application is bigger there is contract tests
    // as well as all implementations test (even if they share the same interface)

    // ! one major note here that we are using a global static class (SecurityContextConfig) in the testing
    // ! instead of wrapping it to be used in the test to separate the tests objects from the app objects

    // * well i need to revamp this tests to delete the abstarct class ... it is faliua3 ...
    // * i thought that i need to run the same auth test before each execution but this is just wrong the actual

    @InjectMocks
    StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Authentication authentication;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("testStudentFirstName");
        student.setLastName("testStudentLastName");
        student.setEmail("testStudentEmail@gmail.com");

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setFirstName("testStudentFirstName");
        studentDto.setLastName("testStudentLastName");
        studentDto.setEmail("testStudentEmail@gmail.com");

    }


    // clearing the context holder after each operation
    @AfterEach
    void clearSecurityContextStaticClass() {
        SecurityContextHolder.clearContext();
    }

    // this class should group authentication tests to be reused in both getInfo and update student password
    abstract class StudentAuthenticationTest {

        @Test
        @DisplayName("should throw AccessDeniedException when authentication is null")
        void getStudentInfo_WhenAuthenticationIsNull_ThrowsAccessDeniedException() {

            // given
            // there is no authentication object to see if the user is authenticated or not
            SecurityContextHolder.getContext().setAuthentication(null);
            // When
            // Then
            assertThrows(AccessDeniedException.class, () -> {
                studentService.getStudentInfo();
            });
            // do this for the next test
//            SecurityContextHolder.clearContext();

        }

        @Test
        @DisplayName("should throw AccessDeniedException when not authenticated")
        void getStudentInfo_WhenNotAuthenticated_ThrowsAccessDeniedException() {

            // given
            // if the authentication is false it won't continue checking for the other fileds of the authentication
            // object
            when(authentication.isAuthenticated()).thenReturn(false);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // When
            // Then
            assertThrows(AccessDeniedException.class, () -> {
                studentService.getStudentInfo();
            });
            // do this for the next test
//            SecurityContextHolder.clearContext();

        }

        @Test
        @DisplayName("should throw AccessDeniedException when user is anonymous (guest)")
        void getStudentInfo_WhenPrincipalIsAnonymousUser_ThrowsAccessDeniedException() {

            // given
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getPrincipal()).thenReturn("anonymousUser");

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // When
            // Then

            assertThrows(AccessDeniedException.class, () -> {
                studentService.getStudentInfo();
            });
//            SecurityContextHolder.clearContext();

        }

    }

    @Nested
    @DisplayName("getStudentInfo() Method")
    class StudentGetInfoMethod extends StudentAuthenticationTest {

        @Test
        void getStudentInfo_WhenStudentIsAuthenticated_ReturnProperObject() {
            // auth is not null
            // auth is authorized
            // auth principle is not anonymous user
            // 1.assert that getstudentinfo doesn't return null
            // 2. assert that the result is equal to the

            when(authentication.isAuthenticated()).thenReturn(true);

            when(authentication.getPrincipal()).thenReturn(student);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when getStudentINfo is called and the map function inside it is called then return the studentDto object
            when(studentMapper.map(student)).thenReturn(studentDto);

            StudentDto result;

            try {
                result = studentService.getStudentInfo();
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }

            assertNotNull(result);

            assertEquals(studentDto, result); // studentDTO have built in hashmap and equal function

            // this ensures that the mapper is being used to create the dto and there is no other way to create this dto
            // except for using the mapper
            // verify ensures that a dependency or a function is called
            verify(studentMapper).map(student);
//            SecurityContextHolder.clearContext();


        }
    }


    @Nested
    @DisplayName("deleteStudent() Method")
    class DeleteStudent extends StudentAuthenticationTest {
        // cases to test
        /*
            1. there is only one admin should throw
            2. there is more than one admin should delete the current admin
            3. there is one admin and the student is logged in so the student should delete himself
        */

        @Test
        void deleteStudent_whenOnlyOneAdmin_ThrowsResponseStatusException() {

            // when the user is authenticated and there is one admin should throw error

            when(authentication.isAuthenticated()).thenReturn(true);

            when(studentRepository.countAdminStudents()).thenReturn(1L);

            when(authentication.getPrincipal()).thenReturn(student);

            student.setAuthorities(List.of(new Authority("ROLE_ADMIN")));

            SecurityContextHolder.getContext().setAuthentication(authentication);


            assertThrows(ResponseStatusException.class, () -> {
                studentService.deleteStudent();
            });
        }

        @Test
        void deleteStudent_whenThereAreMoreAdmins_shouldDelete() {

            // when the user is authenticated and there is more than one admin should throw error

            when(authentication.isAuthenticated()).thenReturn(true);

            when(studentRepository.countAdminStudents()).thenReturn(3L);

            when(authentication.getPrincipal()).thenReturn(student);

            student.setAuthorities(List.of(new Authority("ROLE_ADMIN")));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // i want to write assert not throw something
            assertDoesNotThrow(()-> studentService.deleteStudent());

            verify(studentRepository,times(1)).delete(student);
            // you may use verifyNoInteractions() and add some mock to make sure some mock is not used

        }


        @Test
        void deleteStudent_whenThereOneAdminAndSeveralStudents_shouldDelete() {

            when(authentication.isAuthenticated()).thenReturn(true);

            when(authentication.getPrincipal()).thenReturn(student);

            student.setAuthorities(List.of(new Authority("ROLE_STUDENT")));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            assertDoesNotThrow(()-> studentService.deleteStudent());

            verify(studentRepository,times(1)).delete(student);
            // as this student is not student so i want to make sure that it implicitly passed the check for the admin
            // account

            verify(studentRepository,times(0)).countAdminStudents();

        }
    }



}