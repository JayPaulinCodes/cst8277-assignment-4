/***************************************************************************
 * File:  PojoBaseCompositeKey.java Course materials (23S) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * @author Mike Norman
 * @date 2020 04
 * 
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   041053188, Taeung, Park 
 *   041065803, Doyoung, Kim 
 *   041053986, Dawon, Jun 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@SuppressWarnings("unused")

/**
 * Abstract class that is base of (class) hierarchy for all @Entity classes
 * @param <ID> - type of composite key used
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@EntityListeners({PojoCompositeListener.class})
public abstract class PojoBaseCompositeKey<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Version
	protected int version;

	@Column(name = "created")
	protected LocalDateTime created;

	@Column(name = "updated")
	protected LocalDateTime updated;

	public abstract ID getId();

	public abstract void setId(ID id);

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
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
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof PojoBaseCompositeKey otherPojoBaseComposite) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherPojoBaseComposite.getId());
		}
		return false;
	}
}
