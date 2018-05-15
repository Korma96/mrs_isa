var showCulturalInstitutionsPath = "/myapp/home_page/cultural_institutions";

$(document).on("click", "#cinemas_div", function(event)
{
	if(window.history.pushState)
	{
		window.history.pushState(null, null, $(this).attr('href')); // set URL
	}
	$("#center").load("html/partials/cultural_institutions.html", null, get_data("0"));
});

$(document).on("click", "#theaters_div", function(event)
{
	if(window.history.pushState)
	{
		window.history.pushState(null, null, $(this).attr('href')); // set URL
	}
	$("#center").load("html/partials/cultural_institutions.html", null, get_data("1"));
});

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

function put_data_in_html(data)
{
	var html_string = "";
	html_string += "<table><tr><th>Name</th><th>Address</th><th>Description</th></tr>"
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


















