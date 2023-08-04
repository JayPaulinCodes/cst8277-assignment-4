/***************************************************************************
 * File:  PojoCompositeListener.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 *
 * Updated by:  Group NN
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 *   studentId, firstName, lastName (as from ACSIS)
 * 
 */
package acmecollege.entity;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@SuppressWarnings("unused")

public class PojoCompositeListener {

	// TODO PCL01 - What annotation is used when we want to do something just before object is INSERT'd into database?
	public void setCreatedOnDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		LocalDateTime now = LocalDateTime.now();
		// TODO PCL02 - What member field(s) do we wish to alter just before object is INSERT'd in the database?
	}

	// TODO PCL03 - What annotation is used when we want to do something just before object is UPDATE'd into database?
	public void setUpdatedDate(PojoBaseCompositeKey<?> pojoBaseComposite) {
		// TODO PCL04 - What member field(s) do we wish to alter just before object is UPDATE'd in the database?
	}

}
