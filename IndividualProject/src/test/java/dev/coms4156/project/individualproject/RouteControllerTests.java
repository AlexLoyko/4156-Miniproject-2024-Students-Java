package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/** This class represents a set of unit tests for {@code RouteController} class. */
@SpringBootTest
@ContextConfiguration
public class RouteControllerTests {
  /**
   * Unit tests setup.
   */
  @BeforeAll
  public static void setup() {
    testApplication = new IndividualProjectApplication();
    testApplication.run(new String[] {"setup"});
    testController = new RouteController();
  }

  @BeforeEach
  public void beforeEach() {
    testApplication.resetDataFile();
  }

  @Test
  public void indexTest() {
    final String responseString = testController.index();
    assertTrue(responseString.contains("Welcome"));
    assertTrue(responseString.contains("127.0.0.1:8080"));
  }

  @Test
  public void retrieveDepartmentSuccessTest() {
    final String expectedDepartment =
        """
    COMS 3827:
    Instructor: Daniel Rubenstein; Location: 207 Math; Time: 10:10-11:25
    COMS 1004:
    Instructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55
    COMS 3203:
    Instructor: Ansaf Salleb-Aouissi; Location: 301 URIS; Time: 10:10-11:25
    COMS 4156:
    Instructor: Gail Kaiser; Location: 501 NWC; Time: 10:10-11:25
    COMS 3157:
    Instructor: Jae Lee; Location: 417 IAB; Time: 4:10-5:25
    COMS 3134:
    Instructor: Brian Borowski; Location: 301 URIS; Time: 4:10-5:25
    COMS 3251:
    Instructor: Tony Dear; Location: 402 CHANDLER; Time: 1:10-3:40
    COMS 3261:
    Instructor: Josh Alman; Location: 417 IAB; Time: 2:40-3:55
        """;

    final ResponseEntity<?> response = testController.retrieveDepartment("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedDepartment, response.getBody());
  }

  @Test
  public void retrieveDepartmentNotFoundTest() {
    final ResponseEntity<?> response = testController.retrieveDepartment("NON_EXISTENT_DEPT");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void retrieveDepartmentUnexpectedErrorTest() {
    final ResponseEntity<?> response = testController.retrieveDepartment(null);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("An Error has occurred", response.getBody());
  }

  @Test
  public void retrieveCourseSuccessTest() {
    final ResponseEntity<?> response = testController.retrieveCourse("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(
        "Instructor: Gail Kaiser; Location: 501 NWC; Time: 10:10-11:25", response.getBody());
  }

  @Test
  public void retrieveCourseDepartmentNotFoundTest() {
    final ResponseEntity<?> response = testController.retrieveCourse("NON_EXISTENT_DEPT", 4156);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void retrieveCourseCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.retrieveCourse("COMS", 9009);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void isCourseFullNotFalseTest() {
    testController.setEnrollmentCount("COMS", 4156, 0);

    final ResponseEntity<?> response = testController.isCourseFull("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(false, response.getBody());
  }

  @Test
  public void isCourseFullNotTrueTest() {
    testController.setEnrollmentCount("COMS", 4156, 1_000_000);

    final ResponseEntity<?> response = testController.isCourseFull("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(true, response.getBody());
  }

  @Test
  public void isCourseFullCourseNotFound() {
    final ResponseEntity<?> response = testController.isCourseFull("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void getMajorCtFromDepartmentSuccessTest() {
    final ResponseEntity<?> response = testController.getMajorCtFromDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("There are: 2700 majors in the department", response.getBody());
  }

  @Test
  public void getMajorCtFromDepartmentDeptNotFoundTest() {
    final ResponseEntity<?> response = testController.getMajorCtFromDept("DOESNT_EXIST");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void identifyDepartmentChairSuccessTest() {
    final ResponseEntity<?> response = testController.identifyDeptChair("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Luca Carloni is the department chair.", response.getBody());
  }

  @Test
  public void identifyDepartmentChairDeptNotFoundTest() {
    final ResponseEntity<?> response = testController.identifyDeptChair("DOESNT_EXIST");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void findCourseLocationSuccessTest() {
    final ResponseEntity<?> response = testController.findCourseLocation("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("501 NWC is where the course is located.", response.getBody());
  }

  @Test
  public void findCourseLocationCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.findCourseLocation("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void findCourseInstructorSuccessTest() {
    final ResponseEntity<?> response = testController.findCourseInstructor("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Gail Kaiser is the instructor for the course.", response.getBody());
  }

  @Test
  public void findCourseInstructorCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.findCourseInstructor("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void findCourseTimeSuccessTest() {
    final ResponseEntity<?> response = testController.findCourseTime("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("The course meets at: 10:10-11:25", response.getBody());
  }

  @Test
  public void findCourseTimeCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.findCourseTime("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void addMajorToDepartmentSuccessTest() {
    final ResponseEntity<?> response = testController.addMajorToDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated successfully", response.getBody());
  }

  @Test
  public void addMajorToDepartmentDeptNotFoundTest() {
    final ResponseEntity<?> response = testController.addMajorToDept("DOESNT_EXIST");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void removeMajorFromDepartmentSuccessTest() {
    final ResponseEntity<?> response = testController.removeMajorFromDept("COMS");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attribute was updated or is at minimum", response.getBody());
    assertEquals(
        "There are: 2699 majors in the department",
        testController.getMajorCtFromDept("COMS").getBody());
  }

  @Test
  public void removeMajorFromDepartmentDeptNotFoundTest() {
    final ResponseEntity<?> response = testController.removeMajorFromDept("DOESNT_EXIST");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Department Not Found", response.getBody());
  }

  @Test
  public void dropStudentFromCourseSuccessTest() {
    final ResponseEntity<?> response = testController.dropStudent("COMS", 4156);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Student has been dropped.", response.getBody());
  }

  @Test
  public void dropStudentFromCourseNoDeparmentTest() {
    final ResponseEntity<?> response = testController.dropStudent("DOESNT_EXIST", 4156);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void dropStudentFromCourseNoCourseTest() {
    final ResponseEntity<?> response = testController.dropStudent("COMS", 101);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void dropStudentFromCourseNooneToDropTest() {
    testController.setEnrollmentCount("COMS", 4156, 0);
    final ResponseEntity<?> response = testController.dropStudent("COMS", 4156);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Student has not been dropped.", response.getBody());
  }

  @Test
  public void setEnrollmentCountSuccessTest() {
    final ResponseEntity<?> response = testController.setEnrollmentCount("COMS", 4156, 1);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attributed was updated successfully.", response.getBody());
  }

  @Test
  public void setEnrollmentDeptNotFoundTest() {
    final ResponseEntity<?> response = testController.setEnrollmentCount("DOESNT_EXIST", 4156, 1);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void setEnrollmentCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.setEnrollmentCount("COMS", 101, 1);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseTimeSuccessTest() {
    final ResponseEntity<?> response = testController.changeCourseTime("COMS", 4156, "9:00-10:30");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attributed was updated successfully.", response.getBody());

    String updatedCourse = testController.retrieveCourse("COMS", 4156).getBody();
    assertEquals("Instructor: Gail Kaiser; Location: 501 NWC; Time: 9:00-10:30", updatedCourse);
  }

  @Test
  public void changeCourseTimeDeptNotFoundTest() {
    final ResponseEntity<?> response =
        testController.changeCourseTime("DOESNT_EXIST", 4156, "9:00-10:30");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseTimeCourseNotFoundTest() {
    final ResponseEntity<?> response = testController.changeCourseTime("COMS", 101, "9:00-10:30");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseTeacherSuccessTest() {
    final ResponseEntity<?> response =
        testController.changeCourseTeacher("COMS", 4156, "Oleksandr Loyko");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attributed was updated successfully.", response.getBody());

    String updatedCourse = testController.retrieveCourse("COMS", 4156).getBody();
    assertEquals(
        "Instructor: Oleksandr Loyko; Location: 501 NWC; Time: 10:10-11:25", updatedCourse);
  }

  @Test
  public void changeCourseTeacherDeptNotFoundTest() {
    final ResponseEntity<?> response =
        testController.changeCourseTeacher("DOESNT_EXIST", 4156, "Oleksandr Loyko");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseTeacherCourseNotFoundTest() {
    final ResponseEntity<?> response =
        testController.changeCourseTeacher("COMS", 101, "Oleksandr Loyko");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseLocationSuccessTest() {
    final ResponseEntity<?> response = testController.changeCourseLocation("COMS", 4156, "451 CSB");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Attributed was updated successfully.", response.getBody());

    String updatedCourse = testController.retrieveCourse("COMS", 4156).getBody();
    assertEquals("Instructor: Gail Kaiser; Location: 451 CSB; Time: 10:10-11:25", updatedCourse);
  }

  @Test
  public void changeCourseLocationDeptNotFoundTest() {
    final ResponseEntity<?> response =
        testController.changeCourseLocation("DOESNT_EXIST", 4156, "Oleksandr Loyko");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  @Test
  public void changeCourseLocationCourseNotFoundTest() {
    final ResponseEntity<?> response =
        testController.changeCourseLocation("COMS", 101, "Oleksandr Loyko");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Course Not Found", response.getBody());
  }

  public static IndividualProjectApplication testApplication;
  public static RouteController testController;
}
