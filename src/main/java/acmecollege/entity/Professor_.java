package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-08-06T14:13:35.607-0400")
@StaticMetamodel(Professor.class)
public class Professor_ extends PojoBase_ {
	public static volatile SingularAttribute<Professor, String> firstName;
	public static volatile SingularAttribute<Professor, String> lastName;
	public static volatile SingularAttribute<Professor, String> department;
	public static volatile SetAttribute<Professor, CourseRegistration> courseRegistrations;
}
