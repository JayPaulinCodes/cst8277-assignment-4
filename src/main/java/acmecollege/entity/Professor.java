/***************************************************************************
 * File:  Professor.java Course materials (23S) CST 8277
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("unused")

/**
 * The persistent class for the professor database table.
 */
//Hint - @Entity marks this class as an entity which needs to be mapped by JPA.
//Hint - @Entity does not need a name if the name of the class is sufficient.
//Hint - @Entity name does not matter as long as it is consistent across the code.
@Entity
//Hint - @Table defines a specific table on DB which is mapped to this entity.
@Table(name = "professor")
//Hint - @NamedQuery attached to this class which uses JPQL/HQL.  SQL cannot be used with NamedQuery.
//Hint - @NamedQuery uses the name which is defined in @Entity for JPQL, if no name is defined use class name.
//Hint - @NamedNativeQuery can optionally be used if there is a need for SQL query.
@NamedQuery(name = "Professor.findAll", query = "SELECT p FROM Professor p left join fetch p.courseRegistrations")
@NamedQuery(name = Professor.IS_DUPLICATE_QUERY_NAME, query = "SELECT count(p) FROM Professor p where p.firstName = :param1 and p.lastName = :param2 and p.department = :param3")
@NamedQuery(name = Professor.QUERY_PROFESSOR_BY_NAME_DEPARTMENT, query = "SELECT p FROM Professor p where p.firstName = :param1 and p.lastName = :param2 and p.department = :param3")
//Hint - @AttributeOverride can override column details.  This entity uses professor_id as its primary key name, it needs to override the name in the mapped super class.
@AttributeOverride(name = "id", column = @Column(name = "professor_id"))
//Hint - PojoBase is inherited by any entity with integer as their primary key.
//Hint - PojoBaseCompositeKey is inherited by any entity with a composite key as their primary key.
public class Professor extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

    public static final String IS_DUPLICATE_QUERY_NAME = "Professor.isDuplicate";
    public static final String QUERY_PROFESSOR_BY_NAME_DEPARTMENT = "Professor.findByNameDepartment";

	// Hint - @Basic(optional = false) is used when the object cannot be null.
	// Hint - @Basic or none can be used if the object can be null.
	// Hint - @Basic is for checking the state of object at the scope of our code.
	@Basic(optional = false)
	// Hint - @Column is used to define the details of the column which this object will map to.
	// Hint - @Column is for mapping and creation (if needed) of an object to DB.
	// Hint - @Column can also be used to define a specific name for the column if it is different than our object name.
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Basic(optional = false)
	@Column(name = "department", nullable = false, length = 50)
	private String department;

	// Hint - @Transient is used to annotate a property or field of an entity class, mapped superclass, or embeddable class which is not persistent.
	@Transient
	private String highestEducationalDegree; // Examples:  BS, MS, PhD, DPhil, MD, etc.
	
	@Transient
	private String specialization; // Examples:  Computer Science, Mathematics, Physics, etc.

	// Hint - @OneToMany is used to define 1:M relationship between this entity and another.
	// Hint - @OneToMany option cascade can be added to define if changes to this entity should cascade to objects.
	// Hint - @OneToMany option cascade will be ignored if not added, meaning no cascade effect.
	// Hint - @OneToMany option fetch should be lazy to prevent eagerly initializing all the data.
	@OneToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "professor")
	// Hint - java.util.Set is used as a collection, however List could have been used as well.
	// Hint - java.util.Set will be unique and also possibly can provide better get performance with HashCode.
	private Set<CourseRegistration> courseRegistrations = new HashSet<>();

	public Professor() {
		super();
	}
	
	public Professor(String firstName, String lastName, String department, Set<CourseRegistration> courseRegistrations) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.courseRegistrations = courseRegistrations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHighestEducationalDegree() {
		return highestEducationalDegree;
	}

	public void setHighestEducationalDegree(String highestEducationalDegree) {
		this.highestEducationalDegree = highestEducationalDegree;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Set<CourseRegistration> getCourseRegistrations() {
		return courseRegistrations;
	}

	public void setCourseRegistrations(Set<CourseRegistration> courseRegistrations) {
		this.courseRegistrations = courseRegistrations;
	}

	public void setProfessor(String firstName, String lastName, String department) {
		setFirstName(firstName);
		setLastName(lastName);
		setDepartment(department);
	}

	//Inherited hashCode/equals is sufficient for this entity class

}
