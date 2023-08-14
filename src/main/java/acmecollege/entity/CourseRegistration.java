/***************************************************************************
 * File:  CourseRegistration.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group NN
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   
 */
package acmecollege.entity;

import acmecollege.rest.serializer.CourseSerializer;
import acmecollege.rest.serializer.ProfessorSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@SuppressWarnings("unused")
/**
 * The persistent class for the course_registration database table.
 */
@Entity
@Table(name = "course_registration")
@Access(AccessType.FIELD)
@NamedQuery(name = CourseRegistration.FIND_ALL, query = "SELECT cr FROM CourseRegistration cr")
@NamedQuery(name = CourseRegistration.FIND_BY_ID, query = "SELECT cr FROM CourseRegistration cr WHERE cr.id.studentId = :param1 AND cr.id.courseId = :param2")
public class CourseRegistration extends PojoBaseCompositeKey<CourseRegistrationPK> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_ALL = "CourseRegistration.findAll";
	public static final String FIND_BY_ID = "CourseRegistration.findbyId";

	// Hint - What annotation is used for a composite primary key type?
	@EmbeddedId
	private CourseRegistrationPK id;

	// @MapsId is used to map a part of composite key to an entity.
	@MapsId("studentId")
	@ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
	private Student student;

	@MapsId("courseId")
	@ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
	private Course course;

	@ManyToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "professor_id", referencedColumnName = "professor_id", nullable = true)
	private Professor professor;

	@Column(name = "numeric_grade")
	private int numericGrade;

	@Column(length = 3, name = "letter_grade")
	private String letterGrade;


	public CourseRegistration() {
		id = new CourseRegistrationPK();
	}

	@Override
	public CourseRegistrationPK getId() {
		return id;
	}

	@Override
	public void setId(CourseRegistrationPK id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		id.setStudentId(student.id);
		this.student = student;
	}

//	@JsonBackReference(value = "courseRegistration-course-backref")
//	@JsonSerialize(using = CourseSerializer.class)
	@JsonIgnore
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		id.setCourseId(course.id);
		this.course = course;
	}

//	@JsonBackReference(value = "courseRegistration-professor-backref")
//	@JsonSerialize(using = ProfessorSerializer.class)
	@JsonIgnore
	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public int getNumericGrade() {
		return numericGrade;
	}
	
	public void setNumericGrade(int numericGrade) {
		this.numericGrade = numericGrade;
	}

	public String getLetterGrade() {
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	//Inherited hashCode/equals is sufficient for this entity class

}