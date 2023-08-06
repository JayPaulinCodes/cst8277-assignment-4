/***************************************************************************
 * File:  AcademicStudentClub.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   12345678, Taeung, Park 
 *   12345678, Doyoung, Kim 
 *   12345678, Dawon, Jun 
 */
package acmecollege.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "1")
public class AcademicStudentClub extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public AcademicStudentClub() {
		super(true);
	}
}
