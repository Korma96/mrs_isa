var getCulturalInstitutionsURL = "/myapp/administrators/get_cultural_institutions";
var getShowingsOfCulturalInstitutionURL = "/myapp/administrators/get_showings_of_cultural_institution";
var addRequisiteURL = "/myapp/administrators/add_requisite";
var getRequisitesURL = "/myapp/administrators/get_requisites"; 


function administratorPage() {
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
	 
	    $( "button" ).button();
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