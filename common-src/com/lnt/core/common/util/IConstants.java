package com.lnt.core.common.util;

public interface IConstants {

	public static final String EMPTY_STRING = "";

	public static final String TOKEN_HEADER_KEY = "lnt_access_token";

	public static final int TOKEN_EXPIRY_DURATION = 360;// in minutes

	public static final int CACHE_TYPE_SIMPLE = 1;
	
	public static final int LOGIN_ATTEMPTS = 3;
	
	public static final int SESSION_INACTIVE_TIME = 20;
	
	public final static String TYPE_MASTER_ADMIN = "masterAdmin";

	public final static String TYPE_SERVICE_PROVIDER = "serviceprovider";

	// Change password error messages
	public final static String PASSWORD_MIN_LENGTH = "Password length must be at least  8 characters in length";
	public final static String PASSWORD_CONTROL_CHARACTERS = "Password must not contain control or other non-printing characters";
	public final static String USERNAME_CONTROL_CHARACTERS = "User Name must not contain control or other non-printing characters";
	public final static String PASSWORD_CHARACTER_CRITERIA = "password must contain any three of the following four: Numeric,Uppercharacters,( ~!@#$%^*&;?.+_)Special characters,Lower characters";
	public final static String PASSWORD_CONTIGEOUS_CHARACTER_CRITERIA = "Password must not contain three or more contiguous characters of your user name or full name";
	public final static String PASSWORD_COMPARISION = "New and old password are same. Please choose a different password";
	public final static String PASSWORD_HISTORY = "new password matches to that of previous 6 password. Please choose a different password";
	public final static String PASSWORD_MISSMATCH = "New and confirm password are different. Retype password.";
	public final static String AUTHENTICATION_FAIL = "Authentication failed : Unable to find user";

	public final static String PASSWORD_VALIDATION_SUCCESS = "Success";
	public final static String USERNAME_VALIDATION_SUCCESS = "Success";

	public static final int ASCII_DELETE_CHAR = 127;
	public static final int ASCII_NULL_CHAR = 0;
	public static final int ASCII_UNIT_SEPERATOR = 31;

	public static final String VALIDATE_ANSWER_FAILED = "Security question answer failed";

	public String SUCCESS_STATUS = "success";
	public String RETRIEVE_QUESTION_UNKNOWN_ERROR = "Couldn't retrieve secret Question";
	public String SECURITY_DETAILS_SAVED = SUCCESS_STATUS
			+ "& message=Saved data";
	public String SAVE_DETAILS_ERROR = "Couldn't save secret details";
	public String SECURITY_ANSWER_VALIDATED_SUCCESS = "Validated answer";
	public String SESSION_RETRIEVE_ERROR = "Error while Retrieving Session details";
	public String USER_SESSION_EXPIRED = "User session expired";
	public String USER_NAME_ALREADY_EXISTS = "Couldn't create user - Username already exists";
	public String PASSWORD_LENGTH_INVALID = "Password Length Invalid";
	public String PASSWORD_ALPHANUMERIC_REQUIRED = "Password Aphanumeric Required";
	public String PASSWORD_SPECIAL_CHARACTER = "Special Character Required";
	public String USERNAME_MATCH = "Username matches with Password";
	public String PASSWORD_LENGTH_INVALID_MESSAGE = "Password should be less than 12 and more than 8 characters in length";
	public String PASSWORD_ALPHANUMERIC_REQUIRED_MESSAGE = "Password should be Alphanumeric";
	public String SPACE_INVALID_MESSAGE = "Password should not contain space";
	public String PASSWORD_SPECIAL_CHARACTER_MESSAGE = "Password should contain atleast one special character";

	// temporary password field.
	public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUM = "0123456789";
	public static final String SPL_CHARS = "!@#$%&*_=+-/";

	public static final int NOOFCAPSALPHA = 1;
	public static final int NOOfDIGITS = 1;
	public static final int NOOfSPLCHARS = 1;
	public static final int MINLEN = 8;
	public static final int MAXLEN = 12;
}
