package dev.coms4156.project.individualproject;

import java.io.Serial;
import java.io.Serializable;

/** This class represents a course within an educational institution. */
public class Course implements Serializable {

  /**
   * Constructs a new Course object with the given parameters. Initial count starts at 0.
   *
   * @param instructorName The name of the instructor teaching the course.
   * @param courseLocation The location where the course is held.
   * @param timeSlot The time slot of the course.
   * @param capacity The maximum number of students that can enroll in the course.
   */
  public Course(String instructorName, String courseLocation, String timeSlot, int capacity) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("Capacity must be greater than 0");
    }

    this.courseLocation = courseLocation;
    this.instructorName = instructorName;
    this.courseTimeSlot = timeSlot;
    this.enrollmentCapacity = capacity;
    this.enrolledStudentCount = 0;
  }

  /**
   * Enrolls a student in the course if there is space available.
   *
   * @return true if the student is successfully enrolled, false otherwise.
   */
  public boolean enrollStudent() {
    if (isCourseFull()) {
      return false;
    }

    enrolledStudentCount++;
    return true;
  }

  /**
   * Drops a student from the course if a student is enrolled.
   *
   * @return true if the student is successfully dropped, false otherwise.
   */
  public boolean dropStudent() {
    if (enrolledStudentCount <= 0) {
      return false;
    }

    enrolledStudentCount--;
    return true;
  }

  public String getCourseLocation() {
    return this.courseLocation;
  }

  public String getInstructorName() {
    return this.instructorName;
  }

  public String getCourseTimeSlot() {
    return this.courseTimeSlot;
  }

  public int getEnrolledStudentCount() {
    return this.enrolledStudentCount;
  }

  public int getEnrollmentCapacity() {
    return this.enrollmentCapacity;
  }

  /**
   * Returns a string representation of the course, including its instructor, location, and time.
   *
   * @return A string representing the course.
   */
  @Override
  public String toString() {
    return "Instructor: "
        + instructorName
        + "; Location: "
        + courseLocation
        + "; Time: "
        + courseTimeSlot;
  }

  public void reassignInstructor(String newInstructorName) {
    this.instructorName = newInstructorName;
  }

  public void reassignLocation(String newLocation) {
    this.courseLocation = newLocation;
  }

  public void reassignTime(String newTime) {
    this.courseTimeSlot = newTime;
  }

  public void setEnrolledStudentCount(int count) {
    this.enrolledStudentCount = count;
  }

  public boolean isCourseFull() {
    return enrolledStudentCount >= enrollmentCapacity;
  }

  @Serial private static final long serialVersionUID = 123456L;
  private final int enrollmentCapacity;
  private int enrolledStudentCount;
  private String courseLocation;
  private String instructorName;
  private String courseTimeSlot;
}
