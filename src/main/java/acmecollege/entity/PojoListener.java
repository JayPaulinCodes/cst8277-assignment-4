/***************************************************************************
 * File:  PojoListener.java Course materials (23S) CST 8277
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

public class PojoListener {

	// TODO PL01 - What annotation is used when we want to do something just before object is INSERT'd in the database?
	public void setCreatedOnDate(PojoBase pojoBase) {
		LocalDateTime now = LocalDateTime.now();
		// TODO PL02 - What member field(s) do we wish to alter just before object is INSERT'd in the database?
	}

	// TODO PL03 - What annotation is used when we want to do something just before object is UPDATE'd in the database?
	public void setUpdatedDate(PojoBase pojoBase) {
		// TODO PL04 - What member field(s) do we wish to alter just before object is UPDATE'd in the database?
	}

}
