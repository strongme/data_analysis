package org.scbit.lsbi.protein.pojo;

import java.math.BigDecimal;

// Generated 2012-8-3 11:42:12 by Hibernate Tools 3.4.0.CR1

/**
 * IdmapDbs generated by hbm2java
 */
public class IdmapDbs implements java.io.Serializable {

	private BigDecimal id;
	private String databaseName;

	public IdmapDbs() {
		// TODO Auto-generated constructor stub
	}

	public IdmapDbs(BigDecimal id, String databaseName) {
		super();
		this.id = id;
		this.databaseName = databaseName;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdmapDbs other = (IdmapDbs) obj;
		if (databaseName == null) {
			if (other.databaseName != null)
				return false;
		} else if (!databaseName.equals(other.databaseName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((databaseName == null) ? 0 : databaseName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	

}
