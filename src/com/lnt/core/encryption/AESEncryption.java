package com.lnt.core.encryption;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class AESEncryption implements Serializable, UserType {

	private static final long serialVersionUID = 1L;
	
	private static final int[] SQL_TYPES = { Types.VARCHAR };

	@Override
	public Object assemble(Serializable assembleobj, Object assemblyobj)
			throws HibernateException {

		return assembleobj;
	}

	@Override
	public Object deepCopy(Object deecopyobj) throws HibernateException {

		return deecopyobj;
	}

	@Override
	public Serializable disassemble(Object disassembleobj)
			throws HibernateException {

		return (Serializable) disassembleobj;
	}

	@Override
	public boolean equals(Object booleq, Object booleq1)
			throws HibernateException {
		if (booleq == booleq1)
			return true;
		if (booleq == null || booleq1 == null)
			return false;
		return ((String) booleq).equals((String) booleq1);
	}

	@Override
	public int hashCode(Object hashobj) throws HibernateException {

		return ((String) hashobj).hashCode();
	}

	@Override
	public boolean isMutable() {

		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet getobj, String[] stringtodecrypt,
			SessionImplementor sessionobj, Object obj)
			throws HibernateException, SQLException {
		String encryptedValue = getobj.getString(stringtodecrypt[0]);
		try {
			return getobj.wasNull() ? null : AES.decrypt(encryptedValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement setobj, Object stringtoencrypt,
			int i, SessionImplementor arg3) throws HibernateException,
			SQLException {
		if (stringtoencrypt == null) {
			setobj.setNull(i, Types.VARCHAR);
		} else {
			try {
				setobj.setString(i, AES.encrypt((String) stringtoencrypt));
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	@Override
	public Object replace(Object replaceobj, Object obj, Object delobj)
			throws HibernateException {

		return replaceobj;
	}

	@Override
	public Class returnedClass() {

		return java.lang.String.class;
	}

	@Override
	public int[] sqlTypes() {

		return SQL_TYPES;
	}

}
