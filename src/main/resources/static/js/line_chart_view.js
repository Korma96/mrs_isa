var getDataForLineChartURL = "/myapp/administrators/admin_cultural_institution/get_data_for_line_chart";

function line_chart_main_page()
{	
	$( function() {
	    $( "#id_date" ).datepicker();
	  } );
	
	$("#id_btn_show_chart").click(function(event) {
		event.preventDefault();
		var date = $("#id_date").val();
		if(date == "")
		{
			toastr.error("Date field can not be empty!");
			return;
		}

		var date = date.split("/");
		var date = date[2] + "-" + date[0] + "-" + date[1];
		var timePeriodType = $("#id_line_chart_type").find(":selected").text().trim();

		var ci = getCIForAdmin();
		get_data_for_line_chart(ci, date, timePeriodType);
	});
	
}

function get_data_for_line_chart(ci, date, timePeriodType)
{   	
	$.ajax({
		async: false,     
		type : "POST",
		url : getDataForLineChartURL,
		dataType : "json",
		contentType: "application/json",
        data: JSON.stringify({"ci" : ci, "date" : date, "timePeriodType" : timePeriodType}),
        cache: false,
        success : function(data_for_chart)
        { 						
            show_line_chart(data_for_chart[0], data_for_chart[1])
		},
        error : function(XMLHttpRequest, textStatus, errorThrown) 
        { 
			toastr.error("Ajax ERROR: " +  + ", STATUS: " + textStatus); 
			return null;
		}
	});
}

function show_line_chart(Data, Labels)
{

	$("#line-chart").empty();
	new Chart(document.getElementById("line-chart"), {
	  type: 'line',
	  data: {
	    labels: Labels,
	    datasets: [{ 
	        data: Data,
	        label: "Tickets",
	        borderColor: "#3e95cd",
	        fill: false
	      }
	    ]
	  },
	  options: {
	    title: {
	      display: true,
	      text: 'Number of tickets in chosen period'
	    }
	  }
	});
}