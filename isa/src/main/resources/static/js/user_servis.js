var loginURL = "/myapp/users/login";
var loggedUserURL = "/myapp/users/logged_user";
var registrateUserURL = "/myapp/users/registrate";
var activateAccountURL = "/myapp/users/activate";
var logOutURL = "/myapp/users/logout";
var saveChangesOnProfileURL = "/myapp/users/save_changes_on_profile";
var culturalInstitutionsURL = "/myapp/users/cultural_institutions";
	
	
$(document).ready(function() {
	$("#center").load("html/partials/home_page.html", null, loadHomePageComplete);
	
	if (window.history && window.history.pushState) {
		var url = window.location.href;     // Returns full URL
		var pathname;
		if (url.indexOf("#") >= 0) {
			pathname = getPathname();
		}
		else {
			pathname = "";
		}
		
		window.history.pushState(null, null, "/myapp/#" + pathname); // set URL
		
		activateAccount(pathname);
		
	    /*$(window).on('popstate', function() {
	    	toastr.info('Back button was pressed.');
	    });*/

	}
	
});

$(window).on('hashchange', function(e){
	// Alerts every time the hash changes!
	
	var pathname = getPathname();
	
	switch(pathname) {
	case "/":
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
		loadLogin()
		break;
	case "users/registrate":
	case "users/registrate/":
	case "/users/registrate/":
	case "/users/registrate":
		$("#center").load("html/partials/register.html", null, loadRegisterComplete);
		break;
	default:
		$("#title").empty();
		$("#center").html(getStringFor404());
		break;
	}
	
	activateAccount(pathname);
	
});

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
	var url = window.location.href;     // Returns full URL
	
	var tokens = url.trim().split("#");
	var pathname;
	if(tokens.length == 2) {
		pathname = tokens[1];
	}
	else {
		pathname = "go to default";
	}
	
	return pathname
}

function getStringFor404() {
	var now = new Date(Date.now());
	var str = "<html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>"+now+"</div><div>There was an unexpected error (type=Not Found, status=404).</div><div>No message available</div></body></html>"
	
		return str;
}

function loadHomePageComplete() {
	$("#title").html('HOME PAGE &nbsp;&nbsp; <a href="/myapp/#/users/registrate" class="a_registrate"> Registrate </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
}

function loadRegisterComplete() {
	$("#title").html('REGISTRATE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
}

function loadLoginComplete() {
	$("#title").html('LOGIN &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/registrate" class="a_registrate" > Registrate </a> ');
}

$(document).on("click", ".a_registrate", function(event) {
	event.preventDefault();
	
	 if(window.history.pushState) {
	    window.history.pushState(null, null, $(this).attr('href')); // set URL
	 }
	//window.history.pushState({urlPath:'/myapp/#/registrate'},"",'/myapp/#/registrate');
	
	$("#center").load("html/partials/register.html", null, loadRegisterComplete);
});

$(document).on("click", ".a_login", function(event) {
	event.preventDefault();
	
	// Detect if pushState is available
	  if(window.history.pushState) {
	    window.history.pushState(null, null, $(this).attr('href')); // set URL
	  }
	//window.history.pushState({urlPath:'/myapp/#/login'},"",'/myapp/#/login');
	
	loadLogin();
});


function loadLogin() {
	var loggedUser = loadLoggedUser();
	if (loggedUser) { // ako je vec ulogovan
		successfullyLogged(loggedUser);
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
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

function show_cinemas()
{
	alert('cinemas');
}

function show_theaters()
{
	alert('theaters');
}

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
						if(loggedUser.username == "-1") {
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
        data: JSON.stringify({"username": username, "password": password}),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(loggedUser) {
					if(loggedUser.username == "-1") {
						toastr.error("Invalid username or password!"); 
					}
					else {
						successfullyLogged(loggedUser);
					}
					
				
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}

function successfullyLogged(loggedUser) {
	$("#center").empty();
	
	$('<link>')
	  .appendTo('head')
	  .attr({
	      type: 'text/css', 
	      rel: 'stylesheet',
	      href: 'css/registered_user_page.css'
	  });
	
	$("#center").prepend('<div id="id_menu"></div><br/><br/>');
	$("#id_menu").load("html/partials/registered_user_page.html", null, function () {
		if (loggedUser.userType == "REGISTERED_USER") {
			registeredUserPage(loggedUser);
		}
		else{
			// TODO administratoroPage();
			
			
		}
	});
	
	/*$("#center").append('You have successfully logged in.');
	$("#center").append('<br/> Username: ' + loggedUser.username);
	$("#center").append('<br/> Password: ' + loggedUser.password);*/
	
	toastr.success('You have successfully logged in!'); 
}

function registeredUserPage(loggedUser) {
	$("#title").html('REGISTERED USER PAGE &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/registrate" class="a_registrate" > Registrate </a> ');
	
	$("#myDropdown").append('<a id="id_update_profile" href="/myapp/#/users/update_profile"> Update profile </a>');
	$("#myDropdown").append('<a id="id_logout" href="/myapp/#/users/logout"> Logout </a>');
	
	$("#id_update_profile").click(function(event) {
		event.preventDefault();
		
		updateProfile(loggedUser);
	});
	
	$("#id_logout").click(function(event) {
		event.preventDefault();
		
		logout();
	});
	
}

function updateProfile(loggedUser) {
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

function logout() {
	$.ajax({
		type : "DELETE",
		url : logOutURL,
		success : function() { 
			toastr.success("You've been successful logout!"); 

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

$(document).on("click", "#id_btn_registrate", function(event) {
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
	