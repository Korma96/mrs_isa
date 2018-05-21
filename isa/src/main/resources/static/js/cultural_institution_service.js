var showCulturalInstitutionsPath = "/myapp/home_page/cultural_institutions";
var cultural_institutions = null;

$(document).on("click", "#cinemas_div", function(event)
{
	var logged = isLogged();
	if(logged)
	{
        //call from cultural administrator page
        // TO DO: dodaj dodavanje u history za back dugme!
        deleteAllExceptFirst();
        $("#center").append('<div><div id="search_bar"></div><div id="cultural_institutions"></div></div>');
        get_data_extended('0');
    }
    else
    {
        // call from home page
        if(window.history.pushState)
        {
            window.history.pushState(null, null, "/myapp/#/home_page/cinemas"); // set URL
        }
        
        showCinemas();
    }
});

$(document).on("click", "#theaters_div", function(event)
{
	var logged = isLogged();
	if(logged)
	{
        //call from cultural administrator page
        // TO DO: dodaj dodavanje u history za back dugme!
        deleteAllExceptFirst();
        $("#center").append('<div><div id="search_bar"></div><div id="cultural_institutions"></div></div>');
        get_data_extended('1');
    }
    else
    {
        // call from home page
        if(window.history.pushState)
        {
            window.history.pushState(null, null, "/myapp/#/home_page/theaters"); // set URL
        }
        
        showTheaters();
    }
});

function showCinemas() {
	$("#title").html('CINEMAS &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/registrate" class="a_registrate"> Registrate </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
	$("#center").load("html/partials/cultural_institutions.html", null, get_data("0"));
}

function showTheaters() {
	$("#title").html('THEATERS &nbsp;&nbsp; <a href="/myapp/#/" class="a_home_page"> Home page </a> &nbsp; <a href="/myapp/#/users/registrate" class="a_registrate"> Registrate </a> &nbsp; <a href="/myapp/#/users/login" class="a_login" > Login </a> ');
	$("#center").load("html/partials/cultural_institutions.html", null, get_data("1"));
}

function get_data(param)
{
	$.ajax({
	    type: "POST",
		url:  showCulturalInstitutionsPath,
		data: JSON.stringify({"type" : param}),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(data)
	    {
			if(data)
			{
				put_data_in_html(data);
			}
			else
			{
				toastr.error("No data found!");
			}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus);
		}
	});
}

function get_data_extended(param)
{
	$.ajax({
	    type: "POST",
		url:  showCulturalInstitutionsPath,
		data: JSON.stringify({"type" : param}),
		dataType : "json",
	    contentType: "application/json",
	    cache: false,
	    success: function(data)
	    {
			if(data)
			{
				put_data_in_html_extended(data);
			}
			else
			{
				toastr.error("No data found!");
			}
	   },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus);
		}
	});
}

function update_institution()
{
	var index = this.id.slice(-1);
	alert(cultural_institutions[parseInt(index)].name);
}

function delete_institution(x)
{
	alert(cultural_institutions[x].name);
}

function put_data_in_html(data)
{
	var html_string = "";
	html_string += "<table><tr><th>Name</th><th>Address</th><th>Description</th></tr>";
	for(x in data)
	{
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[x].name;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].address;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].description;
		html_string += "</td>";
		html_string += "</tr>";
	}
	html_string += "</table>"
	$("#cultural_institutions").html(html_string);
}

function put_data_in_html_extended(data)
{
	var html_string = "";
	html_string += '<table><tr><th>Name</th><th>Address</th><th>Description</th><th><input type="button" id="id_btn_add_new_institution" class="buttons" value="Add"/><th/></tr>';
	var counter = 0;
	cultural_institutions = data;
	for(x in data)
	{
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[x].name;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].address;
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[x].description;
		html_string += "</td>";
		html_string += "<td>";
		html_string += '<button id="';
		var update_button_id = 'id_btn_update_institution' + counter.toString();
		html_string += update_button_id;
		html_string += '" class="buttons">Update</button>';
		html_string += '<button id="id_btn_delete_institution';
		html_string += counter.toString();
		html_string += '" class="buttons">Delete</button>';
		html_string += "</td>";
		html_string += "</tr>";
		counter += 1;
	}
	html_string += "</table>";
	$("#cultural_institutions").html(html_string);
	var newCounter = 0;
	while(newCounter < counter)
	{
		var id = "id_btn_update_institution" + newCounter.toString();
		document.getElementById(id).onclick = update_institution;
		newCounter++;
	}

}