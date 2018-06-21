var addNewAuditoriumURL = "/myapp/administrators/admin_cultural_institution/add_new_auditorium";
var updateAuditoriumURL = "/myapp/administrators/admin_cultural_institution/update_auditorium";
var deleteAuditoriumURL = "/myapp/administrators/admin_cultural_institution/delete_auditorium";
var getAuditoriumsForCIURL = "/myapp/administrators/admin_cultural_institution/get_auditoriums_for_cultural_institution";
var getCIForAdminURL = "/myapp/administrators/admin_cultural_institution/get_cultural_institution_for_admin";

var currentInstitution = null;

function getCIForAdmin()
{	
	var ci = null;

	$.ajax({
		async: false,
		type : "GET",
		url : getCIForAdminURL,
		dataType : "json",
		contentType: "application/json",
		cache: false,
		success : function(hm) {
			if(hm["has"]) {
				ci = hm["institution"];
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});

	return ci;
}

function getAuditoriumsForCI()
{
	var auditoriums = null;
	
	$.ajax({
		async: false,
		type : "GET",
		url : getAuditoriumsForCIURL,
		dataType : "json",
		cache: false,
		success : function(receivedAuditoriums) {
			if(["has"]) {
				auditoriums = receivedAuditoriums["auditoriums"];
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
					return null;
		}
	});
	
	return auditoriums;
}
/*
function cultural_institution_changed()
{
	$("#div_for_auditoriums").empty();
	var ciName = $('#id_cultural_institution').find(":selected").text();
	ciName = ciName.trim();
	var auditoriums = getAuditoriumsForCI(ciName);
	if(auditoriums)
	{
		show_auditoriums(auditoriums);
	}
	else
	{
		toastr.error('Auditoriums not found!');
	}
}
*/
function goBackToAuditoriumsMainPage()
{
	auditoriums();
	if(currentInstitution == null)
		return;
	auditoriumsMainPage();
}

function auditoriumsMainPage()
{
	var logged = isLogged();
	if (logged) { // ako je ulogovan
		currentInstitution = getCIForAdmin();
		var auditoriums = getAuditoriumsForCI();
		if(auditoriums)
		{ 
			show_auditoriums(auditoriums);
		}
		else
		{
			toastr.error('Auditoriums not found!');
		}
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
	/*
	var culturalInstitutions = getAllCulturalInstitutions();
	if(culturalInstitutions) {
		if(culturalInstitutions.length > 0) {
			$("#id_cultural_institution").append('<option disabled selected value> -- select an option -- </option>');
			for(ci in culturalInstitutions)
			{
				$("#id_cultural_institution").append("<option " + culturalInstitutions[ci] + "> " + culturalInstitutions[ci] + " </option>");
			}
			
			$("#id_cultural_institution").change(cultural_institution_changed);
		}
	}
	else
	{
		toastr.error('Cultural institutions not found!');
	}
	*/
}

function show_auditoriums(data)
{
	var html_string = "";
	html_string += '<table id="id_auditoriums_table"><tr><th>Name</th><th>Rows</th><th>Columns</th><th><input type="button" id="id_btn_add_new_auditorium" class="buttons" value="Add"/><th/></tr>';
	for(x in data)
	{
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[x].name;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].numOfRows;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].numOfCols;
		html_string += "</td>";
		html_string += "<td>";
		html_string += '<button id="id_btn_update_auditorium' + data[x].id + '" class="buttons_update">Update</button>';
		html_string += '<button id="id_btn_delete_auditorium' + data[x].id + '" class="buttons_remove">Delete</button>';
		html_string += "</td>";
		html_string += "</tr>";
	}
	html_string += "</table>";
	$("#div_for_auditoriums").html(html_string);
	for(x in data)
	{
		// button id is same as auditorium id
		var id = "id_btn_update_auditorium" + data[x].id;
		document.getElementById(id).onclick = update_auditorium;
		var id2 = "id_btn_delete_auditorium" + data[x].id;
		document.getElementById(id2).onclick = delete_auditorium;
	}

	$("#id_btn_add_new_auditorium").click(function(event) {
		event.preventDefault();
		
		add_auditorium();
	});
	
}

function add_auditorium()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		
		deleteAllExceptFirst();
		
		var html_string = "";
		html_string += '<form > \
						<table> \
						<tr><td><label for="id_name">Name:</label></td><td><input type="text" id="id_name"/></td></tr> \
						<tr><td><label for="id_numOfRows">Num of rows:</label></td><td><input type="number" id="id_numOfRows" min="1" max="100"/></td></tr> \
						<tr><td><label for="id_numOfCols">Num of cols:</label></td><td><input type="number" id="id_numOfCols" min="1" max="100"/></td></tr> \
						</table> \
						<div align="center"><input type="button" id="id_btn_save_new_auditorium" class="buttons" value="Save auditorium"/> \
						</div> \
						<br/> \
						</form>';

		$("#center").append(html_string);

		$("#id_btn_save_new_auditorium").click(function(event) {
			event.preventDefault();
			
			addAuditoriumAjax();
		});
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function addAuditoriumAjax()
{
	var obj = {};
	var name = $("#id_name").val();
    var numOfRows = $("#id_numOfRows").val();
    var numOfCols = $("#id_numOfCols").val();
    
	if(name == "" || numOfRows == "" || numOfCols == "")
	{
		toastr.error("Field(s) can not be empty!"); 
		return;
	}

    obj["name"] = name;
    obj["numOfRows"] = numOfRows;
    obj["numOfCols"] = numOfCols;
	
	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  addNewAuditoriumURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {
                $("#id_name").val("");
                $("#id_numOfRows").val(1);
                $("#id_numOfCols").val(1);	
	    		
	    		toastr.success("You have successfully added new showing!");
	    		
				goBackToAuditoriumsMainPage();
	    	}
	    	else {
	    		toastr.error("Name already exists!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
}

function update_auditorium()
{
	var logged = isLogged();
	if (logged) 
	{	
		var button_id = this.id;
		var numb = button_id.match(/\d/g);
		numb = numb.join("");
		var a_id = numb;
		var a = null;
		var auditoriums = getAuditoriumsForCI();
		for(x in auditoriums)
		{
			if(auditoriums[x].id == a_id)
			{
				a = auditoriums[x];
			}
		}
		if(a == null)
		{
			toastr.error("Auditorium don`t exists!");
			goBackToAuditoriumsMainPage();
		}

		deleteAllExceptFirst();
		
		var html_string = "";
		html_string += '<form > \
						<table> \
						<tr><td><label for="id_name">Name:</label></td><td><input type="text" value="' + a.name + '" id="id_name"/></td></tr> \
						<tr><td><label for="id_numOfRows">Num of rows:</label></td><td><input type="number" id="id_numOfRows" min="1" max="100" value="' + a.numOfRows + '" /></td></tr> \
						<tr><td><label for="id_numOfCols">Num of cols:</label></td><td><input type="number" id="id_numOfCols" min="1" max="100" value="' + a.numOfCols + '" /></td></tr> \
						</table> \
						<div align="center"><input type="button" id="id_btn_update_auditorium" class="buttons" value="Update auditorium"/> \
						</div> \
						<br/> \
						</form>';

		$("#center").append(html_string);
		$("#id_btn_update_auditorium").click(function(event) {
			event.preventDefault();
			
			updateAuditoriumAjax(a.name, a_id);
		});
	}
	else
	{
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function updateAuditoriumAjax(old_name, ID)
{
	var name = $("#id_name").val();
    var ci = currentInstitution;
    var numOfRows = $("#id_numOfRows").val();
    var numOfCols = $("#id_numOfCols").val();
    
	if(name == "" || numOfRows == "" || numOfCols == "")
	{
		toastr.error("Field(s) can not be empty!"); 
		return;
	}

	var obj = {};
	obj["name"] = name;
	obj["old_name"] = old_name;
    obj["numOfRows"] = numOfRows;
    obj["numOfCols"] = numOfCols;

	$.ajax({ 
	    type: "PUT",
		url:  updateAuditoriumURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {	    		
	    		toastr.success("You have successfully updated auditorium!");
	    		
				goBackToAuditoriumsMainPage();
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

function delete_auditorium()
{
	var button_id = this.id;
	var numb = button_id.match(/\d/g);
	numb = numb.join("");
	var a_id = numb;
	var a = null;
	var auditoriums = getAuditoriumsForCI();
	for(x in auditoriums)
	{
		if(auditoriums[x].id == a_id)
		{
			a = auditoriums[x];
			break;
		}
	}
	if(a == null)
	{
		toastr.error("Auditorium don`t exists!");
		goBackToAuditoriumsMainPage();
	}

	var obj = {};
	obj["id"] = a_id.toString();
	
	$.ajax({ 
	    type: "DELETE",
		url:  deleteAuditoriumURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {
				toastr.success("You have successfully deleted auditorium!");
				
				goBackToAuditoriumsMainPage();
	    	}
	    	else {
	    		toastr.error("Error deleting auditorium!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
}
