package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/** This class represents a set of unit tests for {@code Department} class. */
@SpringBootTest
@ContextConfiguration
public class DepartmentUnitTests {
  /**
   * Unit tests setup.
   */
  @BeforeEach
  public void setupForTesting() {
    Course coms4156 = new Course("Gail Kaiser", "501 NWC", "10:10-11:25", 120);
    coms4156.setEnrolledStudentCount(109);
    Map<String, Course> courses = new HashMap<>();
    courses.put("4156", coms4156);
    testDepartment = new Department("COMS", courses, "Luca Carloni", 24);
  }

  @Test
  public void getNumberOfMajorsTest() {
    assertEquals(24, testDepartment.getNumberOfMajors());
  }

  @Test
  public void getDepartmentNameTest() {
    assertEquals("Luca Carloni", testDepartment.getDepartmentChair());
  }

  @Test
  public void toStringTest() {
    assertEquals(
        "COMS 4156:\n" + "Instructor: Gail Kaiser; Location: 501 NWC; Time: 10:10-11:25\n",
        testDepartment.toString());
  }

  @Test
  public void createCourseTest() {
    testDepartment.createCourse("testCourseId", "Oleksandr Loyko", "451 CSB", "10:10-11:25", 10);

    final Map<String, Course> expectedCourses = new HashMap<>();
    expectedCourses.put("4156", new Course("Gail Kaiser", "501 NWC", "10:10-11:25", 120));
    expectedCourses.put(
        "testCourseId", new Course("Oleksandr Loyko", "451 CSB", "10:10-11:25", 10));
    assertEquals(expectedCourses.toString(), testDepartment.getCourseSelection().toString());
  }

  @Test
  public void addCourseTest() {
    final Course testCourse = new Course("Oleksandr Loyko", "451 CSB", "10:10-11:25", 120);
    testDepartment.addCourse("addCourseTestId", testCourse);
    assertEquals(
        testDepartment.getCourseSelection().get("addCourseTestId").toString(),
        testCourse.toString());
  }

  @Test
  public void addPersonToMajorTest() {
    assertEquals(24, testDepartment.getNumberOfMajors());

    testDepartment.addPersonToMajor();
    assertEquals(25, testDepartment.getNumberOfMajors());

    testDepartment.addPersonToMajor();
    assertEquals(26, testDepartment.getNumberOfMajors());
  }

  @Test
  public void dropPersonFromMajorTest() {
    testDepartment = new Department("COMS", new HashMap<>(), "Luca Carloni", 1);

    assertEquals(1, testDepartment.getNumberOfMajors());

    testDepartment.dropPersonFromMajor();
    assertEquals(0, testDepartment.getNumberOfMajors());

    testDepartment.dropPersonFromMajor();
    assertEquals(0, testDepartment.getNumberOfMajors());
  }

  /** The test department instance used for testing. */
  public static Department testDepartment;
}
