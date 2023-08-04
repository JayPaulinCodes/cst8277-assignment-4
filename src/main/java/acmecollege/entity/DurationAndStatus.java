/***************************************************************************
 * File:  DurationAndStatus.java Course materials (23S) CST 8277
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
import java.time.LocalDateTime;
import java.util.Objects;

//TODO DS01 - This class is not an entity however it can be embedded in other entities.  Add missing annotation.
public class DurationAndStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	// TODO DS02 - Add annotations
	private LocalDateTime startDate;
	
	// TODO DS03 - Add annotations
	private LocalDateTime endDate;

	// TODO DS04 - Add annotations
	private byte active;

	public DurationAndStatus() {
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public void setDurationAndStatus(LocalDateTime startDate, LocalDateTime endDate, String active) {
		setStartDate(startDate);
		setEndDate(endDate);
		byte p = 0b1;
		byte n = 0b0;
		setActive(("+".equals(active) ? p : n));
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
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
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getStartDate(), getEndDate(), getActive());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof DurationAndStatus otherDurationAndStatus) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getStartDate(), otherDurationAndStatus.getStartDate()) &&
					Objects.equals(this.getEndDate(), otherDurationAndStatus.getEndDate()) &&
					Objects.equals(this.getActive(), otherDurationAndStatus.getActive());
		}
		return false;
	}

}
