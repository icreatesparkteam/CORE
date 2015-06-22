<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" type="text/css" media="screen"
	href="../css/form.css" />
<title></title>
</head>
<body>

	<form action="" id="loginForm" method="post"
		onsubmit="return ValidateForm(this);">
		<script type="text/javascript">
			function ValidateForm(frm) {
				if (frm.UserName.value == "") {
					alert('User Name is required.');
					frm.UserName.focus();
					return false;
				}
				if (frm.Password.value == "") {
					alert('Password is required.');
					frm.Password.focus();
					return false;
				}
				return true;
			}
		</script>
		<table border="0" cellpadding="5" cellspacing="0" width="600"
			 class="login">
			<tr>
				<td><b>User Name:</b></td>
				<td><input id="UserName" name="UserName" type="text"
					class="loginFileds" /></td>
			</tr>
			<tr>
				<td><b>Password:</b></td>
				<td><input id="Password" name="Password" type="password"
					class="loginFileds" /></td>
			</tr>

			<tr>
				<td colspan="2" align="center"><input id="Submit" name="Submit"
					type="submit" value="LOG IN" /></td>
			</tr>
		</table>
		<br />
	</form>

</body></html>