package com.lnt.core.common.util;

public enum ERole {
	OEM(1, IConstants.TYPE_MASTER_ADMIN), SERVICE_PROVIDER(2,
			IConstants.TYPE_SERVICE_PROVIDER);

	private final int id;

	private final String name;

	private ERole(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static ERole getById(int id) {
		for (ERole e : ERole.values()) {
			if (e.getId() == id)
				return e;
		}
		return null;
	}
}
