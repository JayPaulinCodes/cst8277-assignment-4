/***************************************************************************
 * File:  CourseRegistrationPK.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Mike Norman
 * 
 * Updated by:  Group NN
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("unused")
/**
 * The primary key class for the course_registration database table.
 */
//TODO CRPK01 - What annotation is used to define an object which can be embedded in other entities?
//Hint - @Access is used to establish where the annotation for JPA will be placed, field or properties. 
@Access(AccessType.FIELD)
public class CourseRegistrationPK implements Serializable {
	// Default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "student_id", nullable = false)
	private int studentId;

	@Basic(optional = false)
	@Column(name = "course_id", nullable = false)
	private int courseId;

	public CourseRegistrationPK() {
	}

	public CourseRegistrationPK(int studentId, int courseId) {
		setStudentId(studentId);
		setCourseId(courseId);
	}

	public int getStudentId() {
		return this.studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc.  change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getStudentId(), getCourseId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof CourseRegistrationPK otherCourseRegistrationPK) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getStudentId(), otherCourseRegistrationPK.getStudentId()) &&
				Objects.equals(this.getCourseId(),  otherCourseRegistrationPK.getCourseId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CourseRegistrationPK [studentId = ");
		builder.append(studentId);
		builder.append(", courseId = ");
		builder.append(courseId);
		builder.append("]");
		return builder.toString();
	}

}
