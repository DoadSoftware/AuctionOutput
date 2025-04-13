package com.auction.broadcaster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.PlayerCount;
import com.auction.model.Statistics;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
import com.auction.model.Flipper;
import com.auction.model.NameSuper;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;

public class UTT_VIZ extends Scene{

	private String status, side2ValueToProcess = "";
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "UTT_VIZ";
	public Data data = new Data();
	public String which_graphics_onscreen = "",which_data="",which_team="", rtm_googly_on_screen = "";
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1, rowHighlight = 1,prevRowHighlight = 1, rtmGooglyWhichSide = 1;
	public int player_id = 0,team_id=0,player_number=0;
	public int zoneSize = 0, current_index = 0;
	public Statistics Statistics ;
	List<Player> squad = new ArrayList<Player>();
	List<String> data_str = new ArrayList<String>();
	List<PlayerCount> player_count = new ArrayList<PlayerCount>();

	private String base_path = "IMAGE*/Default/Essentials/Base/";
	private String text_path = "IMAGE*/Default/Essentials/Text/";
	
	private String logo_base = "IMAGE*/Default/Essentials/LogoBase/";
	private String logo_path = "IMAGE*/Default/Essentials/TeamBadges/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String flag_path = "IMAGE*/Default/Essentials/Flags/";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	public boolean isProfileStatsOnScreen = false;
	
	public UTT_VIZ() {
		super();
	}

	public UTT_VIZ(String scene_path, String which_Layer) {
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
			
			if(data.isPlayer_sold_or_unsold() == true && data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) && 
					auction.getPlayers().get(auction.getPlayers().size()-1).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
			
				PlayerSoldOrUnsold(print_writer, auction, auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId(), 2);
				if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
					populateRTMEnabled(print_writer, 2);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$RTM START\0");
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$Sold START \0");
				
				TimeUnit.MILLISECONDS.sleep(2000);
				PlayerSoldOrUnsold(print_writer, auction, auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId(), 1);
				if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
					populateRTMEnabled(print_writer, 1);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$RTM SHOW 0.0\0");
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$Sold SHOW 0.0\0");
				data.setBid_result(AuctionUtil.RTM);
			}
			
			if(data.getPreviousBid() < session_curr_bid.getCurrentPlayers().getSoldForPoints() || 
					data.getPreviousBid() > session_curr_bid.getCurrentPlayers().getSoldForPoints()) {
				data.setPreviousBid(session_curr_bid.getCurrentPlayers().getSoldForPoints());
				
				BidChangeOn(print_writer, session_curr_bid, data.getWhichside());
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid START \0");
				TimeUnit.MILLISECONDS.sleep(600);
				BidChangeOn(print_writer, session_curr_bid, 1);
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
			}
		}
		return data;
	}
	
	public Object ProcessGraphicOption(String whatToProcess, Auction auction, Auction session_curr_bid, AuctionService auctionService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess) throws InterruptedException, NumberFormatException, IllegalAccessException {
		System.out.println(whatToProcess.toUpperCase());
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-REMAINING_PURSE_ALL": case "POPULATE-SINGLE_PURSE": case "POPULATE-TOP_SOLD":
		case "POPULATE-L3-NAMESUPER": case "POPULATE-TOP_SOLD_TEAM": case "POPULATE-IDENT": case "POPULATE-RTM_AVAILABLE": case "POPULATE-RTM_ENABLED":
		case "POPULATE-GOOGLY_POWER": case "POPULATE-PROFILE_STATS": case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD":
		case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": case "POPULATE-PLAYERPROFILE_FF": case "POPULATE-LOF_REMAINING_SLOT":
		case "POPULATE-LOF_SQUAD_SIZE": case "POPULATE-LOF_RTM_REMAINING": case "POPULATE-FF_RTM_AND_PURSE_REMAINING": case "POPULATE-FF_TOP_BUYS_AUCTION":
		case "POPULATE-FF_TOP_BUY_TEAM": case "POPULATE-LOF_SQUAD_SIZE_CATEGORY_WISE": case "POPULATE-FF_ICONIC_PLAYERS": case "POPULATE-LT_ICONIC_PLAYERS": 
		case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-PLAYERPROFILE_LT_STATS": case "POPULATE-LOF_SQUAD": case "POPULATE-LOF_SQUAD_REMAIN":
		case "POPULATE-L3-FLIPPER": case "POPULATE-ZONE_PLAYERS_STATS": case "POPULATE-TEAM_CURR_BID": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":
		case "POPULATE-FF_FIVE_TOP_BUY_TEAM": case "POPULATE-ZONEWISE_PLAYERS_SOLD":case "POPULATE-PROFILE_STATS_CHANGE":
			switch (session_selected_broadcaster.toUpperCase()) {
			case "UTT_VIZ":
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
					data.setWithPlayerPhoto(valueToProcess.split(",")[1].equalsIgnoreCase("With_Photo") ? 1 : 0);
					data.setPlayer_id(Integer.valueOf(valueToProcess.split(",")[0]));
					populatePlayerProfile(false,print_writer,whichSide,Integer.valueOf(valueToProcess.split(",")[0]), auctionService.getAllStats(),auction, 
							session_curr_bid,auctionService, session_selected_broadcaster);
					TimeUnit.MILLISECONDS.sleep(500);
					processPreview(print_writer, whatToProcess, whichSide);
					break;
					
				case "POPULATE-PLAYERPROFILE_FF":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					Statistics = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == Integer.valueOf(valueToProcess.split(",")[0])).findAny().orElse(null);
					populatePlayerProfileFF(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess.split(",")[0]), valueToProcess.split(",")[1], 
							auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PROFILE_STATS_CHANGE":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateProfileChange(print_writer,whichSideNotProfile);
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
					populateFlipper(print_writer, whichSideNotProfile, auction, auctionService, session_selected_broadcaster);
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
					populateRTMAvailable(print_writer, rtmGooglyWhichSide, auction, auctionService);
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
					populateProfileStats(print_writer, valueToProcess, whichSideNotProfile, auction, auctionService);
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
					which_data = valueToProcess.split(",")[0];
					which_team = "WithTeam";
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
					which_data = valueToProcess.split(",")[1];
					which_team = "WithoutTeam";
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
							auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-PLAYERPROFILE_LT_STATS":
					whichSideNotProfile = 2;
					side2ValueToProcess = valueToProcess;
					ChangeOnLTStats(print_writer, whichSideNotProfile, player_id, valueToProcess.split(",")[0], auctionService.getAllStats(), 
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
		case "ANIMATE-OUT-PROFILE": case "ANIMATE-OUT-RTM_GOOGLY": case "ANIMATE-OUT-RTM_AVAILABLE":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-RTM_ENABLED": case "ANIMATE-IN-GOOGLY_POWER": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD": case "ANIMATE-IN-SQUAD-PLAYER": 
		case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-LOF_REMAINING_SLOT": case "ANIMATE-IN-LOF_SQUAD_SIZE": case "ANIMATE-IN-LOF_RTM_REMAINING":
		case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-TEAM_CURR_BID": case "ANIMATE-IN-ZONEWISE_PLAYERS_SOLD":
		case "ANIMATE-IN-PROFILE_STATS_CHANGE":
		case "ANIMATE-IN-LOF_SQUAD": case "ANIMATE-IN-LOF_SQUAD_REMAIN":

		case "ANIMATE-IN-LOF_SQUAD_SIZE_CATEGORY_WISE":
		case "ANIMATE-IN-LT_ICONIC_PLAYERS": case "ANIMATE-IN-PLAYERPROFILE_LT": case "ANIMATE-IN-PLAYERPROFILE_LT_STATS": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			switch (session_selected_broadcaster.toUpperCase()) {
			case "UTT_VIZ":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-OUT-PLAYER_STAT":
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats CONTINUE\0");
						isProfileStatsOnScreen = false;
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats SHOW 0.0\0");
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-IN-TEAM_CURR_BID":
					if(data.isData_on_screen()) {
						System.out.println("isProfileStatsOnScreen "+isProfileStatsOnScreen);
						if(isProfileStatsOnScreen) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats START \0");
							TimeUnit.MILLISECONDS.sleep(1000);
							
							if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-PROFILE_STATS")) {
								populateProfileStats(print_writer, side2ValueToProcess, 1, auction, auctionService);
							}else if(whatToProcess.equalsIgnoreCase("ANIMATE-IN-TEAM_CURR_BID")) {
								populateTeamCurrentBid(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, session_curr_bid, auctionService);
							}
							
							TimeUnit.MILLISECONDS.sleep(1000);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats SHOW 0.800\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$BottomStats SHOW 0\0");
							
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats START \0");
							isProfileStatsOnScreen = true;
						}
						
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS_CHANGE":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Stats START\0");
					TimeUnit.MILLISECONDS.sleep(1000);
					populateProfileChange(print_writer,1);
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Stats SHOW 0.0\0");

					break;
				case "ANIMATE-IN-CURR_BID":
					populateCurrentBid(print_writer, 1);
					if(data.getBid_result().equalsIgnoreCase("GAVEL") || data.getBid_result().equalsIgnoreCase("BID")) {
						if(!data.isBid_Start_or_not()) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX START \0");
							TimeUnit.MILLISECONDS.sleep(500);
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid START \0");
						}
					}
						
					data.setBid_Start_or_not(true);
					data.setBid_result("BID");
					
					TimeUnit.MILLISECONDS.sleep(2500);
					populateCurrentBid(print_writer, 1);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					break;
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CenterData START \0");
					
					if(data.isPlayer_sold_or_unsold() == false) {
						data.setBid_result("GAVEL");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX START \0");
						
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold START \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Unsold START \0");
						}
					}
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
					ChangeOnLTStats(print_writer, 1, player_id, side2ValueToProcess.split(",")[0], auctionService.getAllStats(), auction, 
							auctionService, session_selected_broadcaster);	
					TimeUnit.MILLISECONDS.sleep(2000);
					cutBack(print_writer, which_graphics_onscreen, whatToProcess);
					which_graphics_onscreen = "PLAYERPROFILE_LT";
					break;
				//Flipper
				case "ANIMATE-IN-FLIPPER":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll START \0");
//					if(which_graphics_onscreen.isEmpty()) {
//						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper START \0");
//						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll START \0");
//					}else {
//						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
//						populateFlipper(print_writer, 1, auction, auctionService, session_selected_broadcaster);
//						TimeUnit.MILLISECONDS.sleep(2000);
//						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
//					}
					which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					break;
					
				//LT
				case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-PLAYERPROFILE_LT":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Essentials START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Image START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Data START \0");
						
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
									auctionService.getAllStats(), auction, auctionService, session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
				//FF
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident START\0");
					which_graphics_onscreen = "IDENT";
					break;
				case "ANIMATE-IN-PLAYERPROFILE_FF":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile START\0");
					which_graphics_onscreen = "PLAYERPROFILE_FF";
					break;
				case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
				case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_ICONIC_PLAYERS":
				case "ANIMATE-IN-ZONE-PLAYER_STATS": case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials START\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header START\0");
						
						switch(whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details START\0");
							which_graphics_onscreen = "FF_RTM_AND_PURSE_REMAINING";
							break;
						case "ANIMATE-IN-FF_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUYS_AUCTION";
							break;
						case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys START\0");
							which_graphics_onscreen = "FF_FIVE_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys START\0");
							which_graphics_onscreen = "FF_TOP_BUY_TEAM";
							break;
						case "ANIMATE-IN-REMAINING_PURSE_ALL":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$PurseRemaining START\0");
							which_graphics_onscreen = "REMAINING_PURSE_ALL";
							break;
						case "ANIMATE-IN-SQUAD":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad START\0");
							which_graphics_onscreen ="SQUAD";
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad START\0");
							which_graphics_onscreen = "ZONE-PLAYER_STATS";
							break;
						case "ANIMATE-IN-FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$IconPlayers START\0");
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
							Statistics = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == Integer.valueOf(side2ValueToProcess.split(",")[0])).findAny().orElse(null);
							populatePlayerProfileFF(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), side2ValueToProcess.split(",")[1], 
									auction, auctionService, session_selected_broadcaster);
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
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM START\0");
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
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
				
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll SHOW 0.0 \0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Profile SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes SHOW 0.0 \0");
					
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
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CenterData CONTINUE\0");
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$BottomStats CONTINUE\0");
						isProfileStatsOnScreen = false;
					}
				
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX CONTINUE \0");
					
					if(data.isPlayer_sold_or_unsold() == false) {
						if(data.isBid_Start_or_not()) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid CONTINUE \0");
							data.setBid_Start_or_not(false);
						}
					}else {
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold CONTINUE \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Unsold CONTINUE \0");
						}
					}
					if(rtm_googly_on_screen.equalsIgnoreCase("RTM")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM CONTINUE \0");
					}
					
					TimeUnit.MILLISECONDS.sleep(1200);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar SHOW 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Current_Bid SHOW 0.0 \0");
					data.setBid_Start_or_not(false);
					data.setPlayer_sold_or_unsold(false);
					data.setData_on_screen(false);
					data.setBid_result("");
					rtm_googly_on_screen = "";
					break;
				case "ANIMATE-OUT-RTM_GOOGLY": case "ANIMATE-OUT-RTM_AVAILABLE":
					switch (rtm_googly_on_screen) {
					case "RTM":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$RTM SHOW 0\0");
						break;
					}
					rtm_googly_on_screen = "";
					break;
				
				case "ANIMATE-OUT":
					System.out.println(which_graphics_onscreen);
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
					case "PLAYERPROFILE_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Profile SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "IDENT":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Ident SHOW 0\0");
						which_graphics_onscreen = "";
						break;
						
					case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
					case "SQUAD": case "REMAINING_PURSE_ALL": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION":
					case "FF_FIVE_TOP_BUY_TEAM":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials CONTINUE\0");
						switch (which_graphics_onscreen) {
						case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details CONTINUE\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys CONTINUE\0");
							break;
						case "REMAINING_PURSE_ALL":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$PurseRemaining CONTINUE\0");
							break;
						case "SQUAD": case "ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad CONTINUE\0");
							break;
						case "FF_ICONIC_PLAYERS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$IconPlayers CONTINUE\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys CONTINUE\0");
							break;
						}
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes SHOW 0\0");
						break;
					case "NAMESUPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Essentials CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Image CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird$In_Out$CenterData$Data CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LowerThird SHOW 0\0");
						break;
					case "FLIPPER":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Flipper SHOW 0\0");
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
	private void populateProfileChange(PrintWriter print_writer, int whichSideNotProfile2) {
		
		if(side2ValueToProcess.equalsIgnoreCase("with_data")) {			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$SelectStyle*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$1$txt_StatHead"
					+ "*GEOM*TEXT SET WR - WEEK 15\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$1$txt_StatValue"
					+ "*GEOM*TEXT SET " + Statistics.getRank() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$2$txt_StatHead"
					+ "*GEOM*TEXT SET STYLE\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style1$2$txt_StatValue"
					+ "*GEOM*TEXT SET " + Statistics.getStyle() + " " + Statistics.getGrip() + "\0");
			
		}else if(side2ValueToProcess.equalsIgnoreCase("with_info")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$SelectStyle*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side1$Stats$Side" + whichSideNotProfile2 + "$Style2$txt_StatValue"
					+ "*GEOM*TEXT SET "+ Statistics.getInfo1() + "\0");
			
		}
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
		case "PLAYERPROFILE_FF": 
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Profile START\0");
		break;
		case "REMAINING_PURSE_ALL": case "SQUAD": case "FF_RTM_AND_PURSE_REMAINING": 
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": case "FF_ICONIC_PLAYERS": case "ZONE-PLAYER_STATS": 
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header START\0");
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
				break;
			case "REMAINING_PURSE_ALL":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining START\0");
				break;
			case "SQUAD": case "ZONE-PLAYER_STATS":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
				break;
			case "FF_RTM_AND_PURSE_REMAINING":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details START\0");
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
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
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird START \0");
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
			if(!which_graphics_onscreen.equalsIgnoreCase("PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
			}
			break;
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details START\0");
			break;
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_In START\0");
			break;
			
		case "ANIMATE-IN-REMAINING_PURSE_ALL": 
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining START\0");
			break;
		case "ANIMATE-IN-SQUAD":
			if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
			}
			break;
		case "ANIMATE-IN-FF_ICONIC_PLAYERS":
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
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header SHOW 0.0\0");
			switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$MatchId SHOW 2.480\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
					break;
				case "ANIMATE-IN-PLAYERPROFILE_FF":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$Profile SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$PurseRemaining SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");
					break;
				case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-ZONE-PLAYER_STATS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
					break; 
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys SHOW 1.700\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys SHOW 0.0\0");
					break;
				case "ANIMATE-IN-FF_ICONIC_PLAYERS":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$IconPlayers SHOW 1.700\0");
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
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0.0\0");
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
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$MatchId SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
			break;
		case "PLAYERPROFILE_FF":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-PLAYERPROFILE_FF")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$Profile SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
			}
			break;
		case "FF_RTM_AND_PURSE_REMAINING":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details SHOW 0.0\0");
			break;
		case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$TopBuys SHOW 0.0\0");
			break;
		case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys SHOW 0.0\0");
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys SHOW 0.0\0");
			break;
			
		case "REMAINING_PURSE_ALL":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$PurseRemaining SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$PurseRemaining SHOW 0.0\0");
			break;
		case "SQUAD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad SHOW 0.0\0");
			}
			break;
		case "FF_ICONIC_PLAYERS":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Main$IconPlayers SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$IconPlayers SHOW 0.0\0");
			break;
		}
		prevRowHighlight = rowHighlight;
	}

	public void populateIdent(PrintWriter print_writer,int which_side,String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Header$Header_Out$Header_In$Text$txt_Title1*GEOM*TEXT SET " 
				+ "PLAYER" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Header$Header_Out$Header_In$Text$txt_Title2*GEOM*TEXT SET " 
				+ "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Text$Info1_Out$Info1_in$txt_Info1*GEOM*TEXT SET " 
				+ "ULTIMATE TABLE TENNIS SEASON 6" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Ident$Main$Ident$Text$Info2_Out$Info2_In$txt_Info2*GEOM*TEXT SET " 
				+ "LEELA PALACE - MUMBAI" + "\0");
		
	}
	
	public void populateCurrentBid(PrintWriter print_writer,int which_side) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$CurrentPrice$Price"
				+ "$Side1$txt_CurrentPrice*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + " L\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$CurrentPrice$Price"
				+ "$Side2$txt_CurrentPrice*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + " L\0");
	}
	public void BidChangeOn(PrintWriter print_writer, Auction session_curr_bid, int which_side) {
		if(data.isBid_Start_or_not()) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side1$CurrentPrice$Price$Side" + which_side + "$txt_CurrentPrice"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + " L\0");
		}
	}
	public void PlayerSoldOrUnsold(PrintWriter print_writer, Auction auction, int playerId,int which_side) {
		for(int i=auction.getPlayers().size()-1; i >= 0; i--) {
			if(playerId == auction.getPlayers().get(i).getPlayerId()) {
				
				//Auction Result
				if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Sold$Price$txt_SoldValue"
							+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(auction.getPlayers().get(i).getSoldForPoints()) + "L TOKENS" + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Sold$Logo$img_TeamBadges"
							+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$TeamName$txt_TeamName"
							+ "*GEOM*TEXT SET " + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName1() + " \0");
					
					if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Sold$Price$txt_Sold"
								+ "*GEOM*TEXT SET " + "SOLD TO" + " \0");
					}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Sold$Price$txt_Sold"
								+ "*GEOM*TEXT SET " + "RTM" + " \0");
					}
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + "$Unsold$"
							+ "Info$txt_Info*GEOM*TEXT SET " + "UNSOLD" + " \0");
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}
				break;
			}
		}
	}
	
	public void populatePlayerProfile(boolean is_this_updating, PrintWriter print_writer, int which_side, int playerId, List<Statistics> stats, Auction auction, 
			Auction session_curr_bid, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		if(session_curr_bid.getCurrentPlayers() != null) {
			if(data.isData_on_screen() == true) {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, which_side);
					
					if(data.isPlayer_sold_or_unsold() == true) {
						if(data.getBid_result() != null && !data.getBid_result().isEmpty()) {
							if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$CurrentBid START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$Sold START \0");
							}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Shift_PositionX START \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar$Unsold START \0");
							}
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$CurrentBid SHOW 0.0\0");
						if(data.getBid_result() != null && !data.getBid_result().isEmpty()) {
							if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Sold SHOW 0.800\0");
							}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_InfoBar$In_Out$Unsold SHOW 0.800\0");
							}
						}
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_InfoBar SHOW 0.0 \0");
					}
				}
			}else {
				if(data.isPlayer_sold_or_unsold() == false) {
					PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
				}
			}
		}
		
		if(is_this_updating == false) {
			String Container = data.getWithPlayerPhoto()==0 ? "$Without_Image" : "$With_Image";;
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + "$Select_Style*FUNCTION*Omo*vis_con SET "
					+ data.getWithPlayerPhoto() + "\0");
			
			if(data.getWithPlayerPhoto() == 1) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side"+ which_side + Container + "$ImageGrp$ImageGrp_Out$"
						+ "img_PlayerImage*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + Container + "$TopTextGrp$txt_Name*GEOM*TEXT SET " + 
					auctionService.getAllPlayer().get(playerId - 1).getFull_name() + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + Container +  "$TopTextGrp$txt_Age*GEOM*TEXT SET " + 
					(auctionService.getAllPlayer().get(playerId - 1).getAge()== null ? "" : auctionService.getAllPlayer().get(playerId - 1).getAge()+" YEARS" ) + " \0");
			
			if(!auctionService.getAllPlayer().get(playerId - 1).getNationality().equalsIgnoreCase("INDIA")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side"+ which_side + Container + "$BottomLine$Flag$"
						+ "img_Flag*TEXTURE*IMAGE SET "+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + Container +  
						"$BottomLine$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1 \0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + Container + 
						"$BottomLine$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 0 \0");
			}
			
			data.setPreviousBid(Integer.valueOf(auctionService.getAllPlayer().get(playerId - 1).getBasePrice()));
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side"+ which_side + Container +  "$BottomLine$txt_BasePriceValue"
					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(playerId - 1).getBasePrice().replaceAll("0+", "L")+ " TOKENS \0");
			
			if(data.isPlayer_sold_or_unsold() == false) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$Right_Data$Side" + which_side + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
			}
		}
	}
	
	public void populatePlayerProfileFF(PrintWriter print_writer,int which_side, int playerId, String show_stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Header$txt_Header*GEOM*TEXT SET " + "AUCTION 2025" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Header$txt_PlayerName*GEOM*TEXT SET " 
				+ auctionService.getAllPlayer().get(playerId - 1).getFull_name() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$AgeGrp$txt_AgeHead*GEOM*TEXT SET AGE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$AgeGrp$txt_AgeValue*GEOM*TEXT SET " + 
				Statistics.getAge() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$txt_TeamName*GEOM*TEXT SET " 
				+ auctionService.getAllPlayer().get(playerId - 1).getNationality() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$InfoGrp$Flag$img_Flag*TEXTURE*IMAGE SET " 
				+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$1$img_Image*TEXTURE*IMAGE SET " 
				+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$2$img_Image*TEXTURE*IMAGE SET " 
				+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Image$3$img_Image*TEXTURE*IMAGE SET " 
				+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("1100")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 11L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("700")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 7L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("400")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 4L TOKENS\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("200")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$BasePrice$txt_Tokens*GEOM*TEXT SET 2L TOKENS\0");
		}
		
		if(show_stats.equalsIgnoreCase("with_data") || show_stats.equalsIgnoreCase("with_info")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats*ACTIVE SET 1\0");
			switch (show_stats) {
			case "with_data":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$SelectStyle*FUNCTION*Omo*vis_con SET 0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$1$txt_StatHead"
						+ "*GEOM*TEXT SET WR - WEEK 15\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$1$txt_StatValue"
						+ "*GEOM*TEXT SET " + Statistics.getRank() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$2$txt_StatHead"
						+ "*GEOM*TEXT SET STYLE\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style1$2$txt_StatValue"
						+ "*GEOM*TEXT SET " + Statistics.getStyle() + " " + Statistics.getGrip() + "\0");
				break;
			case "with_info":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$SelectStyle*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats$Side1$Style2$txt_StatValue"
						+ "*GEOM*TEXT SET " + Statistics.getInfo1() + "\0");
				break;
			}
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats*ACTIVE SET 0\0");
		}
	}
	
	public void populateLofRemainingPurse(PrintWriter print_writer,String which_type,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int total = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "PURSE REMAINING" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$RupeeSymbol*ACTIVE SET 1\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$TitleText$txt_Title*ACTIVE SET 1\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		
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
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
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
		if(auction.getPlayers() != null) {
			for(Player plyr : auction.getPlayers()){
				if(plyr.getTeamId() == team_id) {
					squad.add(plyr);
				}
			}
		}
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
	        		
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getFull_name() + " \0");
	        		}
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(squad.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
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
						}else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
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
	        		
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getSurname() + " \0");
		        		
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getFull_name() + " \0");
	        		}
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getIconic().equalsIgnoreCase("YES")){
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 1\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Select_IconPlayer"
		    					+ "*FUNCTION*Omo*vis_con SET 0\0");
					}
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Price$txt_Value"
	    					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(squad.get(m).getSoldForPoints()) + " L" + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$ImageGrp$img_Player"
	        				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getPhotoName() 
	        				+ AuctionUtil.PNG_EXTENSION + "\0");
	        		
	        		
	        		if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
		        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
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
						}else if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuysTeam$Row" + row + "$Icon$img_Icon"
				        				+ "*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(squad.get(m).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
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
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header1*GEOM*TEXT SET ISPL SEASON 2"+ "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header2*GEOM*TEXT SET AUCTION"+ "\0");

		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side"+whichSide+"$txt_SubHeader*GEOM*TEXT SET CATEGORY WISE"+ "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Title$txt_Category*GEOM*TEXT SET SQUAD SIZE"+ "\0");
		//Header Flag
		 print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$"
		 		+ "img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		
		for (int i = 1; i <= session_auction.getTeamZoneList().size(); i++) {
		    PlayerCount teamZone = session_auction.getTeamZoneList().get(i - 1); 

		    if (teamZone.getTeamId() == team_id) {
		        rowHighlight = i;

		        // Team Name and Value 
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Highlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET " +
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()+ teamZone.getU19()) + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET " +
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()+teamZone.getU19()) + "\0");

		        for(int k=1; k<=2; k++) {
		        	for (int j = 1; j <= 7; j++) {
			            // Zone names
			            String zoneName = (j == 1 ? "EAST ZONE" :
			                    j == 2 ? "WEST ZONE" :
			                    j == 3 ? "NORTH ZONE" :
			                    j == 4 ? "SOUTH ZONE" :
			                    j == 5 ? "CENTRAL ZONE" :
			                    j == 6 ? "UNDER 19" : "");

			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Category*GEOM*TEXT SET " + zoneName + "\0");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET \0");

			            // Zone values
			            String zoneValue = (j == 1 ? String.valueOf(teamZone.getEastZone()) :
			                    j == 2 ? String.valueOf(teamZone.getWestZone()) :
			                    j == 3 ? String.valueOf(teamZone.getNorthZone()) :
			                    j == 4 ? String.valueOf(teamZone.getSouthZone()) :
			                    j == 5 ? String.valueOf(teamZone.getCentralZone()) :
			                    j == 6 ? String.valueOf(teamZone.getU19()) : "");

			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Value*GEOM*TEXT SET " + zoneValue + "\0");
			        }
		        }
		    } else {
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side1$SquadSize_Category$Team" + i + "$Dehighlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Dehighlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET \0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET " +
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()+ teamZone.getU19()) + "\0");
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
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header1*GEOM*TEXT SET ISPL SEASON 2"+ "\0");
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
			    	
			    	for (int j = 1; j <= 7; j++) {
			            // Zone names
			            String zoneName = (j == 1 ? "EAST ZONE" :
			                    j == 2 ? "WEST ZONE" :
			                    j == 3 ? "NORTH ZONE" :
			                    j == 4 ? "SOUTH ZONE" :
			                    j == 5 ? "CENTRAL ZONE" :
			                    j == 6 ? "UNDER 19" : "");
			            
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Row" + j + "$txt_Category*GEOM*TEXT SET "+zoneName+"\0");
				    	
			            // Zone values
			            String zoneValue = (j == 1 ? String.valueOf(teamZone.getEastZone()) :
			                    j == 2 ? String.valueOf(teamZone.getWestZone()) :
			                    j == 3 ? String.valueOf(teamZone.getNorthZone()) :
			                    j == 4 ? String.valueOf(teamZone.getSouthZone()) :
			                    j == 5 ? String.valueOf(teamZone.getCentralZone()) :
			                    j == 6 ? String.valueOf(teamZone.getU19()) : "");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$SquadSize_Team$Row" + j + "$txt_Value*GEOM*TEXT SET "+zoneValue+"\0");
			        }
			    	break;
			    }
			}
		}
	
	public void populateLofRemainingSlot(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int squadSize = 0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SLOTS REMAINING" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		
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
					+ (16-squadSize) + " \0");
			squadSize = 0;
		}
	}
	public void populateLofZoneWisePlayerSold(PrintWriter print_writer, int whichSide, String which_zone, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		if(which_zone.equalsIgnoreCase("U19")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "UNDER 19 PICKS" + " \0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + which_zone.toUpperCase() + 
					" PICKS" + " \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		
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
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "SQUAD SIZE" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		
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
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + whichSide + "$txt_SubHeader*GEOM*TEXT SET " + "RTM AVAILABLE" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + whichSide + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + "ISPL" + "\0");
		
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
		int rtmUsed=0,squadSize=0,totalAmountSpent=0,row=0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamFirstName*GEOM*TEXT SET " + "SQUAD" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamLastName*GEOM*TEXT SET " + "RTM" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$Slect_ColumnNumber"
				+ "*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List1$0$txt_Name*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List2$0$txt_Name*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List3$0$txt_Name*GEOM*TEXT SET RTM REM.\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List4$0$txt_Name*GEOM*TEXT SET PURSE REM.\0");
		
		for(Team tm : auction.getTeam()) {
			row++;
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List1$" 
					+ row + "$txt_Name*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
			
			for(Player auc : auction.getPlayers()) {
				if(tm.getTeamId() == auc.getTeamId() && auc.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					rtmUsed++;
				}
				if(tm.getTeamId() == auc.getTeamId()) {
					squadSize++;
					totalAmountSpent += auc.getSoldForPoints();
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List2$" + row + 
					"$txt_Name*GEOM*TEXT SET " + squadSize + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List3$" + row + 
					"$txt_Name*GEOM*TEXT SET " + (Integer.valueOf(tm.getTeamTotalRTM()) - rtmUsed) + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List4$" + row + 
					"$txt_Name*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent)) + "L TOKENS" + "\0");
			
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
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Change_Out$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Change_Out$Select_HeaderStyle$Style3$txt_TeamName*GEOM*TEXT SET " + "AUCTION     TOP 5 BUY" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Team*GEOM*TEXT SET " + "TEAM" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + whichSide + "$Select_Graphics$Top5_Buys$Players$WithoutImage$Select_Type$WithTeam$0$Out$In$Group$txt_Price*GEOM*TEXT SET " + "PRICE(TOKENS)" + " \0");
		
		
		
		
		/*print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET ISPL AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0"); */
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 8) break; 
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
        		}
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1()+"\0");
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
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
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		
		  print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$Select_Style*FUNCTION*Omo*vis_con SET " 
		    		+ (which_data.equalsIgnoreCase("WithImage") ? "1" : "0") + "\0");
		  
		if(which_data.equalsIgnoreCase("WithoutImage")){
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
					+ which_data+"$Select_Type*FUNCTION*Omo*vis_con SET "+ (which_team.equalsIgnoreCase("WithoutTeam")? "0": 1) +"\0");	
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Team*GEOM*TEXT SET " + "TEAM" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Price*GEOM*TEXT SET " + "PRICE(TOKENS)" + " \0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamFirstName*GEOM*TEXT SET " + "TOP" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamLastName*GEOM*TEXT SET " + "BUYS" + " \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 5) break;
				String midPath = which_data.trim().equalsIgnoreCase("WithImage") 
					    ? which_data + "$" + row + "$" + which_team 
					    : which_data + "$" + which_team + "$" + row;
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$" + (which_data.equalsIgnoreCase("WithImage") ? "TopLine$txt_TopLine": "txt_Name")+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name() + " \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$" + 
						(which_data.equalsIgnoreCase("WithImage") ? "BottomLine$txt_TopLine": "txt_Price") +"*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + (which_data.equalsIgnoreCase("WithImage") ? " TOKENS": "") + " \0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$Flag$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getNationality().trim().replace(" ", "_") + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$"+(which_data.equalsIgnoreCase("WithImage") ? "TeamNameGrp$txt_TopLine": "txt_Team") + "*GEOM*TEXT SET " + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName1() + " \0");

				if(which_data.equalsIgnoreCase("WithImage")){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"+
							midPath + "$img_Player"+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
							"$BottomLine$txt_RTM*GEOM*TEXT SET " + (plyr.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "") + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
							+  which_data + "$" + row +"$Select_Style*FUNCTION*Omo*vis_con SET "+(which_team.equalsIgnoreCase("WithoutTeam")? "0": 1)+"\0");	
	        			
				}
        		
			}
		}
	}
	public void populateFFTopFiveBuysTeam(PrintWriter print_writer, int whichSide, int team_id, Auction auction,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		List<Player> top_sold = new ArrayList<Player>();
		Auction session_auction = auction;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList()));
		
		PlayerCount team = session_auction.getTeamZoneList().stream().filter(ply->ply.getTeamId()==team_id).findAny().orElse(null);
		top_sold = team.getPlayer();
				
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0\0");	

		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$txt_Title"
				+ "*GEOM*TEXT SET " + "TOP BUYS" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$txt_TeamFirstName"
				+ "*GEOM*TEXT SET " + team.getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$txt_TeamLastName"
				+ "*GEOM*TEXT SET " + team.getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Style1$img-TeamBadges"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		
		 print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$Select_Style*FUNCTION*Omo*vis_con SET " 
		    		+ (which_data.equalsIgnoreCase("WithImage") ? "1" : "0") + "\0");
		 
		if(which_data.equalsIgnoreCase("WithoutImage")){
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
					+ which_data+"$Select_Type*FUNCTION*Omo*vis_con SET "+ (which_team.equalsIgnoreCase("WithoutTeam")? "0": 1) +"\0");	
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Name*GEOM*TEXT SET " + "PLAYER" + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + which_data + "$"+ which_team
					+ "$0$txt_Price*GEOM*TEXT SET " + "PRICE (TOKENS)" + " \0");
			
		}
		for(Player plyr : top_sold) {
			row = row + 1;
			if(row > 5) break;
			String midPath = which_data.trim().equalsIgnoreCase("WithImage") 
				    ? which_data + "$" + row + "$" + which_team 
				    : which_data + "$" + which_team + "$" + row;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
					"$" + (which_data.equalsIgnoreCase("WithImage") ? "TopLine$txt_TopLine": "txt_Name")+ "*GEOM*TEXT SET " + plyr.getFull_name() + " \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + "$" + 
					(which_data.equalsIgnoreCase("WithImage") ? "BottomLine$txt_TopLine": "txt_Price") +"*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + (which_data.equalsIgnoreCase("WithImage") ? " TOKENS": "") + " \0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath +
					"$Flag$img_Flag*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality().trim().replace(" ", "_") + "\0");
			
			if(which_data.equalsIgnoreCase("WithImage")){
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"+
						midPath + "$img_Player"+ "*TEXTURE*IMAGE SET " + photo_path + auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$" + midPath + 
						"$BottomLine$txt_RTM*GEOM*TEXT SET " + (plyr.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "") + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Top5_Buys$"
						+  which_data + "$" + row +"$Select_Style*FUNCTION*Omo*vis_con SET 0\0");	
        			
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
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 3\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header1*GEOM*TEXT SET TOP BUYS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$SubHeader$txt_SubHeader*GEOM*TEXT SET "+team.getTeamName1()+"\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType3$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + team.getTeamName4() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Title$txt_Title2*GEOM*TEXT SET CATEGORY\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Title$txt_Title3*GEOM*TEXT SET PRICE\0");
		
		for(int i=1; i<=8; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+i+"*ACTIVE SET 0\0");
		}
		
		for(Player plyr : top_sold) {
			if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
				if(row > 8) break; 
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"*ACTIVE SET 1\0");
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname() != null) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFirstname()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getSurname()+"\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_FirstName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getFull_name()+"\0");
	        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name$txt_LastName*GEOM*TEXT SET \0");
        		}
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getIconic().equalsIgnoreCase("YES")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 450\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Name*FUNCTION*Maxsize*WIDTH_X SET 530\0");
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$Iconic_Icon$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
        		}
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().equalsIgnoreCase("U19")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET UNDER 19\0");
        		}else {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_TeamName*GEOM*TEXT SET "+auctionService.getAllPlayer().get(plyr.getPlayerId() - 1).getCategory().toUpperCase()+"\0");
        		}
        		
        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$txt_Value*GEOM*TEXT SET "+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
        		
        		
        		if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Keeper"+" \0");
				}else {
					if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
							auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman"+" \0");
						}
						else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "Batsman_Lefthand"+" \0");
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowler"+" \0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerIcon"+" \0");
								break;
							}
						}
					}else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle() == null) {
							if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
							}
							else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
							}
						}else {
							switch(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "FastBowlerAllrounderLeft"+" \0");
								}
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounder"+" \0");
								}
								else if(auctionService.getAllPlayer().get(plyr.getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$TopBuys$Team"+row+"$img_Icon*TEXTURE*IMAGE SET "+icon_path+ "SpinBowlerAllrounderLeft"+" \0");
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
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header1*GEOM*TEXT SET ICON PLAYERS\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$Header$txt_Header2*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$SubHeader$txt_SubHeader*GEOM*TEXT SET ISPL PLAYER AUCTION 2025\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6\0");
		
		for(int i=1; i<=6; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Players$Player" + i + "*ACTIVE SET 0\0");
		}
		
		for(Player player : auction.getPlayersList()) {
			if(player.getIconic().equalsIgnoreCase(AuctionUtil.YES)) {
				row = row + 1;
				if(row <= 6) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + "*ACTIVE SET 1\0");
					if(player.getSurname() != null) {
	        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFirstname()+"\0");
		        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET "+player.getSurname()+"\0");
	        		}else {
	        			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+player.getFull_name()+"\0");
		        		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$NameGrp$txt_LastName*GEOM*TEXT SET \0");
	        		}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + "$ImageGrp$img_Player"
							+ "*TEXTURE*IMAGE SET " + photo_path + player.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$txt_Category*GEOM*TEXT SET " +
							player.getCategory().toUpperCase() + "\0");
					
					if(player.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
								"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
					}else {
						if(player.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || player.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
							}
							else if(player.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(player.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(player.getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(player.getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player" + row + 
											"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
					 
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data1$txt_Value*GEOM*TEXT SET "+"6L"+"\0");
					for(Player plyr : top_sold) {
						if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
							if(player.getPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Select_DataType*FUNCTION*Omo*vis_con SET 1\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$txt_Value*GEOM*TEXT SET "
										+AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints())+" L"+"\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_TeamLogo"
										+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(plyr.getTeamId() - 1).getTeamName4()+"\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + whichSide + "$IconPlayers$Player"+row+"$Data2$img_LogoBase"
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
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_IconLowerThird$PlayerAllGrp$IconPlayer"+row+"$Data1$txt_Value*GEOM*TEXT SET "+"6L"+"\0");
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
	public void populatePlayerProfileLT(PrintWriter print_writer,int which_side, int playerId, String show_stats, List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int team_id = 0;
		List<Player> top_sold = new ArrayList<Player>();
		
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		
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
					"ISPL PLAYER AUCTION" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			for(Player plyr : top_sold) {
				if(plyr.getPlayerId() == playerId) {
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"ISPL SEASON 2" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + "\0");
						
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"ISPL SEASON 2 - UNSOLD" + "\0");
					}
				}
				break;
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
				if(stat.getPlayer_id() == playerId) {
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
	public void ChangeOnLTStats(PrintWriter print_writer,int which_side, int playerId, String show_stats, List<Statistics> stats, Auction auction, 
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
					"ISPL PLAYER AUCTION" + "\0");
		}
		else if(show_stats.equalsIgnoreCase("thisyearteam")) {
			for(Player plyr : top_sold) {
				if(plyr.getPlayerId() == playerId) {
					if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 3\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Title*GEOM*TEXT SET " + 
								"ISPL SEASON 2" + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Text*GEOM*TEXT SET " + 
								auction.getTeam().get(plyr.getTeamId()-1).getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$TeamWithPrice$txt_Value*GEOM*TEXT SET " + 
								AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + " L" + "\0");
						
						
					}else if(plyr.getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$BottomGrp$Side" + which_side + "$Single_Data$txt_Text*GEOM*TEXT SET " + 
								"ISPL SEASON 2 - UNSOLD" + "\0");
					}
				}
				break;
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
				if(stat.getPlayer_id() == playerId) {
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
	
	public void populateNameSuper(PrintWriter print_writer, int whichSide, int nameSuperId, Auction auction, AuctionService auctionService, String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Age*GEOM*TEXT SET \0");
		NameSuper nameSuper = auctionService.getNameSupers().stream().filter(ns->ns.getNamesuperId() == nameSuperId).findAny().orElse(null);
		
		if(nameSuper.getSponsor() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$select_Logo*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$img_TeamBadges*TEXTURE*IMAGE SET " 
					+ logo_path + nameSuper.getSponsor() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$select_Logo*FUNCTION*Omo*vis_con SET 0\0");
		}
		
		if(nameSuper.getSurname() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Name*GEOM*TEXT SET " 
					+ nameSuper.getFirstname() + " " + nameSuper.getSurname() +"\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Name*GEOM*TEXT SET " 
					+ nameSuper.getFirstname() + "\0");
		}
		if(nameSuper.getSubLine() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$BottomLine$txt_BasePrice*GEOM*TEXT SET " 
					+ nameSuper.getSubLine() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$BottomLine$txt_BasePrice*GEOM*TEXT SET \0");
		}
	}
	public void populateFlipper(PrintWriter print_writer, int whichSide, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row=0;
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + "TOP BUYS" + "\0");
		List<Player> top_sold = new ArrayList<Player>();
		if(auction.getPlayers() != null) {
			top_sold = auction.getPlayers();
		}
		Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
		for(Player plyr : top_sold) {
			row = row + 1;
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + plyr.getFull_name() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$FlagAll$img_Flag"
					+ "*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality().trim().replace(" ", "_") + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
					AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + "L TOKENS" + "\0");
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
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKH" + "\0");
	        		
	        		
	        		
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
	        		print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$txt_Unit*GEOM*TEXT SET "+ "LAKH" + "\0");
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
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 4\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType4$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + "INDIAN STREET PREMIER LEAGUE" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType4$Header$txt_Header2"
				+ "*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType4$SubHeader$txt_SubHeader"
				+ "*GEOM*TEXT SET " + "AUCTION 2025" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title1*GEOM*TEXT SET \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title2*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Title$txt_Title3*GEOM*TEXT SET PURSE REMAINING\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3\0");
		
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamFirstName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$TeamGrp$txt_TeamLastName*GEOM*TEXT SET " + match.getTeam().get(i).getTeamName3() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(i).getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$Logo$img_LogoBase*TEXTURE*IMAGE SET " + logo_base + match.getTeam().get(i).getTeamName4() + "\0");
			
			if(match.getPlayers() != null ) {
				for(int j=0; j <= match.getPlayers().size()-1; j++) {
					if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
						row = row + 1;
						total = total + match.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1)
					+ "$txt_SquadSize*GEOM*TEXT SET " + row + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" + (i+1) + "$Value$txt_Value"
					+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total)) + " L" + "\0");
			
			if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 1 \0");
			}else if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
						+ (i+1) + "$Value$txt_Value*GEOM*TEXT SET " + "-" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$PurseRemaining$DataAll$Team" 
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
		data_str = AuctionFunctions.getSquadDataUTTZone(match,team_id);
		
		Auction session_auction = match;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList()));
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0\0");	
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$SubHeader$Select_Subheader*FUNCTION*Omo*vis_con SET 1\0");		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$All_Graphics$Side" + which_side + "$Select_Graphics*FUNCTION*Omo*vis_con SET 0\0");

		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_Title"
				+ "*GEOM*TEXT SET " +"SQUAD" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_TeamFirstName"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName2()+ "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$txt_TeamLastName"
				+ "*GEOM*TEXT SET " + match.getTeam().get(team_id-1).getTeamName3()+ "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Style1$img-TeamBadges"
				+ "*TEXTURE*IMAGE SET " + logo_path + match.getTeam().get(team_id-1).getTeamName4() + "\0");
		PlayerCount teamZone = session_auction.getTeamZoneList().stream().filter(tz -> tz.getTeamId() == team_id).findFirst().orElse(null);
		if (teamZone != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + 
				"$Squad$Select_Subheader$Text$txt_SubHead2*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(teamZone.getPurseRemaing()) + "L TOKENS" + "\0");
							
			for(Player ply : teamZone.getPlayer()) {
				row++;
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$Select_PlayerNumber$" + row + 
						"$Select_Style*FUNCTION*Omo*vis_con SET 2\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$img_Player*TEXTURE*IMAGE SET "+ photo_path + ply.getPhotoName() + AuctionUtil.PNG_EXTENSION  +"\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$Flag$img_Flag*TEXTURE*IMAGE SET "+ flag_path + ply.getNationality().trim().replace(" ", "_") + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$TopLine$txt_TopLine*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$BottomLine$txt_TopLine*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + "L TOKENS"+ "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Select_Graphics$Squad$Players"
						+ "$Select_PlayerNumber$" + row + "$NameGrp$BottomLine$RTM$txt_RTM*GEOM*TEXT SET " +(ply.getSoldOrUnsold().equalsIgnoreCase("RTM")? "RTM" : "") + "\0");		
			}
			for(String Str:data_str) {
				if(Str.matches("IND_M|IND_F|INT_M|INT_F")) {
					row++;
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$Select_PlayerNumber$" + row + 
							"$Select_Style*FUNCTION*Omo*vis_con SET 1\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + which_side + "$Squad$" + row + "$PlayerGrp$WithIcon"
							+ "$img_Icon*TEXTURE*IMAGE SET "+ icon_path + Str  +"\0");
				}
			}
		}
	}
	
	public void populateZoneSquad(PrintWriter print_writer,String ZoneName, int which_side, Auction match, AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int row = 0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$Select_HeaderType*FUNCTION*Omo*vis_con SET 2\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$Header$Select_Icon*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$Select_Gavel*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$Header$txt_Header1"
				+ "*GEOM*TEXT SET " + "ISPL AUCTION 2025" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$Header$txt_Header2"
				+ "*GEOM*TEXT SET  \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$SubHeader"
				+ "$txt_SubHeader*GEOM*TEXT SET " + ZoneName.toUpperCase() + " - REMAINING PLAYERS" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$LogoGrp$Shadow$img_TeamLogo"
				+ "*TEXTURE*IMAGE SET " + logo_path + "ISPL" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + which_side + "$HeaderType2$LogoGrp$img_LogoBase"
				+ "*TEXTURE*IMAGE SET " + logo_base + "ISPL" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2\0");

		for(int i=1;i<=16;i++) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + i 
					+ "$Blank$NameGrp$txt_LastName*GEOM*TEXT SET " + "" + "\0");
		}
	    for(int k = current_index; k<= squad.size()-1 ;k++) {
		   Player plyr = squad.get(k);
				row = row + 1;
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row 
						+ "$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
				
				if(plyr.getSurname() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + plyr.getSurname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_FirstName*GEOM*TEXT SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$NameGrp$"
							+ "txt_LastName*GEOM*TEXT SET " + plyr.getFirstname() + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$ImageGrp$"
						+ "img_Player*TEXTURE*IMAGE SET " + photo_path + plyr.getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$WithData$txt_Category"
						+ "*GEOM*TEXT SET " + "" + "\0");
				
				if(plyr.getIconic().equalsIgnoreCase("YES")){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
							+ "*ACTIVE SET 1\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + "$Icon_Player"
							+ "*ACTIVE SET 0\0");
				}
				if(plyr.getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
							"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Keeper" + "\0");
				}else {
					if(plyr.getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || plyr.getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
						if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman" + "\0");
						}
						else if(plyr.getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "Batsman_Lefthand" + "\0");
						}
					}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
						if(plyr.getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
						}else {
							switch(plyr.getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowler" + "\0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "SpinBowlerIcon" + "\0");
								break;
							}
						}
					}else if(plyr.getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
						if(plyr.getBowlerStyle() == null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
									"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
						}else {
							switch(plyr.getBowlerStyle().toUpperCase()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
										"$Icon$img_Icon*TEXTURE*IMAGE SET " + icon_path + "FastBowlerAllrounder" + "\0");
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Side" + which_side + "$Squad$Players$Player" + row + 
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

	public void populateRTMAvailable(PrintWriter print_writer, int whichSide, Auction match, AuctionService auctionService) {
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == 
				match.getPlayers().get(match.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$img_TeamBadges*TEXTURE*IMAGE SET " + logo_path + 
				auctionService.getTeams().get(player.getLastYearTeam()-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$Info$Status$Side" + whichSide + "$txt_Status*GEOM*TEXT SET AVAILABLE\0");
	}
	public void populateRTMEnabled(PrintWriter print_writer, int whichSide) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$RTM$Info$Status$Side" + whichSide + "$txt_Status*GEOM*TEXT SET ENABLED\0");
	}
	public void populateGooglyPower(PrintWriter print_writer, int teamId, AuctionService auctionService) {
		Team team = auctionService.getTeams().stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Googly_Power$img_TeamLogo*TEXTURE*IMAGE SET " + logo_path + team.getTeamName4() + "\0");
	}
	public void populateProfileStats(PrintWriter print_writer, String whichType, int whichSide, Auction auction, AuctionService auctionService) {
		
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		Statistics stats = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		
		switch (whichType.toUpperCase()) {
		case "RANK":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info*GEOM*TEXT SET " + 
					"WORLD RANK - WEEK 15 : " + stats.getRank() + "\0");
			break;
		case "STYLE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info*GEOM*TEXT SET " + 
				stats.getStyle() + " - " + stats.getGrip() + "\0");
			break;
		case "BIO":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info*GEOM*TEXT SET " + 
					(stats.getInfo1() != null ? stats.getInfo1() : "") + "\0");
			break;
			
		case "PREVTEAM":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info*GEOM*TEXT SET " + 
					(player.getLastYearTeam()== null ? "-": auction.getTeam().get(player.getLastYearTeam()-1).getTeamName1())+ "\0");
			break;
		}
	}
	public void populateTeamCurrentBid(PrintWriter print_writer, int team_id, int whichSide, Auction auction, Auction current_bid, AuctionService auctionService) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET CURRENT BID\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + 
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
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF-PLAYERPROFILE":
				previewCommand = previewCommand +  "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data 0.800 anim_InfoBar$In_Out$CenterData$Data$In 0.800 Shift_PositionX 0.800 ";
				if(data.isBid_Start_or_not()) {
					previewCommand += "anim_InfoBar$In_Out$CurrentBid 0.800 anim_InfoBar$In_Out$CurrentBid$In 0.800 ";
				}
				if(data.getBid_result()!=null && (data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM))) {
					previewCommand += "anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$Sold$In 0.800 ";
				}else if(data.getBid_result()!= null && data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					previewCommand += "anim_InfoBar$In_Out$Unsold 0.800 anim_InfoBar$In_Out$UnSold$In 0.800 ";
				}
				break;
			case "POPULATE-TEAM_CURR_BID":
				previewCommand = previewCommand + "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$In 0.800";
				break;
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand + "anim_InfoBar$In_Out$BottomStats 0.800 anim_InfoBar$In_Out$BottomStats$In 0.800";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = previewCommand +  "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data 0.800 anim_InfoBar$In_Out$CenterData$Data$In 0.800 Shift_PositionX 0.800 "
						+ "anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$RTM$Logo 0.800 anim_InfoBar$In_Out$RTM$Essentials 0.800";
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
				previewCommand = previewCommand +  "anim_InfoBar$In_Out$CenterData 0.800 anim_InfoBar$In_Out$CenterData$Essentials 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Essentials$In 0.800 anim_InfoBar$In_Out$CenterData$Image 0.800 anim_InfoBar$In_Out$CenterData$Image$In 0.800 "
						+ "anim_InfoBar$In_Out$CenterData$Data 0.800 anim_InfoBar$In_Out$CenterData$Data$In 0.800 Shift_PositionX 0.800 ";
				if(data.isBid_Start_or_not()) {
					previewCommand += "anim_InfoBar$In_Out$CurrentBid 0.800 anim_InfoBar$In_Out$CurrentBid$In 0.800 ";
				}
				if(data.getBid_result()!=null && (data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM))) {
					previewCommand += "anim_InfoBar$In_Out$Sold 0.800 anim_InfoBar$In_Out$Sold$In 0.800 ";
				}else if(data.getBid_result()!= null && data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					previewCommand += "anim_InfoBar$In_Out$Unsold 0.800 anim_InfoBar$In_Out$UnSold$In 0.800 ";
				}
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
				previewCommand = previewCommand+ "Change_InfoBar$BottomStats 0.800 Change_InfoBar$$BottomStats$In 0.800";
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
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.png " + previewCommand + "\0");
		
	}
	public void processPreviewFullFrames(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		if (whatToProcess.equalsIgnoreCase("POPULATE-PROFILE_STATS_CHANGE")) {
			previewCommand = "anim_Profile 2.800 anim_Profile$In_Out 1.700 anim_Profile$In_Out$Essentials 1.700 anim_Profile$In_Out$Essentials$In 1.400 "
					+ "anim_Profile$In_Out$Profile 2.800 anim_Profile$In_Out$Profile$In 2.800 Change_Stats 0.700 Change_Stats$Side1 0.700 Change_Stats$Side2 0.700 ";	
		}
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT":
				previewCommand = "anim_Ident 2.500 anim_Ident$In_Out 1.600 anim_Ident$In_Out$Ident 1.600 anim_Ident$In_Out$Ident$In 1.600 ";
				break;
			case "POPULATE-FF_RTM_AND_PURSE_REMAINING":
				previewCommand = "anim_Fullframes 2.800 anim_Fullframes$In_Out$Essentials$In 1.400 anim_Fullframes$In_Out$Header$In 1.000 "
						+ "anim_Fullframes$In_Out$Team_Details$In 1.500";
				break;
			case "POPULATE-PLAYERPROFILE_FF":
				previewCommand = "anim_Profile 2.800 anim_Profile$In_Out 2.800 anim_Profile$In_Out$Essentials 2.800 anim_Profile$In_Out$Essentials$In 2.800 "
						+ "anim_Profile$In_Out$Profile 2.800 anim_Profile$In_Out$Profile$In 2.800";
				break;
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-SQUAD": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				previewCommand = "anim_Fullframes 2.800 anim_Fullframes$In_Out$Essentials$In 1.400 anim_Fullframes$In_Out$Header$In 1.000 ";
				
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
					previewCommand = previewCommand + "anim_Fullframes$In_Out$Top5_Buys$In 1.500";
					break;
				case "POPULATE-SQUAD":
					previewCommand = previewCommand + "anim_Fullframes$In_Out$Squad$In 1.500";
					break;
				}
				break;
			}
		}else {
			
			switch (which_graphics_onscreen.toUpperCase()) {
			case "FF_FIVE_TOP_BUYS_AUCTION": case "SQUAD": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":case "FF_RTM_AND_PURSE_REMAINING":
				
				previewCommand = "Change_Fullframes 1.700 Change_Fullframes$Header 1.000 ";
				
				switch (which_graphics_onscreen.toUpperCase()) {
				case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
					previewCommand = "Change_Fullframes$Top5_Buys 1.500 Change_Fullframes$Top5_Buys$Change_Out 0.600 Change_Fullframes$Top5_Buys$Change_In 1.500 ";
					break;
				case "SQUAD":
					previewCommand = "Change_Fullframes$Squad 1.500 Change_Fullframes$Squad$Change_Out 0.640 Change_Fullframes$Squad$Change_In 1.500 ";
					break;
				case "FF_RTM_AND_PURSE_REMAINING":
					previewCommand = "Change_Fullframes$Team_Details 1.700 Change_Fullframes$Team_Details$Change_Out 0.720 Change_Fullframes$Team_Details$Change_In 1.700 ";
					break;
				}
				
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				if(!which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUYS_AUCTION") && 
						!which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUY_TEAM")) {
					previewCommand = previewCommand + "Change_Fullframes$Top5_Buys 1.500 Change_Fullframes$Top5_Buys$Change_Out 0.600 "
							+ "Change_Fullframes$Top5_Buys$Change_In 1.500";	
				}
				break;
			case "POPULATE-SQUAD":
				previewCommand = previewCommand + "Change_Fullframes$Squad 1.500 Change_Fullframes$Squad$Change_Out 0.640 "
						+ "Change_Fullframes$Squad$Change_In 1.500";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/gfx_Fullframes " + "C:/Temp/Preview.png " + previewCommand + "\0");
	}
	public void processPreviewLowerThirds(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-LT_ICONIC_PLAYERS":
				previewCommand = "anim_IconLowerThird$In_Out 1.800 anim_IconLowerThird$In_Out$In 1.800";
				break;
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "anim_LowerThird 1.400 anim_LowerThird$In_Out 0.800 anim_LowerThird$In_Out$CenterData 0.800 "
						+ "anim_LowerThird$In_Out$CenterData$Essentials$In 0.600 anim_LowerThird$In_Out$CenterData$Image$In 0.600 "
						+ "anim_LowerThird$In_Out$CenterData$Data$In 0.600";
				break;
			case "POPULATE-L3-FLIPPER":
				previewCommand = "Flipper$In_Out$In 0.560 Scroll 8.540 ";
				break;
			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "Change_LowerThird 0.900 Change_LowerThird$Change_Out 0.600 Change_LowerThird$Chnage_In 0.900";
				break;
			case "POPULATE-PLAYERPROFILE_LT_STATS":
				previewCommand = "Change_LowerThird$BottomData 1.300 Change_LowerThird$BottomData$Change_Out 1.000 Change_LowerThird$BottomData$Change_In 1.800";
				break;
			case "POPULATE-L3-FLIPPER":
				previewCommand = "Flipper$In_Out$In 0.560 Scroll 8.540 ";
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