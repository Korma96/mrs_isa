var loginURL = "/myapp/users/login";
var loggedUserURL = "/myapp/users/logged_user";
var registrateUserURL = "/myapp/users/registrate";
var activateAccountURL = "/myapp/users/activate";
var logOutURL = "/myapp/users/logout";
var saveChangesOnProfileURL = "/myapp/users/save_changes_on_profile";
var peopleURL = "/myapp/users/get_people";
var friendsURL = "/myapp/users/get_friends";
var requestsURL = "/myapp/users/get_requests";
var sendRequestFriendURL = "/myapp/users/send_request_friend";
var acceptRequestFriendURL = "/myapp/users/accept_request_friend";
var declineRequestFriendURL = "/myapp/users/decline_request_friend";
var isLoggedURL = "/myapp/users/is_logged";
var removeFriendURL = "/myapp/users/remove_friend";
var saveChangedPasswordURL = "/myapp/users/save_changed_password";
var bookSelectedSeatsURL = "/myapp/users/book_selected_seats";
var getIndexesOfBusySeatsAndRowsColsURL = "/myapp/users/get_indexes_of_busy_seats_and_rows_cols";
var sendSeatsAndFriendsURL = "/myapp/users/send_seats_and_friends";
var getCulturalInstitutionsForReservationURL = "/myapp/users/get_cultural_institutions";
var getShowingsOfCulturalInstitutionForReservationURL = "/myapp/users/get_showings_of_cultural_institution";
var getAuditoriumsURL = "/myapp/users/get_auditoriums";
var getTimesURL = "/myapp/users/get_times";
var getDatesURL = "/myapp/users/get_dates";
var invitationsURL = "/myapp/users/get_invitations";
var ticketsURL = "/myapp/users/get_tickets";
var acceptInvitationURL = "/myapp/users/accept_invitation";
var declineInvitationURL = "/myapp/users/decline_invitation";
var removeTicketURL = "/myapp/users/remove_ticket";
var getVisitedAndUnvisitedCulturalInstitutionsURL = "/myapp/users/get_visited_and_unvisited_cultural_institutions"; 
var getImageURL = "/myapp/users/get_image";
var getShowingForShowURL = "/myapp/users/get_showing_for_show";
var getPriceURL = "/myapp/users/get_price";

	
var indexOfFriends = 0;
var indexOfRequests = 0;
var indexOfTickets = 0;
var indexOfInvitations = 0;
var fastLogin;
	
	
$(document).ready(function() {
	//$("#center").load("html/partials/home_page.html", null, loadHomePageComplete);
	
	/*if (window.history && window.history.pushState) {
		var url = window.location.href;     // Returns full URL
		var pathname;
		if (url.indexOf("#") >= 0) {
			pathname = getPathname();
		}
		else {
			pathname = "";
		}
		
		window.history.pushState(null, null, "/myapp/#" + pathname); // set URL
		
		reactionOnChangeUrl();
		//activateAccount(pathname);
		
	    $(window).on('popstate', function() {
	    	toastr.info('Back button was pressed.');
	    });

		
	}*/
	
	reactionOnChangeUrl();
	
});

$(window).on('hashchange', function(e){
	// Alerts every time the hash changes!
	
	reactionOnChangeUrl();
	
});

function reactionOnChangeUrl() {
var pathname = getPathname();
	
	switch(pathname) {
	case "":
	case "/":
	case "/#":
	case "#/":
	case "/#/":
	case "#":
	case "/home_page":
	case "home_page/":
	case "/home_page/":
	case "home_page":
		$("#center").load("html/partials/home_page.html", null, loadHomePageComplete);
		break;
	case "users/login":
	case "users/login/":
	case "/users/login/":
	case "/users/login":
		fastLogin = false;
		loadLogin();
		break;
	case "users/register":
	case "users/register/":
	case "/users/register/":
	case "/users/register":
		$("#center").load("html/partials/register.html", null, loadRegisterComplete);
		break;
	case "users/update_profile":
	case "users/update_profile/":
	case "/users/update_profile/":
	case "/users/update_profile":
		updateProfile();
		break;
	case "users/friends":
	case "users/friends/":
	case "/users/friends/":
	case "/users/friends":
		friendsPage();
		break;
	case "users/visited_and_unvisited_cultural_institutions":
	case "users/visited_and_unvisited_cultural_institutions/":
	case "/users/visited_and_unvisited_cultural_institutions/":
	case "/users/visited_and_unvisited_cultural_institutions":
		showVisitedAndUnvisitedCulturalInstitutions();
		loadVisitedAndUnvisitedCulturalInstitutionsPageComplete();
		break;
	case "users/seats_reservation":
	case "users/seats_reservation/":
	case "/users/seats_reservation/":
	case "/users/seats_reservation":
		reservationSeats();
	case "users/invitations_and_tickets":
	case "users/invitations_and_tickets/":
	case "/users/invitations_and_tickets/":
	case "/users/invitations_and_tickets":
		fastLogin = true;
		loadLogin();
		break;
	case "home_page/cinemas":
	case "home_page/cinemas/":
	case "/home_page/cinemas/":
	case "/home_page/cinemas":
		showCinemas();
		break;
	case "home_page/theaters":
	case "home_page/theaters/":
	case "/home_page/theaters/":
	case "/home_page/theaters":
		showTheaters();
		break;
	case "administrators/sys_admin/update_profile":
	case "administrators/sys_admin/update_profile/":
	case "/administrators/sys_admin/update_profile/":
	case "/administrators/sys_admin/update_profile":
		updateProfileSysAdmin();
		break;
	case "administrators/sys_admin/register":
	case "administrators/sys_admin/register/":
	case "/administrators/sys_admin/register/":
	case "/administrators/sys_admin/register":
		registerAdmin();
		break;
	case "administrators/admin_funzone/update_profile":
	case "administrators/admin_funzone/update_profile/":
	case "/administrators/admin_funzone/update_profile/":
	case "/administrators/admin_funzone/update_profile":
		updateProfileFanZone();
		break;
	case "administrators/admin_funzone/add_requisite":
	case "administrators/admin_funzone/add_requisite/":
	case "/administrators/admin_funzone/add_requisite/":
	case "/administrators/admin_funzone/add_requisite":
		addRequisite();
		break;
	case "administrators/admin_funzone/get_requisites":
	case "administrators/admin_funzone/get_requisites/":
	case "/administrators/admin_funzone/get_requisites/":
	case "/administrators/admin_funzone/get_requisites":
		showRequisites();
		break;
	case "administrators/admin_cultural_institution/update_profile":
	case "administrators/admin_cultural_institution/update_profile/":
	case "/administrators/admin_cultural_institution/update_profile/":
	case "/administrators/admin_cultural_institution/update_profile":
		updateProfileCulturalInstitutionsAdmin();
		break;
	case "administrators/admin_cultural_institution/showings":
	case "administrators/admin_cultural_institution/showings/":
	case "/administrators/admin_cultural_institution/showings/":
	case "/administrators/admin_cultural_institution/showings":
		showings();
		break;
	case "administrators/admin_cultural_institution/culturalInstitutions":
	case "administrators/admin_cultural_institution/culturalInstitutions/":
	case "/administrators/admin_cultural_institution/culturalInstitutions/":
	case "/administrators/admin_cultural_institution/culturalInstitutions":
		culturalInstitutions();
		break;
	default:
		$("#title").empty();
		$("#center").html(getStringFor404());
		break;
	}
	
	activateAccount(pathname);
}

function activateAccount(pathname) {
	if (pathname.indexOf("activate?id_for_activation=") >= 0) {
		var tokens = pathname.trim().split("=");
		if(tokens.length == 2) {
			var idForActivation = tokens[1];
			
			$.ajax({ 
			    type: "PUT",
				url:  activateAccountURL + "/" + idForActivation,
				dataType : "json",
			    contentType: "application/json",
			    cache: false,
			    success: function(successActivate) {
							if(successActivate) {
								toastr.success("You have successfully activated the account!"); 
							}
							else {
								toastr.error("You have unsuccessfully activated the account!"); 
							}
							
						
			   },
				error : function(XMLHttpRequest, textStatus, errorThrown) { 
							toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
				}
			});
		}
		
	}
	
}

function getPathname() {
	var url = window.location.href.trim();     // Returns full URL
	
	var tokens = url.split("#");
	var pathname;
	if(tokens.length == 2) {
		pathname = tokens[1];
	}
	else {
		if(url =="https://isaapp.herokuapp.com/myapp"  || url == "https://isaapp.herokuapp.com/myapp/" || url =="http://isaapp.herokuapp.com/myapp"  || url == "http://isaapp.herokuapp.com/myapp/") {
			pathname = "";
		}
		else {
			pathname = "go to default";
		}
		
	}
	
	return pathname
}

function getStringFor404() {
	var now = new Date(Date.now());
	var str = "<html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>"+now+"</div><div>There was an unexpected error (type=Not Found, status=404).</div><div>No message available</div></body></html>"
	
		return str;
}

function loadHomePageComplete() {
	$("#title").html('HOME PAGE &nbsp;&nbsp; <a href="/myapp/#/users/register" class="a_register"> Register </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
}

function loadRegisterComplete() {
	$("#id_username").focus();
	$("#title").html('REGISTER &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
}

function loadLoginComplete() {
	$("#login_username").focus();
	$("#title").html('LOGIN &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a> ');
}

function loadUpadteProfileComplete() {
	$("#title").html('UPDATE PROFILE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a> ');
}

function loadChangedPasswordComplete() {
	$("#title").html('CHANGE PASSWORD &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a> ');
}

function loadFriendsPageComplete() {
	$("#title").html('FRIENDS PAGE &nbsp;&nbsp;  <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a>  ');
}

function loadInvitationsAndTicketsPageComplete() {
	$("#title").html('INVITATIONS AND TICKETS PAGE &nbsp;&nbsp;  <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a>  ');
}

function loadCulturalInstitutionPageComplete() {
	$("#title").html('CULTURAL INSTITUTION PAGE &nbsp;&nbsp;  <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a>  ');
}

function loadShowingPageComplete() {
	$("#title").html('SHOWING PAGE &nbsp;&nbsp;  <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a>  ');
}

function loadVisitedAndUnvisitedCulturalInstitutionsPageComplete() {
	$("#title").html('VISITED AND UNVISITED CULTURAL INSTITUTIONS PAGE &nbsp;&nbsp;  <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a>  ');
}

$(document).on("click", ".a_register", function(event) {
	event.preventDefault();
	
	 if(window.history.pushState) {
	    window.history.pushState(null, null, $(this).attr('href')); // set URL
	 }
	//window.history.pushState({urlPath:'/myapp/#/register'},"",'/myapp/#/register');
	
	$("#center").load("html/partials/register.html", null, loadRegisterComplete);
});

$(document).on("click", ".a_login", function(event) {
	event.preventDefault();
	
	// Detect if pushState is available
	  if(window.history.pushState) {
	    window.history.pushState(null, null, $(this).attr('href')); // set URL
	  }
	//window.history.pushState({urlPath:'/myapp/#/login'},"",'/myapp/#/login');
	
	fastLogin = false;
	loadLogin();
});


function loadLogin() {
	var logged = isLogged();
	if (logged) { // ako je vec ulogovan
		successfullyLogged();
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function isLogged() {
	var receivedLogged = false;
	
	$.ajax({
		async: false,
		type : "GET",
		url : isLoggedURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(logged) {
						receivedLogged = logged;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return receivedLogged; 
}

$(document).on("click", ".a_home_page", function(event) {
	event.preventDefault();
	
	 if(window.history.pushState) {
	    window.history.pushState(null, null, $(this).attr('href')); // set URL
	 }
	
	//window.history.pushState('forward', null, 'http://localhost:8080/myapp');
	//window.history.pushState({urlPath:'/myapp/#/'},"",'/myapp/#/');
	$("#center").load("html/partials/home_page.html", null, loadHomePageComplete);
});


function loadLoggedUser() {	
	var receivedUser;
	
	$.ajax({
		async: false, // ovo stavljamo da bi kod, koji sledi nakon ajax poziva, sacekao da se izvrsi ajax poziv
		              // konkretno ovde, "return receivedUser;" se nece izvrsiti dok se ne izvrsi ajax poziv
		type : "GET",
		url : loggedUserURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(loggedUser) {  // ova funkcija ce se izvrsiti kada se ajax poziv uspesno izvrsi
										  	// iz ove funkcije nije moguce odraditi return loadLoggedUser funkciju,
											// pa zato uvodimo promenljivu receivedUser, koju cemo setovati ako se 
											// ajax poziv izvrsi uspesno
						if(loggedUser== "") {
							receivedUser = null;
						}
						else {
							receivedUser = loggedUser;
						}
						
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return receivedUser; // da nismo stavili "async: false" u ajax pozivu, ovaj return statement bi se izvrsio pre nego
	                     // sto bi bio zavrsen ajax poziv i setovanje promenljive receivedUser u okviru ajax poziva
}

function attemptToLog(username, password) {
	$.ajax({ 
	    type: "PUT",
		url:  loginURL,
        data: JSON.stringify({"username": username.trim(), "password": password.trim()}),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(logged) {
					if(logged) { 
						successfullyLogged();
						
					}
					else {
						toastr.error("Invalid username or password!");
					}
					
				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}

function successfullyLogged() {
	$("#center").empty();
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/menu.css'
	});
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/showing.css'
	});
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: '//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css'
	});
	
	$("#center").prepend('<div id="id_menu"></div><br/><br/>');
	$("#id_menu").load("html/partials/menu.html", null, function () {
		var loggedUser = loadLoggedUser();
		
		if(loggedUser) {
			if (loggedUser.userType == "REGISTERED_USER") {
				registeredUserPage();
				toastr.success('You have successfully logged in!'); 
			}
			else if (loggedUser.userType == "SYS_ADMINISTRATOR") {
				adminSystemPage(loggedUser);
				toastr.success('You have successfully logged in!'); 
			}
			
			else if (loggedUser.userType == "FUNZONE_ADMINISTRATOR") {
				adminFunzonePage(loggedUser);
				toastr.success('You have successfully logged in!'); 
			}
			
			else if (loggedUser.userType == "INSTITUTION_ADMINISTRATOR") {
				adminCulturalInstitutionsPage(loggedUser);
				toastr.success('You have successfully logged in!'); 
			}
			else {
				toastr.error('Problem with logging!'); 	
			}
			
		}
		else {
			toastr.error('Problem with logging!'); 
			
		}
		
	});
	
}

function registeredUserPage() {
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/friends_page.css'
	});
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/invitations_and_tickets.css'
	});
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/visited_and_unvisited_cultural_institutions.css'
	});
	
	$("#title").html('REGISTERED USER PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/register" class="a_register" > Register </a> ');
	
	$("#myDropdown").append('<a id="id_seats_reservation" href="/myapp/#/users/seats_reservation"> Seats reservation </a>');
	$("#myDropdown").append('<a id="id_invitations_and_tickets" href="/myapp/#/users/invitations_and_tickets"> Invitations and tickets </a>');
	$("#myDropdown").append('<a id="id_change_password" href="/myapp/#/users/change_password"> Change password </a>');
	$("#myDropdown").append('<a id="id_update_profile" href="/myapp/#/users/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_friends" href="/myapp/#/users/friends"> Friends </a>');
	$("#myDropdown").append('<a id="id_visited_unvisited" href="/myapp/#/users/visited_and_unvisited_cultural_institutions"> Visited and unvisited cultural institutions </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/login"> Logout </a>');
	
	showVisitedAndUnvisitedCulturalInstitutions();
	
	$("#id_seats_reservation").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		reservationSeats();
	});
	
	$("#id_invitations_and_tickets").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		invitationsAndTickets();
	});
	
	$("#id_change_password").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		changePassword(saveChangedPasswordURL);
	});
	
	$("#id_update_profile").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		updateProfile();
	});
	
	$("#id_friends").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		friendsPage();
	});
	
	$("#id_visited_unvisited").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		showVisitedAndUnvisitedCulturalInstitutions();
		loadVisitedAndUnvisitedCulturalInstitutionsPageComplete();
	});
	
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		if(window.history.pushState) {
		    window.history.pushState(null, null, $(this).attr('href')); // set URL
		}
		
		logout();
	});
	
	if(fastLogin) {	
		invitationsAndTickets();
	}
}

function updateProfile() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();
		
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
						<tr>  <td><label for="id_city">City:</label></td> <td><input type="text" id="id_city" value="' + loggedUser.city + '" /></td>  </tr> \
						<tr>  <td><label for="id_phone_number">Phone number:</label></td>  <td><input type="text" id="id_phone_number" value="'+ loggedUser.phoneNumber +'" /></td>  </tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Save changes"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			
			saveChangesOnProfile();
		});
		
		loadUpadteProfileComplete();
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function saveChangesOnProfile() {
	var username = $("#id_username").val();
	var oldPassword = $("#id_old_password").val();
	var newPassword = $("#id_new_password").val();
	var repeatNewPassword = $("#id_repeat_new_password").val();
	var firstName = $("#id_first_name").val();
	var lastName = $("#id_last_name").val();
	var email = $("#id_email").val();
	var city = $("#id_city").val();
	var phoneNumber = $("#id_phone_number").val();

	$.ajax({ 
	    type: "PUT",
		url:  saveChangesOnProfileURL,
	    data: JSON.stringify({
			"username": username,
			"oldPassword": oldPassword,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword,
			"firstName": firstName,
			"lastName": lastName,
			"email": email,
			"city": city,
			"phoneNumber": phoneNumber
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
		    		toastr.error("Incorrect email!");
		    		break;
		    	case 5:
		    		toastr.error("Incorrect phone number!");
		    		break;
		    	case 6:
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

function changePassword(changePasswordUrl) {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var loggedUser = loadLoggedUser();
		
		var center = $("#center");
		
		deleteAllExceptFirst();
		
		center.append(
				'<form > \
					<table> \
						<tr>  <td><label for="id_old_password">Old password:</label></td>  <td><input type="password" id="id_old_password" /></td>  </tr> \
						<tr>  <td><label for="id_new_password">New password:</label></td>  <td><input type="password" id="id_new_password" /></td>  </tr> \
						<tr>  <td><label for="id_repeat_new_password">Repeat new password:</label></td>  <td><input type="password" id="id_repeat_new_password" /></td>  </tr> \
					</table> \
					<div align="center"><input type="button" id="id_btn_save_changes_on_profile" class="buttons" value="Save new password"/> \
					</div> \
					<br/> \
				</form>');
		
		$("#id_old_password").focus();
		
		$("#id_btn_save_changes_on_profile").click(function(event) {
			event.preventDefault();
			
			saveChangedPassword(changePasswordUrl);
			
		});
		
		loadChangedPasswordComplete();
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}

}

function saveChangedPassword(changePasswordUrl) {
	var oldPassword = $("#id_old_password").val();
	var newPassword = $("#id_new_password").val();
	var repeatNewPassword = $("#id_repeat_new_password").val();

	$.ajax({ 
	    type: "PUT",
		url:  changePasswordUrl,
	    data: JSON.stringify({
			"oldPassword": oldPassword,
			"newPassword": newPassword,
			"repeatNewPassword": repeatNewPassword
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
		    		toastr.error("You did not enter the correct old password!");
		    		break;
		    	case 2:
		    		toastr.error("You are not the first to enter the same new password the second time!");
		    		break;
		    	case 3:
		    		$("#id_old_password").val("")
		    		$("#id_new_password").val("");
		    		$("#id_repeat_new_password").val("");
		    		toastr.success("You have successfully changed password!");
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

function friendsPage() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		
		deleteAllExceptFirst();
		$("#center").append('<div id="id_friends_page"></div>');
		
		$("#id_friends_page").load("html/partials/friends_page.html", workWithFriends);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function workWithFriends() {
	var people = getPeople();
	var friends = getFriends();
	var requests = getRequests();
	
	$("#id_new_friend").focus();
	
	//popunjavanje tabele zahteva 
	if(requests) {
		for (var i in requests) {
			  $( "#id_table_requests" ).append('<tr id="id_request_row_' + indexOfRequests + '"> <td></td> <td> <b>' + requests[i] + ' </b></td> <td><input type="button" id="id_accept_request_' + indexOfRequests + '" class="buttons" value="Accept request"/></td> <td><input type="button" id="id_decline_request_' + indexOfRequests + '" class="buttons_remove" value="Decline request"/></td> </tr>');
			  $("#id_decline_request_" + indexOfRequests).on("click", {index: indexOfRequests, request: requests[i]}, declineRequest);
			  
			  $("#id_accept_request_" + indexOfRequests).on("click", {index: indexOfRequests, request: requests[i]}, acceptRequest);
			  
			  indexOfRequests++;  
		}
		
		$("#id_header_requests").click(function(event) {
			event.preventDefault();
			
			sortTable(1, 1, "id_table_requests");
		});
	}
	
	//popunjavanje tabele prijatelja 
	if(friends) {
		for (var i in friends) {
			  $( "#id_table_friends" ).append('<tr id="id_friend_row_' + indexOfFriends + '"> <td></td> <td> <b>' + friends[i] + ' </b></td> <td><input type="button" id="id_remove_friend_' + indexOfFriends + '" class="buttons_remove" value="Remove friend"/></td> </tr>');
			  $("#id_remove_friend_" + indexOfFriends).on("click", {index: indexOfFriends, removeFriend: friends[i]}, removeFriend);
			  
			  indexOfFriends++;  
		}
		
		$("#id_header_friends").click(function(event) {
			event.preventDefault();
			
			sortTable(1, 1, "id_table_friends");
		});
	}
	
	// podesavanje za autocomplete
	$( function() {
	    $( "#id_new_friend" ).autocomplete({
	      source: people,
	      minLength: 0
	    }).focus(function(){            
            // The following works only once.
            // $(this).trigger('keydown.autocomplete');
            // As suggested by digitalPBK, works multiple times
            // $(this).data("autocomplete").search($(this).val());
            // As noted by Jonny in his answer, with newer versions use uiAutocomplete
            $(this).data("uiAutocomplete").search($(this).val());
        });
	});
	
	
	$("#id_btn_send_request").click(function(event) {
		event.preventDefault();
		
		var newFriend = $("#id_new_friend").val();
		
		var retValue = sendRequestFriend(newFriend);
		if(retValue) {
			$("#id_new_friend").val("");
		
		}
		
	});
	
	loadFriendsPageComplete();
}

function declineRequest(event) {
	event.preventDefault();
	var retValue = declineRequestFriend(event.data.request);
	if(retValue) {
		$( "#id_request_row_" + event.data.index ).remove();
		//indexOfRequests--;
	}
}

function acceptRequest(event) {
	event.preventDefault();
	
	var retValue = acceptRequestFriend(event.data.request);
	if(retValue) {
		$( "#id_request_row_" + event.data.index ).remove();
		//indexOfRequests--;
		
		$( "#id_tr_header_friends" ).after('<tr id="id_friend_row_' + indexOfFriends + '"> <td></td> <td><b>' + event.data.request + ' </b></td> <td><input type="button" id="id_remove_friend_' + indexOfFriends + '" class="buttons_remove" value="Remove friend"/></td> </tr>');
		$("#id_remove_friend_" + indexOfFriends).on("click", {index: indexOfFriends, removeFriend: event.data.request}, removeFriend);
		
		indexOfFriends++;
	}
}

function removeFriend(event) {
	event.preventDefault();
	
	var retValue = sendRemoveFriend(event.data.removeFriend);
	$( "#id_friend_row_" + event.data.index ).remove();
	
	
}

function sendRemoveFriend(friendForRemove) {
	var retValue = false;
	
	var tokens = friendForRemove.split("-");
	if(tokens.length == 2) {
		var username = tokens[0].trim();
		
		$.ajax({
			async: false,
			type : "DELETE",
			url : removeFriendURL,
			data: JSON.stringify({"username": username}),
			dataType : "json",
		    contentType: "application/json",
		    cache: false, 
			success : function(successRemove) { 
				if(successRemove) {
					toastr.success("You've successfully removed a friend!"); 
				}
				else {
					toastr.error(friendForRemove + " has already deleted your friendship!"); 
				}
				retValue = successRemove;
			
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
	}
	else {
		toastr.error("Unsuccessful removal of friendship with " + newFriend + "!"); 
	}
	
	return retValue;
}

function sendRequestFriend(newFriend) {
	var retValue = false;
	
	var tokens = newFriend.split("-");
	if(tokens.length == 2) {
		var username = tokens[0].trim();
		
		$.ajax({ 
			async: false,
		    type: "PUT",
			url:  sendRequestFriendURL,
			data: JSON.stringify({"username": username}),
			dataType : "json",
		    contentType: "application/json",
		    cache: false,
		    success: function(successSendRequest) {
		    	if(successSendRequest) {
		    		toastr.success("Request sent!"); 
		    	}
		    	else {
		    		toastr.error("Request not sent!"); 
		    	}
		    	retValue = successSendRequest;
					
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
	}
	else {
		toastr.error("Request not sent!"); 
	}
	
	return retValue;
}

function acceptRequestFriend(newFriend) {
	var retValue = false;
	
	var tokens = newFriend.split("-");
	if(tokens.length == 2) {
		var username = tokens[0].trim();
		
		$.ajax({ 
			async: false,
		    type: "PUT",
			url:  acceptRequestFriendURL,
			data: JSON.stringify({"username": username}),
			dataType : "json",
		    contentType: "application/json",
		    cache: false,
		    success: function(successAcceptRequest) {
		    	if(successAcceptRequest) {
		    		toastr.success("You accepted a request for friendship with " + newFriend + "!"); 
		    	}
		    	else {
		    		toastr.error("Unsuccessful acceptance of the request for friendship with " + newFriend + "!"); 
		    	}
		    	retValue = successAcceptRequest;
					
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
	}
	else {
		toastr.error("Unsuccessful acceptance of the request for friendship with " + newFriend + "!"); 
	}
	
	return retValue;
}

function declineRequestFriend(newFriend) {
	var retValue = false;
	
	var tokens = newFriend.split("-");
	if(tokens.length == 2) {
		var username = tokens[0].trim();
		
		$.ajax({ 
			async: false,
		    type: "PUT",
			url:  declineRequestFriendURL,
			data: JSON.stringify({"username": username}),
			dataType : "json",
		    contentType: "application/json",
		    cache: false,
		    success: function(successDeclineRequest) {
		    	if(successDeclineRequest) {
		    		toastr.success("You declined a request for friendship with " + newFriend + "!"); 
		    	}
		    	else {
		    		toastr.error("Unsuccessful decline of the request for friendship with " + newFriend + "!"); 
		    	}
		    	retValue = successDeclineRequest;
					
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
	}
	else {
		toastr.error("Unsuccessful decline of the request for friendship with " + newFriend + "!"); 
	}
	
	return retValue;
}


function getPeople() {
	var people = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : peopleURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receivePeople) {
						people = receivePeople;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return people;
}

function getAuditoriums() {
	var auditoriums = null;
	
	var culturalInstitution = $("#id_cultural_institution").val();
	var showing = $("#id_showing").val();
	var date = $("#id_date").val();
	
	$.ajax({
		async: false,
		type : "PUT",
		url : getAuditoriumsURL,
		dataType : "json",
		data: JSON.stringify({"culturalInstitution": culturalInstitution.trim(), 
			                  "showing": showing.trim(),
			                  "date": date.trim()
         }),
		contentType: "application/json",
		cache: false,
		success : function(receiveAuditoriums) {
			auditoriums = receiveAuditoriums;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return auditoriums;
}

function getTimes() {
	var times = null;
	
	var culturalInstitution = $("#id_cultural_institution").val();
	var showing = $("#id_showing").val();
	var date = $("#id_date").val();
	var auditorium = $("#id_auditorium").val();
	
	$.ajax({
		async: false,
		type : "PUT",
		url : getTimesURL,
		dataType : "json",
		data: JSON.stringify({"culturalInstitution": culturalInstitution.trim(), 
			                  "showing": showing.trim(),
			                  "date": date.trim(),
			                  "auditorium": auditorium.trim()
         }),
		contentType: "application/json",
		cache: false,
		success : function(receiveTimes) {
			times = receiveTimes;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return times;
}

function getDates() {
	var dates = null;
	
	var culturalInstitution = $("#id_cultural_institution").val();
	var showing = $("#id_showing").val();
	
	$.ajax({
		async: false,
		type : "PUT",
		url : getDatesURL,
		dataType : "json",
		data: JSON.stringify({"culturalInstitution": culturalInstitution.trim(), 
			"showing": showing.trim()
         }),
		contentType: "application/json",
		cache: false,
		success : function(receiveDates) {
			dates = receiveDates;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return dates;
}


function getFriends() {
	var friends = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : friendsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveFriends) {
						friends = receiveFriends;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return friends;
}

function getRequests() {
	var requests = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : requestsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveRequests) {
				requests = receiveRequests;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return requests;
}

function logout() {
	$.ajax({
		type : "DELETE",
		url : logOutURL,
		success : function() { 
			toastr.success("You've been successfully logged out!"); 

			$("#center").load("html/partials/login.html", null, loadLoginComplete);			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}

function deleteAllExceptFirst() {
	var center = $("#center");
	var children = center.children();
	var len = children.length;
	
	for(var i = 1; i < len; i++) {
		children[i].remove();
	}
}


$(document).on("click", "#id_btn_login", function(event) {
	event.preventDefault();
	
	var username = $("#login_username").val();
	var password = $("#login_password").val();
	
	if (username === "" || password === "") {
		toastr.error("You did not fill all the fields!");
	}
	else {
		attemptToLog(username, password);
	}
});

$(document).on("click", "#id_btn_register", function(event) {
	event.preventDefault();
	
	registrateUser();
});

function registrateUser() {
	var username = $("#id_username").val();
	var password = $("#id_password").val();
	var repeatPassword = $("#id_repeat_password").val();
	var firstName = $("#id_first_name").val();
	var lastName = $("#id_last_name").val();
	var email = $("#id_email").val();
	var city = $("#id_city").val();
	var phoneNumber = $("#id_phone_number").val();
	
	$.ajax({ 
	    type: "POST",
		url:  registrateUserURL,
	    data: JSON.stringify({
			"username": username,
			"password": password,
			"repeatPassword": repeatPassword,
			"firstName": firstName,
			"lastName": lastName,
			"email": email,
			"city": city,
			"phoneNumber": phoneNumber
		}),
	    dataType: "json", 
	    contentType: "application/json",
	    cache: false,
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

//Menu
$(window).on("click", function(event) {
	  //event.preventDefault();
	  
	  if (!event.target.matches('.dropbtn')) {
		  var dropdowns = $(".dropdown-content");

		    $.each(dropdowns, function(index, openDropdown) {
		      if (openDropdown.classList.contains('show')) {
		        openDropdown.classList.remove('show');
		      }
		    });
	  }
});

$(document).on("click", ".dropbtn", function(event)  {
	event.preventDefault();
	
    //$("#myDropdown").classList.toggle("show");
	$("#myDropdown").addClass("show");
});
	


function sortTable(startIndexRow, n, id_for_table) {
	  var rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
	  //table = $("#id_table_friends")
	  switching = true;
	  // Set the sorting direction to ascending:
	  dir = "asc"; 
	  /* Make a loop that will continue until
	  no switching has been done: */
	  while (switching) {
	    // Start by saying: no switching is done:
	    switching = false;
	    rows = $("#" + id_for_table + " tr")
	    /* Loop through all table rows (except the
	    first, which contains table headers): */
	    for (var i = startIndexRow; i < (rows.length - 1); i++) {
			// Start by saying there should be no switching:
			shouldSwitch = false;
			/* Get the two elements you want to compare,
			one from current row and one from the next: */
			x = $(rows[i]).children()[n];
			y = $(rows[i + 1]).children()[n];
			/* Check if the two rows should switch place,
			based on the direction, asc or desc: */
			if (dir == "asc") {
				if($($(x).children()[0]).is("a") && $($(y).children()[0]).is("a")) {
					if ($($(x).children()[0]).text().toLowerCase() > $($(y).children()[0]).text().toLowerCase()) {
					  // If so, mark as a switch and break the loop:
					  shouldSwitch= true;
					  break;
					}
				}
				else {
					 if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
					  // If so, mark as a switch and break the loop:
					  shouldSwitch= true;
					  break;
					}
				}
			   
			} 
			else if (dir == "desc") {
				if($($(x).children()[0]).is("a") && $($(y).children()[0]).is("a")) {
					if ($($(x).children()[0]).text().toLowerCase() < $($(y).children()[0]).text().toLowerCase()) {
					  // If so, mark as a switch and break the loop:
					  shouldSwitch= true;
					  break;
					}
				}
				else {
					if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
					  // If so, mark as a switch and break the loop:
					  shouldSwitch= true;
					  break;
					}
				}
				
			}
	    }
		
	    if (shouldSwitch) {
	      /* If a switch has been marked, make the switch
	      and mark that a switch has been done: */
	      $(rows[i]).before(rows[i + 1]);
	      switching = true;
	      // Each time a switch is done, increase this count by 1:
	      switchcount ++; 
	    } 
	    else {
	      /* If no switching has been done AND the direction is "asc",
	      set the direction to "desc" and run the while loop again. */
	      if (switchcount == 0 && dir == "asc") {
	        dir = "desc";
	        switching = true;
	      }
	    }
	  }
}


function reservationSeats() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		$('<link>')
		  .appendTo('head')
		  .attr({
		      type: 'text/css', 
		      rel: 'stylesheet',
		      href: 'css/seat_reservation.css'
		});
		
		deleteAllExceptFirst();
		
		$("#center").append('<div id="id_seat_reservation_page"></div>');
		$("#id_seat_reservation_page").load("html/partials/reservation_page.html", loadReservationPageComplete);
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}


function getShowingsOfCulturalInstitutionForReservation(culturalInstitutionName) {
	var showingsOfCulturalInstitution = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getShowingsOfCulturalInstitutionForReservationURL + "/" + culturalInstitutionName,
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


function getCulturalInstitutionsForReservation() {
	var culturalInstitutions = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getCulturalInstitutionsForReservationURL,
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

function loadAllForReservation() {
	$("#id_price").text(0);
	
	var culturalInstitutions = getCulturalInstitutionsForReservation();
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
			
			var showingsOfCulturalInstitution = getShowingsOfCulturalInstitutionForReservation(culturalInstitutions[0]);
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
					
					
					var dates = getDates(culturalInstitutions[0], showingsOfCulturalInstitution[0]);
					if(dates) {
						if (dates.length > 0) {
							$.each(dates, function(index3, date) {
								if(index3 == 0) {
									selected = "selected";
								}
								else{
									selected = "";
								}
								$("#id_date").append("<option " + selected + "> " + date + " </option>");
							});
							
							
							var auditoriums = getAuditoriums(culturalInstitutions[0], showingsOfCulturalInstitution[0], dates[0]);
							if(auditoriums) {
								if (auditoriums.length > 0) {
									$.each(auditoriums, function(index4, auditorium) {
										if(index4 == 0) {
											selected = "selected";
										}
										else{
											selected = "";
										}
										$("#id_auditorium").append("<option " + selected + "> " + auditorium + " </option>");
									});
									
									var times = getTimes(culturalInstitutions[0], showingsOfCulturalInstitution[0], dates[0], auditoriums[0]);
									if(times) {
										if (times.length > 0) {
											$.each(times, function(index5, time) {
												if(index5 == 0) {
													selected = "selected";
												}
												else{
													selected = "";
												}
												$("#id_time").append("<option " + selected + "> " + time + " </option>");
											});
											
											var price = getPrice(culturalInstitutions[0], showingsOfCulturalInstitution[0], dates[0], auditoriums[0], times[0]);
											if(price) {
												$("#id_price").text(price);
											}
											else {
												$("#id_price").text(0);
												toastr.error("Not found term for selected options!");
											}
											
										}
										else {
											toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
										}
									}
									else {
										toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
									}
								}
								else {
									toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
								}
							}
							else {
								toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
							}
							
						}
						else {
							toastr.error("Dates, for selected cultural institution and showing, are not available!");
						}
					}
					else {
						toastr.error("Dates, for selected cultural institution and showing, are not available!");
					}
					
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

function changedCulturalInstitutionForReservation() {
	$("#id_seats").remove();
	
	var currentCulturalInstitution = $("#id_cultural_institution").val();
	$("#id_showing").empty();
	$("#id_date").empty();
	$("#id_auditorium").empty();
	$("#id_time").empty();
	
	var showingsOfCulturalInstitution = getShowingsOfCulturalInstitutionForReservation(currentCulturalInstitution);
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
			
			var dates = getDates(currentCulturalInstitution, showingsOfCulturalInstitution[0]);
			if(dates) {
				if (dates.length > 0) {
					$.each(dates, function(index2, date) {
						if(index2 == 0) {
							selected = "selected";
						}
						else{
							selected = "";
						}
						$("#id_date").append("<option " + selected + "> " + date + " </option>");
					});
					
					
					var auditoriums = getAuditoriums(currentCulturalInstitution, showingsOfCulturalInstitution[0], dates[0]);
					if(auditoriums) {
						if (auditoriums.length > 0) {
							$.each(auditoriums, function(index3, auditorium) {
								if(index3 == 0) {
									selected = "selected";
								}
								else{
									selected = "";
								}
								$("#id_auditorium").append("<option " + selected + "> " + auditorium + " </option>");
							});
							
							
							var times = getTimes(currentCulturalInstitution, showingsOfCulturalInstitution[0], dates[0], auditoriums[0]);
							if(times) {
								if (times.length > 0) {
									$.each(times, function(index, time) {
										if(index4 == 0) {
											selected = "selected";
										}
										else{
											selected = "";
										}
										$("#id_time").append("<option " + selected + "> " + time + " </option>");
									});
									
									var price = getPrice(currentCulturalInstitution, showingsOfCulturalInstitution[0], dates[0], auditoriums[0], times[0]);
									if(price) {
										$("#id_price").text(price);
									}
									else {
										$("#id_price").text(0);
										toastr.error("Not found term for selected options!");
									}
									
								}
								else {
									toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
								}
							}
							else {
								toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
							}
							
						}
						else {
							toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
						}
					}
					else {
						toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
					}
					
				}
				else {
					toastr.error("Dates, for selected cultural institution and showing, are not available!");
				}
			}
			else {
				toastr.error("Dates, for selected cultural institution and showing, are not available!");
			}
			
		}
		else {
			toastr.error("Showings, for selected cultural institution, are not available!");
		}
	}
	else {
		toastr.error("Showings, for selected cultural institution, are not available!");
	}
	
}


function changedShowingForReservation() {
	$("#id_seats").remove();
	
	var currentCulturalInstitution = $("#id_cultural_institution").val();
	var currentShowing = $("#id_showing").val();
	$("#id_date").empty();
	$("#id_auditorium").empty();
	$("#id_time").empty();
	
	var dates = getDates(currentCulturalInstitution, currentShowing);
	if(dates) {
		if (dates.length > 0) {
			$.each(dates, function(index, date) {
				if(index == 0) {
					selected = "selected";
				}
				else{
					selected = "";
				}
				$("#id_date").append("<option " + selected + "> " + date + " </option>");
			});
			
			
			var auditoriums = getAuditoriums(currentCulturalInstitution, currentShowing, dates[0]);
			if(auditoriums) {
				if (auditoriums.length > 0) {
					$.each(auditoriums, function(index2, auditorium) {
						if(index2 == 0) {
							selected = "selected";
						}
						else{
							selected = "";
						}
						$("#id_auditorium").append("<option " + selected + "> " + auditorium + " </option>");
					});
					
					var times = getTimes(currentCulturalInstitution, currentShowing, dates[0], auditoriums[0]);
					if(times) {
						if (times.length > 0) {
							$.each(times, function(index3, time) {
								if(index3 == 0) {
									selected = "selected";
								}
								else{
									selected = "";
								}
								$("#id_time").append("<option " + selected + "> " + time + " </option>");
							});
							
							var price = getPrice(currentCulturalInstitution, currentShowing, dates[0], auditoriums[0], times[0]);
							if(price) {
								$("#id_price").text(price);
							}
							else {
								$("#id_price").text(0);
								toastr.error("Not found term for selected options!");
							}
							
						}
						else {
							toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
						}
					}
					else {
						toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
					}
					
				}
				else {
					toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
				}
			}
			else {
				toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
			}
			
		}
		else {
			toastr.error("Dates, for selected cultural institution and showing, are not available!");
		}
	}
	else {
		toastr.error("Dates, for selected cultural institution and showing, are not available!");
	}
	
}

function changedDatesForReservation() {
	$("#id_seats").remove();
	
	var currentCulturalInstitution = $("#id_cultural_institution").val();
	var currentShowing = $("#id_showing").val();
	var currentDate = $("#id_date").val();
	$("#id_auditorium").empty();
	$("#id_time").empty();
	
	var auditoriums = getAuditoriums(currentCulturalInstitution, currentShowing, currentDate);
	if(auditoriums) {
		if (auditoriums.length > 0) {
			$.each(auditoriums, function(index, auditorium) {
				if(index == 0) {
					selected = "selected";
				}
				else{
					selected = "";
				}
				$("#id_auditorium").append("<option " + selected + "> " + auditorium + " </option>");
			});
			
			var times = getTimes(currentCulturalInstitution, currentShowing, currentDate, auditoriums[0]);
			if(times) {
				if (times.length > 0) {
					$.each(times, function(index2, time) {
						if(index2 == 0) {
							selected = "selected";
						}
						else{
							selected = "";
						}
						$("#id_time").append("<option " + selected + "> " + time + " </option>");
					});
					
					var price = getPrice(currentCulturalInstitution, currentShowing, currentDate, auditoriums[0], times[0]);
					if(price) {
						$("#id_price").text(price);
					}
					else {
						$("#id_price").text(0);
						toastr.error("Not found term for selected options!");
					}
				}
				else {
					toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
				}
			}
			else {
				toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
			}
			
		}
		else {
			toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
		}
	}
	else {
		toastr.error("Auditoriums, for selected cultural institution and showing and date, are not available!");
	}
	
}

function changedAuditoriumsForReservation() {
	$("#id_seats").remove();
	
	var currentCulturalInstitution = $("#id_cultural_institution").val();
	var currentShowing = $("#id_showing").val();
	var currentDate = $("#id_date").val();
	var currentAuditorium = $("#id_auditorium").val();
	$("#id_time").empty();
	
	var times = getTimes(currentCulturalInstitution, currentShowing, currentDate, currentAuditorium);
	if(times) {
		if (times.length > 0) {
			$.each(times, function(index2, time) {
				if(index2 == 0) {
					selected = "selected";
				}
				else{
					selected = "";
				}
				$("#id_time").append("<option " + selected + "> " + time + " </option>");
			});
			
			var price = getPrice(currentCulturalInstitution, currentShowing, currentDate, currentAuditorium, times[0]);
			if(price) {
				$("#id_price").text(price);
			}
			else {
				$("#id_price").text(0);
				toastr.error("Not found term for selected options!");
			}
		}
		else {
			toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
		}
	}
	else {
		toastr.error("Times, for selected cultural institution and showing and date and auditorium, are not available!");
	}
			
}

function getPrice(currentCulturalInstitution, currentShowing, currentDate, currentAuditorium, currentTime) {
	var price = null;
	
	$.ajax({ 
		async: false,
	    type: "PUT",
		url: getPriceURL,
		dataType : "json",
		data: JSON.stringify({"date": currentDate.trim(), 
								"time": currentTime.trim(), 
								"culturalInstitution": currentCulturalInstitution.trim(), 
								"showing": currentShowing.trim(),
								"auditorium": currentAuditorium.trim()
		}),
	    contentType: "application/json",
	    cache: false,
	    success: function(retValue) {
	    	if(retValue["hasPrice"]) {
	    		var retPrice = retValue["price"]; 
	    		if(retPrice) {
	    			price = retPrice;
	    		}
	    	}
	    				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
	return price;
}

function loadReservationPageComplete() {
	
	loadAllForReservation();
	
	$("#center").append('<div id="dialog" class="ui-front" title="Invite friends..."><table id="id_invite_friends" class="ui-widget"></table></div>');
	
	$("#id_cultural_institution").change(changedCulturalInstitutionForReservation);
	$("#id_showing").change(changedShowingForReservation);
	$("#id_date").change(changedDatesForReservation);
	$("#id_auditorium").change(changedAuditoriumsForReservation);
	$("#id_time").change(function() { 
		var price = getPrice($("#id_cultural_institution").val(), $("#id_showing").val(), $("#id_date").val(), $("#id_auditorium").val(), $("#id_time").val());
		if(price) {
			$("#id_price").text(price);
		}
		else {
			$("#id_price").text(0);
			toastr.error("Not found term for selected options!");
		}
		$("#id_seats").remove();
	});
	
	$('#id_btn_show_seats').click(function (event) {
		event.preventDefault();
		
		$("#id_seats").remove();
		
		var indexesOfBusySeatsAndRowsCols = getIndexesOfBusySeatsAndRowsCols();
		if(indexesOfBusySeatsAndRowsCols) {
			$("#id_seat_reservation_page").after('<div id="id_seats"></div>');
			$("#id_seats").load("html/partials/seat_reservation.html", function () {
						settings.rows = indexesOfBusySeatsAndRowsCols[indexesOfBusySeatsAndRowsCols.length - 2];
						settings.cols = indexesOfBusySeatsAndRowsCols[indexesOfBusySeatsAndRowsCols.length - 1];
						var reservedSeat = indexesOfBusySeatsAndRowsCols.slice(0, indexesOfBusySeatsAndRowsCols.length-2);
						
						
						$("#holder").width(settings.cols * settings.seatWidth + 25);
						$("#holder").height(settings.rows * settings.seatHeight + 25);
						init(reservedSeat);
						
						$('.' + settings.seatCss).click(function () {
							event.preventDefault();
							
							if ($(this).hasClass(settings.selectedSeatCss)) {
							    toastr.error('This seat is already reserved');
							}
							else{
							    $(this).toggleClass(settings.selectingSeatCss);
						    }
						});
						 
						$('#id_btn_book_selected_seats').click(function (event) {
							event.preventDefault();
							
						    var str = [], item;
						    $.each($('#place li.' + settings.selectingSeatCss + ' a'), function (index, value) {
						        item = $(this).attr('title');                   
						        str.push(item);                   
						    });
						    var selectedSeats = str.join(',');
						    
						    bookSelectedSeats(selectedSeats);
						});
			});
		}
		else {
			toastr.error('There is no term for the selected parameters!');
		}
		
	});
	
}

var settings = {
   rows: 5,  // default
   cols: 15, // default
   rowCssPrefix: 'row-',
   colCssPrefix: 'col-',
   seatWidth: 35,
   seatHeight: 35,
   seatCss: 'seat',
   selectedSeatCss: 'selectedSeat',
   selectingSeatCss: 'selectingSeat'
};

function getIndexesOfBusySeatsAndRowsCols() {
	var date = $("#id_date").val();
	var auditorium = $("#id_auditorium").val();
	var time = $("#id_time").val();
	var culturalInstitution = $("#id_cultural_institution").val();
	var showing = $("#id_showing").val();
	
	if(date == null || auditorium == null || time == null || culturalInstitution == null || showing == null){
		toastr.error("You did not select all the necessary options!");
		return null;
	}
	else {
		var indexesOfBusySeatsAndRowsCols = null;
		
		$.ajax({ 
			async: false,
		    type: "PUT",
			url: getIndexesOfBusySeatsAndRowsColsURL,
			dataType : "json",
			data: JSON.stringify({"date": date.trim(), 
									"time": time.trim(), 
									"culturalInstitution": culturalInstitution.trim(), 
									"showing": showing.trim(),
									"auditorium": auditorium.trim()
			}),
		    contentType: "application/json",
		    cache: false,
		    success: function(retValue) {
		    	if(retValue.existsTerm) {
		    		indexesOfBusySeatsAndRowsCols =  retValue.indexOfBusySeatsAndRowsCols;
		    	}
		    	
					
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
		
		return indexesOfBusySeatsAndRowsCols;
	}
	
}

function bookSelectedSeats(selectedSeats) {
	if(selectedSeats!== "") {
		var date = $("#id_date").val();
		var auditorium = $("#id_auditorium").val();
		var time = $("#id_time").val();
		var culturalInstitution = $("#id_cultural_institution").val();
		var showing = $("#id_showing").val();
		
		$.ajax({ 
		    type: "PUT",
			url: bookSelectedSeatsURL,
			dataType : "json",
			data: JSON.stringify({"date": date.trim(), 
									"time": time.trim(), 
									"culturalInstitution": culturalInstitution.trim(), 
									"showing": showing.trim(),
									"auditorium": auditorium.trim(),
									"selectedSeats": selectedSeats.trim()}),
		    contentType: "application/json",
		    cache: false,
		    success: function(success) {
		    	if(success) {
		    		toastr.success("You have successfully booked a seat!");
		    		switchSelectedToReserved(selectedSeats.trim());
		    	}
		    	else {
		    		toastr.info('There was a conflict. Please show the seats again.!!!');
		    	}
					
		   },
			error : function(XMLHttpRequest, textStatus, errorThrown) { 
						toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			}
		});
	}
	else {
		toastr.error("No seat was selected!"); 
	}
}

function switchSelectedToReserved(selectedSeats) {
	var tokens = selectedSeats.split(",");
	
	$("#id_invite_friends").empty();
	
	var people = getFriends();
	if(people) {
		if(people.length == 0) {
			toastr.info('You currently do not have any friends.');
		}
		else {
			if(tokens.length == 1) {
				people = [];
			}
		}
	}
	else {
		people = [];
		toastr.info('You currently do not have any friends.');
	}
	
	var loggedUser = loadLoggedUser();
	people.push("me - " + loggedUser.firstName + " " + loggedUser.lastName);
	
	 $.each(tokens, function(index, seat) {
		 if ($("#id_seat_" + seat).hasClass(settings.selectingSeatCss)) {
			 $("#id_seat_" + seat).removeClass(settings.selectingSeatCss);
		 }
		 
		 $("#id_seat_" + seat).toggleClass(settings.selectedSeatCss);
		 
		 $("#id_invite_friends").append('<tr> <td><label for="'+ seat +'">Seat_' + seat + ': </label></td>  <td><input class="autocomplete" type="text" id="'+ seat +'" value="'+people[people.length - 1]+'"/></td> </tr>');
		 
	 });
	 
	 $( "#dialog" ).dialog({
		  dialogClass: "no-close",
		  width: 500,
		  height: 500,
		  modal: true,
		  buttons: [
		    {
		      text: "Save and send invitations",
		      click: function() {
		    	  		var retValue = sendSeatsAndFriendsAndMe(loggedUser);
		    	  		if(retValue == 0) {
		    	  			$( this ).dialog( "close" );
		    	  		}
		    	  		
		      }
		    },
			
			{
		    	text: "Close",
		    	click: function() {
		    		sendSeatsForRelease();
		    		
		    		$( this ).dialog( "close" );
		    	}
		    }
		  ]
	});
	 
	 $( function() {
		    $('.autocomplete').autocomplete({
		      source: people,
		      minLength: 0,
		      appendTo: '#dialog'
		      /*open: function () {
		          autoComplete.css('z-index', $("#dialog").css('z-index')+1);
		      }*/
		    }).focus(function(){            
	            // The following works only once.
	            // $(this).trigger('keydown.autocomplete');
	            // As suggested by digitalPBK, works multiple times
	            // $(this).data("autocomplete").search($(this).val());
	            // As noted by Jonny in his answer, with newer versions use uiAutocomplete
	            $(this).data("uiAutocomplete").search($(this).val());
	        });
	});
	
}

$( window ).unload(function() {
	var isOpen = $("#dialog").dialog( "isOpen" );
	if(isOpen === true) {
		sendSeatsForRelease();
	}
});

$(window).on("beforeunload", function(e) { 
	var isOpen = $("#dialog").dialog( "isOpen" );
	if(isOpen === true) {
		var dialogText = "The seats you have reserved will be released. Do you really want to close?";
		e.returnValue = dialogText;
		return dialogText;
	}
	
    
});


function sendSeatsForRelease() {
	var seats = new Array();
	$('.autocomplete').each(function() {
		seats.push($(this).attr('id'));	
	});
	
	var obj = {};
	
	obj["culturalInstitution"] = $("#id_cultural_institution").val();
	obj["showing"] = $("#id_showing").val();
	obj["date"] = $("#id_date").val();
	obj["auditorium"] = $("#id_auditorium").val();
	obj["time"] = $("#id_time").val();
	obj["seats"] = seats;
	
	$.ajax({ 
	    type: "PUT",
		url:  sendSeatsForReleaseURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    cache: false,
	    success: function(success) {
	    	if(success) {
	    		$.each(seats, function(index, seat) {
    				if ($("#id_seat_" + seat).hasClass(settings.selectedSeatCss)) {
	    				 $("#id_seat_" + seat).removeClass(settings.selectedSeatCss);
	    			 }
    			});
	    		
	    		toastr.success("Successful release of the seats!");
	    	}
	    	else {
	    		toastr.error("Unsuccessful release of the seats!");
	    	}	
	    
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
}


function sendSeatsAndFriendsAndMe(loggedUser) {
	var obj = {};
	var seatsAndFriends = {};
	
	var success = -1;
	
	$('.autocomplete').each(function() {
		var username = $(this).val().split("-")[0].trim(); // usernameOfFriend
		if (username == "me") {
			username = loggedUser.username;
		}
		seatsAndFriends[$(this).attr('id')] = username;	
	});
	
	obj["culturalInstitution"] = $("#id_cultural_institution").val();
	obj["showing"] = $("#id_showing").val();
	obj["date"] = $("#id_date").val();
	obj["auditorium"] = $("#id_auditorium").val();
	obj["time"] = $("#id_time").val();
	obj["seatsAndFriends"] = seatsAndFriends;
	
	
	$.ajax({ 
		async: false,
	    type: "POST",
		url:  sendSeatsAndFriendsURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    cache: false,
	    success: function(retValue) {
	    		success = retValue;
	    	
	    		switch(retValue) {
		    	case -1:
		    		toastr.error("There was a problem. No logged user!");
		    		break;
		    	case 0:
		    		toastr.success("You have successfully booked tickets!");
		    		break;
		    	case 1:
		    		toastr.error("You did not assign any seat to yourself!");
		    		break;
		    	case 2:
		    		toastr.error("There are repetitions!");
		    		break;
		    	default:
		    		toastr.error("There was a problem.!");
		    		break;
	    	}
	    		
	    
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					success = -1;
		}
	});
	
	return success;
}

function init(reservedSeat) {
    var str = [], seatNo, className;
    
	for (i = 0; i < settings.rows; i++) {
		for (j = 0; j < settings.cols; j++) {
            seatNo = (i*settings.cols + j + 1); // i + j * settings.rows + 1
            className = settings.seatCss + ' ' + settings.rowCssPrefix + i.toString() + ' ' + settings.colCssPrefix + j.toString();
            if ($.isArray(reservedSeat) && $.inArray(seatNo, reservedSeat) != -1) {
                className += ' ' + settings.selectedSeatCss;
            }
            str.push('<li id="id_seat_' + seatNo + '" class="' + className + '"' +
                      'style="top:' + (i * settings.seatHeight).toString() + 'px;left:' + (j * settings.seatWidth).toString() + 'px">' +
                      '<a title="' + seatNo + '">' + seatNo + '</a>' +
                      '</li>');
        }
    }
    $('#place').html(str.join(''));
}


function invitationsAndTickets() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		
		deleteAllExceptFirst();
		$("#center").append('<div id="id_invitations_and_tickets_page"></div>');
		
		$("#id_invitations_and_tickets_page").load("html/partials/invitations_and_tickets.html", workWithInvitationsAndTickets);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function workWithInvitationsAndTickets() {
	$("#id_invitations_and_tickets_page").prepend("<br/><br/>");
	
	var invitations = getInvitations();
	var tickets = getTickets();
	
	//popunjavanje tabele poziva
	if(invitations) {
		for (var i in invitations) {
			  $( "#id_table_invitations" ).append('<tr id="id_invitation_row_' + indexOfInvitations + '"> <td></td> <td> ' + invitations[i].showing + ' </td> <td>' + invitations[i].culturalInstitution + ' </td> <td> ' + invitations[i].date + '</td> <td>'+ invitations[i].time +'</td> <td> '+ invitations[i].auditorium +' </td> <td> '+invitations[i].seat+' </td> <td>'+invitations[i].price+'</td> <td>'+invitations[i].reservedBy+'</td> <td> '+ invitations[i].reservedDateAndTime+' </td> <td><input type="button" id="id_accept_invitation_' + indexOfInvitations + '" class="buttons" value="Accept invitation"/></td> <td><input type="button" id="id_decline_invitation_' + indexOfInvitations + '" class="buttons_remove" value="Decline invitation"/></td> </tr>');
			  $("#id_decline_invitation_" + indexOfInvitations).on("click", {index: indexOfInvitations, invitation: invitations[i]}, declineInvitation);
			  
			  $("#id_accept_invitation_" + indexOfInvitations).on("click", {index: indexOfInvitations, invitation: invitations[i]}, acceptInvitation);
			  
			  indexOfInvitations++;  
		}
		
		$("#id_invitations_header_showing").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 1, "id_table_invitations");
		});
		
		$("#id_invitations_header_cultural_institution").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 2, "id_table_invitations");
		});
		
		$("#id_invitations_header_date").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 3, "id_table_invitations");
		});
		
		$("#id_invitations_header_time").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 4, "id_table_invitations");
		});
		
		$("#id_invitations_header_auditorium").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 5, "id_table_invitations");
		});
		
		$("#id_invitations_header_seat").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 6, "id_table_invitations");
		});
		
		$("#id_invitations_header_price").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 7, "id_table_invitations");
		});
		
		$("#id_invitations_header_reserved_by").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 8, "id_table_invitations");
		});
		
		$("#id_invitations_reserved_reserved_date_and_time").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 9, "id_table_invitations");
		});
	}
	
	//popunjavanje tabele karata
	if(tickets) {
		for (var i in tickets) {
			  $( "#id_table_tickets" ).append('<tr id="id_ticket_row_' + indexOfTickets + '"> <td></td> <td> ' + tickets[i].showing + ' </td> <td>' + tickets[i].culturalInstitution + ' </td> <td> ' + tickets[i].date + '</td> <td>'+ tickets[i].time +'</td> <td> '+ tickets[i].auditorium +' </td> <td> '+tickets[i].seat+' </td> <td>'+tickets[i].price+'</td> <td>'+tickets[i].reservedBy+' </td> <td> '+tickets[i].reservedDateAndTime+' </td>  <td><input type="button" id="id_remove_ticket_' + indexOfTickets + '" class="buttons_remove" value="Remove ticket"/></td> </tr>');
			  $("#id_remove_ticket_" + indexOfTickets).on("click", {index: indexOfTickets, removeTicket: tickets[i]}, removeTicket);
			  
			  indexOfTickets++;  
		}
		
		$("#id_tickets_header_showing").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 1, "id_table_tickets");
		});
		
		$("#id_tickets_header_cultural_institution").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 2, "id_table_tickets");
		});
		
		$("#id_tickets_header_date").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 3, "id_table_tickets");
		});
		
		$("#id_tickets_header_time").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 4, "id_table_tickets");
		});
		
		$("#id_tickets_header_auditorium").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 5, "id_table_tickets");
		});
		
		$("#id_tickets_header_seat").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 6, "id_table_tickets");
		});
		
		$("#id_tickets_header_price").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 7, "id_table_tickets");
		});
		
		$("#id_tickets_header_reserved_by").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 8, "id_table_tickets");
		});
		
		$("#id_tickets_header_reserved_reserved_date_and_time").click(function(event) {
			event.preventDefault();
			
			sortTable(2, 9, "id_table_tickets");
		});
	}
	
	loadInvitationsAndTicketsPageComplete();
}


function declineInvitation(event) {
	event.preventDefault();
	var retValue = declineInvitationTicket(event.data.invitation);
	if(retValue) {
		$( "#id_invitation_row_" + event.data.index ).remove();
		//indexOfInvitations--;
	}
}

function acceptInvitation(event) {
	event.preventDefault();
	
	var retValue = acceptInvitationTicket(event.data.invitation);
	if(retValue) {
		$( "#id_invitation_row_" + event.data.index ).remove();
		//indexOfRequests--;
		
		$( "#id_tickets_tr_header_tickets" ).after('<tr id="id_ticket_row_' + indexOfTickets + '"> <td></td>  <td> ' + event.data.invitation.showing + ' </td> <td>' + event.data.invitation.culturalInstitution + ' </td> <td> ' + event.data.invitation.date + '</td> <td>'+ event.data.invitation.time +'</td> <td> '+ event.data.invitation.auditorium +' </td> <td> '+event.data.invitation.seat+' </td> <td>'+event.data.invitation.price+'</td> <td>'+event.data.invitation.reservedBy+' </td> <td> '+event.data.invitation.reservedDateAndTime+' </td>  <td><input type="button" id="id_remove_ticket_' + indexOfTickets + '" class="buttons_remove" value="Remove ticket"/></td> </tr>');
		$("#id_remove_ticket_" + indexOfTickets).on("click", {index: indexOfTickets, removeTicket: event.data.invitation}, removeTicket);
		
		indexOfFriends++;
	}
}

function removeTicket(event) {
	event.preventDefault();
	
	var retValue = sendRemoveTicket(event.data.removeTicket);
	if(retValue) {
		$( "#id_ticket_row_" + event.data.index ).remove();
	}
	
	
}

function sendRemoveTicket(ticketForRemove) {
	var retValue = false;
	
	$.ajax({
		async: false,
		type : "DELETE",
		url : removeTicketURL,
		data: JSON.stringify(ticketForRemove),
		dataType : "json",
	    contentType: "application/json",
	    cache: false, 
		success : function(successRemove) { 
			if(successRemove) {
				toastr.success("You've been successful remove ticket!"); 
			}
			else {
				toastr.error("You've been unsuccessful remove ticket!"); 
			}
			retValue = successRemove;
		
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
	return retValue;
}


function acceptInvitationTicket(newInvitationTicket) {
	var retValue = false;
		
	$.ajax({ 
		async: false,
	    type: "PUT",
		url:  acceptInvitationURL,
		data: JSON.stringify(newInvitationTicket),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(successAcceptInvitationTicket) {
	    	if(successAcceptInvitationTicket) {
	    		toastr.success("You accepted an invitation for showing " + newInvitationTicket.showing + "!"); 
	    	}
	    	else {
	    		toastr.error("Unsuccessful acceptance of the invitation showing " + newInvitationTicket.showing + "!"); 
	    	}
	    	retValue = successAcceptInvitationTicket;
				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
	return retValue;
}

function declineInvitationTicket(newInvitationTicket) {
	var retValue = false;
	
	$.ajax({ 
		async: false,
	    type: "PUT",
		url:  declineInvitationURL,
		data: JSON.stringify(newInvitationTicket),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(successDeclineInvitationTicket) {
	    	if(successDeclineInvitationTicket) {
	    		toastr.success("You declined an invitation for friendship with " + newInvitationTicket.showing + "!"); 
	    	}
	    	else {
	    		toastr.error("Unsuccessful decline of the invitation showing " + newInvitationTicket.showing + "!"); 
	    	}
	    	retValue = successDeclineInvitationTicket;
				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
	return retValue;
}


function getInvitations() {
	var invitations = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : invitationsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveInvitations) {
					invitations = receiveInvitations;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return invitations;
}

function getTickets() {
	var tickets = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : ticketsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveTickets) {
					tickets = receiveTickets;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return tickets;
}

function getVisitedAndUnvisitedCulturalInstitutions() {
	var visitedAndUnvisitedCulturalInstitutions = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getVisitedAndUnvisitedCulturalInstitutionsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receiveVisitedAndUnvisitedCulturalInstitutions) {
			if(receiveVisitedAndUnvisitedCulturalInstitutions["has"]) {
				visitedAndUnvisitedCulturalInstitutions = receiveVisitedAndUnvisitedCulturalInstitutions;
			}
			else {
				visitedAndUnvisitedCulturalInstitutions = null;
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return visitedAndUnvisitedCulturalInstitutions;
}

function showVisitedAndUnvisitedCulturalInstitutions() {
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		
		deleteAllExceptFirst();
		$("#center").append('<div id="id_visited_and_unvisited_cultural_institutions"></div>');
		
		$("#id_visited_and_unvisited_cultural_institutions").load("html/partials/visited_and_unvisited_cultural_institutions.html", workWithVisitedAndUnvisitedCulturalInstitutions);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function workWithVisitedAndUnvisitedCulturalInstitutions() {
	$("#id_visited_and_unvisited_cultural_institutions").prepend("<br/><br/>");
	
	var visitedAndUnvisitedCulturalInstitutions = getVisitedAndUnvisitedCulturalInstitutions();
	if(visitedAndUnvisitedCulturalInstitutions) {
		var visitedCulturalInstitutions = visitedAndUnvisitedCulturalInstitutions["visited"];
		var unvisitedCulturalInstitutions = visitedAndUnvisitedCulturalInstitutions["unvisited"];
		
		//popunjavanje tabele visited
		if(visitedCulturalInstitutions) {
			for (var i in visitedCulturalInstitutions) {
				  $( "#id_table_visited" ).append('<tr> <td></td> <td>  <a href="/myapp/#/users/culturual_institution" id="id_visited_culturual_institution"> ' + visitedCulturalInstitutions[i].name + '</a> </td> <td>' + visitedCulturalInstitutions[i].address + ' </td> <td> ' + visitedCulturalInstitutions[i].description + '</td> <td>'+ visitedCulturalInstitutions[i].type +'</td> </tr>');
				  $("#id_visited_culturual_institution").on("click", {name: visitedCulturalInstitutions[i].name, address: visitedCulturalInstitutions[i].address, description: visitedCulturalInstitutions[i].description, type: visitedCulturalInstitutions[i].type}, showCulturalInstitution);
			}
			
			$("#id_header_visited_name").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 1, "id_table_visited");
			});
			
			$("#id_header_visited_address").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 2, "id_table_visited");
			});
			
			$("#id_header_visited_description").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 3, "id_table_visited");
			});
			
			$("#id_header_visited_type").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 4, "id_table_visited");
			});
			
		}
		
		//popunjavanje tabele unvisited
		if(unvisitedCulturalInstitutions) {
			for (var i in unvisitedCulturalInstitutions) {
				  $( "#id_table_unvisited" ).append('<tr> <td></td> <td> <a href="/myapp/#/users/culturual_institution" id="id_unvisited_culturual_institution_'+i+'"> ' + unvisitedCulturalInstitutions[i].name + '</a>  </td> <td>' + unvisitedCulturalInstitutions[i].address + ' </td> <td> ' + unvisitedCulturalInstitutions[i].description + '</td> <td>'+ unvisitedCulturalInstitutions[i].type +'</td> </tr>');
				  $("#id_unvisited_culturual_institution_"+i).on("click", {name: unvisitedCulturalInstitutions[i].name, address: unvisitedCulturalInstitutions[i].address, description: unvisitedCulturalInstitutions[i].description, type: unvisitedCulturalInstitutions[i].type}, showCulturalInstitution);
			}
			
			$("#id_header_unvisited_name").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 1, "id_table_unvisited");
			});
			
			$("#id_header_unvisited_address").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 2, "id_table_unvisited");
			});
			
			$("#id_header_unvisited_description").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 3, "id_table_unvisited");
			});
			
			$("#id_header_unvisited_type").click(function(event) {
				event.preventDefault();
				
				sortTable(2, 4, "id_table_unvisited");
			});
			
		}
	}
	
}

function loadAndSetImage(imageName, id) {
	$.ajax({
		type : "GET",
		url : getImageURL + "/" + imageName,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(retValue) {
			if(retValue["hasImage"]) {
				var imageModel = retValue["imageModel"];
				if(imageModel) {
					if(imageModel.pic != "") {
						$("#"+id).attr('src', `data:image/png;base64,${imageModel.pic}`); // pic je niz byte-ova koji pretstavlja sliku
						return;
					}
					
				}
				
			}
			
			$("#"+id).attr('src', 'images/no_image_available.jpg');					
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus);
		}
	});
	
}

function getShowingForShow(name, culturalInstitutionName) {
	var retShowing = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getShowingForShowURL + "/" + name + "/" + culturalInstitutionName,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(retValue) {
			if(retValue["hasShowing"]) {
				retShowing = retValue["showing"];
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus);
		}
	});
	
	return retShowing;
}

function showCulturalInstitution(event) {
	event.preventDefault();
	
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		if(event.data.name) {
			deleteAllExceptFirst();
			$("#center").append('<div> <table id="id_main_table"> </table></div>');
			
			var showings = getShowingsOfCulturalInstitution(event.data.name);
			
			$("#id_main_table").append('<tr> <td> <div class="div_image_or_google_map cultural_institution_div"><img id="id_img"alt="No image" src="#" class="medium_img"/></div> </td> <td> <div class="div_image_or_google_map cultural_institution_div" id="google_map"></div>  </td>  </tr>');
			$("#id_main_table").append('<tr><td colspan="2"><div class="cultural_institution_div"> <table id="id_cultural_institution_table"></table></div>');
			$("#id_main_table").append('<tr><td colspan="2"><div class="cultural_institution_div"> <table id="id_showings_table"></table></div>');
			
			$("#id_cultural_institution_table").append('<tr> <td><b>Name:</b></td> <td class="blue"> <b>'+event.data.name+'</b> </td>  </tr>');
			$("#id_cultural_institution_table").append('<tr> <td><b>Address:</b></td> <td class="blue"> <b>'+event.data.address+'</b> </td>  </tr>');
			$("#id_cultural_institution_table").append('<tr> <td><b>Description:</b></td> <td class="blue"> <b>'+event.data.description+'</b> </td>  </tr>');
			$("#id_cultural_institution_table").append('<tr> <td><b>Type:</b></td> <td class="blue"> <b>'+event.data.type+'</b> </td>  </tr>');
			
			loadAndSetImage("cultural_institution_" + event.data.name, "id_img");
			
			showGoogleMap(event.data.address);
				
			$("#id_showings_table").append('<tr> <th colspan="2"> Showings </th> </tr>');
			
			if(showings) {
				if(showings.length > 0) {
					$.each(showings, function(index, item) {
						$("#id_showings_table").append('<tr> <td><img class="small_img" id="id_showing_img_'+index+'" alt="No image" src="#"/></td> <td> <a id="id_showing_'+index+'" href="/myapp/#/users/showing" > <h3>' + item + ' </h3> </a> </td> </tr>');
						$("#id_showing_img_" + index).on("click", {name: item, culturalInstitutionName: event.data.name}, showShowing);
						$("#id_showing_" + index).on("click", {name: item, culturalInstitutionName: event.data.name}, showShowing);
						
						loadAndSetImage(event.data.name + "_" + item, "id_showing_img_" + index);
					});
				}
				else {
					toastr.error("Not found showings for this cultural institution!");
				}
			}
			else {
				toastr.error("Not found showings for this cultural institution!");
			}
			
			
			loadCulturalInstitutionPageComplete();
		}
		else {
			toastr.error("Not found this cultural institution!");
		}
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}

function showShowing(event) {
	event.preventDefault();
	
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		var showing = getShowingForShow(event.data.name, event.data.culturalInstitutionName);
		if(showing) {
			deleteAllExceptFirst();
			$("#center").append('<div> <table id="id_main_table"> </table></div>');
			
			$("#id_main_table").append('<tr> <td> <div align="center"><input type="button" id="id_btn_quick_booking" class="buttons" value="Quick booking..."/> </td> </tr>');
			$("#id_btn_quick_booking").on("click", { showingName: event.data.name, culturalInstitutionName: event.data.culturalInstitutionName}, quickBooking);
			
			$("#id_main_table").append('<tr> <td> <div class="div_image_or_google_map cultural_institution_div"><img id="id_showing_img"alt="No image" src="#" class="medium_img"/></div> </td> </tr>');
			$("#id_main_table").append('<tr><td><div class="showing_div"> <table id="id_showing_table"></table></div>');
			
			$("#id_showing_table").append('<tr> <td><b>Name:</b></td> <td class="blue"> <b>'+showing.name+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Genre:</b></td> <td class="blue"> <b>'+showing.genre+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Actors:</b></td> <td class="blue"> <b>'+showing.listOfActors+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Name of director:</b></td> <td class="blue"> <b>'+showing.nameOfDirector+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Duration:</b></td> <td class="blue"> <b>'+showing.duration+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Average rating:</b></td> <td class="blue"> <b>'+showing.averageRating+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Short description:</b></td> <td class="blue"> <b>'+showing.shortDescription+'</b> </td>  </tr>');
			$("#id_showing_table").append('<tr> <td><b>Type:</b></td> <td class="blue"> <b>'+showing.type+'</b> </td>  </tr>');
			
			loadAndSetImage(event.data.culturalInstitutionName + "_" + event.data.name, "id_showing_img");
			
			loadShowingPageComplete();
		}
		else {
			toastr.error("Not found this showing!");
		}
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	
}


function showGoogleMap(addr) {
    $("#google_map").googleMap();
    $("#google_map").addMarker({
    	address: addr, 
    	zoom: 50
    });
}


function quickBooking(event) {
	event.preventDefault();
	
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		
		var showingName = event.data.showingName // markane ovo ce ti trebati
		var culturalInstitutionName = event.data.culturalInstitutionName; // markane ovo ce ti trebati
		
		deleteAllExceptFirst();
		$("#center").append('<div id="id_quick_booking_div"> </div>');

		quick_tickets_page(showingName, culturalInstitutionName);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}
