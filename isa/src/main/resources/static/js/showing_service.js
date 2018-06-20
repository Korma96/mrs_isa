var addNewShowingURL = "/myapp/administrators/admin_cultural_institution/add_new_showing";
var updateShowingURL = "/myapp/administrators/admin_cultural_institution/update_showing";
var deleteShowingURL = "/myapp/administrators/admin_cultural_institution/delete_showing";
var uploadShowingImageURL = "/myapp/administrators/admin_cultural_institution/upload_showing_image";
var getShowingsForChosenCIURL = "/myapp/administrators/admin_cultural_institution/get_showings_for_ci"
var getAllCIsURL = "/myapp/administrators/admin_cultural_institution/get_cultural_institutions"


var currentShowings = null;
var lastCi = null;

function get_showings_for_ci(ci)
{
	var showings = null;

	var obj = {};
	obj["ci"] = ci;
	
	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  getShowingsForChosenCIURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(data) {
	    	if(data) {    	        
				showings = data;
	    	}
	    	else {
				toastr.error("There are no showings for chosen cultural institution!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});
	
	return showings;
}

function get_all_cis()
{
	var cis = null;

	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  getAllCIsURL,
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(data) {
	    	if(data) {    	        
				cis = data;
	    	}
	    	else {
				toastr.error("There are no cultural institutions!"); 
	    	}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	});

	return cis;
}

function showingsMainPage()
{
	var ci = getCIForAdmin();
    var showings = get_showings_for_ci(ci);
    if(showings)
    {
        show_all_showings(showings);
    }
    else
    {
        toastr.error("There are no showings!");
    }
}


function show_all_showings(data)
{
	currentShowings = data;
	var html_string = "";
	html_string += '<table><tr><th>Name</th><th>Type</th><th>Genre</th><th>Duration</th><th>Rating</th><th>Actors</th><th>Director</th><th>Description</th><th><input type="button" id="id_btn_add_new_showing" class="buttons" value="Add"/><th/></tr>';
	for(x in data)
	{
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[x].name;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].type;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].genre;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].duration;
        html_string += "</td>";
        html_string += "<td>";
		html_string += data[x].averageRating;
        html_string += "</td>";
        html_string += "<td>";
		html_string += data[x].listOfActors;
        html_string += "</td>";
        html_string += "<td>";
		html_string += data[x].nameOfDirector;
        html_string += "</td>";
        html_string += "<td>";
		html_string += data[x].shortDescription;
        html_string += "</td>";
		html_string += "<td>";
		html_string += '<button id="id_btn_update_showing' + data[x].id + '" class="buttons_update">Update</button>';
		html_string += '<button id="id_btn_delete_showing' + data[x].id + '" class="buttons_remove">Delete</button>';
		html_string += "</td>";
		html_string += "</tr>";
	}
	html_string += "</table>";
	$("#showings").html(html_string);

	for(x in data)
	{
		// button id is same as showing id
		var id = "id_btn_update_showing" + data[x].id;
		document.getElementById(id).onclick = update_showing;
		var id2 = "id_btn_delete_showing" + data[x].id;
		document.getElementById(id2).onclick = delete_showing;
	}

	$("#id_btn_add_new_showing").click(function(event) {
		event.preventDefault();
		
		add_showing();
	});
	
}

function add_showing()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan

		deleteAllExceptFirst();
		
		$("#center").append(
				'<div class="image_preview"><img alt="Niste odabrali sliku" src="#" class="previewing" /></div> \
				<form > \
				<table> \
					<tr><td><label for="id_name">Name:</label></td><td><input type="text" id="id_name"/></td></tr> \
					<tr>  <td><label for="id_type">Type:</label></td>  <td class = "select"> \
					<select id="id_type"> \
					<option value="MOVIE">MOVIE</option>\
					<option value="THEATRICAL_PLAY">THEATRICAL_PLAY</option>\
					</select>\
					</td> \
					</tr> \
                    <tr><td><label for="id_genre">Genre:</label></td><td><input type="text" id="id_genre"/></td></tr> \
                    <tr><td><label for="id_duration">Duration:</label></td><td><input type="number" id="id_duration"/></td></tr> \
                    <tr><td><label for="id_actors">Actors:</label></td><td><input type="text" id="id_actors"/></td></tr> \
                    <tr><td><label for="id_director">Director:</label></td><td><input type="text" id="id_director"/></td></tr> \
                    <tr><td><label for="id_description">Description:</label></td><td><input type="text" id="id_description"/></td></tr> \
				<tr>  <td><label for="id_showing_image"> Showing image:</label></td>  <td><input type="file" id="id_showing_image" class="id_image" accept=".gif, .jpg, .png" /></td>  </tr> \
				</table> \
				<div align="center"><input type="button" id="id_btn_save_new_showing" class="buttons" value="Save showing"/> \
				</div> \
				<br/> \
			</form>');
		
		$('.previewing').width($('.previewing').parent().width());
		$('.previewing').height('230px');
		drawImage();
		
		$("#id_btn_save_new_showing").click(function(event) {
			event.preventDefault();
			
			addShowingAjax();
		});
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function addShowingAjax()
{
	var obj = {};
	var name = $("#id_name").val();
    var type = $("#id_type").val();
    var genre = $("#id_genre").val();
    var duration = $("#id_duration").val();
    var actors = $("#id_actors").val();
    var director = $("#id_director").val();
    var description = $("#id_description").val();
    
	if(name == "" || type == "" || genre == "" || duration == "" || actors == "" || director == "" || description == "")
	{
		toastr.error("Field(s) can not be empty!"); 
		return;
	}

	var ci = getCIForAdmin();

    obj["name"] = name;
    obj["type"] = type ;
    obj["genre"] = genre;
    obj["duration"] = duration;
    obj["actors"] = actors;
    obj["director"] = director;
	obj["description"] = description;
	obj["ci"] = ci;
	
	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  addNewShowingURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {
                $("#id_name").val("");
                $("#id_genre").val("");
                $("#id_duration").val("");
                $("#id_rating").val("");
                $("#id_actors").val("");
                $("#id_director").val("");
                $("#id_description").val("");	
                
                uploadImage(uploadShowingImageURL);
	    		
				toastr.success("You have successfully added new showing!");
				deleteAllExceptFirst();
        		$("#center").append('<div><div id="search_bar"></div><div id="showings"></div></div>');
				showingsMainPage();
    	        
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

function update_showing()
{
	var logged = isLogged();
	if (logged) 
	{	
		var button_id = this.id;
		var numb = button_id.match(/\d/g);
		numb = numb.join("");
		var s_id = numb;
		var s = null;
		var ci = getCIForAdmin();
		var showings = get_showings_for_ci(ci);
		for(x in showings)
		{
			if(showings[x].id == s_id)
			{
				s = showings[x];
				break;
			}
		}
		if(s == null)
		{
			toastr.error("Showing don`t exists!");
			deleteAllExceptFirst();
			$("#center").append('<div><div id="search_bar"></div><div id="showings"></div></div>');
			showingsMainPage();
		}
		
		deleteAllExceptFirst();
		
		var update_html = 
			'<form > \
			<table> \
				<tr><td><label for="id_name">Name:</label></td><td><input type="text" id="id_name" value="' + s.name + '" /></td></tr> \
				<tr>  <td><label for="id_type">Type:</label></td>  <td class = "select"> \
				<select id="id_type" >';

		if(s.type == "MOVIE")
		{
			update_html += '<option selected value="MOVIE">MOVIE</option>\
			<option value="THEATRICAL_PLAY">THEATRICAL_PLAY</option>\
			</select>\
			</td> \
			</tr>'
		}
		else
		{
			update_html += '<option selected value="THEATRICAL_PLAY">THEATRICAL_PLAY</option> \
			<option value="MOVIE">MOVIE</option>\
			</select>\
			</td> \
			</tr>'
		}
		update_html += '<tr><td><label for="id_genre">Genre:</label></td><td><input type="text" id="id_genre" value="' + s.genre + '" /></td></tr> \
				<tr><td><label for="id_duration">Duration:</label></td><td><input type="number" id="id_duration" value="' + s.duration + '" /></td></tr> \
				<tr><td><label for="id_actors">Actors:</label></td><td><input type="text" id="id_actors" value="' + s.listOfActors + '" /></td></tr> \
				<tr><td><label for="id_director">Director:</label></td><td><input type="text" id="id_director" value="' + s.nameOfDirector + '" /></td></tr> \
				<tr><td><label for="id_description">Description:</label></td><td><input type="text" id="id_description" value="' + s.shortDescription + '" /></td></tr> \
			</table> \
			<div align="center"><input type="button" id="id_btn_update_showing" class="buttons" value="Update showing"/> \
			</div> \
			<br/> \
		</form>';
		$("#center").append(update_html);
		$("#id_btn_update_showing").click(function(event) {
			event.preventDefault();
			
			updateShowingAjax(s.name, s_id);
		});
	}
	else
	{
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function updateShowingAjax(old_name, ID)
{
	var obj = {};
	var name = $("#id_name").val();
    var type = $("#id_type").val();
    var genre = $("#id_genre").val();
    var duration = $("#id_duration").val();
    var actors = $("#id_actors").val();
    var director = $("#id_director").val();
    var description = $("#id_description").val();

	if(name == "" || type == "" || genre == "" || duration == "" || actors == "" || director == "" || description == "")
	{
		toastr.error("Field(s) can not be empty!"); 
		return;
	}
	var ci = getCIForAdmin();
	
	obj["id"] = ID.toString();
	obj["old_name"] = old_name;
    obj["name"] = name;
    obj["type"] = type ;
    obj["genre"] = genre;
    obj["duration"] = duration;
    obj["actors"] = actors;
    obj["director"] = director;
	obj["description"] = description;
	obj["ci"] = ci;

	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  updateShowingURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
	    	if(success) {	    		
	    		toastr.success("You have successfully updated showing!");
				deleteAllExceptFirst();
        		$("#center").append('<div><div id="search_bar"></div><div id="showings"></div></div>');
				showingsMainPage();
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

function delete_showing()
{
	var button_id = this.id;
	var numb = button_id.match(/\d/g);
	numb = numb.join("");
	var s_id = numb;
	var s = null;
	var ci = getCIForAdmin();
	var showings = get_showings_for_ci(ci);
	for(x in showings)
	{
		if(showings[x].id == s_id)
		{
			s = showings[x];
			break;
		}
	}
	if(s == null)
	{
		toastr.error("Showing don`t exists!");
		deleteAllExceptFirst();
		$("#center").append('<div><div id="search_bar"></div><div id="showings"></div></div>');
		showingsMainPage();
	}

	var obj = {};
	obj["id"] = s_id.toString();
	obj["ci"] = ci;
	
	$.ajax({ 
	    type: "POST",
	    async: false,
		url:  deleteShowingURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    success: function(success) {
			if(success) 
			{
				toastr.success("You have successfully deleted showing!");
				deleteAllExceptFirst();
        		$("#center").append('<div><div id="search_bar"></div><div id="showings"></div></div>');
				showingsMainPage();
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
