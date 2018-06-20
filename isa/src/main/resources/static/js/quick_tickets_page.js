var getQuickTicketsURL = "/myapp/administrators/admin_cultural_institution/get_quick_tickets";
var getReservedQuickTicketsURL = "/myapp/administrators/admin_cultural_institution/get_reserved_quick_tickets";
var addNewQuickTicketURL = "/myapp/administrators/admin_cultural_institution/add_new_quick_ticket";

var showing_name = null;
var cultural_institution = null;

function quick_tickets_page(showingName, culturalInstitutionName)
{
	showing_name = showingName;
	cultural_institution = culturalInstitutionName;
	var center = $("#center");

	deleteAllExceptFirst();
	
	center.append('<div id="id_quick_tickets"></div>');
	
	show_quick_tickets();
}

function get_quick_tickets()
{
	var obj = {};
	obj["showing"] = showing_name;
	obj["ci"] = cultural_institution;

	var tickets = null;

	$.ajax({ 
		async: false,
	    type: "POST",
		url:  getQuickTicketsURL,
	    data: JSON.stringify(obj),
	    dataType: "json", 
	    contentType: "application/json",
	    cache: false,
	    success: function(retValue)
	    {
	    	tickets = retValue;	    
	    }
	});	

	return tickets;
}

function show_quick_tickets()
{
	var data = get_quick_tickets();

	var html_string = "";
	html_string += '<table><tr><th>Showing</th><th>Date</th><th>Time</th><th>Auditorium</th><th>Seat</th><th>Price</th><th>Discount</th><th><input type="button" id="id_btn_add_new_quick_ticket" class="buttons" value="Add"/><th/></tr>';
	if(data != null)
	{
		for(x in data)
		{
			html_string += "<tr>";
			html_string += "<td>";
			html_string += data[x].term.date;
			html_string += "</td>";
			html_string += "<td>";
			html_string += data[x].term.time;
			html_string += "</td>";
			html_string += "<td>";
			html_string += data[x].term.auditorium;
			html_string += "</td>";
			html_string += "<td>";
			html_string += data[x].seat;
			html_string += "</td>";
			html_string += "<td>";
			html_string += data[x].term.price;
			html_string += "</td>";
			html_string += "<td>";
			html_string += data[x].discount;
			html_string += "</td>";
			html_string += "<td>";
			html_string += '<button id="';
			var delete_button_id = data[x].id;
			html_string += delete_button_id;
			html_string += '" class="buttons_remove">Delete</button>';
			html_string += "</td>";
			html_string += "</tr>";
		}
	}
	html_string += "</table>";
	$("#id_quick_tickets").html(html_string);
	if(data != null)
	{
		for(x in data)
		{
			var id = data[x].id;
			document.getElementById(id).onclick = delete_quick_ticket;
		}
	}

	$("#id_btn_add_new_quick_ticket").click(function(event) {
		event.preventDefault();
		
		add_quick_ticket();
	});
	
}

function add_quick_ticket()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		 
		var center = $("#center");

		deleteAllExceptFirst();
		
		center.append(
			'<div style="display:flex"><div> \
			<form> \
			<table> \
				<tr> <td> \
						<table class="ui-widget"> \
							<tr> <td><label for="id_date"> Date: </label></td>  <td><input type="text" id="id_date"/></td> </tr> \
							<tr> <td><label for="id_auditorium"> Auditorium: </label></td>  <td><select id="id_auditorium"></select></td> </tr> \
						</table> \
					</td>  \
				</tr> \
			</table> \
		</form></div> \
		<div id="div_for_terms"></div> \
		</div>');
			
		$( function() {
			$( "#id_date" ).datepicker();
			} );
		
		$("#id_date").change(searchTermsModified);
		changeAuditoriumsModified();
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function searchTermsModified()
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
			addTermsToUIModified(receivedTerms);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});
}

function changeAuditoriumsModified()
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
	
	$("#id_auditorium").change(searchTermsModified);
}

function addTermsToUIModified(receivedTerms)
{
	var html_string = '<table><tr><th>Term</th><th>Price</th><th></th></tr>';
	for(var t in receivedTerms)
	{
		var data = receivedTerms[t].split("*");
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[2];
		html_string += "</td>";
		html_string += "<td>";
		html_string += data[3];
		html_string += "</td>";
		html_string += "<td>";
		html_string += '<button id="';
		var choose_button_id = data[0];
		html_string += choose_button_id;
		html_string += '" class="buttons">Choose</button>';
		html_string += "</td>";
		html_string += "</tr>";
	}	
	html_string += "</table>";
	$("#id_quick_tickets").html(html_string);
	
	for(var t in receivedTerms)
	{
		var data = receivedTerms[t].split("*");
		document.getElementById(data[0]).onclick = choose_term;
	}
}

function get_reserved_quick_tickets(term_id)
{
	var quick_tickets = null;

	var obj = {};
	obj["term_id"] = term_id;

	$.ajax({
		type: "POST",
		url : getReservedQuickTicketsURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify(obj),
		cache: false,
		success : function(successData) {
			if(successData)
			{
				quick_tickets = successData;
			}
			else
			{
				toastr.error("Error!");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});

	return quick_tickets;
}

function choose_term()
{
	var term_id = this.id;
	var seats_of_reserved_quick_tickets = get_reserved_quick_tickets(term_id);
	var center = $("#center");

	deleteAllExceptFirst();
	
	center.append(
		'<div style="display:flex"><div> \
		<form> \
		<table> \
			<tr> <td> \
					<table class="ui-widget"> \
						<tr> <td><label for="id_reserved_seats"> Reserved seats: </label></td>  <td><input type="text" id="id_reserved_seats"/></td> </tr> \
						<tr> <td><label for="id_seat"> Seat: </label></td>  <td><input id="id_seat" type="number"/></td> </tr> \
						<tr> <td><label for="id_discount"> Discount: </label></td>  <td><input placeholder="%" id="id_discount" type="text"/></td> </tr> \
						<tr> <td><input type="button" id="id_btn_add_quick_ticket" class="buttons" value="Add quick ticket"/></td></tr> \
					</table> \
				</td>  \
			</tr> \
		</table> \
	</form></div> \
	<div id="div_for_terms"></div> \
	</div>');

	var reserved_seats = "";
	for(x in seats_of_reserved_quick_tickets)
	{
		reserved_seats += seats_of_reserved_quick_tickets[x];
		if(x != (seats_of_reserved_quick_tickets.length -1))
			reserved_seats += ",";
	}

	$("#id_reserved_seats").val(reserved_seats);

	$("#id_btn_add_quick_ticket").click(function(event) {
		event.preventDefault();

		var discount = $("#id_discount").val(); 
		var seat = $("#id_seat").val();

		if(discount == "" || seat == "")
		{
			toastr.error("Field(s) can`t be empty!");
			return;
		}

		for(x in seats_of_reserved_quick_tickets)
		{
			if(seats_of_reserved_quick_tickets[x] == seat.toString())
			{
				toastr.error("Seat is already reserved!");
				return;
			}
		}

		add_quick_ticket_ajax(term_id, seat);
	});

}

function add_quick_ticket_ajax(term_id, seat)
{

	var obj = {};
	obj["term_id"] = term_id;
	obj["seat"] = seat.toString();

	$.ajax({
		type: "POST",
		url : addNewQuickTicketURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify(obj),
		cache: false,
		success : function(success) {
			if(success)
			{
				toastr.success("Quick ticket added successfully!"); 
				quick_booking_main_page();
			}
			else
			{
				toastr.error("Error!");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});
}


function delete_quick_ticket()
{
	var quick_ticket_id = this.id;

	var obj = {};
	obj["quick_ticket_id"] = quick_ticket_id;
	obj["ci"] = cultural_institution;

	$.ajax({
		type: "POST",
		url : deleteQuickTicketURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify(obj),
		cache: false,
		success : function(success) {
			if(success)
			{
				toastr.success("Quick ticket deleted successfully!"); 
				quick_booking_main_page();
			}
			else
			{
				toastr.error("Error!");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
					toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
		}
	
	});
}