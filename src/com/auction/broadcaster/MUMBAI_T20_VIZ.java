package com.auction.broadcaster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.PlayerCount;
import com.auction.model.Statistics;
import com.auction.model.StatsType;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
import com.auction.model.Flipper;
import com.auction.model.NameSuper;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class MUMBAI_T20_VIZ extends Scene{

	private String status, side2ValueToProcess = "";
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "MUMBAI_T20_VIZ";
	public Data data = new Data();
	public String which_graphics_onscreen = "",which_data="", rtm_googly_on_screen = "";
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1, rowHighlight = 1,prevRowHighlight = 1, rtmGooglyWhichSide = 1;
	public int player_id = 0,team_id=0,player_number=0;
	public int zoneSize = 0, current_index = 0;
	
	List<Player> squad = new ArrayList<Player>();
	List<String> data_str = new ArrayList<String>();
	List<PlayerCount> player_count = new ArrayList<PlayerCount>();

	private String base_path = "IMAGE*/Default/Essentials/Base/";
	private String text_path = "IMAGE*/Default/Essentials/Text/";
	
	private String base_path_1 = "IMAGE*/Default/Essentials/Base1/";
	private String text_path_1 = "IMAGE*/Default/Essentials/Text1/";
	private String base_path_2 = "IMAGE*/Default/Essentials/Base2/";
	private String text_path_2 = "IMAGE*/Default/Essentials/Text2/";
	
	private String logo_base = "IMAGE*/Default/Essentials/LogoBase/";
	private String logo_path = "IMAGE*/Default/Essentials/Logos/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	public boolean isProfileStatsOnScreen = false;
	
	public MUMBAI_T20_VIZ() {
		super();
	}

	public MUMBAI_T20_VIZ(String scene_path, String which_Layer) {
		super(scene_path, which_Layer);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Data updateData(Auction auction, Auction session_curr_bid,AuctionService auctionService, PrintWriter print_writer) throws InterruptedException
	{
		if(data.isData_on_screen()) {
			if(data.isPlayer_sold_or_unsold() == false) {
				populatePlayerProfile(true,print_writer, 2, data.getPlayer_id(),auctionService.getAllStats(), auction, 
						session_curr_bid, auctionService, session_selected_broadcaster);
			}
			
			if(data.getPreviousBid() < session_curr_bid.getCurrentPlayers().getSoldForPoints() || 
					data.getPreviousBid() > session_curr_bid.getCurrentPlayers().getSoldForPoints()) {
				
				data.setPreviousBid(session_curr_bid.getCurrentPlayers().getSoldForPoints());
				
				BidChangeOn(print_writer, session_curr_bid, 2);
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Bid_Value START \0");
				TimeUnit.MILLISECONDS.sleep(800);
				BidChangeOn(print_writer, session_curr_bid, 1);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Bid_Value SHOW 0.0 \0");
			}
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, Auction session_curr_bid, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws InterruptedException, NumberFormatException, IllegalAccessException {
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SINGLE_PURSE": case "POPULATE-TOP_SOLD":
		case "POPULATE-L3-NAMESUPER": case "POPULATE-TOP_SOLD_TEAM": case "POPULATE-IDENT": case "POPULATE-RTM_AVAILABLE": case "POPULATE-RTM_ENABLED":
		case "POPULATE-GOOGLY_POWER": case "POPULATE-PROFILE_STATS": case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD":
		case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-LOF_REMAINING_SLOT":
		case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-FF_RTM_AND_PURSE_REMAINING": case "POPULATE-FF_TOP_BUYS_AUCTION":
		case "POPULATE-FF_TOP_BUY_TEAM": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": case "POPULATE-FF_ICONIC_PLAYERS": case "POPULATE-LT_ICONIC_PLAYERS": 
		case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-PLAYERPROFILE_LT_STATS": case "POPULATE-LOF_SQUAD": case "POPULATE-LOF_SQUAD_REMAIN":
		case "POPULATE-L3-FLIPPER": case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-TEAM_CURR_BID": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":
		case "POPULATE-FF_FIVE_TOP_BUY_TEAM": case "POPULATE-ZONEWISE_PLAYERS_SOLD":
			switch (session_selected_broadcaster.toUpperCase()) {
			case "MUMBAI_T20_VIZ":
				if(which_graphics_onscreen != "") {
					switch(which_graphics_onscreen) {
					case "PLAYERPROFILE": 
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_All CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_PlayerInfo CONTINUE \0");
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Anim_BasePrice CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER":
					case "TOP_SOLD_TEAM": case "IDENT":
						print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
						TimeUnit.MILLISECONDS.sleep(1500);
						break;
					}
				}
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-FF-PLAYERPROFILE":
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[0]));
					populatePlayerProfile(false,print_writer,whichSide,Integer.valueOf(valueToProcess.split(",")[0]), auctionService.getAllStats(),auction, 
							session_curr_bid,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSide);
					break;
					
				case "POPULATE-PLAYERPROFILE_FF":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populatePlayerProfileFF(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-REMAINING_PURSE_ALL":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateRemainingPurse(print_writer, whichSideNotProfile,auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSquad(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-TOP_SOLD":
					populateTopSold(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-IDENT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateIdent(print_writer,whichSideNotProfile,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-TOP_SOLD_TEAM":
					populateTopSoldTeam(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), 
							auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-L3-FLIPPER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFlipper(print_writer, whichSideNotProfile,Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService, 
							session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-L3-NAMESUPER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateNameSuper(print_writer, whichSideNotProfile,Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService, 
							session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-RTM_AVAILABLE":
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						rtmGooglyWhichSide = 2;
					}else {
						rtmGooglyWhichSide = 1;
					}
					populateRTMAvailable(print_writer, rtmGooglyWhichSide, session_curr_bid, auctionService);
					processPreview(print_writer, whatToProcess, rtmGooglyWhichSide);
					break;
				case "POPULATE-RTM_ENABLED":
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						rtmGooglyWhichSide = 2;
					}else {
						rtmGooglyWhichSide = 1;
					}
					populateRTMEnabled(print_writer, rtmGooglyWhichSide);
					processPreview(print_writer, whatToProcess, rtmGooglyWhichSide);
					break;
				case "POPULATE-GOOGLY_POWER":
					populateGooglyPower(print_writer, Integer.valueOf(valueToProcess), auctionService);
					processPreview(print_writer, whatToProcess, 1);
					break;
				case "POPULATE-PROFILE_STATS":
					if(isProfileStatsOnScreen) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					
					
					
					populateProfileStats(print_writer, valueToProcess.split(",")[0], 
							valueToProcess.split(",").length != 2 ? 0 : auctionService.getStatsTypes().stream()
						        .filter(st -> st.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[1]))
						        .findAny().map(StatsType::getStats_id).orElse(0), whichSideNotProfile, auction, auctionService);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-TEAM_CURR_BID":
					if(isProfileStatsOnScreen) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateTeamCurrentBid(print_writer, Integer.valueOf(valueToProcess), whichSideNotProfile, auction, session_curr_bid, auctionService);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-LOF_REMAINING_PURSE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofRemainingPurse(print_writer, valueToProcess.split(",")[0],whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_TOP_SOLD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateLofTopSold(print_writer,whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_TEAM_TOP_SOLD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofTeamTopSold(print_writer,Integer.valueOf(valueToProcess.split(",")[0]),whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					player_number = 0;
					squad.clear();
					
					team_id = Integer.valueOf(valueToProcess.split(",")[0]);
					side2ValueToProcess = valueToProcess;
					populateLofSquad(print_writer,Integer.valueOf(valueToProcess.split(",")[0]),whichSideNotProfile, auction,auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_SQUAD_REMAIN":
					whichSideNotProfile = 2;
					ChangeOnLofSquad(print_writer, team_id, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				
				case "POPULATE-SQUAD-PLAYER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofSquadSizeCategoryWise(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofSquadSizeCategoryWiseOnly(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_REMAINING_SLOT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofRemainingSlot(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-ZONEWISE_PLAYERS_SOLD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofZoneWisePlayerSold(print_writer, whichSideNotProfile, side2ValueToProcess, auction, auctionService, session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-LOF_SQUAD_SIZE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofSquadSize(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LOF_RTM_REMAINING":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateLofRTMRemaining(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFRTMAndPurseRemaining(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_TOP_BUYS_AUCTION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopBuysAuction(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFFiveTopBuysAuction(print_writer, whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_TOP_BUY_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopBuysTeam(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFFTopFiveBuysTeam(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), auction,auctionService,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
					
				case "POPULATE-FF_ICONIC_PLAYERS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateFFIconicPlayers(print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-LT_ICONIC_PLAYERS":
					populateLTIconicPlayers(print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PLAYERPROFILE_LT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					player_id = Integer.valueOf(valueToProcess.split(",")[0]);
					side2ValueToProcess = valueToProcess;
					populatePlayerProfileLT(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							valueToProcess.split(",").length != 3 ? 0 : auctionService.getStatsTypes().stream()
							        .filter(st -> st.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[2]))
							        .findAny().map(StatsType::getStats_id).orElse(0),
							        auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PLAYERPROFILE_LT_STATS":
					whichSideNotProfile = 2;
					side2ValueToProcess = valueToProcess;
					ChangeOnLTStats(print_writer, whichSideNotProfile, player_id, valueToProcess.split(",")[0],
							valueToProcess.split(",").length != 2 ? 0 : auctionService.getStatsTypes().stream()
							        .filter(st -> st.getStats_short_name().equalsIgnoreCase(valueToProcess.split(",")[1]))
							        .findAny().map(StatsType::getStats_id).orElse(0), auctionService.getAllStats(), 
							auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-ZONE_PLAYERS_STATS":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					if(!which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS") || 
							(!valueToProcess.equalsIgnoreCase("undefined") && !side2ValueToProcess.equalsIgnoreCase(valueToProcess))) {
						squad.clear();
						current_index = 0;
						squad = auction.getPlayersList().stream()
							    .filter(ply -> ply.getCategory().equalsIgnoreCase(valueToProcess.split(",")[0]))
							    .collect(Collectors.toList());
						Iterator<Player> squadIterator = squad.iterator();

						while (squadIterator.hasNext()) {
						    Player sq = squadIterator.next();						    
						    for (Player ply : auction.getPlayers()) {
						        if (sq.getPlayerId() == ply.getPlayerId() &&
						            (ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM) || ply.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD))) {						                
						            squadIterator.remove();
						            break;
						        }
						    }
						}
						zoneSize = squad.size();
						side2ValueToProcess = valueToProcess;
					}
					populateZoneSquad(print_writer, side2ValueToProcess.split(",")[0], whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				}
			}
		case "ANIMATE-OUT-PROFILE": case "ANIMATE-OUT-RTM_GOOGLY":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-RTM_ENABLED": case "ANIMATE-IN-GOOGLY_POWER": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD": case "ANIMATE-IN-SQUAD-PLAYER": 
		case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING":
		case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-TEAM_CURR_BID": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
		
		case "ANIMATE-IN-LOF_SQUAD": case "ANIMATE-IN-LOF_SQUAD_REMAIN":

		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
		case "ANIMATE-IN-LT_ICONIC_PLAYERS": case "ANIMATE-IN-PLAYERPROFILE_LT": case "ANIMATE-IN-PLAYERPROFILE_LT_STATS": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "MUMBAI_T20_VIZ":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-OUT-PLAYER_STAT":
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						isProfileStatsOnScreen = false;
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats SHOW 0.0\0");
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-IN-TEAM_CURR_BID":
					if(data.isData_on_screen()) {
						if(isProfileStatsOnScreen) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats$Change_Out START \0");
							TimeUnit.MILLISECONDS.sleep(800);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats$Change_In START \0");
							
							if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PROFILE_STATS")) {
								populateProfileStats(print_writer, side2ValueToProcess.split(",")[0], 
										side2ValueToProcess.split(",").length != 2 ? 0 : auctionService.getStatsTypes().stream()
										        .filter(st -> st.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[1]))
										        .findAny().map(StatsType::getStats_id).orElse(0), 1, auction, auctionService);
							}else if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-TEAM_CURR_BID")) {
								populateTeamCurrentBid(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, session_curr_bid, auctionService);
							}
							
							TimeUnit.MILLISECONDS.sleep(1000);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats SHOW 0\0");
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats START \0");
						}
						isProfileStatsOnScreen = true;
					}
					break;
				case "ANIMATE-IN-CURR_BID":
					populateCurrentBid(print_writer, 2);
					if(data.getBid_result().equalsIgnoreCase("GAVEL") || data.getBid_result().equalsIgnoreCase("BID")) {
//						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Gavel START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$BasePriceHead START \0");
						TimeUnit.MILLISECONDS.sleep(200);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Right_Data_Move START \0");
						TimeUnit.MILLISECONDS.sleep(200);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$CurrentBid START \0");
					}
					
					data.setBid_Start_or_not(true);
					data.setBid_result("BID");
					
					TimeUnit.MILLISECONDS.sleep(777);
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side1$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0");
					
					populateCurrentBid(print_writer, 1);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Right_Data_Move SHOW 1.560 \0");
					break;
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Essentials START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$CenterData START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Right_Data START \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Loop START \0");
					TimeUnit.MILLISECONDS.sleep(500);
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats START \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats START \0");
					if(data.isPlayer_sold_or_unsold() == false) {
						data.setBid_result("GAVEL");
					}else {
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold START \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
//							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Right_Data_Move START \0");
						}
					}
					//isProfileStatsOnScreen = true;
					data.setData_on_screen(true);
					break;
				case "ANIMATE-IN-SINGLE_PURSE":
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In START;");
					print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Loop START;");
					which_graphics_onscreen = "SINGLE_PURSE";
					break;
				case "ANIMATE-IN-TOP_SOLD":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD";
					break;
				case "ANIMATE-IN-TOP_SOLD_TEAM":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "TOP_SOLD_TEAM";
					break;
				case "ANIMATE-IN-LT_ICONIC_PLAYERS":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird START\0");
					which_graphics_onscreen = "LT_ICONIC_PLAYERS";
					break;
					
				case "ANIMATE-IN-PLAYERPROFILE_LT_STATS":
					ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
					ChangeOnLTStats(print_writer, 1, player_id, side2ValueToProcess.split(",")[0],
							side2ValueToProcess.split(",").length != 2 ? 0 : auctionService.getStatsTypes().stream()
							        .filter(st -> st.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[1]))
							        .findAny().map(StatsType::getStats_id).orElse(0), auctionService.getAllStats(), auction, 
							auctionService, session_selected_broadcaster);	
					TimeUnit.MILLISECONDS.sleep(2000);
					cutBack(print_writer, which_graphics_onscreen, whatToProcess);
					which_graphics_onscreen = "PLAYERPROFILE_LT";
					break;
				//Flipper
				case "ANIMATE-IN-FLIPPER":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Header START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Text START \0");
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						populateFlipper(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), auction, 
								auctionService, session_selected_broadcaster);
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
					}
					which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					break;
					
				//LT
				case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-PLAYERPROFILE_LT":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$TopData START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$BottomData START \0");
						
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-NAMESUPER": 
							populateNameSuper(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), auction, auctionService, 
									session_selected_broadcaster);
							break;
						case "ANIMATE-IN-PLAYERPROFILE_LT":
							populatePlayerProfileLT(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									side2ValueToProcess.split(",").length != 3 ? 0 : auctionService.getStatsTypes().stream()
									        .filter(st -> st.getStats_short_name().equalsIgnoreCase(side2ValueToProcess.split(",")[2]))
									        .findAny().map(StatsType::getStats_id).orElse(0),
									        auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
				//FF	
				case "ANIMATE-IN-IDENT": case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
				case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
				case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials START\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header START\0");
						
						switch(whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId START\0");
							which_graphics_onscreen = "IDENT";
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile START\0");
							which_graphics_onscreen = "PLAYERPROFILE_FF";
							break;
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRTM START\0");
							which_graphics_onscreen = "FF_RTM_AND_PURSE_REMAINING";
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-REMAINING_PURSE_ALL":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining START\0");
							which_graphics_onscreen = "REMAINING_PURSE_ALL";
							break;
						case "ANIMATE-IN-SQUAD":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad START\0");
							which_graphics_onscreen = "SQUAD";
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad START\0");
							which_graphics_onscreen = "ZONE-PLAYER_STATS";
							break;
						case "ANIMATE-IN-FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers START\0");
							which_graphics_onscreen = "FF_ICONIC_PLAYERS";
							break;
						}
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							populateIdent(print_writer,1,session_selected_broadcaster);
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							populatePlayerProfileFF(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-REMAINING_PURSE_ALL":
							populateRemainingPurse(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD":
							populateSquad(print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]), 
									1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS":
							populateZoneSquad(print_writer, side2ValueToProcess.split(",")[0], 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							populateFFRTMAndPurseRemaining(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							populateFFTopBuysAuction(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							populateFFFiveTopBuysAuction(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							populateFFTopFiveBuysTeam(print_writer, 1, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							populateFFTopBuysTeam(print_writer, 1, Integer.valueOf(side2ValueToProcess), auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-FF_ICONIC_PLAYERS":
							populateFFIconicPlayers(print_writer, whichSide, auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
					
				case "ANIMATE-IN-RTM_AVAILABLE":
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_Out START\0");
						TimeUnit.MILLISECONDS.sleep(250);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_In START\0");
						TimeUnit.MILLISECONDS.sleep(500);
						populateRTMAvailable(print_writer, 1, session_curr_bid, auctionService);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out START \0");
					}
					rtm_googly_on_screen = "RTM";
					break;
				case "ANIMATE-IN-RTM_ENABLED":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_Out START\0");
					TimeUnit.MILLISECONDS.sleep(250);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_In START\0");
					TimeUnit.MILLISECONDS.sleep(500);
					populateRTMEnabled(print_writer, 1);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0\0");
					rtm_googly_on_screen = "RTM";
					break;
				case "ANIMATE-IN-GOOGLY_POWER":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out START\0");
					rtm_googly_on_screen = "GOOGLY";
					break;
					
				case "ANIMATE-IN-LOF_SQUAD_REMAIN":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam START \0");
					TimeUnit.MILLISECONDS.sleep(1000);
					ChangeOnLofSquad(print_writer, team_id, 1, auction, auctionService, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(2000);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam SHOW 0.0\0");
					which_graphics_onscreen = "LOF_SQUAD";
					break;
				
				//LOF	
				case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
				case "ANIMATE-IN-SQUAD-PLAYER": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING":
				case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE": case "ANIMATE-IN-LOF_SQUAD": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Header START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$SubHeader START \0");
						
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-LOF_REMAINING_PURSE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo START \0");
							which_graphics_onscreen = "LOF_REMAINING_PURSE";
							break;
							
						case "ANIMATE-IN-LOF_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys START \0");
							which_graphics_onscreen = "LOF_TOP_SOLD";
							break;
						case "ANIMATE-IN-LOF_SQUAD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam START \0");
							which_graphics_onscreen = "LOF_SQUAD";
							break;
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam START \0");
							which_graphics_onscreen = "LOF_TEAM_TOP_SOLD";
							break;
						case "ANIMATE-IN-SQUAD-PLAYER":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side"+whichSide+"$"+rowHighlight +" START \0");
							for(int i=rowHighlight;i<=6;i++) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side"+whichSide+"$"+(i+1 )+" START \0");
							}
							prevRowHighlight = rowHighlight;
							which_graphics_onscreen = "SQUAD-PLAYER";
							break;
						case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING": 
						case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name START \0");
							which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team START \0");
							which_graphics_onscreen = "LOF_SQUAD_SIZE_CATEGORY_WISE";
							break;
						}
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-LOF_REMAINING_PURSE":
							populateLofRemainingPurse(print_writer, side2ValueToProcess, 1, auction, auctionService, session_selected_broadcaster);
							break;

						case "ANIMATE-IN-LOF_TOP_SOLD":
							populateLofTopSold(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_SQUAD":
							populateLofSquad(print_writer, team_id, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							populateLofTeamTopSold(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD-PLAYER":
							populateLofSquadSizeCategoryWise(print_writer,  Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_REMAINING_SLOT":
							populateLofRemainingSlot(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
							populateLofZoneWisePlayerSold(print_writer, 1, side2ValueToProcess, auction, auctionService, session_selected_broadcaster);	
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE":
							populateLofSquadSize(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_RTM_REMAINING":
							populateLofRTMRemaining(print_writer, 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
							populateLofSquadSizeCategoryWiseOnly(print_writer,  Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;


				
				case "CLEAR-ALL":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Bid_Value SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF SHOW 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper SHOW 0\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare SHOW 0.0 \0");
					
					rtm_googly_on_screen = "";
		            which_graphics_onscreen = "";
		            side2ValueToProcess ="";
		            rtmGooglyWhichSide = 1;
		            whichSideNotProfile = 1;
		            data.setBid_Start_or_not(false);
		            data.setData_on_screen(false);
		            isProfileStatsOnScreen = false;
					data.setBid_result("");
					break;
				case "ANIMATE-OUT-PROFILE":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Essentials CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$CenterData CONTINUE REVERSE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Right_Data CONTINUE \0");
					
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						isProfileStatsOnScreen = false;
					}
					
					if(!rtm_googly_on_screen.isEmpty()) {
						switch (rtm_googly_on_screen) {
						case "RTM":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out CONTINUE\0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0\0");
							break;

						case "GOOGLY":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out CONTINUE\0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0\0");
							break;
						}
						rtm_googly_on_screen = "";
					}
					
					TimeUnit.MILLISECONDS.sleep(1200);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug SHOW 0\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold SHOW 0.0 \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					data.setBid_Start_or_not(false);
					data.setPlayer_sold_or_unsold(false);
					data.setData_on_screen(false);
					data.setBid_result("");
					break;
				case "ANIMATE-OUT-RTM_GOOGLY":
					switch (rtm_googly_on_screen) {
					case "RTM":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0\0");
						break;

					case "GOOGLY":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0\0");
						break;
					}
					rtm_googly_on_screen = "";
					break;
				
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "LT_ICONIC_PLAYERS":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_IconLowerThird SHOW 0\0");
						which_graphics_onscreen = "";
						break;
					case "PLAYERPROFILE_LT":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Essentials CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$TopData CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$BottomData CONTINUE\0");
						
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
					case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
						switch (which_graphics_onscreen) {
						case "LOF_REMAINING_PURSE": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING":
						case "ZONEWISE_PLAYERS_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "LOF_SQUAD": case "LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam CONTINUE \0");
							which_graphics_onscreen = "";
							break;
							
						case "LOF_TOP_SOLD": 
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "SQUAD-PLAYER":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category CONTINUE\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight CONTINUE REVERSE\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight CONTINUE REVERSE \0");
							break;
						case "LOF_SQUAD_SIZE_CATEGORY_WISE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team CONTINUE \0");
							break;
						}
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Essentials CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Header CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$SubHeader CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "IDENT": case "PLAYERPROFILE_FF": case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
					case "SQUAD": case "REMAINING_PURSE_ALL": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION":
					case "FF_FIVE_TOP_BUY_TEAM":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials CONTINUE\0");
						switch (which_graphics_onscreen) {
						case "IDENT":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId CONTINUE\0");
							break;
						case "PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile CONTINUE\0");
							break;
						case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRTM CONTINUE\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys CONTINUE\0");
							break;
						case "REMAINING_PURSE_ALL":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining CONTINUE\0");
							break;
						case "SQUAD": case "ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad CONTINUE\0");
							break;
						case "FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers CONTINUE\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage CONTINUE\0");
							break;
						}
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe SHOW 0\0");
						break;
					case "NAMESUPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$Essentials CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$TopData CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$BottomData CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						break;
					case "FLIPPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Header CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Text CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Flipper$In_Out$Essentials CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Flipper SHOW 0\0");
						break;
						
						
//					case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER": case "TOP_SOLD_TEAM":
//						AnimateOutGraphics(print_writer, whatToProcess.toUpperCase());
//						which_graphics_onscreen = "";
//						break;
					}
					break;
				}
				
			}
		}
		return null;
	}
	public void ChangeOn(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException {
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": 
		case "LOF_RTM_REMAINING": case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
			if(whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER") && whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader START \0");
			}
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_Out START \0");
			break;
		case "LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_Out START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo$Change_Out START \0");
			break;
		case "LOF_TEAM_TOP_SOLD": case "LOF_SQUAD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam START \0");
			break;
		case "LOF_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys START \0");
			break;
		case "SQUAD-PLAYER":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category START \0");
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$"+prevRowHighlight +" CONTINUE REVERSE\0");
			for(int i=1;i<=6;i++) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$"+i+" CONTINUE REVERSE\0");
			}
			break;
		case "LOF_SQUAD_SIZE_CATEGORY_WISE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team START \0");
			break;
			
		//FF	
		case "IDENT": case "PLAYERPROFILE_FF": case "REMAINING_PURSE_ALL": case "SQUAD": case "FF_RTM_AND_PURSE_REMAINING": 
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": 
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Header START\0");
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
				break;
			case "PLAYERPROFILE_FF":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
				break;
			case "REMAINING_PURSE_ALL":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining START\0");
				break;
			case "SQUAD": case "ZONE-PLAYER_STATS":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Squad START\0");
				break;
			case "FF_RTM_AND_PURSE_REMAINING":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRTM START\0");
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys$Change_Out START\0");
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuysImage$Change_Out START\0");
				break;
			case "FF_ICONIC_PLAYERS":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$IconPlayers START\0");
				break;
			}
			break;
		
		//Flipper	
		case "FLIPPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Header START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Text START \0");
			break;
			
		//LT
		case "NAMESUPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$TopData START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData START \0");
			break;
		case "PLAYERPROFILE_LT":
			if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_LT_STATS")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData START \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$TopData START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData START \0");
			}
			break;
		}
		TimeUnit.MILLISECONDS.sleep(700);
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING": 
		case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_In START \0");
			break;
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name$Change_In START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo$Change_In START \0");
			break;
		case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
			if(!whichGraphicOnScreen.equalsIgnoreCase("LOF_TEAM_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam START \0");
			}
			break;
		case "ANIMATE-IN-LOF_TOP_SOLD": 
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys START \0");
			break;
		case "ANIMATE-IN-SQUAD-PLAYER":
			if(!whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category START \0");
				TimeUnit.MILLISECONDS.sleep(2200);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side2$"+rowHighlight +" START\0");
				for(int i=rowHighlight;i<=6;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side2$"+(i+1)+" START\0");
				}
			}else {
				TimeUnit.MILLISECONDS.sleep(1000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$"+rowHighlight +" START\0");
				for(int i=rowHighlight;i<=6;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$"+(i+1)+" START\0");
				}
			}
			break;
		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
			if(!whichGraphicOnScreen.equalsIgnoreCase("LOF_SQUAD_SIZE_CATEGORY_WISE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team START \0");
			}
			break;
			
		//FF	
		case "ANIMATE-IN-IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
			break;
		case "ANIMATE-IN-PLAYERPROFILE_FF":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
			}
			break;
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRTM START\0");
			break;
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuysImage$Change_In START\0");
			break;
			
		case "ANIMATE-IN-REMAINING_PURSE_ALL": 
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining START\0");
			break;
		case "ANIMATE-IN-SQUAD":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Squad START\0");
			}
			break;
		case "ANIMATE-IN-FF_ICONIC_PLAYERS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$IconPlayers START\0");
			break;
			
		}
	}
	public void cutBack(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException { 
		
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING":
		case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader SHOW 0\0");
			break;
		}
		
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING": 
		case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
			break;
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
			break;
		case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam SHOW 0\0");
			break;
		case "ANIMATE-IN-LOF_TOP_SOLD": 
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys SHOW 0\0");
			break;
		case "ANIMATE-IN-SQUAD-PLAYER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category SHOW 0\0");
			if(!whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$"+rowHighlight +" SHOW 1.000\0");
				for(int i=rowHighlight;i<=6;i++) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$"+(i+1)+" SHOW 1.000\0");
				}
			}
			break;
		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team SHOW 0\0");
			break;
			
		//FF
		case "ANIMATE-IN-IDENT": case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": 
		case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_ICONIC_PLAYERS": case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Header SHOW 0.0\0");
			switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
					break;
				case "ANIMATE-IN-PLAYERPROFILE_FF":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");
					break;
				case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-ZONE-PLAYER_STATS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Squad SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRTM SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRTM SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break; 
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuysImage SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_ICONIC_PLAYERS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break;
			}
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_SLOT") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE") 
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_RTM_REMAINING")
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-ZONEWISE_PLAYERS_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			}
			break;
		case "LOF_REMAINING_PURSE": 
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_SLOT") 
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_RTM_REMAINING")
					&& !whatToProcess.equalsIgnoreCase("ANIMATE-IN-ZONEWISE_PLAYERS_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			}
			break;
		case "LOF_SQUAD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam SHOW 0\0");
			break;
		case "LOF_TEAM_TOP_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TEAM_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuysTeam SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuysTeam SHOW 0\0");
			}
			break;
		case "LOF_TOP_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys SHOW 0\0");
			}
			break;
		case "SQUAD-PLAYER":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category$In SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category SHOW 0\0");
			}
			break;
		case "LOF_SQUAD_SIZE_CATEGORY_WISE":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Team SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Team SHOW 0\0");
			}
			break;
			
		//Flipper	
		case "FLIPPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Header SHOW 0.0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Text SHOW 0.0\0");
			break;
			
		//LT
		case "NAMESUPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$TopData SHOW 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData SHOW 0 \0");
			break;
		case "PLAYERPROFILE_LT":
			if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_LT_STATS")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData SHOW 0.0\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$TopData SHOW 0.0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird$BottomData SHOW 0.0\0");
			}
			break;
		
		
		//FF
		case "IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
			break;
		case "PLAYERPROFILE_FF":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
			}
			break;
		case "FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRTM SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRTM SHOW 0.0\0");
			break;
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
			break;
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$TopBuysImage SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuysImage SHOW 0.0\0");
			break;
			
		case "REMAINING_PURSE_ALL":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$PurseRemaining SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");
			break;
		case "SQUAD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Squad SHOW 0.0\0");
			}
			break;
		case "FF_ICONIC_PLAYERS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$IconPlayers SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$IconPlayers SHOW 0.0\0");
			break;
		}
		prevRowHighlight = rowHighlight;
	}

	public void populateIdent(PrintWriter print_writer,int which_side,String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header1$txt_Header1*GEOM*TEXT SET " 
				+ "PLAYER" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header2$txt_Header2*GEOM*TEXT SET " 
				+ "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info1$txt_Info*GEOM*TEXT SET " 
				+ "INDIAN STREET PREMIER LEAGUE SEASON 2" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info2$txt_Info*GEOM*TEXT SET " 
				+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		
	}
	
	public void populateCurrentBid(PrintWriter print_writer,int which_side) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$CurrentBid$ValueGrp$Value"
				+ "$Side1$txt_CurrentBid*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$CurrentBid$ValueGrp$Value"
				+ "$Side2$txt_CurrentBid*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + "\0");
	}
	public void BidChangeOn(PrintWriter print_writer, Auction session_curr_bid, int which_side) {
		if(data.isBid_Start_or_not()) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side1$CurrentBid$ValueGrp$Side" + which_side + "$txt_CurrentBid"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + "\0");
		}
	}
	public void PlayerSoldOrUnsold(PrintWriter print_writer, Auction auction, int playerId,int which_side) {
		for(int i=auction.getPlayers().size()-1; i >= 0; i--) {
			if(playerId == auction.getPlayers().get(i).getPlayerId()) {
				
				if(which_side == 2) {
					if(auction.getPlayersList().get(playerId - 1).getSurname() != null) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_FirstName*GEOM*TEXT SET " + 
								auction.getPlayersList().get(playerId - 1).getFirstname() + " \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_LastName*GEOM*TEXT SET " + 
								auction.getPlayersList().get(playerId - 1).getSurname() + " \0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_FirstName*GEOM*TEXT SET " + 
								"" + " \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_LastName*GEOM*TEXT SET " + 
								auction.getPlayersList().get(playerId - 1).getFirstname() + " \0");
					}
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_Role*GEOM*TEXT SET " + 
							auction.getPlayersList().get(playerId - 1).getRole().toUpperCase() + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_Category*GEOM*TEXT SET " + 
							auction.getPlayersList().get(playerId - 1).getCategory().toUpperCase() + " \0");
				}
				
				//Auction Result
				if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + 
							"$Select_DataType*FUNCTION*Omo*vis_con SET 2 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Sold$TextOut$txt_SoldValue"
							+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(auction.getPlayers().get(i).getSoldForPoints()) + "L" + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Sold$LogoOut$txt_TeamName"
							+ "*GEOM*TEXT SET " + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName1() + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Sold$LogoOut$img_TeamLogo"
							+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
//							+ "*TEXTURE*IMAGE SET " + text_path_1 + "GENERIC" + " \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
//							+ "*TEXTURE*IMAGE SET " + base_path_1 + "GENERIC" + " \0");
					
					if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Sold$TextOut$txt_Title"
								+ "*GEOM*TEXT SET " + "SOLD" + " \0");
					}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + "$Sold$TextOut$txt_Title"
								+ "*GEOM*TEXT SET " + "SOLD - RTM" + " \0");
					}
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + 
							"$Select_DataType*FUNCTION*Omo*vis_con SET 3 \0");
					
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
//							+ "*TEXTURE*IMAGE SET " + text_path_1 + "UNSOLD" + " \0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
//							+ "*TEXTURE*IMAGE SET " + base_path_1 + "UNSOLD" + " \0");
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}
				break;
			}
		}
	}
	
	public void populatePlayerProfile(boolean is_this_updating,PrintWriter print_writer,int which_side, int playerId,List<Statistics> stats, Auction auction, 
			Auction session_curr_bid,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(session_curr_bid.getCurrentPlayers() != null) {
			if(data.isData_on_screen() == true) {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, which_side);
					
					if(data.isPlayer_sold_or_unsold() == true) {
						if(data.getBid_result() != null && !data.getBid_result().isEmpty()) {
							if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$BasePriceHead START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$CurrentBid START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Sold START \0");
//								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold START \0");
							}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$BasePriceHead START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Right_Data_Move START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Unsold START \0");
//								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold START \0");
							}
						}
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData START \0");
						TimeUnit.MILLISECONDS.sleep(2000);
						PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Right_Data_Move SHOW 1.560 \0");
						}
					}
				}
			}else {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
				}
			}
		}
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Select*FUNCTION*Omo*vis_con SET 0\0");
//			populateProfileStats(print_writer, "STYLE",0, which_side, auction, auctionService);
			if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_FirstName*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getFirstname() + " \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_LastName*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getSurname() + " \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_FirstName*GEOM*TEXT SET " + 
						"" + " \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_LastName*GEOM*TEXT SET " + 
						auctionService.getAllPlayer().get(playerId - 1).getFirstname() + " \0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_Role*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + " \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1$txt_Category*GEOM*TEXT SET " + 
					auction.getPlayersList().get(playerId - 1).getCategory().toUpperCase() + " \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$GenericImage$img_Player*TEXTURE*IMAGE SET "+ photo_path + 
					auctionService.getAllPlayer().get(playerId - 1).getPhotoName().trim() + AuctionUtil.PNG_EXTENSION + "\0");
//			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$UnsoldImage$img_Player*TEXTURE*IMAGE SET "+ photo_path + 
//					auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$GenericImage$IconPlayer$Select_IconPlayer"
						+ "*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$UnsoldImage$IconPlayer$Select_IconPlayer"
						+ "*FUNCTION*Omo*vis_con SET 1\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$GenericImage$IconPlayer$Select_IconPlayer"
						+ "*FUNCTION*Omo*vis_con SET 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$UnsoldImage$IconPlayer$Select_IconPlayer"
						+ "*FUNCTION*Omo*vis_con SET 0\0");	
			}
			
			if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
				data.setPreviousBid(300000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$txt_BasePrice*GEOM*TEXT SET " + "3L" + " \0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("2000")) {
				data.setPreviousBid(2000000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$txt_BasePrice*GEOM*TEXT SET " + "20L" + " \0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("500")) {
				data.setPreviousBid(500000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$txt_BasePrice*GEOM*TEXT SET " + "5L" + " \0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("200")) {
				data.setPreviousBid(200000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$txt_BasePrice*GEOM*TEXT SET " + "2L" + " \0");
			}else {
				data.setPreviousBid((Integer.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice())*1000));
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$txt_BasePrice*GEOM*TEXT SET " + 
						(Integer.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice())/100) + "L" + " \0");	
			}
			
			if(data.isPlayer_sold_or_unsold() == false) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$CenterDataGrp$Side" + which_side + 
						"$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0");
				
//				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
//						+ "*TEXTURE*IMAGE SET " + text_path_1 + "GENERIC" + " \0");
//				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
//						+ "*TEXTURE*IMAGE SET " + base_path_1 + "GENERIC" + " \0");
			}
		}
	}
	
	public void populatePlayerProfileFF(PrintWriter print_writer,int which_side, int playerId, String show_stats, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$HeaderType1$txt_Header*GEOM*TEXT SET " + "AUCTION 2025" + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
					+ "*FUNCTION*Omo*vis_con SET 1\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$Icon$Select_Icon"
					+ "*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_FirstName*GEOM*TEXT SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Name$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Abhishek_Kumar_Dalhor"
				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName().trim() + AuctionUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$PlayerRole$txt_Role*GEOM*TEXT SET " + 
				auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getCategory().equalsIgnoreCase("U19")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET UNDER 19\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
				+ "Select_Value$txt_Title*GEOM*TEXT SET BASE PRICE\0");
		if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
					+ "Select_Value$txt_Value*GEOM*TEXT SET 3L"+"\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("600")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
					+ "Select_Value$txt_Value*GEOM*TEXT SET 6L"+"\0");
		}
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
				+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
		
		if(auction.getPlayers() != null) {
			for(Player auc : auction.getPlayers()) {
				if(auc.getPlayerId() == playerId) {
					if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
								+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value$txt_Title*GEOM*TEXT SET SOLD\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" L"+"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
								+ "img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
								+ "img_LogoBase*TEXTURE*IMAGE SET "+logo_base+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
					}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$"
								+ "ImageGrp$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value$txt_Title*GEOM*TEXT SET SOLD-RTM\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(auc.getSoldForPoints())+" L"+"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
								+ "img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$ImageGrp$Select_Logo$"
								+ "img_LogoBase*TEXTURE*IMAGE SET "+logo_base+auctionService.getTeams().get(auc.getTeamId() - 1).getTeamName4()+"\0");
					}else if(auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
								+ "Select_Value*FUNCTION*Omo*vis_con SET 1\0");
					}
					break;
				}
			}
		}
		
		
		if(show_stats.equalsIgnoreCase("with")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 1\0");
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats*ACTIVE SET 0\0");
		}
		
		if(auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam() != null) {
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatHead*GEOM*TEXT SET AGE\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatHead*GEOM*TEXT SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$1$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$3$txt_StatValue*GEOM*TEXT SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Stats$2$txt_StatValue*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getAge() + "\0");
		}
	}
	
	public void populateLofRemainingPurse(PrintWriter print_writer,String which_type,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int total = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "PURSE REMAINING" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 1\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		for(int i=0; i <= auction.getTeam().size()-1; i++) {
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + auction.getTeam().get(i).getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + auction.getTeam().get(i).getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(i).getTeamName4() + " \0");
			
			if(which_type.equalsIgnoreCase("logo")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$Select_DataType"
						+ "*FUNCTION*Omo*vis_con SET 1 \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$Select_DataType"
						+ "*FUNCTION*Omo*vis_con SET 0 \0");
			}
			
			if(auction.getPlayers() != null ) {
				for(int j=0; j <= auction.getPlayers().size()-1; j++) {
					if(auction.getPlayers().get(j).getTeamId() == auction.getTeam().get(i).getTeamId()) {
						total = total + auction.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			if((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + 
						"$txt_Value*GEOM*TEXT SET " + "-" + " \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.ConvertToLakh((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total)) + " \0");
			}
			total = 0;
		}
	}
	public void populateLofTopSold(PrintWriter print_writer,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		for(int i=1; i<=4; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + i + "*ACTIVE SET 0 \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "TOP BUYS" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 4) {
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$TeamNameGrp$txt_TeamNameName"
	    					+ "*GEOM*TEXT SET " + auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_Name"
	    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() + " \0");
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
	}
	public void populateLofTeamTopSold(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		for(int i=1; i<=4; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + i + "*ACTIVE SET 0 \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "TOP BUYS" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 4) {
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFull_name() + " \0");
	        		}
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
	}
	
	public void populateLofSquad(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		
		auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(auction.getTeam(), 
		        auction.getPlayers(), auction.getPlayersList() ));

		squad = auction.getTeamZoneList().stream() .filter(tm -> tm.getTeamId() == team_id)
		    .flatMap(tm -> tm.getPlayer().stream()).collect(Collectors.toList());
		
		Collections.sort(squad,new AuctionFunctions.PlayerStatsComparator());
		
		for(int i=1; i<=4; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + i + "*ACTIVE SET 0 \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int m=0; m<= squad.size() - 1; m++) {
			if(squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
	        	if(row < 4) {
	        		row = row + 1;
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		if(squad.get(m).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + squad.get(m).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " +  squad.get(m).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " +  squad.get(m).getFull_name() + " \0");
	        		}
	        		if(squad.get(m).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(squad.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path +  squad.get(m).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(squad.get(m).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(squad.get(m).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(squad.get(m).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(squad.get(m).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(squad.get(m).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(squad.get(m).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		player_number = player_number + row;
	}
	public void ChangeOnLofSquad(PrintWriter print_writer,int team_id,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row = 0;
		
		for(int i=1; i<=4; i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + i + "*ACTIVE SET 0 \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int m = player_number; m<= squad.size() - 1; m++) {
			if(squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || squad.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
	        	if(row < 4) {
	        		row = row + 1;
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		if(squad.get(m).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + squad.get(m).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + squad.get(m).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + squad.get(m).getFull_name() + " \0");
	        		}
	        		if(squad.get(m).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(squad.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path + squad.get(m).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(squad.get(m).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(squad.get(m).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(squad.get(m).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(squad.get(m).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(squad.get(m).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(squad.get(m).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(squad.get(m).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
					        				+ "*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		if(which_side == 1) {
			player_number = player_number + row;
		}
	}
	
	private void populateLofSquadSizeCategoryWise(PrintWriter print_writer,int team_id, int whichSide , Auction match,AuctionService auctionService, 
		String session_selected_broadcaster) {
		Auction session_auction = match;

		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
			session_auction.getPlayers(), session_auction.getPlayersList()));

		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1" + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0"+ "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header1*GEOM*TEXT SET T20 MUMBAI LEAGUE"+ "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header2*GEOM*TEXT SET AUCTION"+ "\0");

		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side"+whichSide+"$txt_SubHeader*GEOM*TEXT SET CATEGORY WISE"+ "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Title$txt_Category*GEOM*TEXT SET SQUAD SIZE"+ "\0");
		
		for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
		    PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 

		    if (teamZone.getTeamId() == team_id) {
		   	 // Team Name and Value 
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Highlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET " +
		        		teamZone.getPlayers() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET " +
		        		teamZone.getPlayers()+ "\0");
		        
		        rowHighlight = i;
		        
		        for(int k=1; k<=2; k++) {
		        	
		        	int j=0;
			    	Map<String, Integer> category = teamZone.getCategory();
			    	for (Map.Entry<String, Integer> entry : category.entrySet()) {
			    		j++;
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Category*GEOM*TEXT SET " + entry.getKey() + "\0");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET \0");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Value*GEOM*TEXT SET " + entry.getValue() + "\0");


			    	}
		        }
		    } else {
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Dehighlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Dehighlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET \0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET " +
		        		teamZone.getPlayers() + "\0");
		    }
		}
	}
	private void populateLofSquadSizeCategoryWiseOnly(PrintWriter print_writer,int team_id, int whichSide , Auction match,AuctionService auctionService, 
			String session_selected_broadcaster) {
			Auction session_auction = match;

			session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList()));
			
			Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == team_id).findAny().orElse(null);
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Select_Lines*FUNCTION*Grid*num_row SET 6\0");

			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0"+ "\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header1*GEOM*TEXT SET T20 MUMBAI LEAGUE"+ "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header2*GEOM*TEXT SET "+ team.getTeamName4()+"\0");

			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side"+whichSide+"$txt_SubHeader*GEOM*TEXT SET CATEGORY WISE"+ "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Title$txt_Category*GEOM*TEXT SET SQUAD SIZE"+ "\0");
			//Header Flag
			 print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$"
			 		+ "img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
			
			for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
			    PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 

			    if (teamZone.getTeamId() == team_id) {
			    	print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Title$txt_Value*GEOM*TEXT SET "
			    			+(teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()+ teamZone.getU19())+"\0");
			    	int j=0;
			    	Map<String, Integer> category = teamZone.getCategory();
			    	for (Map.Entry<String, Integer> entry : category.entrySet()) {
			    		j++;
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Row" + j + "$txt_Category*GEOM*TEXT SET "+entry.getKey()+"\0");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Row" + j + "$txt_Value*GEOM*TEXT SET "+entry.getValue()+"\0");
			    	}
			    	break;
			    }
			}
		}
	
	public void populateLofRemainingSlot(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SLOTS REMAINING" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 0\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
					+ (18-squadSize) + " \0");
			squadSize = 0;
		}
	}
	public void populateLofZoneWisePlayerSold(PrintWriter print_writer, int whichSide, String which_zone, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		if(which_zone.equalsIgnoreCase("U19")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "UNDER 19 PICKS" + " \0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + which_zone.toUpperCase() + 
					" PICKS" + " \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 0\0");
		
		
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			
			switch(which_zone) {
			case "EAST ZONE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getEastZone() + " \0");
				break;
			case "WEST ZONE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getWestZone() + " \0");
				break;
			case "NORTH ZONE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getNorthZone() + " \0");
				break;
			case "SOUTH ZONE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getSouthZone() + " \0");
				break;
			case "CENTRAL ZONE":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getCentralZone() + " \0");
				break;
			case "U19":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
						+ AuctionFunctions.PlayerCountZoneWise(auction.getTeam(), auction.getPlayers(), auction.getPlayersList()).get(which_zone).get((row-1)).getU19() + " \0");
				break;
			}
		}
	}
	
	public void populateLofSquadSize(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD SIZE" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 0\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
					+ squadSize + " \0");
			squadSize = 0;
		}
	}
	public void populateLofRTMRemaining(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int rtmUsed = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "T20 MUMBAI LEAGUE" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "RTM AVAILABLE" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 0\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_FirstName"
					+ "*GEOM*TEXT SET " + tm.getTeamName2() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$NameGrp$txt_LastName"
					+ "*GEOM*TEXT SET " + tm.getTeamName3() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$LogoGrp$MainBase"
					+ "*TEXTURE*IMAGE SET " + logo_path + tm.getTeamName4() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$Select_DataType"
					+ "*FUNCTION*Omo*vis_con SET 0 \0");
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId() && auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					rtmUsed++;
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$RemainingPurse$Row" + row + "$txt_Value*GEOM*TEXT SET " 
					+ (Integer.valueOf(tm.getTeamTotalRTM()) - rtmUsed) + " \0");
			rtmUsed = 0;
		}
	}
	
	public void populateFFRTMAndPurseRemaining(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int rtmUsed = 0;
		int squadSize = 0;
		int totalAmountSpent = 0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 4\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType4$txt_Header1*GEOM*TEXT SET INDIAN STREET PREMIER LEAGUE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType4$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType4$txt_SubHeader*GEOM*TEXT SET AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Title$txt_Title2*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Title$txt_Title3*GEOM*TEXT SET RTM REM.\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Title$txt_Title4*GEOM*TEXT SET PURSE REM.\0");
		
		int row = 0;
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$img_LogoBase*TEXTURE*IMAGE SET "+logo_base+tm.getTeamName4()+"\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$img_TeamLogo*TEXTURE*IMAGE SET "+logo_path+tm.getTeamName4()+"\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$txt_TeamFirstName*GEOM*TEXT SET "+tm.getTeamName2()+"\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$txt_TeamLastName*GEOM*TEXT SET "+tm.getTeamName3()+"\0");
			
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId() && auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					rtmUsed++;
				}
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
					totalAmountSpent += auc.getSoldForPoints();
				}
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$txt_SquadSize*GEOM*TEXT SET "+squadSize+"\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$txt_RTM*GEOM*TEXT SET "+(Integer.valueOf(tm.getTeamTotalRTM()) - rtmUsed)+"\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$PurseRTM$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent))+" L"+"\0");
			
			rtmUsed = 0;
			totalAmountSpent = 0;
			squadSize = 0;
		}
	}
	public void populateFFTopBuysAuction(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET ISPL AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0");
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 8) break; 
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Keeper"+" \0");
				}else {
					if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
							auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman"+" \0");
						}
						else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman_Lefthand"+" \0");
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerIcon"+" \0");
								break;
							}
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
							}
							else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
							}
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounderLeft"+" \0");
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	public void populateFFFiveTopBuysAuction(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET "
				+ "ISPL PLAYER AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8\0");
		
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 5) break; 
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+""+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$txt_Category*GEOM*TEXT SET "+"UNDER 19"+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$txt_Category*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
    					"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        				"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        				"$Data1$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_Player"
						+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp"
        				+ "$Select_Logo*FUNCTION*Omo*vis_con SET 1\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_TeamLogo"
						+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper"+" \0");
				}else {
					if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
							auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman"+" \0");
						}
						else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand"+" \0");
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler"+" \0");
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler"+" \0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon"+" \0");
								break;
							}
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder"+" \0");
							}
							else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounderLeft"+" \0");
							}
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row +
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounderLeft"+" \0");
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	public void populateFFTopFiveBuysTeam(PrintWriter print_writer, int whichSide, int team_id, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == team_id).findAny().orElse(null);
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET "
				+ team.getTeamName1() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8\0");
		
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 5) break; 
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$NameGrp$txt_FirstName*GEOM*TEXT SET "+""+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$NameGrp$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$txt_Category*GEOM*TEXT SET "+"UNDER 19"+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
	        				"$txt_Category*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
    					"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        				"$Data2$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp$img_Player"
						+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + "$ImageGrp"
        				+ "$Select_Logo*FUNCTION*Omo*vis_con SET 0\0");
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
        					"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper"+" \0");
				}else {
					if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
							auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman"+" \0");
						}
						else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand"+" \0");
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
									"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler"+" \0");
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler"+" \0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon"+" \0");
								break;
							}
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder"+" \0");
							}
							else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
										"$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounderLeft"+" \0");
							}
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row +
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuysImage$IconPlayer" + row + 
											"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounderLeft"+" \0");
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void populateFFTopBuysTeam(PrintWriter print_writer, int whichSide, int teamId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == teamId) {
					top_sold.add(plyr);
				}
			}
		}
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET "+team.getTeamName1()+"\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET CATEGORY\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0");
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 8) break; 
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
        		}
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET UNDER 19\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Keeper"+" \0");
				}else {
					if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
							auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman"+" \0");
						}
						else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman_Lefthand"+" \0");
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerIcon"+" \0");
								break;
							}
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
							}
							else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
							}
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounderLeft"+" \0");
								}
								break;
							}
						}
					}
				}
			}
		}
	}
	public void populateFFIconicPlayers(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET ICON PLAYERS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET ISPL PLAYER AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6\0");
		
		for(int i=1; i<=6; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Players$Player" + i + "*ACTIVE SET 0\0");
		}
		
		for(Player player : auction.getPlayersList()) {
			if(player.getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				row = row + 1;
				if(row <= 6) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + "*ACTIVE SET 1\0");
					if(player.getSurname() != null) {
	        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET "+player.getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET \0");
	        		}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + player.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$txt_Category*GEOM*TEXT SET " +
							player.getCategory().toUpperCase() + "\0");
					
					if(player.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
								"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
					}else {
						if(player.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || player.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
							}
							else if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
					 
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data1$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(Double.valueOf(player.getBasePrice()+"000"))+"L"+"\0");
					for(Player plyr : top_sold) {
						if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							if(player.getPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$txt_Value*GEOM*TEXT SET "
										+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_TeamLogo"
										+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_LogoBase"
										+ "*TEXTURE*IMAGE SET " + logo_base + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public void populateLTIconicPlayers(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$Header$NameGrp$txt_Title*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$Header$NameGrp$txt_Title*GEOM*TEXT SET " + "ICON PLAYERS" + "\0");
		
		for(Player player : auction.getPlayersList()) {
			if(player.getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				row = row + 1;
				if(row <= 6) {
					if(player.getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET "+player.getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET \0");
	        		}
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$ImageGrp$Select_TeamLogo"
							+ "*FUNCTION*Omo*vis_con SET 0\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + player.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$txt_Category*GEOM*TEXT SET " +
							player.getCategory().toUpperCase() + "\0");
					
					if(player.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
								"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
					}else {
						if(player.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || player.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
							}
							else if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
					 
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data1$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(Double.valueOf(player.getBasePrice()+"000")).replace(".00", "")+"L"+"\0");
					for(Player plyr : top_sold) {
						if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							if(player.getPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$ImageGrp$Select_TeamLogo"
										+ "*FUNCTION*Omo*vis_con SET 1\0");
								
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data2$txt_Value*GEOM*TEXT SET "
										+ AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data2$txt_TeamName*GEOM*TEXT SET "
										+ auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
								
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer" + row + "$ImageGrp$img_TeamLogo"
										+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4() + "\0");
								break;
							}
						}
					}
				}
			}
		}
	}
	public void populatePlayerProfileLT(PrintWriter print_writer,int which_side, int playerId, String show_stats,int st, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int team_id = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		System.out.println(top_sold.size());
		if(auctionService.getAllPlayer().get(playerId - 1).getSurname() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getSurname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_FirstName*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_LastName*GEOM*TEXT SET " 
					+ auctionService.getAllPlayer().get(playerId - 1).getFirstname() + "\0");
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$img_Text1$NameGrp$txt_Role*GEOM*TEXT SET " + 
				auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase() + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side" + which_side + "$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		if(show_stats.equalsIgnoreCase("category")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
		}
		else if(show_stats.equalsIgnoreCase("player")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					"T20 MUMBAI LEAGUE" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			for(Player plyr : top_sold) {
				System.out.println("plyr = " + plyr.getPlayerId() + " id = " + playerId);
				if(Integer.valueOf(plyr.getPlayerId()) == Integer.valueOf(playerId)) {
					System.out.println("plyrinside = " + plyr.getPlayerId() + " id = " + playerId);
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + "\0");
						
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE - UNSOLD" + "\0");
					}
				}
			}
		}
		else if(show_stats.equalsIgnoreCase("prevteam")) {
			team_id = auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam();
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
					"ISPL SEASON 1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
					auction.getTeam().get(team_id-1).getTeamName1() + "\0");
			
			String price = auctionService.getAllPlayer().get(playerId - 1).getLastYearPrice() + "000";
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(Double.valueOf(price)) + " L" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("stats")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == st) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}
	}
	public void ChangeOnLTStats(PrintWriter print_writer,int which_side, int playerId, String show_stats,int sta, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int team_id = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		if(show_stats.equalsIgnoreCase("category")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getCategory().toUpperCase() + "\0");
		}
		else if(show_stats.equalsIgnoreCase("player")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
					"T20 MUMBAI LEAGUE" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			for(Player plyr : top_sold) {
				if(plyr.getPlayerId() == playerId) {
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + "\0");
						
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"T20 MUMBAI LEAGUE - UNSOLD" + "\0");
					}
				}
//				break;
			}
		}
		else if(show_stats.equalsIgnoreCase("prevteam")) {
			team_id = auctionService.getAllPlayer().get(playerId - 1).getLastYearTeam();
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
					"ISPL SEASON 1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
					auction.getTeam().get(team_id-1).getTeamName1() + "\0");
			
			String price = auctionService.getAllPlayer().get(playerId - 1).getLastYearPrice() + "000";
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(Double.valueOf(price)) + " L" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("stats")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			for(Statistics stat : stats) {
				if(stat.getPlayer_id() == playerId && stat.getStats_type_id() == sta) {
					switch (auctionService.getAllPlayer().get(playerId - 1).getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getStrikeRate().equalsIgnoreCase("0") ? "-" : stat.getStrikeRate()) + "\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getEconomy().equalsIgnoreCase("0") ? "-" : stat.getEconomy()) + "\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$1$txt_Text*GEOM*TEXT SET " 
								+ (stat.getMatches().equalsIgnoreCase("0") ? "-" : stat.getMatches()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$2$txt_Text*GEOM*TEXT SET " 
								+ (stat.getRuns().equalsIgnoreCase("0") ? "-" : stat.getRuns()) + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Stats$3$txt_Text*GEOM*TEXT SET " 
								+ (stat.getWickets().equalsIgnoreCase("0") ? "-" : stat.getWickets()) + "\0");
					}
					break;
				}
			}
		}
	}
	
	public void populateNameSuper(PrintWriter print_writer, int whichSide, int nameSuperId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
		for(NameSuper ns : auctionService.getNameSupers()) {
			if(ns.getNamesuperId() == nameSuperId) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$txt_Role*GEOM*TEXT SET \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
				
				if(ns.getSurname() != null) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$txt_FirstName*GEOM*TEXT SET "+ns.getFirstname()+"\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$txt_LastName*GEOM*TEXT SET "+ns.getSurname()+"\0");
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$txt_FirstName*GEOM*TEXT SET "+ns.getFirstname()+"\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$NameGrp$Side"+whichSide+"$txt_LastName*GEOM*TEXT SET \0");
				}
				if(ns.getSubLine() != null) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET "+ns.getSubLine()+"\0");
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET \0");
				}
			}
		}
	}
	public void populateFlipper(PrintWriter print_writer, int whichSide, int FlipperId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		for(Flipper flipper : auctionService.getFlipper()) {
			if(flipper.getFlipperId() == FlipperId) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Header$Side"+whichSide+"$TextGrp$HeaderAll$NameGrp$txt_Title"
						+ "*GEOM*TEXT SET " + flipper.getHeader() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Text$Side"+whichSide+"$TextGrp$txt_Text*GEOM*TEXT SET "+flipper.getSubLine()+"\0");
			}
		}
	}
	public void populateTopSoldTeam(PrintWriter print_writer,String viz_scene,int team_id , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					top_sold.add(plyr);
				}
			}
		}
		
		for(int i = 1; i <= 5; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TopText*GEOM*TEXT SET "+ "TOP BUYS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auction.getTeam().get(team_id-1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auction.getTeam().get(team_id-1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$AllBase$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase("SOLD")) {
				row = row + 1;
	        	if(row <= 5) {
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1 \0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerFirstName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerLastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + "\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerFirstName*GEOM*TEXT SET "+ "" + "\0");
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerLastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        		}
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints())  + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
	        		
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.200 \0");
	}
	public void populateTopSold(PrintWriter print_writer,String viz_scene, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ "TOP" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ "BUYS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		for(int i=1; i<=10; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0 \0");
		}
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase("SOLD")) {
				row = row + 1;
	        	if(row <= 10) {
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1 \0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_TeamName*GEOM*TEXT SET "+ auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName1() + "\0");
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() != null) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() + "\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PlayerName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + "\0");
	        		}
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh(top_sold.get(m).getSoldForPoints())  + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Logo*TEXTURE*IMAGE SET "+ logo_path + auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + "\0");
	        		
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
	        	}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.700 \0");
	}
	public void populateRemainingPurse(PrintWriter print_writer, int which_side, Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int total = 0;
		int row = 0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 4\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType4$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + "INDIAN STREET PREMIER LEAGUE" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType4$Header$txt_Header2"
				+ "*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType4$SubHeader$txt_SubHeader"
				+ "*GEOM*TEXT SET " + "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title2*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title3*GEOM*TEXT SET PURSE REMAINING\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamFirstName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamLastName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName3() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(i).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_LogoBase*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(i).getTeamName4() + "\0");
			
			if(match.getPlayers() != null ) {
				for(int j=0; j <= match.getPlayers().size()-1; j++) {
					if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
						row = row + 1;
						total = total + match.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$txt_SquadSize*GEOM*TEXT SET " + row + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1) + "$Value$txt_Value"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total)) + " L" + "\0");
			
			if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}else if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$txt_Value*GEOM*TEXT SET " + "-" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}
			row = 0;
			total = 0;
		}
	}
	public void populateRemainingPurseSingle(PrintWriter print_writer,String viz_scene,int team_id , Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		
		
		for(int j=0; j <= match.getPlayers().size()-1; j++) {
			if(match.getPlayers().get(j).getTeamId() == team_id) {
				row = row + match.getPlayers().get(j).getSoldForPoints();
			}
		}
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgPlayerImage " + logo_path + 
				match.getTeam().get(team_id-1).getTeamName4() + AuctionUtil.PNG_EXTENSION + ";");
		print_writer.println("LAYER" + current_layer + "*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET tPlayerFirstName " + 
				(Integer.valueOf(match.getTeam().get(team_id-1).getTeamTotalPurse()) - row) + ";");
		row = 0;
		
		
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW ON;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In STOP;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out STOP;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In SHOW 71.0;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT_PATH C:/Temp/Preview.png;");
		print_writer.println("LAYER1*EVEREST*GLOBAL SNAPSHOT 1920 1080;");
		TimeUnit.SECONDS.sleep(1);
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
		print_writer.println("LAYER" + current_layer + "*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
		print_writer.println("LAYER1*EVEREST*GLOBAL PREVIEW OFF;");
	}
	public void populateSquad(PrintWriter print_writer,int team_id, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataInZone(match,team_id);
		
		Auction session_auction = match;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList()));
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName3() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
				+ "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		
		for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
			PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 
		    if (teamZone.getTeamId() == team_id) {
		        
		     // Team Total Value 
		        print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$txt_SquadSize"
		        	+ "*GEOM*TEXT SET " + (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() 
		        		+ teamZone.getCentralZone()+ teamZone.getU19()) + "\0");

		        for(int k=1; k<=2; k++) {
		        	for (int j = 1; j <= 7; j++) {
			            // Zone names
			            String zoneName = (j == 1 ? "NZ" :
			                    j == 2 ? "CZ" :
			                    j == 3 ? "EZ" :
			                    j == 4 ? "WZ" :
			                    j == 5 ? "SZ" :
			                    j == 6 ? "U19" : "");
			            
			            print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Zone*GEOM*TEXT SET " + zoneName + "\0");
			            print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Value*GEOM*TEXT SET " + "" + "\0");

			            // Zone values
			            String zoneValue = (j == 1 ? String.valueOf(teamZone.getNorthZone()) :
			                    j == 2 ? String.valueOf(teamZone.getCentralZone()) :
			                    j == 3 ? String.valueOf(teamZone.getEastZone()) :
			                    j == 4 ? String.valueOf(teamZone.getWestZone()) :
			                    j == 5 ? String.valueOf(teamZone.getSouthZone()) :
			                    j == 6 ? String.valueOf(teamZone.getU19()) : "");
			            
			            print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SquadData$Category$" 
			            		+ j + "$txt_Value*GEOM*TEXT SET " + zoneValue + "\0");
			        }
		        }
		    } 
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");
		
		for(int k=0;k<=data_str.size()-1;k++) {
			row = row + 1;
			if(data_str.get(k).equalsIgnoreCase("NZ") || data_str.get(k).equalsIgnoreCase("SZ") || data_str.get(k).equalsIgnoreCase("CZ") || 
					data_str.get(k).equalsIgnoreCase("EZ") || data_str.get(k).equalsIgnoreCase("WZ") || data_str.get(k).equalsIgnoreCase("U19") ||
					data_str.get(k).equalsIgnoreCase("ZONE")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
				
				switch(data_str.get(k)) {
				case "NZ":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "NORTH" + "\0");
					break;
				case "CZ":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "CENTRAL" + "\0");
					break;
				case "EZ":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "EAST" + "\0");
					break;
				case "WZ":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "WEST" + "\0");
					break;
				case "SZ":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SOUTH" + "\0");
					break;
				case "U19":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + "U19" + "\0");
					break;
				case "ZONE":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$NoData$IconGrpGrp$img_Icon*TEXTURE*IMAGE SET " + icon_path + row + "\0");
					break;
				}
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				for(Player plyr : match.getPlayersList()) {
					if(plyr.getPlayerId() == Integer.valueOf(data_str.get(k))) {
						if(plyr.getSurname() != null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_FirstName*GEOM*TEXT SET " + "" + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
									+ "txt_LastName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
								+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
						
						if(plyr.getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET UNDER 19\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
									+ "*GEOM*TEXT SET " + plyr.getCategory().toUpperCase() + "\0");
						}
						if(plyr.getIconic().equalsIgnoreCase("YES")){
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
									+ "*ACTIVE SET 1\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
									+ "*ACTIVE SET 0\0");
						}
						if(plyr.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
						}else {
							if(plyr.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || plyr.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
								if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
								}
								else if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
								}
							}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
								if(plyr.getBowlerStyle() == null) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
								}else {
									switch(plyr.getBowlerStyle().toUpperCase()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
												"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
												"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
										break;
									}
								}
							}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
								if(plyr.getBowlerStyle() == null) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
								}else {
									switch(plyr.getBowlerStyle().toUpperCase()) {
									case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
												"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
										break;
									case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
												"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerAllrounder" + "\0");
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void populateZoneSquad(PrintWriter print_writer,String ZoneName, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + "ISPL AUCTION 2025" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET  \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$SubHeader"
				+ "$txt_SubHeader*GEOM*TEXT SET " + ZoneName.toUpperCase() + " - REMAINING PLAYERS" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");

		for(int i=1;i<=16;i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Blank$NameGrp$txt_LastName*GEOM*TEXT SET " + "" + "\0");
		}
	    for(int k = current_index; k<= squad.size()-1 ;k++) {
		   Player plyr = squad.get(k);
				row = row + 1;
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				
				if(plyr.getSurname() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
						+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
						+ "*GEOM*TEXT SET " + "" + "\0");
				
				if(plyr.getIconic().equalsIgnoreCase("YES")){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
							+ "*ACTIVE SET 1\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
							+ "*ACTIVE SET 0\0");
				}
				if(plyr.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
				}else {
					if(plyr.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || plyr.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
						}
						else if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
						}
					}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(plyr.getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
						}else {
							switch(plyr.getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
								break;
							}
						}
					}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(plyr.getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
						}else {
							switch(plyr.getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerAllrounder" + "\0");
								break;
							}
						}
					}
				}
				if(row == 16) {
					break;
				}
			}
		if(which_side == 1) {
			current_index =(current_index + 16);
		}
	}

	public void populateRTMAvailable(PrintWriter print_writer, int whichSide, Auction current_bid, AuctionService auctionService) {
		if(!which_graphics_onscreen.isEmpty()) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$RTM$TextGrp$Side"+whichSide+"$txt_Bottom*GEOM*TEXT SET AVAILABLE\0");
		}else {
			Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == current_bid.getCurrentPlayers().getPlayerId()).findAny().orElse(null);
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$RTM$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + auctionService.getTeams().get(player.getLastYearTeam()-1).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$RTM$TextGrp$Side"+whichSide+"$txt_Bottom*GEOM*TEXT SET AVAILABLE\0");
		}
	}
	public void populateRTMEnabled(PrintWriter print_writer, int whichSide) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$RTM$TextGrp$Side"+whichSide+"$txt_Bottom*GEOM*TEXT SET ENABLED\0");
	}
	public void populateGooglyPower(PrintWriter print_writer, int teamId, AuctionService auctionService) {
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$Googly_Power$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
	}
	public void populateProfileStats(PrintWriter print_writer, String whichType,int stat, int whichSide, Auction auction, AuctionService auctionService) {
		
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		String BowlStyle = "";
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Select*FUNCTION*Omo*vis_con SET 1\0");
		
		switch (whichType.toUpperCase()) {
		case "FREETEXT":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$FreeText$txt_Title*GEOM*TEXT SET " + 
					"FRANCHISE PICK" + "\0");
			break;
		case "CATEGORY":
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET CATEGORY\0");
			if(player.getCategory().equalsIgnoreCase("U19")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET UNDER 19\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + player.getCategory().toUpperCase() + "\0");
			}
			break;

		case "STYLE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
			if(player.getAge() != null) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Age*GEOM*TEXT SET AGE " + player.getAge() + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Age*GEOM*TEXT SET \0");
			}
			switch(player.getRole().toUpperCase()) {
			case "BOWLER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BOWLING STYLE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BOWLING STYLE\0");
				if(player.getBowlerStyle() != null) {
					if(player.getBowlerStyle().charAt(0) == 'L') {
						BowlStyle = "Left-Arm" ;
					}else {
						BowlStyle = "Right-Arm" ;
					}
					if(player.getBowlerStyle() == "WSL") {
						BowlStyle = "Left-Arm Wrist Spin";
					}else if(player.getBowlerStyle() == "WSR"){
						BowlStyle = "Right-Arm Wrist Spin";
					}
					switch (player.getBowlerStyle().substring(1).trim()) {
					case "":
						BowlStyle = BowlStyle + " Bowler";
						break;
					case "F":
						BowlStyle = BowlStyle + " Fast";
						break;
					case "FM":
						BowlStyle = BowlStyle + " Fast-Medium";
						break;
					case "MF":
						BowlStyle = BowlStyle + " Medium-Fast";
						break;
					case "M":
						BowlStyle = BowlStyle + " Medium";
						break;
					case "SM":
						BowlStyle = BowlStyle + " Slow-Medium";
						break;
					case "OB":
						BowlStyle = BowlStyle + " Off-Break";
						break;
					case "LB": case "LG":
						BowlStyle = BowlStyle + " Leg-Break";
						break;
					case "CH":
						BowlStyle = BowlStyle + " Chinaman";
						break;
					case "SO":
						BowlStyle = BowlStyle + " Orthodox";
						break;
					case "SL":
						BowlStyle = "Slow Left-Arm";
						break;
					
					}
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET " + BowlStyle.toUpperCase() + "\0");
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET FAST BOWLER\0");
				}
				break;
			case "BATSMAN":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				
				if(player.getBatsmanStyle() != null) {
					if(player.getBatsmanStyle().equalsIgnoreCase("RHB")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET RIGHT HANDED BATTER\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET LEFT HANDED BATTER\0");
					}
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET RIGHT HANDED BATTER\0");
				}
				break;
			case "ALL-ROUNDER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BAT & BOWL STYLE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BAT & BOWL STYLE\0");
				BowlStyle = "";
				if(player.getBowlerStyle() != null) {
					if(player.getBowlerStyle().contains("FM") || player.getBowlerStyle().contains("MF")
							 || player.getBowlerStyle().contains("SM")  || player.getBowlerStyle().contains("RF")
							 || player.getBowlerStyle().contains("LF") || player.getBowlerStyle().contains("LM") ||
							 player.getBowlerStyle().contains("F") || player.getBowlerStyle().contains("M")) {
						BowlStyle = "SEAM";
					}
					
					if(player.getBowlerStyle().contains("SL") || player.getBowlerStyle().contains("SO") 
							 || player.getBowlerStyle().contains("CH")  || player.getBowlerStyle().contains("LB")
							 || player.getBowlerStyle().contains("LG")  || player.getBowlerStyle().contains("OB")
							 || player.getBowlerStyle().contains("WSL") || player.getBowlerStyle().contains("WSR")) {
						BowlStyle = "SPIN";
					}
				}else {
					BowlStyle = "SEAM";
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET BAT "+(BowlStyle.equalsIgnoreCase("") ? "" : "& "+BowlStyle)+"\0");
				break;
			case "BAT/KEEPER": case "WICKET-KEEPER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				if(player.getBatsmanStyle() != null) {
					if(player.getBatsmanStyle().equalsIgnoreCase("RHB")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET RIGHT HANDED BATTER\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET LEFT HANDED BATTER\0");
					}
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET RIGHT HANDED BATTER\0");
				}
				break;
			}
			
			break;
		case "PREVTEAM":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET ISPL SEASON 1\0");
			if(auctionService.getTeams().get(player.getLastYearTeam() - 1) != null) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + 
						auctionService.getTeams().get(player.getLastYearTeam() - 1).getTeamName1() + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + "-" + "\0");
			}
			
			break;
		case "STATS":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			for(Statistics stats : auctionService.getAllStats()) {
				if(stats.getPlayer_id() == player.getPlayerId() && stats.getStats_type_id() == stat) {
					switch (player.getRole().toUpperCase()) {
					case "BATSMAN": case "BAT/KEEPER": case "WICKET-KEEPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Text*GEOM*TEXT SET "+(stats.getMatches().equalsIgnoreCase("0") ? "-" : stats.getMatches())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Text*GEOM*TEXT SET "+(stats.getRuns().equalsIgnoreCase("0") ? "-" : stats.getRuns())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Title*GEOM*TEXT SET STRIKE RATE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Text*GEOM*TEXT SET "+(stats.getStrikeRate().equalsIgnoreCase("0") ? "-" : stats.getStrikeRate())+"\0");
						break;

					case "BOWLER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Text*GEOM*TEXT SET "+(stats.getMatches().equalsIgnoreCase("0") ? "-" : stats.getMatches())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Text*GEOM*TEXT SET "+(stats.getWickets().equalsIgnoreCase("0") ? "-" : stats.getWickets())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Title*GEOM*TEXT SET ECONOMY\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Text*GEOM*TEXT SET "+(stats.getEconomy().equalsIgnoreCase("0") ? "-" : stats.getEconomy())+"\0");
						break;
					case "ALL-ROUNDER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Title*GEOM*TEXT SET MATCHES\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$1$txt_Text*GEOM*TEXT SET "+(stats.getMatches().equalsIgnoreCase("0") ? "-" : stats.getMatches())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Title*GEOM*TEXT SET RUNS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$2$txt_Text*GEOM*TEXT SET "+(stats.getRuns().equalsIgnoreCase("0") ? "-" : stats.getRuns())+"\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Title*GEOM*TEXT SET WICKETS\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Stats$3$txt_Text*GEOM*TEXT SET "+(stats.getWickets().equalsIgnoreCase("0") ? "-" : stats.getWickets())+"\0");
					}
					break;
				}
			}
			break;
		}
	}
	public void populateTeamCurrentBid(PrintWriter print_writer, int team_id, int whichSide, Auction auction, Auction current_bid, AuctionService auctionService) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET CURRENT BID\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + 
				auction.getTeam().get(team_id -1).getTeamName1() + "\0");
	}
	
	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException
	{
		
		switch(whichGraphic) {
		case "FFPLAYERPROFILE":	
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
			break;
		
		case "RESET":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reset START \0");
			break;
		
		}	
	}	
	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");	
	}
	
	public void processPreview(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			if(data.isData_on_screen() && !whatToProcess.equalsIgnoreCase("POPULATE-FF-PLAYERPROFILE")) {
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500 ";
			}
			if(isProfileStatsOnScreen) {
				previewCommand =previewCommand + "anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760 ";
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF-PLAYERPROFILE":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500";
				break;
			case "POPULATE-PROFILE_STATS": case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = previewCommand + "anim_RTM$In_Out 0.500 anim_RTM$In_Out$Essentials 0.500 anim_RTM$In_Out$Essentials$In 0.500 anim_RTM$In_Out$Text$In 0.500";
				break;
			case "POPULATE-GOOGLY_POWER":
				previewCommand = previewCommand + "anim_Googly$In_Out 0.500 anim_Googly$In_Out$In 0.500";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": 
			case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": 
			case "POPULATE-LOF_SQUAD": case "POPULATE-ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "anim_LOF$In_Out 2.000 anim_LOF$In_Out$Essentials 2.000 anim_LOF$In_Out$Essentials$In 1.100 "
						+ "anim_LOF$In_Out$Header 2.000 anim_LOF$In_Out$Header$In 1.200 anim_LOF$In_Out$SubHeader 2.000 anim_LOF$In_Out$SubHeader$In 1.300 anim_LOF$In_Out$Main 2.000 ";
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING":
				case "POPULATE-ZONEWISE_PLAYERS_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$RemainingPurse 2.000 anim_LOF$In_Out$Main$RemainingPurse$In 2.000 "
							+ "anim_LOF$In_Out$Main$Name 2.000 anim_LOF$In_Out$Main$Name$In 1.100 anim_LOF$In_Out$Main$Logo 2.000 anim_LOF$In_Out$Main$Logo$In 1.100";
					break;
				case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-LOF_SQUAD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$TopBuysTeam 2.000 anim_LOF$In_Out$Main$TopBuysTeam$In 1.700";
					break;
				case "POPULATE-LOF_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$TopBuys 2.000 anim_LOF$In_Out$Main$TopBuys$In 1.700";
					break;
				case "POPULATE-SQUAD-PLAYER":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Category 2.000 anim_LOF$In_Out$Main$SquadSize_Category$In 1.900 CtegoryHighlight$Side1$"+rowHighlight +" 1.200";
					for(int i = rowHighlight; i<=5; i++) {
						previewCommand = previewCommand + " MoveForCatHighlight$Side1$"+(i+1) +" 1.200";
					}
					break;
				case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Team 2.000 anim_LOF$In_Out$Main$SquadSize_Team$In 1.880";
					break;
				}
				break;
			}
		}else {
			if(data.isData_on_screen() && !whatToProcess.equalsIgnoreCase("POPULATE-FF-PLAYERPROFILE")) {
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel 2.500 anim_ScoreBug$In_Out$RightData$Gavel$in 2.500 ";
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING":
			case "LOF_SQUAD_SIZE_CATEGORY_WISE": case "LOF_SQUAD": case "ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$Header$Change_Out 0.500 Change_LOF$Header$Change_In 1.000 Change_LOF$SubHeader$Change_Out 0.500 "
						+ "Change_LOF$SubHeader$Change_In 1.000 ";
				break;
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_REMAINING_SLOT": case "LOF_SQUAD_SIZE": case "LOF_RTM_REMAINING": case "ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_Out 0.860 Change_LOF$Name$Change_Out 0.600 Change_LOF$Logo$Change_Out 0.600 ";
				break;
			case "LOF_TEAM_TOP_SOLD": case "LOF_SQUAD":
				previewCommand = previewCommand + "Change_LOF$TopBuysTeam$Change_Out 0.680 ";
				break;
			case "LOF_TOP_SOLD": 
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_Out 0.680 ";
				break;
			case "SQUAD-PLAYER":
				if(!whatToProcess.equalsIgnoreCase("POPULATE-SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_Out 0.800";
				}
				break;
			case "LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_Out 0.820 ";
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS": case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand+ "Change$Stats$Change_Out 1.000 Change$Stats$Change_In 1.300";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_REMAINING_SLOT": case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING":
			case "POPULATE-ZONEWISE_PLAYERS_SOLD":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_In 1.600 Change_LOF$Name$Change_In 1.100 Change_LOF$Logo$Change_In 1.100";
				break;
			case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-LOF_SQUAD": case "POPULATE-LOF_SQUAD_REMAIN":
				previewCommand = previewCommand + "Change_LOF$TopBuysTeam$Change_In 1.500";
				break;
			case "POPULATE-LOF_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_In 1.500";
				break;
			case "POPULATE-SQUAD-PLAYER":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_In 1.700";
				}
				break;
			case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE":
				previewCommand = previewCommand + "Change_LOF$SquadSize_Team$Change_In 1.680 ";
				break;
			}	
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
		
	}
	public void processPreviewFullFrames(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT": case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-FF_RTM_AND_PURSE_REMAINING": case "POPULATE-FF_TOP_BUYS_AUCTION": 
			case "POPULATE-FF_TOP_BUY_TEAM": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SQUAD": case "POPULATE-FF_ICONIC_PLAYERS":
			case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				previewCommand = "anim_Fullframe$In_Out 2.480 anim_Fullframe$In_Out$Essentials 2.480 anim_Fullframe$In_Out$Essentials$In 1.300 ";
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-IDENT":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Main$MatchId 2.480 anim_Fullframe$In_Out$Main$MatchId$In 2.340";
					break;
				case "POPULATE-PLAYERPROFILE_FF":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Profile 2.480 anim_Fullframe$In_Out$Main$Profile$In 2.180";
					break;
				case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$PurseRTM 2.480 anim_Fullframe$In_Out$Main$PurseRTM$In 2.100";
					break;
				case "POPULATE-FF_TOP_BUYS_AUCTION": case "POPULATE-FF_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$TopBuys 2.480 anim_Fullframe$In_Out$Main$TopBuys$In 2.300";
					break;
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$TopBuysImage 2.480 anim_Fullframe$In_Out$Main$TopBuysImage$In 1.880";
					break;
				case "POPULATE-REMAINING_PURSE_ALL":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$PurseRemaining 2.480 anim_Fullframe$In_Out$Main$PurseRemaining$In 2.100";
					break;
				case "POPULATE-SQUAD": case "POPULATE-ZONE_PLAYERS_STATS":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Squad 2.480 anim_Fullframe$In_Out$Main$Squad$In 2.000";
					break;
				case "POPULATE-FF_ICONIC_PLAYERS":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$IconPlayers 2.480 anim_Fullframe$In_Out$Main$IconPlayers$In 2.000";
					break;
				}
				break;
			}
		}else {
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$Ident 2.340 Change$Ident$Change_Out 0.700 Change$Ident$Change_In 2.340 ";
				break;
			case "PLAYERPROFILE_FF":
				previewCommand = "anim_Fullframe$In_Out$Header 0.0 anim_Fullframe$In_Out$Header$In 0.0 Change$Profile 2.180 "
						+ "Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				break;
			case "FF_RTM_AND_PURSE_REMAINING":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$PurseRTM 2.100 Change$PurseRTM$Change_Out 0.740 Change$PurseRTM$Change_In 2.100 ";
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$TopBuys 2.300 Change$TopBuys$Change_Out 0.820 ";
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$Header 2.600 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$TopBuysImage 1.880 Change$TopBuysImage$Change_Out 0.920 ";
				break;
			case "REMAINING_PURSE_ALL":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$PurseRemaining 2.100 Change$PurseRemaining$Change_Out 0.740 Change$PurseRemaining$Change_In 2.100 ";
				break;
			case "SQUAD": case "ZONE-PLAYER_STATS":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$Squad 2.000 Change$Squad$Change_Out 0.560 Change$Squad$Change_In 2.000 ";
				break;
			case "FF_ICONIC_PLAYERS":
				previewCommand = "Change$Header 2.660 Change$Header$Change_Out 0.640 Change$Header$Change_In 2.660 "
						+ "Change$IconPlayers 2.000 Change$IconPlayers$Change_Out 0.560 Change$IconPlayers$Change_In 2.000 ";
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT":
				previewCommand = previewCommand + "Change$Ident 2.340 Change$Ident$Change_Out 0.700 Change$Ident$Change_In 2.340 ";
				break;
			case "POPULATE-PLAYERPROFILE_FF":
				if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
					previewCommand = previewCommand + "Change$Profile 2.180 Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				}
				break;
			case "POPULATE-REMAINING_PURSE_ALL":
				previewCommand = previewCommand + "Change$PurseRemaining 2.100 Change$PurseRemaining$Change_Out 0.740 Change$PurseRemaining$Change_In 2.100 ";
				break;
			case "POPULATE-SQUAD": case "POPULATE-ZONE_PLAYERS_STATS":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD") && !which_graphics_onscreen.equalsIgnoreCase("ZONE-PLAYER_STATS")) {
					previewCommand = previewCommand + "Change$Squad 2.000 Change$Squad$Change_Out 0.560 Change$Squad$Change_In 2.000 ";
				}
				break;
			case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
				previewCommand = previewCommand + "Change$PurseRTM 2.100 Change$PurseRTM$Change_Out 0.740 Change$PurseRTM$Change_In 2.100";
				break;
			case "POPULATE-FF_TOP_BUYS_AUCTION": case "POPULATE-FF_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$TopBuys$Change_In 2.300";
				break;
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				previewCommand = previewCommand + "Change$TopBuysImage$Change_In 1.880";
				break;
			case "POPULATE-FF_ICONIC_PLAYERS":
				previewCommand = previewCommand + "Change$IconPlayers 2.000 Change$IconPlayers$Change_Out 0.560 Change$IconPlayers$Change_In 2.000 ";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/FullFrames " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
	}
	public void processPreviewLowerThirds(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-LT_ICONIC_PLAYERS":
				previewCommand = "anim_IconLowerThird$In_Out 1.800 anim_IconLowerThird$In_Out$In 1.800";
				break;
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "anim_LowerThird$In_Out$Essentials 1.800 anim_LowerThird$In_Out$$Essentials$In 1.400 anim_LowerThird$In_Out$TopData 1.800 "
						+ "anim_LowerThird$In_Out$$TopData$In 1.800 anim_LowerThird$In_Out$BottomData 1.800 anim_LowerThird$In_Out$$BottomData$In 1.760";
				break;
			case "POPULATE-L3-FLIPPER":
				previewCommand = "anim_Flipper$In_Out$Essentials 1.800 anim_Flipper$In_Out$$Essentials$In 1.800 anim_Flipper$In_Out$Header 1.800 "
						+ "anim_Flipper$In_Out$$Header$In 1.740 anim_Flipper$In_Out$Text 1.800 anim_Flipper$In_Out$$Text$In 1.360";
				break;
			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "Change_LowerThird$TopData 1.800 Change_LowerThird$TopData$Change_Out 1.000 Change_LowerThird$TopData$Change_In 1.800 "
						+ "Change_LowerThird$BottomData 1.300 Change_LowerThird$BottomData$Change_Out 1.000 Change_LowerThird$BottomData$Change_In 1.800";
				break;
			case "POPULATE-PLAYERPROFILE_LT_STATS":
				previewCommand = "Change_LowerThird$BottomData 1.300 Change_LowerThird$BottomData$Change_Out 1.000 Change_LowerThird$BottomData$Change_In 1.800";
				break;
			case "POPULATE-L3-FLIPPER":
				previewCommand = "Change_Flipper$Header 1.740 Change_Flipper$Header$Change_Out 0.720 Change_Flipper$Header$Change_In 1.740 "
						+ "Change_Flipper$Text 1.000 Change_Flipper$Text$Change_Out 0.700 Change_Flipper$Text$Change_In 1.000";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	
}