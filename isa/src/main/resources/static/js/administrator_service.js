var getCulturalInstitutionsURL = "/myapp/administrators/get_cultural_institutions";
var getShowingsOfCulturalInstitutionURL = "/myapp/administrators/get_showings_of_cultural_institution";
var addRequisiteURL = "/myapp/administrators/add_requisite";
var getRequisitesURL = "/myapp/administrators/get_requisites"; 
var saveChangesOnProfileSysAdminURL = "/myapp/administrators/save_changes_on_profile";
var registerAdminURL = "/myapp/administrators/register";
var changePasswordURL = "/myapp/administrators/admin_changes_default_password";


function adminSystemPage(loggedUser) {
	$("#title").html('SYSTEM ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	$("#myDropdown").append('<a id="id_update_profile" href="/myapp/#/administrators/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_register_admin" href="/myapp/#/administrators/register"> Register administrator </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/logout"> Logout </a>');
	
	$("#id_update_profile").click(function(event) {
		event.preventDefault();
		
		updateProfileSysAdmin(loggedUser);
	});
	$("#id_register_admin").click(function(event) {
		event.preventDefault();
		
		registerAdmin(loggedUser);
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		logout();
	});
	
}

function adminFunzonePage(loggedUser) {
	$("#title").html('FUN ZONE ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	$("#myDropdown").append('<a id="id_update_profile_admin_fan_zone" href="/myapp/#/administrators/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="add_requisite" href="/myapp/#/administrators/register_system"> Add requisite </a>');
	$("#myDropdown").append('<a id="show_requisites" href="/myapp/#/administrators/get_requisites"> Show requisites </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/administrators/logout"> Logout </a>');
	
	$("#id_update_profile_admin_fan_zone").click(function(event) {
		event.preventDefault();

		updateProfileFanZone(loggedUser);
	});
	$("#add_requisite").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, "/myapp/#/administrators/add_requisite"); // set URL
		}
		
		addRequisite();
	});
	
	$("#show_requisites").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, "/myapp/#/administrators/get_requisites"); // set URL
		}
		
		showRequisites();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		logout();
	});
	
}

function adminCulturalInstitutionsPage(loggedUser) {
	$("#title").html('CULTURAL INSTITUTIONS ADMIN PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/admins/register_system" class="a_registrer" > Register </a> ');
	
	if(loggedUser.userStatus == 'PENDING')
	{
		forceAdminToChangePassword(loggedUser);
	}
	else if(loggedUser.userStatus == 'ACTIVATED')
	{
		adminCulturalInstitutionsMainPage(loggedUser);
	}
	else
	{
		toastr.error("User is DEACTIVATED!");
	}
}

function adminCulturalInstitutionsMainPage(loggedUser)
{
	$("#myDropdown").append('<a id="id_update_profile_cultural_institutions_admin" href="/myapp/#/administrators/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_update_showings" href="/myapp/#/administrators/update_showings"> Update showings </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/logout"> Logout </a>');
	
	$("#id_update_profile_cultural_institutions_admin").click(function(event) {
		event.preventDefault();
		
		updateProfileCulturalInstitutionsAdmin(loggedUser);
	});
	$("#id_update_showings").click(function(event) {
		event.preventDefault();
		
		updateShowings();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		logout();
	});
}

function forceAdminToChangePassword(loggedUser)
{
	if(window.history.pushState)
	{
		window.history.pushState(null, null, $(this).attr('href')); // set URL
	}
	$("#center").load("html/partials/admin_change_password.html", null, null);
	var alert_string = loggedUser.username + ', please set your new password.'
	alert(alert_string);
}

$(document).on("click", "#id_btn_chpwd", function(event) {
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
	    		$("#id_username").val("");
	    		$("#id_password").val("");
	    		adminCulturalInstitutionsMainPage(loadLoggedUser());
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
});

function updateProfileFanZone(loggedUser)
{
	var center = $("#center");
	
	deleteAllExceptFirst();
	
	center.append(
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
		
		saveChangesOnProfileSystemAdmin();
	});
	
}

function updateProfileCulturalInstitutionsAdmin(loggedUser)
{
	var center = $("#center");
	
	deleteAllExceptFirst();
	
	center.append(
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
		
		saveChangesOnProfileSystemAdmin();
	});
}
	

function updateProfileSysAdmin(loggedUser) {
	
	var center = $("#center");
	
	deleteAllExceptFirst();
	
	center.append(
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
		
		saveChangesOnProfileSystemAdmin();
	});
	
}

function saveChangesOnProfileSystemAdmin() {
	var username = $("#id_username").val();
	var oldPassword = $("#id_old_password").val();
	var newPassword = $("#id_new_password").val();
	var repeatNewPassword = $("#id_repeat_new_password").val();
	var firstName = $("#id_first_name").val();
	var lastName = $("#id_last_name").val();
	var email = $("#id_email").val();

	$.ajax({ 
	    type: "PUT",
		url:  saveChangesOnProfileSysAdminURL,
	    data: JSON.stringify({
			"username": username,
			"oldPassword": oldPassword,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword,
			"firstName": firstName,
			"lastName": lastName,
			"email": email,
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(intValue) {
	    	switch(intValue) {
		    	case -1:
		    		toastr.error("There was a problem. No logged user!");
		    		break;
		    	case 0:
		    		toastr.error("All fields are not filled!");
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
		    		toastr.error("Incorrect email!");
		    		break;
		    	case 5:
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


function registerAdmin(loggedUser) {
	
	var center = $("#center");
	
	deleteAllExceptFirst();
	
	center.append(
			'<form > \
				<table> \
					<tr>  <td><label for="id_username_a">Username:</label></td>  <td><input type="text" id="id_username_a" /></td>  </tr> \
					<tr>  <td><label for="id_new_password_a">Password:</label></td>  <td><input type="password" id="id_new_password_a" /></td>  </tr> \
					<tr>  <td><label for="id_repeat_new_password_a">Repeat password:</label></td>  <td><input type="password" id="id_repeat_new_password_a" /></td>  </tr> \
					<tr>  <td><label for="id_first_name_a">First name:</label></td>  <td><input type="text" id="id_first_name_a"/></td>  </tr> \
					<tr>  <td><label for="id_last_name_a">Last name:</label></td>  <td><input type="text" id="id_last_name_a"/></td>  </tr> \
					<tr>  <td><label for="id_email_a">Email:</label></td>  <td><input type="text" id="id_email_a" /></td>  </tr> \
					<tr>  <td><label for="id_role_a">Role:</label></td>  <td class = "select"> \
					<select id="id_role_a"> \
            		<option value="SA">system admin</option>\
            		<option value="CIA">Institution admin</option>\
            		<option value="FZA">fan zone admin</option>\
            		</select>\
            		</td> \
            		</tr> \
				</table> \
				<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Register system admin"/> \
				</div> \
				<br/> \
			</form>');
	
	$("#id_btn_save_changes_on_profile").click(function(event) {
		event.preventDefault();
		saveAdmin();
	});
	
}

function saveAdmin(){
	var username = $("#id_username_a").val();
	var newPassword = $("#id_new_password_a").val();
	var repeatNewPassword = $("#id_repeat_new_password_a").val();
	var firstName = $("#id_first_name_a").val();
	var lastName = $("#id_last_name_a").val();
	var email = $("#id_email_a").val();
	var role = $("#id_role_a").find('option:selected').val();

	$.ajax({ 
	    type: "POST",
		url:  registerAdminURL,
	    data: JSON.stringify({
			"username": username,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword,
			"firstName": firstName,
			"lastName": lastName,
			"email": email,
			"role" : role,
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(successRegistrate) {
	    	if(successRegistrate) {
	    		$("#id_username").val("");
	    		$("#id_password").val("");
	    		$("#id_repeat_password").val("");
	    		$("#id_first_name").val("");
	    		$("#id_last_name").val("");
	    		$("#id_email").val("");
	    		$("#id_city").val("");
	    		$("#id_phone_number").val("");
	    		
	    		toastr.success("You have successfully registered! You will soon receive an email to activate your account.");
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



/*function administratorPage() {
	$("#title").html('ADMINISTRATOR PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/registrate" class="a_registrate" > Registrate </a> ');
	
	
	$("#myDropdown").append('<a id="add_requisite" href="/myapp/#/administrators/add_requisite"> Add requisite </a>');
	$("#myDropdown").append('<a id="show_requisites" href="/myapp/#/administrators/get_requisites"> Show requisites </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/logout"> Logout </a>');
	
	$("#add_requisite").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, "/myapp/#/administrators/add_requisite"); // set URL
		}
		
		addRequisite();
	});
	
	$("#show_requisites").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, "/myapp/#/administrators/get_requisites"); // set URL
		}
		
		showRequisites();
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		logout();
	});
}*/

function updateShowings()
{
	alert('showings!');
}


function addRequisite() {
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

function showRequisites() {
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

function loadAddRequisiteComplete() {
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
	
	
	
	$( function() {
	    var spinner = $( "#spinner" ).spinner();
	    spinner.spinner( "value", 0 );
	});
	
	$("#id_btn_add_requisite").click(function(event) {
		event.preventDefault();
		
		sendAddedRequisite();
	});
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
	var price = $( "#spinner" ).spinner( "value" );
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
	    		$( "#spinner" ).spinner( "value", 0 );
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