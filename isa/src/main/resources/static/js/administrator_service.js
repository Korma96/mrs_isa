var getCulturalInstitutionsURL = "/myapp/administrators/admin_funzone/get_cultural_institutions";
var getShowingsOfCulturalInstitutionURL = "/myapp/administrators/admin_funzone/get_showings_of_cultural_institution";
var addRequisiteURL = "/myapp/administrators/admin_funzone/add_requisite";
var getRequisitesURL = "/myapp/administrators/admin_funzone/get_requisites"; 
var saveChangesOnProfileSysAdminURL = "/myapp/administrators/sys_admin/update_profile";
var saveChangesOnProfileAdminFunZoneURL = "/myapp/administrators/admin_funzone/update_profile";
var saveChangesOnProfileadminCulturalInstitutionURL = "/myapp/administrators/admin_cultural_institution/update_profile";
var registerAdminURL = "/myapp/administrators/sys_admin/register";
var sysAdminChangeUsernameAndPasswordURL = "/myapp/administrators/sys_admin/changes_default_username_password";
var adminFunzoneChangeUsernameAndPasswordURL = "/myapp/administrators/admin_funzone/changes_default_username_password";
var culturalInstitutionAdminChangeUsernameAndPasswordURL = "/myapp/administrators/admin_cultural_institution/changes_default_username_password";
var sysAdminChangePasswordURL = "/myapp/administrators/sys_admin/save_changed_password";
var adminFunzoneChangePasswordURL = "/myapp/administrators/admin_funzone/save_changed_password";
var culturalInstitutionAdminChangePasswordURL = "/myapp/administrators/admin_cultural_institution/save_changed_password";
var addNewCulturalInstitutionURL = "/myapp/administrators/admin_cultural_institution/add_new_cultural_institution";



function adminSystemPage(loggedUser) {
	$("#title").html('SYSTEM ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	if(loggedUser.userStatus == 'PENDING')
	{
		forceAdminToChangeUsernameAndPassword(sysAdminChangeUsernameAndPasswordURL);
	}
	else if(loggedUser.userStatus == 'ACTIVATED')
	{
		adminSystemMainPage();
	}
	else
	{
		toastr.error("User is DEACTIVATED!");
	}
	
}

function adminSystemMainPage() {
	$("#myDropdown").append('<a id="id_change_password" href="/myapp/#/administrators/sys_admin/change_password"> Change password </a>');
	$("#myDropdown").append('<a id="id_update_profile" href="/myapp/#/administrators/sys_admin/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_register_admin" href="/myapp/#/administrators/sys_admin/register"> Register administrator </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/login"> Logout </a>');
	
	$("#id_change_password").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		changePassword(sysAdminChangePasswordURL);
	});
	
	$("#id_update_profile").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		updateProfileSysAdmin();
	});
	
	$("#id_register_admin").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		registerAdmin();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		logout();
	});
}

function adminFunzonePage(loggedUser) {
	$("#title").html('FUN ZONE ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	if(loggedUser.userStatus == 'PENDING')
	{
		forceAdminToChangeUsernameAndPassword(adminFunzoneChangeUsernameAndPasswordURL);
	}
	else if(loggedUser.userStatus == 'ACTIVATED')
	{
		adminFunzoneMainPage();
	}
	else
	{
		toastr.error("User is DEACTIVATED!");
	}
	
}

function adminCulturalInstitutionsPage(loggedUser) {
	$("#title").html('CULTURAL INSTITUTIONS ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	if(loggedUser.userStatus == 'PENDING')
	{
		forceAdminToChangeUsernameAndPassword(culturalInstitutionAdminChangeUsernameAndPasswordURL);
	}
	else if(loggedUser.userStatus == 'ACTIVATED')
	{
		adminCulturalInstitutionsMainPage();
	}
	else
	{
		toastr.error("User is DEACTIVATED!");
	}
}


function adminFunzoneMainPage() {
	$("#myDropdown").append('<a id="id_change_password" href="/myapp/#/administrators/admin_funzone/change_password"> Change password </a>');
	$("#myDropdown").append('<a id="id_update_profile_admin_fan_zone" href="/myapp/#/administrators/admin_funzone/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="add_requisite" href="/myapp/#/administrators/admin_funzone/add_requisite"> Add requisite </a>');
	$("#myDropdown").append('<a id="show_requisites" href="/myapp/#/administrators/admin_funzone/get_requisites"> Show requisites </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/login"> Logout </a>');
	
	$("#id_change_password").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		changePassword(adminFunzoneChangePasswordURL);
	});
	
	$("#id_update_profile_admin_fan_zone").click(function(event) {
		event.preventDefault();

		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		updateProfileFanZone();
	});
	$("#add_requisite").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		addRequisite();
	});
	
	$("#show_requisites").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		showRequisites();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		logout();
	});
}


function adminCulturalInstitutionsMainPage() {
	$("#myDropdown").append('<a id="id_update_profile_cultural_institutions_admin" href="/myapp/#/administrators/admin_cultural_institution/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_cultural_institutions" href="/myapp/#/administrators/admin_cultural_institution/cultural_institutions"> Cultural institutions </a>');
	$("#myDropdown").append('<a id="id_showings" href="/myapp/#/administrators/admin_cultural_institution/showings"> Showings </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/login"> Logout </a>');
	
	$("#id_change_password").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		changePassword(culturalInstitutionAdminChangePasswordURL);
	});
	
	$("#id_update_profile_cultural_institutions_admin").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		updateProfileCulturalInstitutionsAdmin();
	});
	
	$("#id_cultural_institutions").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		culturalInstitutions();
	});
	
	$("#id_showings").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		showings();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		logout();
	});
}

function forceAdminToChangeUsernameAndPassword(url)
{
	if(window.history.pushState)
	{
		window.history.pushState(null, null, $(this).attr('href')); // set URL
	}
	$("#center").load("html/partials/admin_change_password.html", null, function() {
		$("#id_btn_chpwd").click(function() {
			event.preventDefault();
			sendChangedUsernameAndPassword(url);
		});
	}
);
	
}

function sendChangedUsernameAndPassword(url) {
	var username = $("#chpwd_username").val();
	var password = $("#chpwd_password").val();
	var repeatPassword = $("#chpwd_repassword").val();
	
	$.ajax({ 
	    type: "PUT",
		url:  url,
		dataType : "json",
		 data: JSON.stringify({"username": username, "password": password, "repeatPassword": repeatPassword}),
	    contentType: "application/json",
	    cache: false,
	    success: function(successChanged) {
					if(successChanged) {
						successfullyLogged();
						toastr.success("You have successfully changed username and password!"); 
					}
					else {
						toastr.error("You have unsuccessfully changed username and password!"); 
					}
					
				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}

/*$(document).on("click", "#id_btn_chpwd", function(event) {
	$.ajax({ 
	    type: "POST",
		url:  registerAdminURL,
	    data: JSON.stringify({
			"password": $("#chpwd_password"),
			"rePassword": $("#chpwd_repassword")
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(successRegistrate) {
	    	if(successRegistrate) {
	    		//$("#id_username").val("");
	    		//$("#id_password").val("");
	    		var loggedUser = loadLoggedUser();
	    		if (loggedUser.userType == "SYS_ADMINISTRATOR") {
	    			adminSystemPage(loggedUser);
	    		}
	    		
	    		else if (loggedUser.userType == "FUNZONE_ADMINISTRATOR") {
	    			adminFunzonePage(loggedUser);
	    		}
	    		
	    		else if (loggedUser.userType == "INSTITUTION_ADMINISTRATOR") {
	    			adminCulturalInstitutionsPage(loggedUser);
	    		}
	    		toastr.success("You have successfully changed password!");
	    	}
	    	else {
	    		toastr.error("Registration error!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
});*/

function updateProfileFanZone() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();
		
		deleteAllExceptFirst();
		
		$("#center").append(
				'<form > \
					<table> \
						<tr>  <td><label for="id_username">Username:</label></td>  <td><input type="text" id="id_username" value="' + loggedUser.username + '" /></td>  </tr> \
						<tr>  <td><label for="id_old_password">Old password:</label></td>  <td><input type="password" id="id_old_password" /></td>  </tr> \
						<tr>  <td><label for="id_new_password">New password:</label></td>  <td><input type="password" id="id_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_repeat_new_password">Repeat new password:</label></td>  <td><input type="password" id="id_repeat_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_first_name">First name:</label></td>  <td><input type="text" id="id_first_name" value="' + loggedUser.firstName + '" /></td>  </tr> \
						<tr>  <td><label for="id_last_name">Last name:</label></td>  <td><input type="text" id="id_last_name" value="' + loggedUser.lastName + '" /></td>  </tr> \
						<tr>  <td><label for="id_email">Email:</label></td>  <td><input type="text" id="id_email" value="' + loggedUser.email + '" /></td>  </tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Save changes"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			
			saveChangesOnProfileAdmin(saveChangesOnProfileadminCulturalInstitutionURL);
		});
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function updateProfileCulturalInstitutionsAdmin() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();
		
		deleteAllExceptFirst();
		
		$("#center").append(
				'<form > \
					<table> \
						<tr>  <td><label for="id_username">Username:</label></td>  <td><input type="text" id="id_username" value="' + loggedUser.username + '" /></td>  </tr> \
						<tr>  <td><label for="id_old_password">Old password:</label></td>  <td><input type="password" id="id_old_password" /></td>  </tr> \
						<tr>  <td><label for="id_new_password">New password:</label></td>  <td><input type="password" id="id_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_repeat_new_password">Repeat new password:</label></td>  <td><input type="password" id="id_repeat_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_first_name">First name:</label></td>  <td><input type="text" id="id_first_name" value="' + loggedUser.firstName + '" /></td>  </tr> \
						<tr>  <td><label for="id_last_name">Last name:</label></td>  <td><input type="text" id="id_last_name" value="' + loggedUser.lastName + '" /></td>  </tr> \
						<tr>  <td><label for="id_email">Email:</label></td>  <td><input type="text" id="id_email" value="' + loggedUser.email + '" /></td>  </tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Save changes"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			
			saveChangesOnProfileAdmin(saveChangesOnProfileAdminFunZoneURL);
		});
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}
	

function updateProfileSysAdmin() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();
		deleteAllExceptFirst();
		
		$("#center").append(
				'<form > \
					<table> \
						<tr>  <td><label for="id_username">Username:</label></td>  <td><input type="text" id="id_username" value="' + loggedUser.username + '" /></td>  </tr> \
						<tr>  <td><label for="id_old_password">Old password:</label></td>  <td><input type="password" id="id_old_password" /></td>  </tr> \
						<tr>  <td><label for="id_new_password">New password:</label></td>  <td><input type="password" id="id_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_repeat_new_password">Repeat new password:</label></td>  <td><input type="password" id="id_repeat_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_email">Email:</label></td>  <td><input type="text" id="id_email" /></td>  </tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Save changes"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			
			saveChangesOnProfileSystemAdmin();
		});
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function saveChangesOnProfileSystemAdmin() {
	var username = $("#id_username").val();
	var oldPassword = $("#id_old_password").val();
	var newPassword = $("#id_new_password").val();
	var repeatNewPassword = $("#id_repeat_new_password").val();
	var email = $("#id_email").val();
	
	$.ajax({ 
	    type: "PUT",
		url:  saveChangesOnProfileSysAdminURL,
	    data: JSON.stringify({
			"username": username,
			"oldPassword": oldPassword,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword,
			"email": email
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(intValue) {
	    	switch(intValue) {
		    	case -1:
		    		toastr.error("There was a problem. No logged user!");
		    		break;
		    	case 0:
		    		toastr.error("All fields are not filled (only the new password and repeat new password can remain unfilled)!");
		    		break;
		    	case 1:
		    		toastr.error("This username already exists!");
		    		break;
		    	case 2:
		    		toastr.error("You did not enter the correct old password!");
		    		break;
		    	case 3:
		    		toastr.error("You are not the first to enter the same new password the second time!");
		    		break;
		    	case 4:
		    		$("#id_old_password").val("")
		    		$("#id_new_password").val("");
		    		$("#id_repeat_new_password").val("");
		    		toastr.success("You have successfully edited the data!");
		    		break;
		    	default:
		    		toastr.error("There was a problem.!");
		    		break;
	    	}
	    	
    	},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
}


function saveChangesOnProfileAdmin(url) {
	var username = $("#id_username").val();
	var oldPassword = $("#id_old_password").val();
	var newPassword = $("#id_new_password").val();
	var repeatNewPassword = $("#id_repeat_new_password").val();
	var firstName = $("#id_first_name").val();
	var lastName = $("#id_last_name").val();
	var email = $("#id_email").val();
	
	$.ajax({ 
	    type: "PUT",
		url:  url,
	    data: JSON.stringify({
			"username": username,
			"oldPassword": oldPassword,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword,
			"firstName": firstName,
	        "lastName": lastName,
	        "email": email
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(intValue) {
	    	switch(intValue) {
		    	case -1:
		    		toastr.error("There was a problem. No logged user!");
		    		break;
		    	case 0:
		    		$("#id_old_password").val("")
		    		$("#id_new_password").val("");
		    		$("#id_repeat_new_password").val("");
		    		toastr.success("You have successfully edited the data!");
		    		break;
		    	case 1:
		    		toastr.error("All fields are not filled (only the new password and repeat new password can remain unfilled)!");
		    		break;
		    	case 2:
		    		toastr.error("This username already exists!");
		    		break;
		    	case 3:
		    		toastr.error("You did not enter the correct old password!");
		    		break;
		    	case 4:
		    		toastr.error("You are not the first to enter the same new password the second time!");
		    		break;
		    	case 5:
		    		toastr.error("Incorrect email!");
		    		break;
		    	default:
		    		toastr.error("There was a problem.!");
		    		break;
	    	}
	    	
    	},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
}


function registerAdmin() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var center = $("#center");
		
		deleteAllExceptFirst();
		
		center.append(
				'<form > \
					<table> \
						<tr id="id_tr_first_name">  <td><label for="id_first_name_a">First name:</label></td>  <td><input type="text" id="id_first_name_a"/></td>  </tr> \
						<tr id="id_tr_last_name">  <td><label for="id_last_name_a">Last name:</label></td>  <td><input type="text" id="id_last_name_a"/></td>  </tr> \
						<tr id="id_tr_email">  <td><label for="id_email_a">Email:</label></td>  <td><input type="text" id="id_email_a" /></td>  </tr> \
						<tr>  <td><label for="id_role_a">Role:</label></td>  <td class = "select"> \
						<select id="id_role_a"> \
	            		<option value="SA">system admin</option>\
	            		<option value="CIA">Institution admin</option>\
	            		<option value="FZA">fan zone admin</option>\
	            		</select>\
	            		</td> \
	            		</tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Register administrator"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_tr_first_name").hide();
		$("#id_tr_last_name").hide();
		
		$("#id_role_a").change(changedAdminRole);
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			saveAdmin();
		});
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function changedAdminRole() {
	var adminRole = $("#id_role_a").val();
	
	if(adminRole == "SA") {
		$("#id_tr_first_name").hide();
		$("#id_tr_last_name").hide();
	}
	else {
		$("#id_tr_first_name").show();
		$("#id_tr_last_name").show();
	}
}

function saveAdmin(){
	var obj = {};
	var adminRole = $("#id_role_a").val();
	var email = $("#id_email_a").val();
	
	obj["adminRole"] = adminRole;
	obj["email"] = email;
	
	if(adminRole != "SA") {
		var firstName = $("#id_first_name_a").val();
		var lastName = $("#id_last_name_a").val();
		
		obj["firstName"] = firstName;
		obj["lastName"] = lastName;
	}

	$.ajax({ 
	    type: "POST",
		url:  registerAdminURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(successRegistrate) {
	    	if(successRegistrate) {
	    		if(adminRole != "SA") {
	    			$("#id_first_name_a").val("");
	    			$("#id_last_name_a").val("");
	    		}
	    		$("#id_email_a").val("");
	    		
	    		
	    		toastr.success("You have successfully registered a new administrator. He will soon receive an email with login information.");
	    	}
	    	else {
	    		toastr.error("Registration error!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}


function showings()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		alert('showings!');
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function culturalInstitutions()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();

		deleteAllExceptFirst();
		
		$("#center").append('<div><div id="cinemas_div"> \
			<img  style="width:50.62%;height:75%;float:left;position:static" src="images/cinema.jpg" class="cinema" alt="Cinemas "title="Cinemas"/> \
			</div> \
			<div id="theaters_div"> \
			<img style="width:49.38%;height:75%;float:right" src="images/theater.jpg" class="theater" alt="Theaters" title="Theaters"/> \
			</div> \
			</div>');
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function addCulturalInstitution()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();

		deleteAllExceptFirst();
		
		$("#center").append(
				'<form > \
				<table> \
					<tr><td><label for="id_institution_name">Name:</label></td><td><input type="text" id="id_institution_name"/></td></tr> \
					<tr><td><label for="id_address">Last name:</label></td><td><input type="text" id="id_address"/></td></tr> \
					<tr><td><label for="id_description">Email:</label></td><td><input type="text" id="id_description"/></td></tr> \
					<tr><td><label for="id_institution_role">Role:</label></td><td class = "select"> \
					<select id="id_institution_role"> \
            		<option value="CINEMA">CINEMA</option>\
            		<option value="THEATER">THEATER</option>\
            		</select>\
            		</td> \
            		</tr> \
				</table> \
				<div align="center"><input type="button" id="id_btn_save_new_institution" class="buttons" value="Save institution"/> \
				</div> \
				<br/> \
			</form>');
		
		$("#id_btn_save_new_institution").click(function(event) {
			event.preventDefault();
			
			addCulturalInstitutionAjax();
		});
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function addCulturalInstitutionAjax()
{
	var obj = {};
	var name = $("#id_institution_name").val();
	var address = $("#id_address").val();
	var description = $("#id_description").val();
	var role = $("#id_institution_role").val();

	obj["name"] = name;
	obj["address"] = address;	
	obj["description"] = description;
	obj["role"] = role;
	
	$.ajax({ 
	    type: "POST",
		url:  addNewCulturalInstitutionURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {
	    		$("#id_institution_name").val("");
	    		$("#id_address").val("");
	    		$("#id_description").val("");		
	    		
	    		toastr.success("You have successfully added new institution!");
	    	}
	    	else {
	    		toastr.error("Wrong data!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}


function addRequisite() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		$('<link>')
		  .appendTo('head')
		  .attr({
		      type: 'text/css', 
		      rel: 'stylesheet',
		      href: '//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css'
		});
		
		deleteAllExceptFirst();
		$("#center").append('<div id="id_add_requisite_page"></div>');
		
		$("#id_add_requisite_page").load("html/partials/add_requisite.html", loadAddRequisiteComplete);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
	
}

function showRequisites() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		deleteAllExceptFirst();
		$("#center").append('<div><table id="id_requisites"> </table> </div>');
		
		var requistes = getRequistes();
		
		if(requistes) {
			if(requistes.length > 0) {
				$("#id_requisites").append('<tr> <th colspan="5"> <b> REQUISITES </b></th></tr>');
				$("#id_requisites").append('<tr> <th> Name </th> <th> Description </th> <th> Price </th> <th> Showing name </th> <th> Cultural institution name </th> </tr>');
				
				$.each(requistes, function(index, requiste) {
					
					$("#id_requisites").append('<tr> <td> ' + (index+1) + '. </td> <td> ' + requiste.name + ' </td> <td> ' + requiste.description + ' </td> <td> ' + requiste.price + ' </td> <td> ' + requiste.showingName + ' </td> <td> ' + requiste.culturalInstitutionName + ' </td> </tr>');
				});
			}
			else {
				toastr.error("Requisites are not available!");
			}
		}
		else {
			toastr.error("Requisites are not available!");
		}
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function loadAddRequisiteComplete() {
	loadCulturalInstitutionsAndShowings();

	$( "#id_price" ).val("0");
	
	$("#id_btn_add_requisite").click(function(event) {
		event.preventDefault();
		
		sendAddedRequisite();
	});
}

function loadCulturalInstitutionsAndShowings() {
	var culturalInstitutions = getCulturalInstitutions();
	if(culturalInstitutions) {
		if(culturalInstitutions.length > 0) {
			var selected;
			
			$.each(culturalInstitutions, function(index, culturalInstitution) {
				if(index == 0) {
					selected = "selected";
				}
				else{
					selected = "";
				}
				$("#id_cultural_institution").append("<option " + selected + "> " + culturalInstitution + " </option>");
			});
			
			$("#id_cultural_institution").change(changedCulturalInstitution);
			
			var showingsOfCulturalInstitution = getShowingsOfCulturalInstitution(culturalInstitutions[0]);
			if(showingsOfCulturalInstitution) {
				if (showingsOfCulturalInstitution.length > 0) {
					$.each(showingsOfCulturalInstitution, function(index2, showing) {
						if(index2 == 0) {
							selected = "selected";
						}
						else{
							selected = "";
						}
						$("#id_showing").append("<option " + selected + "> " + showing + " </option>");
					});
				}
				else {
					toastr.error("Showings, for selected cultural institution, are not available!");
				}
			}
			else {
				toastr.error("Showings, for selected cultural institution, are not available!");
			}
			
		}
		else {
			toastr.error("Cultural institutions are not available!");
		}
	}
	else {
		toastr.error("Cultural institutions are not available!");
	}
	
}

function changedCulturalInstitution() {
	var currentCulturalInstitution = $("#id_cultural_institution").val();
	$("#id_showing").empty();
	
	var showingsOfCulturalInstitution = getShowingsOfCulturalInstitution(currentCulturalInstitution);
	if(showingsOfCulturalInstitution) {
		if (showingsOfCulturalInstitution.length > 0) {
			$.each(showingsOfCulturalInstitution, function(index, showing) {
				if(index == 0) {
					selected = "selected";
				}
				else{
					selected = "";
				}
				$("#id_showing").append("<option " + selected + "> " + showing + " </option>");
			});
		}
		else {
			toastr.error("Showings, for selected cultural institution, are not available!");
		}
	}
	else {
		toastr.error("Showings, for selected cultural institution, are not available!");
	}
	
}

function getShowingsOfCulturalInstitution(culturalInstitutionName) {
	var showingsOfCulturalInstitution = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getShowingsOfCulturalInstitutionURL + "/" + culturalInstitutionName,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveShowingsOfCulturalInstitution) {
			showingsOfCulturalInstitution = receiveShowingsOfCulturalInstitution;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return showingsOfCulturalInstitution;
}

function getCulturalInstitutions() {
	var culturalInstitutions = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getCulturalInstitutionsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveCulturalInstitutions) {
			culturalInstitutions = receiveCulturalInstitutions;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return culturalInstitutions;
}

function getRequistes() {
	var requistes = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getRequisitesURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveRequistes) {
			requistes = receiveRequistes;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return requistes;
}

function sendAddedRequisite() {
	var name = $("#id_name").val();
	var description = $("#id_description").val();
	var price = $("#id_price").val();
	var culturalInstitution = $("#id_cultural_institution").val();
	var showing = $("#id_showing").val();
	
	$.ajax({ 
	    type: "POST",
		url:  addRequisiteURL,
	    data: JSON.stringify({
			"name": name,
			"description": description,
			"price": price,
			"culturalInstitution": culturalInstitution,
			"showing": showing
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    cache: false,
	    success: function(successAdd) {
	    	if(successAdd) {
	    		$("#id_name").val("");
	    		$("#id_description").val("");
	    		$("#id_price").val("0");
	    		$("#id_cultural_institution").val("");
	    		$("#id_showing").val("");
	    		
	    		toastr.success("You have successfully add requisite!");
	    	}
	    	else {
	    		toastr.error("You did not successfully add requiste!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}