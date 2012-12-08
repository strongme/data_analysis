package org.scbit.lsbi.velocity;

public enum Gender {
	
	/*
	 * public static final
	 */
	MALE("男"),FEMALE("女");
	private String alias;
	
	private Gender(String alias) {
		this.alias = alias;
	}
	
	public String getAlias() {
		return alias;
	}
	
}
