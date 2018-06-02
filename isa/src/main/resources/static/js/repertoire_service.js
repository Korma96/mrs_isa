var getAllCulturalInstitutionsURL = "/myapp/administrators/admin_cultural_institution/get_cultural_institutions";
var getShowingsURL = "/myapp/administrators/admin_cultural_institution/get_showings";
var getAuditoriumsURL = "/myapp/administrators/admin_cultural_institution/get_auditoriums_for_ci";
var getTermsURL = "/myapp/administrators/admin_cultural_institution/get_terms";
var addTermURL = "/myapp/administrators/admin_cultural_institution/add_term";
var deleteTermURL = "/myapp/administrators/admin_cultural_institution/delete_term";

function getShowings() {
	var showings = null;
	
	$.ajax({
		async: false,
		type : "POST",
		url : getShowingsURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(receivedShowings) {
			showings = receivedShowings;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return showings;
}

function getAllCulturalInstitutions() {
	var culturalInstitutions = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getAllCulturalInstitutionsURL,
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

function changeАuditoriums()
{
	$('#id_auditorium').empty();
	$("#div_for_terms").empty();
	var ciName = $('#id_cultural_institution').find(":selected").text();
	ciName = ciName.trim();
	var auditoriums = getAuditoriumsForCulturalInstitution(ciName);
	$("#id_auditorium").append('<option disabled selected value> -- select an option -- </option>');
	for(au in auditoriums)
	{
		$("#id_auditorium").append("<option " + auditoriums[au] + "> " + auditoriums[au] + " </option>");
	}
	
	$("#id_auditorium").change(searchTerms);
}

function getAuditoriumsForCulturalInstitution(ciName)
{
	var auditoriums = null;
	
	$.ajax({
		async: false,
		type : "POST",
		url : getAuditoriumsURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify({"ciName" : ciName}),
		cache: false,
		success : function(receivedAuditoriums) {
			auditoriums = receivedAuditoriums;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return auditoriums;
}

function repertoireMainPageComplete()
{
	var culturalInstitutions = getAllCulturalInstitutions();
	if(culturalInstitutions) {
		if(culturalInstitutions.length > 0) {
			$("#id_cultural_institution").append('<option disabled selected value> -- select an option -- </option>');
			for(ci in culturalInstitutions)
			{
				$("#id_cultural_institution").append("<option " + culturalInstitutions[ci] + "> " + culturalInstitutions[ci] + " </option>");
			}
			
			$("#id_cultural_institution").change(changeАuditoriums);
			
			var showings = getShowings();
			if(showings) {
				if (showings.length > 0) {
					$("#id_showing").append('<option disabled selected value> -- select an option -- </option>');
					for(sh in showings)
					{
						$("#id_showing").append("<option " + showings[sh].name + "> " + showings[sh].name+" - "+showings[sh].duration+"min" + " </option>");
					}
					$("#id_showing").change(searchTerms);
					$("#id_date").change(searchTerms);
				}
				else {
					toastr.error("Showings are not available!");
				}
			}
			else {
				toastr.error("Showings are not available!");
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

function searchTerms()
{
	var ci = $("#id_cultural_institution").find(":selected").text().trim();
	if(ci == "-- select an option --")
	{
		return;
	}
	var showing = $("#id_showing").find(":selected").text().trim();
	if(showing == "-- select an option --")
		return;
	var auditorium = $("#id_auditorium").find(":selected").text().trim();
	if(auditorium == "-- select an option --")
		return;
	var date = $("#id_date").val();
	if(date == "")
		return;
	var date = date.split("/");
	var date = date[2] + "-" + date[0] + "-" + date[1];
	var showing = showing.split(" - ");
	var showing = showing[0];
	
	var obj = {}
	obj["showing"] = showing;
	obj["auditorium"] = auditorium;
	obj["date"] = date;
	
	$.ajax({
		type: "POST",
		url : getTermsURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify(obj),
		cache: false,
		success : function(receivedTerms) {
			addTermsToUI(receivedTerms);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});
}

function addTermsToUI(receivedTerms)
{
	var html_string = '<table><tr><th><input type="time" id="id_time"/></th><th><input type="button" id="id_btn_add_term" class="buttons" value="Add term"/></th></tr>';
	for(var t in receivedTerms)
	{
		var data = receivedTerms[t].split("*");
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[1];
		html_string += "</td>";
		html_string += "<td>";
		html_string += '<button id="';
		var delete_button_id = data[0];
		html_string += delete_button_id;
		html_string += '" class="buttons_remove">Delete</button>';
		html_string += "</td>";
		html_string += "</tr>";
	}	
	html_string += "</table>";
	$("#div_for_terms").html(html_string);
	
	$("#id_btn_add_term").click(function(event) {
		event.preventDefault();
		addTerm();
	});
	
	for(var t in receivedTerms)
	{
		var data = receivedTerms[t].split("*");
		document.getElementById(data[0]).onclick = deleteTerm;
	}
}

function addTerm()
{
	var time = $("#id_time").val();
	if(time == "")
		return;
	var ci = $("#id_cultural_institution").find(":selected").text().trim();
	if(ci == "-- select an option --")
		return;
	var showing = $("#id_showing").find(":selected").text().trim();
	if(showing == "-- select an option --")
		return;
	var auditorium = $("#id_auditorium").find(":selected").text().trim();
	if(auditorium == "-- select an option --")
		return;
	var date = $("#id_date").val();
	if(date == "")
		return;
	var date = date.split("/");
	var date = date[2] + "-" + date[0] + "-" + date[1];
	var showing = showing.split(" - ");
	var showing = showing[0];
	
	var obj = {}
	obj["ci"] = ci;
	obj["showing"] = showing;
	obj["auditorium"] = auditorium;
	obj["date"] = date;
	obj["time"] = time;
	
	$.ajax({
		type: "POST",
		url : addTermURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify(obj),
		cache: false,
		success : function(success) {
			if(success)
			{
				toastr.success("Term added successfully!"); 
				searchTerms();
			}
			else
			{
				toastr.error("Time not set properly!");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});
}

function deleteTerm()
{
	var termId = this.id;
	
	$.ajax({
		async: false,
		type : "POST",
		url : deleteTermURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify({"id" : termId}),
		cache: false,
		success : function(success) {
			if(success)
			{
				toastr.success("Term deleted successfully!"); 
				searchTerms();
			}
			else
			{
				toastr.error("Error while deleting!");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
}
