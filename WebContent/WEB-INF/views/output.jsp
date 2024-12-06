<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Output Screen</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/select2/4.0.13/js/select2.js"/>"></script>
  <link rel="stylesheet" href="<c:url value="/webjars/select2/4.0.13/css/select2.css"/>"/>  
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  <script type="text/javascript">
	$(document).on("keydown", function(e){
	  
	  if($('#waiting_modal').hasClass('show')) {
		  e.cancelBubble = true;
		  e.stopImmediatePropagation();
    	  e.preventDefault();
		  return false;
	  }
	  
      var evtobj = window.event? event : e;
      
      switch(e.target.tagName.toLowerCase())
      {
      case "input": case "textarea":
    	 break;
      default:
    	  e.preventDefault();
	      var whichKey = '';
		  var validKeyFound = false;
	    
	      if(evtobj.ctrlKey) {
	    	  whichKey = 'Control';
	      }
	      if(evtobj.altKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Alt';
	    	  } else {
	        	  whichKey = 'Alt';
	    	  }
	      }
	      if(evtobj.shiftKey) {
	    	  if(whichKey) {
	        	  whichKey = whichKey + '_Shift';
	    	  } else {
	        	  whichKey = 'Shift';
	    	  }
	      }
	      
		  if(evtobj.keyCode) {
	    	  if(whichKey) {
	    		  if(!whichKey.includes(evtobj.key)) {
	            	  whichKey = whichKey + '_' + evtobj.key;
	    		  }
	    	  } else {
	        	  whichKey = evtobj.key;
	    	  }
		  }
		  validKeyFound = false;
		  if (whichKey.includes('_')) {
			  whichKey.split("_").forEach(function (this_key) {
				  switch (this_key) {
				  case 'Control': case 'Shift': case 'Alt':
					break;
				  default:
					validKeyFound = true;
					break;
				  }
			  });
		   } else {
			  if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
				  validKeyFound = true;
			  }
		   }
			  
		   if(validKeyFound == true) {
			   console.log('whichKey = ' + whichKey);
			   processUserSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
		   }
	      }
	  });
 	setInterval(() => {
 		processAuctionProcedures('READ-MATCH-AND-POPULATE');		
	}, 1000);
  </script>
</head>
<body>
<form:form name="output_form" autocomplete="off" action="POST">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
            <!--   <h3 class="mb-0">${licence_expiry_message}</h3>  -->
           </div>
          <div class="card-body">
          
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  	<!--  <label class="col-sm-4 col-form-label text-left">${licence_expiry_message} </label> 
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.matchFileName} </label> -->
			    <label class="col-sm-4 col-form-label text-left">IP Address: ${session_selected_ip} </label>
			    <label class="col-sm-4 col-form-label text-left">Port Number: ${session_port} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster} </label>
			    
			    <div class="left">
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)"> AnimateOut (-)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_profile_graphic_btn" id="animateout_profile_graphic_btn" onclick="processUserSelection(this)"> AnimateOut SCOREBUG (=) </button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_profile_stat_graphic_btn" id="animateout_profile_stat_graphic_btn" onclick="processUserSelection(this)"> Animate out Stats (9)</button>
			  	<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="clearall_graphic_btn" id="clearall_graphic_btn" onclick="processUserSelection(this)"> Clear All (Space) </button>

			  </div>
			    
			    <label class="col-sm-4 col-form-label text-left" style="font-size: 20px;">SCOREBUG</label>
			    <div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="playerprofile_graphic_btn" id="playerprofile_graphic_btn" onclick="processUserSelection(this)"> SCOREBUG (F1) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="curr_bid_section" id="curr_bid_section" onclick="processUserSelection(this)"> Populate Current bid (1) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="profile_stats_graphic_btn" id="profile_stats_graphic_btn" onclick="processUserSelection(this)"> Profile stats (F9) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> LT </label>
			  	<div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="namesuper_graphic_btn" id="namesuper_graphic_btn" onclick="processUserSelection(this)"> Name Super (F7) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="rtm_available_graphic_btn" id="rtm_available_graphic_btn" onclick="processUserSelection(this)"> RTM Available (F8) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="rtm_enabled_graphic_btn" id="rtm_enabled_graphic_btn" onclick="processUserSelection(this)"> RTM Enabled (8) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="googly_power_graphic_btn" id="googly_power_graphic_btn" onclick="processUserSelection(this)"> Googly Power (E) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> LOF </label>
			  	<div class="left">
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_top_sold_graphic_btn" id="lof_top_sold_graphic_btn" onclick="processUserSelection(this)"> Lof Top Sold (F6) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_remaining_purse_graphic_btn" id="lof_remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Lof Remaining Purse (F10) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="lof_top_sold_team_graphic_btn" id="lof_top_sold_team_graphic_btn" onclick="processUserSelection(this)"> Lof Team Top Sold (F11) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_Player_graphic_btn" id="squad_Player_graphic_btn" onclick="processUserSelection(this)"> Lof Team Player Category (F12) </button>
			  	</div>
			  	
			  	<label class="col-sm-4 col-form-label text-left" style="font-size: 20px;"> FULLFRAMES </label>
			  	<div class="left">
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_graphic_btn" id="remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse All (F2) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_graphic_btn" id="squad_graphic_btn" onclick="processUserSelection(this)"> Squad (F3) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ident_graphic_btn" id="ident_graphic_btn" onclick="processUserSelection(this)"> Ident (F4) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_graphic_btn" id="top_sold_graphic_btn" onclick="processUserSelection(this)"> Top Sold Auction (F5) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_team_graphic_btn" id="top_sold_team_graphic_btn" onclick="processUserSelection(this)"> Top Sold Team (Q) </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="ff_playerprofile_graphic_btn" id="ff_playerprofile_graphic_btn" onclick="processUserSelection(this)"> ProfilePlayer FF (W) </button>
			  	</div>
			  	
			  	
			   
			    
				<!--<c:if test="${(session_selected_second_broadcaster == 'ISPL')}">
  					<div class="left">
				
			    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="playerprofile_graphic_btn" id="playerprofile_graphic_btn" onclick="processUserSelection(this)"> PlayerProfile </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_graphic_btn" id="remaining_purse_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse All </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="remaining_purse_single_graphic_btn" id="remaining_purse_single_graphic_btn" onclick="processUserSelection(this)"> Remaining Purse Single </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="top_sold_graphic_btn" id="top_sold_graphic_btn" onclick="processUserSelection(this)"> Top Sold </button>
			  	<button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="squad_graphic_btn" id="squad_graphic_btn" onclick="processUserSelection(this)"> Squad </button>	
			  	
			  	</div>	
  				 </c:if> -->
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
<input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.which_key_press}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" name="selected_which_layer" id="selected_which_layer" value="${selected_layer}"></input>
</form:form>
</body>
</html>