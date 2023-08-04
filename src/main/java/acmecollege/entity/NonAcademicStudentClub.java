/***************************************************************************
 * File:  NonAcademicStudentClub.java Course materials (23S) CST 8277
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

//TODO NASC01 - Add missing annotations, please see Week 9 slides page 15.  Value 1 is academic and value 0 is non-academic.
public class NonAcademicStudentClub extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public NonAcademicStudentClub() {
		super(false);

	}
}