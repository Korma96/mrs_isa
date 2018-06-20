var getQuickTicketsURL = "/myapp/administrators/admin_cultural_institution/get_quick_tickets";


var showing_name = null;
var cultural_institution = null;

function quick_tickets_page(showingName, culturalInstitutionName)
{
	showing_name = showingName;
	cultural_institution = culturalInstitution;
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
	    },
		error : function(XMLHttpRequest, textStatus, errorThrown) { 
			toastr.error("Ajax ERROR: " + errorThrown + ", STATUS: " + textStatus); 
			success = -1;
		}
	});	

	return tickets;
}

function show_quick_tickets(data)
{
	var quick_tickets = get_quick_tickets();

	currentShowings = data;
	var html_string = "";
	html_string += '<table><tr><th>Showing</th><th>Date</th><th>Time</th><th>Auditorium</th><th>Seat</th><th>Price</th><th>Discount</th><th><input type="button" id="id_btn_add_new_quick_ticket" class="buttons" value="Add"/><th/></tr>';
	var counter = 0;
	for(x in data)
	{
		html_string += "<tr>";
		html_string += "<td>";
		html_string += data[x].term.showing;
		html_string += "</td>";
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
		var delete_button_id = 'id_btn_delete_quick_ticket' + data[x].id;
		html_string += delete_button_id;
		html_string += '" class="buttons_remove">Delete</button>';
		html_string += "</td>";
		html_string += "</tr>";
		counter += 1;
	}
	html_string += "</table>";
	$("#id_quick_tickets").html(html_string);
	var newCounter = 0;
	while(newCounter < counter)
	{
		var id2 = "id_btn_delete_quick_ticket" + newCounter.toString();
		document.getElementById(id2).onclick = delete_quick_ticket;
		newCounter++;
	}

	$("#id_btn_add_new_quick_ticket").click(function(event) {
		event.preventDefault();
		
		add_quick_ticket();
	});
	
}

function delete_quick_ticket()
{
	var quick_ticket_id = this.id;

	var obj = {};
	obj["quick_ticket_id"] = quick_ticket_id;
	obj["ci"] = currentCi;

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

function add_quick_ticket()
{
	var logged = isLogged();
	if (logged) { // ako je  ulogovan
		 
		deleteAllExceptFirst();
		
		'<table><tr><th>Showing</th><th>Date</th><th>Time</th><th>Auditorium</th><th>Seat</th><th>Price</th> \
		<th>Discount</th><th><input type="button" id="id_btn_add_new_quick_ticket" class="buttons" value="Add"/><th/></tr>'
		$("#center").append(
				'<form > \
				<table> \
					<tr><td><label for="id_showing">Showing:</label></td><td class = "select"><select id="id_showing"/></td></tr> \
					<tr>  <td><label for="id_date">Date:</label></td><td class = "select"><select id="id_date" ></td></tr> \
                    <tr><td><label for="id_auditorium">Auditorium:</label></td><td class = "select"><select id="id_auditorium" ></td></tr> \
                    <tr><td><label for="id_time">Time:</label></td><td><select id="id_time" /></td></tr> \
                    <tr><td><label for="id_discount">Discount:</label></td><td><input type="number" min="0" max="100" id="id_discount"/></td>%<td></td></tr> \
				</table> \
				<div align="center"><input type="button" id="id_btn_save_new_quick_ticket" class="buttons" value="Save quick ticket"/> \
				</div><div id="seats"></div> \
				<br/> \
			</form>');

		$( function() {
			$( "#id_date" ).datepicker();
			} );

		$( function() {
			var showings = get_showings_for_ci(currentCi);
		} );
		// add showings for current ci
		// add auditoriums for current ci, only auditoriums which have term
		// add times for chosen sh, ci, date, aud
		// show seats(like jovo`s reservation, mark already reserved)	
		
		$("#id_btn_save_new_quick_ticket").click(function(event) {
			event.preventDefault();
			
			add_quick_ticket_ajax(ci);
		});
		
	}
	else {
		$("#center").load("html/partials/login.html", null, loadLoginComplete);
	}
}

function add_quick_ticket_ajax()
{
	'<table><tr><th>Showing</th><th>Date</th><th>Time</th><th>Auditorium</th><th>Seat</th><th>Price</th> \
	<th>Discount</th><th><input type="button" id="id_btn_add_new_quick_ticket" class="buttons" value="Add"/><th/></tr>'

	var showing = $("")

	var obj = {};
	obj["ci"] = currentCi;


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