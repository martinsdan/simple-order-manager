showHideDetails = function(){
	var details = $(".status_details");
	if(details[0].offsetParent==null){
		details.show('blind');
		$("#showButton").text("Show Less");
	}
	else{
		details.hide('blind');
		$("#showButton").text("Show More");
	}
};