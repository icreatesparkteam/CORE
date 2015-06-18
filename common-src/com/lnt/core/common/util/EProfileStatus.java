package com.lnt.core.common.util;

public enum EProfileStatus {
	ACTIVE(1), BLOCKED(2);

	int profileStatusId;

	private EProfileStatus(int statusId) {
		profileStatusId = statusId;
	}

	public int getProfileStatusId() {
		return profileStatusId;
	}

	public static EProfileStatus getById(int id) {
		for (EProfileStatus e : EProfileStatus.values()) {
			if (e.getProfileStatusId() == id)
				return e;
		}
		return null;
	}

}
