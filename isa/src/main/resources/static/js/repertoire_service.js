var getAllCulturalInstitutionsURL = "/myapp/administrators/admin_cultural_institution/get_cultural_institutions";
var getShowingsURL = "/myapp/administrators/admin_cultural_institution/get_showings";
var getAuditoriumsForCulInsURL = "/myapp/administrators/admin_cultural_institution/get_auditoriums_for_ci";
var getTermsURL = "/myapp/administrators/admin_cultural_institution/get_terms";
var addTermURL = "/myapp/administrators/admin_cultural_institution/add_term";
var deleteTermURL = "/myapp/administrators/admin_cultural_institution/delete_term";

function getShowings() 
{
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

function getAllCulturalInstitutions() 
{
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

function changeAuditoriums()
{
	$('#id_auditorium').empty();
	$("#div_for_terms").empty();
	var ciName = getCIForAdmin();
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
		url : getAuditoriumsForCulInsURL,
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
	$("#id_date").change(searchTerms);
	changeAuditoriums();
}

function searchTerms()
{
	var auditorium = $("#id_auditorium").find(":selected").text().trim();
	if(auditorium == "-- select an option --")
		return;
	var date = $("#id_date").val();
	if(date == "")
		return;
	var date = date.split("/");
	var date = date[2] + "-" + date[0] + "-" + date[1];
	
	var obj = {}
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
	var html_string = '<table><tr><th><label for="id_showing">Showing</label></th><th>Term</th><th>Price</th><th></th></tr>';
	html_string += '<tr><td><select id="id_showing"></select></td><td><input type="time" id="id_time"/></td><td><input type="number" min="0" max="10000" id="id_price_input" /></td><td><input type="button" id="id_btn_add_term" class="buttons" value="Add term"/></td></tr>';
	for(var t in receivedTerms)
	{
		var data = receivedTerms[t].split("*");
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[1];
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[2];
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[3];
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

	var currentInstitution = getCIForAdmin();
	var showings = get_showings_for_ci(currentInstitution);
	if(showings) {
		if (showings.length > 0) {
			$("#id_showing").append('<option disabled selected value> -- select an option -- </option>');
			for(sh in showings)
			{
				$("#id_showing").append("<option " + showings[sh].name + "> " + showings[sh].name+" - "+showings[sh].duration+"min" + " </option>");
			}
		}
		else {
			toastr.error("Showings are not available!");
		}
	}
	else {
		toastr.error("Showings are not available!");
	}

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
	var showing = $("#id_showing").find(":selected").text().trim();
	if(showing == "-- select an option --")
		return;
	var showing = showing.split(" - ");
	var showing = showing[0];
	var auditorium = $("#id_auditorium").find(":selected").text().trim();
	if(auditorium == "-- select an option --")
		return;
	var date = $("#id_date").val();
	if(date == "")
		return;
	var date = date.split("/");
	var date = date[2] + "-" + date[0] + "-" + date[1];
	var price = $("#id_price_input").val();
	if(price == "")
	{
		return;
	}
	var currentInstitution = getCIForAdmin();
	
	var obj = {}
	obj["ci"] = currentInstitution;
	obj["showing"] = showing;
	obj["auditorium"] = auditorium;
	obj["date"] = date;
	obj["time"] = time;
	obj["price"] = price;
	
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
