/***************************************************************************
 * File:  Course.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   041053188, Taeung, Park 
 *   041065803, Doyoung, Kim 
 *   041053986, Dawon, Jun 
 */
package acmecollege.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")

/**
 * The persistent class for the course database table.
 */
@Entity
@Table(name = "course")
@NamedQuery(name = Course.ALL_COURSES_QUERY, query = "SELECT c FROM Course c")
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
public class Course extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_COURSES_QUERY = "Course.findAll";

	@Basic(optional = false)
	@Column(name = "course_code", nullable = false, length = 7)
	private String courseCode;

	@Basic(optional = false)
	@Column(name = "course_title", nullable = false, length = 100)
	private String courseTitle;

	@Basic(optional = false)
	@Column(name = "year",  nullable = false)
	private int year;

	@Basic(optional = false)
	@Column(name = "semester",  nullable = false, length = 6)
	private String semester;

	@Basic(optional = false)
	@Column(name = "credit_units",  nullable = false)
	private int creditUnits;

	@Basic(optional = false)
	@Column(name = "online",  nullable = false)
	private byte online;

	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "course")
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public Course() {
		super();
	}

	public Course(String courseCode, String courseTitle, int year, String semester, int creditUnits, byte online) {
		this();
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.year = year;
		this.semester = semester;
		this.creditUnits = creditUnits;
		this.online = online;
	}

	public Course setCourse(String courseCode, String courseTitle, int year, String semester, int creditUnits, byte online) {
		setCourseCode(courseCode);
		setCourseTitle(courseTitle);
		setYear(year);
		setSemester(semester);
		setCreditUnits(creditUnits);
		setOnline(online);
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getCreditUnits() {
		return creditUnits;
	}

	public void setCreditUnits(int creditUnits) {
		this.creditUnits = creditUnits;
	}

	public byte getOnline() {
		return online;
	}

	public void setOnline(byte online) {
		this.online = online;
	}
	
	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	//Inherited hashCode/equals is sufficient for this Entity class

}
