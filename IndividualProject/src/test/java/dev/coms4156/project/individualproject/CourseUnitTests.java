package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/** This class represents a set of unit tests for {@code Course} class. */
@SpringBootTest
@ContextConfiguration
public class CourseUnitTests {
  @BeforeEach
  public void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }

  @Test
  public void invalidCapacityTest() {
    assertThrowsExactly(
            IllegalArgumentException.class,
            () -> new Course("Oleksandr Loyko", "417 IAB", "11:40-12:55", 0));
  }

  @Test
  public void getInstructorNameTest() {
    assertEquals("Griffin Newbold", testCourse.getInstructorName());
  }

  @Test
  public void reassignInstructorTest() {
    testCourse.reassignInstructor("Oleksandr Loyko");
    assertEquals("Oleksandr Loyko", testCourse.getInstructorName());
  }

  @Test
  public void getCourseLocationTest() {
    assertEquals("417 IAB", testCourse.getCourseLocation());
  }

  @Test
  public void reassignCourseLocationTest() {
    testCourse.reassignLocation("451 CSB");
    assertEquals("451 CSB", testCourse.getCourseLocation());
  }
  
  @Test
  public void getCourseTimeSlotTest() {
    assertEquals("11:40-12:55", testCourse.getCourseTimeSlot());
  }

  @Test
  public void reassignCourseTimeSlotTest() {
    testCourse.reassignTime("10:10-11:25");
    assertEquals("10:10-11:25", testCourse.getCourseTimeSlot());
  }

  @Test
  public void getEnrolledStudentCountTest() {
    assertEquals(0, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void getEnrollmentCapacityTest() {
    assertEquals(250, testCourse.getEnrollmentCapacity());
  }

  @Test
  public void toStringTest() {
    String expectedResult = "Instructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  public void enrollStudentSuccessTest() {
    testCourse.setEnrolledStudentCount(0);
    assertTrue(testCourse.enrollStudent());
    assertEquals(1, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void enrollStudentFailureTest() {
    testCourse.setEnrolledStudentCount(250);
    assertTrue(testCourse.isCourseFull());

    assertFalse(testCourse.enrollStudent());
    assertEquals(250, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void dropStudentSuccessTest() {
    testCourse.setEnrolledStudentCount(1);
    assertTrue(testCourse.dropStudent());
    assertEquals(0, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void dropStudentFailureTest() {
    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent());
    assertEquals(0, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void isCourseFullTest() {
    assertFalse(testCourse.isCourseFull());

    testCourse.setEnrolledStudentCount(249);
    assertFalse(testCourse.isCourseFull());

    testCourse.setEnrolledStudentCount(250);
    assertTrue(testCourse.isCourseFull());
  }

  /** The test course instance used for testing. */
  public static Course testCourse;
}
