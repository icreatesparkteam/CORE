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

	<form action="" id="registrationForm" method="post"
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
				if (frm.serviceProviderName.value == "") {
					alert('Service ProviderName is required.');
					frm.serviceProviderName.focus();
					return false;
				}
				if (frm.EmailAddress.value == "") {
					alert('Email address is required.');
					frm.EmailAddress.focus();
					return false;
				}
				if (frm.EmailAddress.value.indexOf("@") < 1
						|| frm.EmailAddress.value.indexOf(".") < 1) {
					alert('Please enter a valid email address.');
					frm.EmailAddress.focus();
					return false;
				}
				if (frm.Phone.value == "") {
					alert('Phone number is required.');
					frm.Phone.focus();
					return false;
				}
				if (frm.Address.value == "") {
					alert('Address is required.');
					frm.Address.focus();
					return false;
				}
				if (frm.City.value == "") {
					alert('City is required.');
					frm.City.focus();
					return false;
				}
				if (frm.State.value == "") {
					alert('State is required.');
					frm.State.focus();
					return false;
				}
				if (frm.Country.value == "") {
					alert('Country is required.');
					frm.Country.focus();
					return false;
				}
				if (frm.Zip.value == "") {
					alert('Zip Code is required.');
					frm.Zip.focus();
					return false;
				}
				if (frm.Active.value == "") {
					alert('Select Active Status');
					frm.Active.focus();
					return false;
				}
				if (frm.Question.value == "") {
					alert('Select Security Question');
					frm.Question.focus();
					return false;
				}
				if (frm.Answer.value == "") {
					alert('Answer is required.');
					frm.Answer.focus();
					return false;
				}
				return true;
			}
		</script><div id="parent">
		<table border="0" cellpadding="5" cellspacing="0" width="600"
			id="container">
			
			<tr>
				<td colspan="2"><h1 id="header">SERVICE PROVIDER
						REGISTARTION</h1></td>
			</tr>

			<tr>
				<td><b>User Name:</b></td>
				<td><input id="UserName" name="UserName" type="text" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Password:</b></td>
				<td><input id="Password" name="Password" type="password" class ="registartionFileds"/></td>
			</tr>

			<tr>
				<td><b>Service Provider Name:</b></td>
				<td><input id="serviceProviderName" name="serviceProviderName"
					type="text" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Email address:</b></td>
				<td><input id="EmailAddress" name="EmailAddress" type="text" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Phone:</b></td>
				<td><input id="Phone" name="Phone" type="text" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Alternate Phone:</b></td>
				<td><input id="AltPhone" name="AltPhone" type="text" class ="registartionFileds"/></td>
			</tr>

			<tr>
				<td><b>Address:</b></td>
				<td><input id="Address" name="Address" type="text" class ="registartionFileds" /></td>
			</tr>
			<tr>
				<td><b>City:</b></td>
				<td><input id="City" name="City" type="text" maxlength="120" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>State/Province:</b></td>
				<td><input id="State" name="State" type="text" maxlength="120" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Country:</b></td>
				<td><input id="Country" name="Country" type="text" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Zip/Postal Code:</b></td>
				<td><input id="Zip" name="Zip" type="text" maxlength="30" class ="registartionFileds"/></td>
			</tr>
			<tr>
				<td><b>Active:</b></td>
				<td><select name="Active">
						<option value="" selected>Select Active Status</option>
						<option value="1">Yes</option>
						<option value="2">No</option>
				</select></td>
			</tr>
			<tr>
				<td><b>Security Question:</b></td>
				<td><select name="Question" >
						<option value="" selected>Select Security Question</option>
						<option value="1">What was your childhood nickname?</option>
						<option value="2">What is the name of your favorite
							childhood friend?</option>
						<option value="3">What was the make and model of your
							first car?</option>
						<option value="4">Who is your childhood sports hero?</option>
						<option value="5">What is your pets name?</option>
						<option value="6">In what year was your father born?</option>
						<option value="7">What is your favorite fruit?</option>
				</select></td>
			</tr>
			<tr>
				<td><b>Answer:</b></td>
				<td><input id="Answer" name="Answer" type="text" class ="registartionFileds"/></td>
			</tr>

			<tr>
				<td colspan="2" align="center"><input id="Submit" name="Submit"
					type="submit" value="SUBMIT" /></td>
			</tr>
		</table>
		</div>
		<br />
	</form>

</body>
</html>