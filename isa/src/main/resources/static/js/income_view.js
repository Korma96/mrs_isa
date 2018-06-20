var getIncomeURL = "/myapp/administrators/admin_cultural_institution/get_income";

function income_main_page()
{	
	$( function() {
	    $( "#id_date1" ).datepicker();
	  } );

	$( function() {
	    $( "#id_date2" ).datepicker();
	  } );
	
	$("#id_btn_show_income").click(function(event) {
		event.preventDefault();
		var date1 = $("#id_date1").val();
		if(date1 == "")
		{
			toastr.error("Date field can not be empty!");
			return;
		}
		var date2 = $("#id_date2").val();
		if(date2 == "")
		{
			toastr.error("Date field can not be empty!");
			return;
		}

		var date1 = date1.split("/");
		var date1 = date1[2] + "-" + date1[0] + "-" + date1[1];
		var date2 = date2.split("/");
		var date2 = date2[2] + "-" + date2[0] + "-" + date2[1];

		var ci = getCIForAdmin();
		calculate_income(ci, date1, date2);
	});
}

function calculate_income(ci, date1, date2)
{   	
	$.ajax({
		async: false,     
		type : "POST",
		url : getIncomeURL,
		dataType : "json",
		contentType: "application/json",
		data: JSON.stringify({"ci" : ci, "date1" : date1, "date2" : date2}),
		cache: false,
		success : function(data)
		{ 		
			alert("Income: " + data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{ 
			toastr.error("Ajax ERROR: " +  + ", STATUS: " + textStatus); 
			return null;
		}
	});
}
