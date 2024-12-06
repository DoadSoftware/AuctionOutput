var match_data,session_auction;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		switch(dataToProcess){
		case 32:
			processAuctionProcedures('CLEAR-ALL');
			break;
		case 189:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processAuctionProcedures('ANIMATE-OUT');	
			}
		break;
		case 187:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processAuctionProcedures('ANIMATE-OUT-PROFILE');	
			}
			break;
		case 'RE_READ':
			processAuctionProcedures('RE_READ_DATA');
		}
		break;
	}
}
function initialiseForm(whatToProcess,dataToProcess)
{
	session_auction = dataToProcess;
	switch (whatToProcess) {
	case 'initialise':
		processUserSelection($('#select_broadcaster'));
		break;
	case 'UPDATE-MATCH-ON-OUTPUT-FORM':
		
		break;
	}
}
function processUserSelection(whichInput)
{	
	switch ($(whichInput).attr('name')) {
		
	case 'animateout_profile_stat_graphic_btn':
		processAuctionProcedures('ANIMATE-OUT-PLAYER_STAT');
		break;
	case 'animateout_graphic_btn':
		if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
			processAuctionProcedures('ANIMATE-OUT');	
		}
		break;
	case 'clearall_graphic_btn':
		processAuctionProcedures('CLEAR-ALL');
		break;	
	case 'ident_graphic_btn':
		processAuctionProcedures('POPULATE-IDENT');
		break;
	case 'top_sold_graphic_btn':
		processAuctionProcedures('POPULATE-TOP_SOLD');
		break;
	case 'lof_top_sold_graphic_btn':
		processAuctionProcedures('POPULATE-LOF_TOP_SOLD');
		break;
	case 'remaining_purse_graphic_btn':
		processAuctionProcedures('POPULATE-REMAINING_PURSE_ALL');
		break;
	case 'rtm_available_graphic_btn':
		processAuctionProcedures('POPULATE-RTM_AVAILABLE');
		break;
	case 'rtm_enabled_graphic_btn':
		processAuctionProcedures('POPULATE-RTM_ENABLED');
		break;
	case 'playerprofile_graphic_btn': case 'squad_graphic_btn': case 'remaining_purse_single_graphic_btn': case 'namesuper_graphic_btn': case 'top_sold_team_graphic_btn':
	case 'googly_power_graphic_btn': case 'profile_stats_graphic_btn': case 'lof_remaining_purse_graphic_btn': case 'lof_top_sold_team_graphic_btn':
	case 'squad_Player_graphic_btn': case 'ff_playerprofile_graphic_btn':
	
		$("#captions_div").hide();
		$("#cancel_match_setup_btn").hide();
		$("#expiry_message").hide();
		
		switch ($(whichInput).attr('name')) {
		
		case 'namesuper_graphic_btn':
			processAuctionProcedures('NAMESUPER_GRAPHICS-OPTIONS');
			break;
		case 'playerprofile_graphic_btn':
			processAuctionProcedures('PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'ff_playerprofile_graphic_btn':
			processAuctionProcedures('FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'squad_graphic_btn':
			processAuctionProcedures('SQUAD_GRAPHICS-OPTIONS');
			break;
		case 'top_sold_team_graphic_btn':
			processAuctionProcedures('TOP-SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'remaining_purse_single_graphic_btn':
			processAuctionProcedures('SINGLE_PURSE_GRAPHICS-OPTIONS');
			break;
		case 'googly_power_graphic_btn':
			processAuctionProcedures('GOOGLY_GRAPHICS-OPTIONS');
			break;
		case 'profile_stats_graphic_btn':
			addItemsToList('PROFILE_STATS-OPTIONS',null);
			break;
		case 'lof_remaining_purse_graphic_btn':
			addItemsToList('LOF_REMAINING_PURSE-OPTIONS',null);
			break;
		case 'lof_top_sold_team_graphic_btn':
			processAuctionProcedures('LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS');
			break;
		case 'squad_Player_graphic_btn':
			processAuctionProcedures('SQUAD_PLAYER_GRAPHICS-OPTIONS');
			break;
		}
		break;
		
	case 'populate_namesuper_btn': case 'populate_namesuper_player_btn': case 'populate_playerprofile_btn':	case 'populate_squad_btn': case 'populate_Top_Sold_btn':
	case 'populate_single_purse_btn': case 'curr_bid_section': case 'populate_googly_purse_btn': case 'populate_profile_stats_btn': case 'populate_single_purse_btn': 
	case 'curr_bid_section': case 'populate_lof_remaining_purse_btn': case 'populate_Lof_Top_Sold_team_btn': case "populate_squad_Player_btn": 
	case 'populate_ff_playerprofile_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		switch ($(whichInput).attr('name')) {
		case 'curr_bid_section':
			processAuctionProcedures('POPULATE-CURR_BID');
			break;
		case 'populate_namesuper_btn':
			processAuctionProcedures('POPULATE-L3-NAMESUPER');
			break;
		case 'populate_namesuper_player_btn':
			processAuctionProcedures('POPULATE-L3-NAMESUPER-PLAYER');
			break;
		case 'populate_playerprofile_btn':
			processAuctionProcedures('POPULATE-FF-PLAYERPROFILE');
			break;
		case 'populate_ff_playerprofile_btn':
			processAuctionProcedures('POPULATE-PLAYERPROFILE_FF');
			break;
		case 'populate_squad_btn':
			processAuctionProcedures('POPULATE-SQUAD');
			break;
		case 'populate_Top_Sold_btn':
			processAuctionProcedures('POPULATE-TOP_SOLD_TEAM');
			break;
		case 'populate_single_purse_btn':
			processAuctionProcedures('POPULATE-SINGLE_PURSE');
			break;
		case 'populate_googly_purse_btn':
			processAuctionProcedures('POPULATE-GOOGLY_POWER');
			break;
		case 'populate_profile_stats_btn':
			processAuctionProcedures('POPULATE-PROFILE_STATS');
			break;
		case 'populate_lof_remaining_purse_btn':
			processAuctionProcedures('POPULATE-LOF_REMAINING_PURSE');
			break;
		case 'populate_Lof_Top_Sold_team_btn':
			processAuctionProcedures('POPULATE-LOF_TEAM_TOP_SOLD');
			break;
		case "populate_squad_Player_btn":
			processAuctionProcedures('POPULATE-SQUAD-PLAYER');
			break;
		}
		break;
	case 'cancel_match_setup_btn':
		document.output_form.method = 'post';
		document.output_form.action = 'initialise';
	   	document.output_form.submit();
		break;
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		$("#cancel_match_setup_btn").show();
		break;
	case 'select_broadcaster':
		switch ($('#select_broadcaster :selected').val().toUpperCase()) {
		case 'HANDBALL': case 'ISPL':
			$('#vizPortNumber').attr('value','1980');
			$('label[for=which_layer], select#which_layer').hide();
			break;
		}
		break;
	case 'load_scene_btn':
		if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}
      	document.initialise_form.submit();
		break;
	case 'selectTeams':
		switch ($(whichInput).attr('name')) {
		case 'selectTeams':
			addItemsToList('POPULATE-PROFILE',match_data);
			break;
	}
	break;
	/*case 'selectTeams':
		switch ($(whichInput).attr('name')) {
		case 'selectTeams':
			addItemsToList('POPULATE-PROFILE',match_data);
			break;
	}
	break;*/
	}
}
function processAuctionProcedures(whatToProcess)
{
	var valueToProcess;
	switch(whatToProcess) {
	
	case 'READ-MATCH-AND-POPULATE': 
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	case 'POPULATE-L3-NAMESUPER':
		case 'ISPL_VIZ':
			valueToProcess = '/Default/LT'+ ',' + $('#selectNameSuper option:selected').val();
		break;
		
	case 'POPULATE-PLAYERPROFILE_FF':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectPlayerName option:selected').val();
			break;
		}
		break;
		
	case 'POPULATE-FF-PLAYERPROFILE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/PlayerProfile_Pic.sum' + ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/PlayerProfile_Pic.sum' + ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'ISPL_VIZ':
			valueToProcess = '/Default/BidLt'+ ',' + $('#selectPlayerName option:selected').val();
			break;
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectPlayerName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-SQUAD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/Squad.sum' + ',' + $('#selectTeamName option:selected').val();
			break;	
		case 'ISPL_VIZ': 
			valueToProcess = '/Default/Squad'+ ',' + $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-SINGLE_PURSE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/RemainingPurse_Individual.sum' + ',' + $('#selectTeamName option:selected').val();
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/RemainingPurse_Individual.sum' + ',' + $('#selectTeamName option:selected').val();
			break;	
		}
		break;
		
	case 'POPULATE-LOF_REMAINING_PURSE':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectType option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-LOF_TEAM_TOP_SOLD':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case "POPULATE-SQUAD-PLAYER":
		switch ($('#selected_broadcaster').val().toUpperCase()) {	
		case 'VIZ_ISPL_2024': 
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
		
	case 'POPULATE-TOP_SOLD_TEAM':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ISPL_VIZ':
			valueToProcess = '/Default/TeamTopBuys'+ ',' + $('#selectTeamName option:selected').val();
			break;	
		}
		break;
	case 'POPULATE-TOP_SOLD': 
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/Top_Buys.sum' ;
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/Top_Buys.sum' ;
			break;
		case 'ISPL_VIZ':
			valueToProcess = '/Default/TopBuys';
			break;	
		}
		break;
	case 'POPULATE-IDENT':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'ISPL_VIZ':
			valueToProcess = '/Default/Ident';
			break;	
		}
		break;
	case 'POPULATE-REMAINING_PURSE_ALL':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/RemainingPurse_All.sum' ;
			break;
		case 'ISPL':
			valueToProcess = 'D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/RemainingPurse_All.sum' ;
			break;	
		case 'ISPL_VIZ':
			valueToProcess = '/Default/RemainingPurse' ;
			break;	
		}
		break;
	case 'POPULATE-GOOGLY_POWER':
		switch ($('#selected_broadcaster').val().toUpperCase()){
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectTeamName option:selected').val();
			break;
		}
		break;
	case 'POPULATE-PROFILE_STATS':
		switch ($('#selected_broadcaster').val().toUpperCase()){
		case 'VIZ_ISPL_2024':
			valueToProcess = $('#selectProfileStats option:selected').val();
			break;
		}
		break;
	}

	$.ajax({    
        type : 'Get',     
        url : 'processAuctionProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + valueToProcess, 
        dataType : 'json',
        success : function(data) {
			//match_data = data;
			
        	switch(whatToProcess) {
			case 'READ-MATCH-AND-POPULATE': case 'RE_READ_DATA':
				session_auction = data;
				if(data){
					//alert("match = " + $('#matchFileTimeStamp').val() + "Data = "+ data.matchFileTimeStamp)
					if($('#matchFileTimeStamp').val() != data.matchFileTimeStamp) {
						document.getElementById('matchFileTimeStamp').value = data.matchFileTimeStamp;
						initialiseForm("UPDATE-MATCH-ON-OUTPUT-FORM",data);
						//match_data = data;
					}
				}
				if(whatToProcess == 'RE_READ_DATA'){
					alert('Data is Loaded');
				}
				break;
			case "SQUAD_PLAYER_GRAPHICS-OPTIONS":
				addItemsToList('SQUAD_PLAYER-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'NAMESUPER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-OPTIONS',data);
				match_data = data;
				break;
			case 'NAMESUPER_PLAYER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER_PLAYER-OPTIONS',data);
				addItemsToList('POPULATE-PLAYER',data);
				match_data = data;
				break;
			
			case 'PLAYERPROFILE_GRAPHICS-OPTIONS': 
				addItemsToList('PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
			case 'FF_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('FF_PLAYERPROFILE-OPTIONS',data);
				addItemsToList('POPULATE-PROFILE',data);
				match_data = data;
				break;
				
			case 'SQUAD_GRAPHICS-OPTIONS':  
				addItemsToList('SQUAD-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'TOP-SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('TOP-SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'LOF_TOP_SOLD_TEAM_GRAPHICS-OPTIONS':
				addItemsToList('LOF_TOP_SOLD_TEAM-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'SINGLE_PURSE_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE_PURSE-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			case 'GOOGLY_GRAPHICS-OPTIONS':
				addItemsToList('GOOGLY-OPTIONS',data);
				addItemsToList('POPULATE-TEAM',data);
				match_data = data;
				break;
			
			case 'POPULATE-FF-PLAYERPROFILE': case 'POPULATE-SQUAD': case 'POPULATE-REMAINING_PURSE_ALL': case 'POPULATE-SINGLE_PURSE': case 'POPULATE-TOP_SOLD':
			case 'POPULATE-L3-NAMESUPER': case 'POPULATE-TOP_SOLD_TEAM': case 'POPULATE-IDENT': case 'POPULATE-CURR_BID': case 'POPULATE-RTM_AVAILABLE':
			case 'POPULATE-RTM_ENABLED': case 'POPULATE-GOOGLY_POWER': case 'POPULATE-PROFILE_STATS': case 'POPULATE-LOF_REMAINING_PURSE':
			case 'POPULATE-LOF_TOP_SOLD': case 'POPULATE-LOF_TEAM_TOP_SOLD': case "POPULATE-SQUAD-PLAYER": case 'POPULATE-PLAYERPROFILE_FF':
			
				if(whatToProcess == 'POPULATE-RTM_ENABLED' || whatToProcess == 'POPULATE-CURR_BID')	{
					switch(whatToProcess){
						case 'POPULATE-RTM_ENABLED':
							processAuctionProcedures('ANIMATE-IN-RTM_ENABLED');
							break;
						case 'POPULATE-CURR_BID':
							processAuctionProcedures('ANIMATE-IN-CURR_BID');
							break;
					}
				}else{
					if(confirm('Animate In?') == true){
						if(whatToProcess != 'POPULATE-FF-PLAYERPROFILE'){
							$('#select_graphic_options_div').empty();
							document.getElementById('select_graphic_options_div').style.display = 'none';
							$("#captions_div").show();	
						}
			        	switch(whatToProcess) {
						case 'POPULATE-IDENT':
							processAuctionProcedures('ANIMATE-IN-IDENT');
							break;
						case 'POPULATE-L3-NAMESUPER':
							processAuctionProcedures('ANIMATE-IN-NAMESUPER');	
							break;
						case 'POPULATE-TOP_SOLD': 
							processAuctionProcedures('ANIMATE-IN-TOP_SOLD');				
							break;
						case 'POPULATE-TOP_SOLD_TEAM':
							processAuctionProcedures('ANIMATE-IN-TOP_SOLD_TEAM');				
							break;
						case 'POPULATE-REMAINING_PURSE_ALL':
							processAuctionProcedures('ANIMATE-IN-REMAINING_PURSE_ALL');				
							break;
						case 'POPULATE-SQUAD':
							processAuctionProcedures('ANIMATE-IN-SQUAD');				
							break;
						case 'POPULATE-FF-PLAYERPROFILE':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE');				
							break;
						case 'POPULATE-SINGLE_PURSE':
							processAuctionProcedures('ANIMATE-IN-SINGLE_PURSE');				
							break;
						case 'POPULATE-RTM_AVAILABLE':
							processAuctionProcedures('ANIMATE-IN-RTM_AVAILABLE');
							break;
						case 'POPULATE-GOOGLY_POWER':
							processAuctionProcedures('ANIMATE-IN-GOOGLY_POWER');
							break;
						case 'POPULATE-PROFILE_STATS':
							processAuctionProcedures('ANIMATE-IN-PROFILE_STATS');
							break;
						case 'POPULATE-LOF_REMAINING_PURSE':
							processAuctionProcedures('ANIMATE-IN-LOF_REMAINING_PURSE');				
							break;
						case 'POPULATE-LOF_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-LOF_TOP_SOLD');				
							break;
						case 'POPULATE-LOF_TEAM_TOP_SOLD':
							processAuctionProcedures('ANIMATE-IN-LOF_TEAM_TOP_SOLD');				
							break;
						case "POPULATE-SQUAD-PLAYER":
							processAuctionProcedures('ANIMATE-IN-SQUAD-PLAYER');
						case 'POPULATE-PLAYERPROFILE_FF':
							processAuctionProcedures('ANIMATE-IN-PLAYERPROFILE_FF');
							break;
						}
					}
				}
				break;
        	}
			processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var select,option,header_text,div,table,tbody,row,max_cols;
	var cellCount = 0;

	switch (whatToProcess) {
	case 'POPULATE-TEAM':
		$('#selectTeamName').empty();
		
		dataToProcess.forEach(function(tm,index,arr1){
			$('#selectTeamName').append(
					$(document.createElement('option')).prop({
					value: tm.teamId,
					text: tm.teamName1
				}))
		});
		
		break;
	case 'POPULATE-PROFILE' :
		$('#selectPlayerName').empty();
		session_auction.playersList.forEach(function(plyr,index,arr1){
			if(plyr.playerId == session_auction.players[session_auction.players.length- 1].playerId){
				$('#selectPlayerName').append(
					$(document.createElement('option')).prop({
					value: plyr.playerId,
					text: plyr.playerNumber + ' - ' + plyr.full_name + ' - ' + plyr.category + ' - ' + plyr.role
				}))
			}
		});
		
		
		dataToProcess.forEach(function(plyr,index,arr1){
			if(plyr.playerId != session_auction.players[session_auction.players.length - 1].playerId){
				$('#selectPlayerName').append(
					$(document.createElement('option')).prop({
					value: plyr.playerId,
					text: plyr.playerNumber + ' - ' + plyr.full_name + ' - ' + plyr.category + ' - ' + plyr.role
				}))
			}
		});
		
		break;
		
	case 'POPULATE-PLAYER':
		$('#selectPlayer').empty();
		if(dataToProcess.homeTeamId ==  $('#selectTeam option:selected').val()){
			dataToProcess.homeSquad.forEach(function(hs,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: hs.playerId,
	                text: hs.full_name
		        }))					
			});
		}
		else {
			dataToProcess.awaySquad.forEach(function(as,index,arr){
				$('#selectPlayer').append(
					$(document.createElement('option')).prop({
	                value: as.playerId,
	                text: as.full_name
		        }))					
			});
		}
		
		break;
		
	case'NAMESUPER-OPTIONS': case 'NAMESUPER_PLAYER-OPTIONS':  case'PLAYERPROFILE-OPTIONS': case 'SQUAD-OPTIONS': case 'SINGLE_PURSE-OPTIONS': 
	case 'TOP-SOLD_TEAM-OPTIONS': case 'GOOGLY-OPTIONS': case 'PROFILE_STATS-OPTIONS': case 'LOF_REMAINING_PURSE-OPTIONS': case 'LOF_TOP_SOLD_TEAM-OPTIONS': 
	case'SQUAD_PLAYER-OPTIONS': case 'FF_PLAYERPROFILE-OPTIONS':
		switch ($('#selected_broadcaster').val().toUpperCase()) {
		case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
 			
 			/*if(whatToProcess != 'PLAYERPROFILE-OPTIONS'){
				$("#select_graphic_options_div").empty();
				document.getElementById('select_graphic_options_div').style.display = 'none';
				$("#captions_div").show();
			}*/
			
			$('#select_graphic_options_div').empty();
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'SINGLE_PURSE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'HANDBALL': case 'ISPL':
						select = document.createElement('select');
						select.id = 'selectTeamName';
						select.name = select.id;
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						break;
					}
					break;
				case 'SQUAD-OPTIONS': case 'TOP-SOLD_TEAM-OPTIONS': case 'GOOGLY-OPTIONS': case 'LOF_TOP_SOLD_TEAM-OPTIONS': case'SQUAD_PLAYER-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
							select = document.createElement('select');
							select.id = 'selectTeamName';
							select.name = select.id;
							
							select.setAttribute('onchange',"processUserSelection(this)");
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							
							break;
						} 
						break;
				case'NAMESUPER-OPTIONS':
					switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'DOAD_IN_HOUSE_EVEREST': case 'DOAD_IN_HOUSE_VIZ': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectNameSuper';
						select.name = select.id;
						
						dataToProcess.forEach(function(ns,index,arr1){
							option = document.createElement('option');
							option.value = ns.namesuperId;
							option.text = ns.firstname ;
							select.appendChild(option);
						});
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						switch ($('#selected_broadcaster').val().toUpperCase()) {
						case 'DOAD_IN_HOUSE_EVEREST': 
							select = document.createElement('input');
							select.type = "text";
							select.id = 'namesuperScene';
							select.value = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/LT_NameSuper.sum';
							
							row.insertCell(cellCount).appendChild(select);
							cellCount = cellCount + 1;
							
							break;
						}
						break;
					}
					break;
				
			case 'NAMESUPER_PLAYER-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'DOAD_IN_HOUSE_EVEREST': case 'DOAD_IN_HOUSE_VIZ':
						select = document.createElement('select');
						select.id = 'selectTeam';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = dataToProcess.homeTeamId;
						option.text = dataToProcess.homeTeam.shortname;
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = dataToProcess.awayTeamId;
						option.text = dataToProcess.awayTeam.shortname;
						select.appendChild(option);
					
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
		
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectCaptainWicketKeeper';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'Captain';
						option.text = 'Captain';
						select.appendChild(option);
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						select = document.createElement('select');
						select.style = 'width:100px';
						select.id = 'selectPlayer';
						select.name = select.id;
						
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
						
						switch ($('#selected_broadcaster').val().toUpperCase()) {
							case 'DOAD_IN_HOUSE_EVEREST': 
								select = document.createElement('input');
								select.type = "text";
								select.id = 'namesuperplayerScene';
								select.value = 'D:/DOAD_In_House_Everest/Everest_Cricket/EVEREST_APL2022/Scenes/LT_NameSuper.sum';
								
								row.insertCell(cellCount).appendChild(select);
								cellCount = cellCount + 1;
								
								break;
						}
						break;
				}
				break;
			case'PLAYERPROFILE-OPTIONS': case 'FF_PLAYERPROFILE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
					case 'HANDBALL': case 'ISPL': case 'ISPL_VIZ': case 'VIZ_ISPL_2024':
						select = document.createElement('select');
						select.id = 'selectPlayerName';
						select.name = select.id;
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						$(select).select2();
						cellCount = cellCount + 1;
						
						/*option = document.createElement('input');
		    			option.type = 'button';
		    			option.name = 'curr_bid_section';
			    		option.value = 'Populate Current bid';
			    		option.id = option.name;
					    option.setAttribute('onclick',"processUserSelection(this)");
					    row.insertCell(cellCount).appendChild(option);
					    cellCount = cellCount + 1;*/
						
						break;
				} 
				break;
			case 'PROFILE_STATS-OPTIONS':
				select = document.createElement('select');
				select.style = 'width:100px';
				select.id = 'selectProfileStats';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'category';
				option.text = 'Category';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'style';
				option.text = 'Style';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'prevteam';
				option.text = 'Previous Team';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'stats';
				option.text = 'Stats';
				select.appendChild(option);
				
				select.setAttribute('onchange',"processUserSelection(this)");
				row.insertCell(cellCount).appendChild(select);
				cellCount = cellCount + 1;
				break;
			case 'LOF_REMAINING_PURSE-OPTIONS':
				switch ($('#selected_broadcaster').val().toUpperCase()) {
				case 'VIZ_ISPL_2024':
					select = document.createElement('select');
					select.id = 'selectType';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'name';
					option.text = 'Team Name';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'logo';
					option.text = 'Team Logo';
					select.appendChild(option);
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					$(select).select2();
					cellCount = cellCount + 1;
						
					break;
				}
				break;
			}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			
			case'NAMESUPER-OPTIONS':
			    option.name = 'populate_namesuper_btn';
			    option.value = 'Populate Namesuper';
				break;
			case 'NAMESUPER_PLAYER-OPTIONS':	
				option.name = 'populate_namesuper_player_btn';
			    option.value = 'Populate Namesuper-Player';
				break;
				
			case 'LOF_REMAINING_PURSE-OPTIONS':
				option.name = 'populate_lof_remaining_purse_btn';
			    option.value = 'Populate Lof Remaining Purse';
				break;
			
			case'PLAYERPROFILE-OPTIONS':
			    option.name = 'populate_playerprofile_btn';
			    option.value = 'Populate Playerprofile';
				break;
			case 'FF_PLAYERPROFILE-OPTIONS':
				option.name = 'populate_ff_playerprofile_btn';
			    option.value = 'Populate Playerprofile FF';
				break;
				
			case'SQUAD-OPTIONS':
			    option.name = 'populate_squad_btn';
			    option.value = 'Populate Squad';
				break;
			case 'TOP-SOLD_TEAM-OPTIONS':
				option.name = 'populate_Top_Sold_btn';
			    option.value = 'Populate Squad';
				break;
			case 'LOF_TOP_SOLD_TEAM-OPTIONS':
				option.name = 'populate_Lof_Top_Sold_team_btn';
			    option.value = 'Populate Team Top Sold';
				break;
			case 'SINGLE_PURSE-OPTIONS':
				option.name = 'populate_single_purse_btn';
			    option.value = 'Populate Single Purse';
				break;	
			case 'GOOGLY-OPTIONS':
				option.name = 'populate_googly_purse_btn';
			    option.value = 'Googly Power';
				break;
			case 'PROFILE_STATS-OPTIONS':
				option.name = 'populate_profile_stats_btn';
			    option.value = 'Profile Stats';
				break;
			case "SQUAD_PLAYER-OPTIONS":
				option.name = 'populate_squad_Player_btn';
			    option.value = 'Populate Squad Player';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);
		    	switch (whatToProcess) {
					case'PLAYERPROFILE-OPTIONS':
						
						option = document.createElement('input');
		    			option.type = 'button';
		    			option.name = 'curr_bid_section';
			    		option.value = 'Populate Current bid';
			    		option.id = option.name;
					    option.setAttribute('onclick',"processUserSelection(this)");
					    
		    			div.append(option);
					   
						break;
				} 

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
	}
	
	
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}