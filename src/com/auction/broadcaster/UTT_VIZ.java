package com.auction.broadcaster;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
import com.auction.model.PlayerCount;
import com.auction.model.Statistics;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.model.Auction;
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
		case "POPULATE-FF-PLAYERPROFILE": case "POPULATE-SQUAD": case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION":case "POPULATE-PROFILE_STATS_CHANGE":
		case "POPULATE-L3-FLIPPER":case "POPULATE-PROFILE_STATS":case "POPULATE-CURR_BID":case "POPULATE-IDENT":case "POPULATE-PLAYERPROFILE_FF":
		case "POPULATE-FF_RTM_AND_PURSE_REMAINING":case "POPULATE-L3-NAMESUPER":case "POPULATE-RTM_AVAILABLE":case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
		case "POPULATE-FLIPPER_SQUAD":
			 
			switch (session_selected_broadcaster.toUpperCase()) {
			case "UTT_VIZ":
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
				case "POPULATE-IDENT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateIdent(print_writer,whichSideNotProfile,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-L3-FLIPPER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFlipper(print_writer, whichSideNotProfile, valueToProcess, auction, auctionService, session_selected_broadcaster);
					processPreviewLowerThirds(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-FLIPPER_SQUAD":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateFlipperSquad(print_writer, whichSideNotProfile, Integer.valueOf(valueToProcess), auction, auctionService, session_selected_broadcaster);
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
				}
			}
		case "ANIMATE-OUT-PROFILE": case "ANIMATE-OUT-RTM_GOOGLY": case "ANIMATE-OUT-RTM_AVAILABLE":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": 
		case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT":case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		case "ANIMATE-IN-PLAYERPROFILE_FF": case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-TEAM_CURR_BID": 
		case "ANIMATE-IN-PROFILE_STATS_CHANGE":case "ANIMATE-IN-ZONE-PLAYER_STATS":
		case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM": 
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM": case "ANIMATE-IN-FLIPPER_SQUAD":
			
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
				//Flipper
				case "ANIMATE-IN-FLIPPER": case "ANIMATE-IN-FLIPPER_SQUAD":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Scroll START \0");
					which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					break;
					
				//LT
				case "ANIMATE-IN-NAMESUPER": 
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
				case "ANIMATE-IN-FF_TOP_BUY_TEAM": case "ANIMATE-IN-SQUAD":case "ANIMATE-IN-ZONE-PLAYER_STATS":
				case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
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
						case "ANIMATE-IN-SQUAD":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad START\0");
							which_graphics_onscreen ="SQUAD";
							break;
						case "ANIMATE-IN-ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad START\0");
							which_graphics_onscreen = "ZONE-PLAYER_STATS";
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
						case "ANIMATE-IN-SQUAD":
							populateSquad(print_writer, Integer.valueOf(side2ValueToProcess.split(",")[0]), 
									1, auction, auctionService, session_selected_broadcaster);
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
							populateFFTopFiveBuysTeam(print_writer, 1, Integer.valueOf(side2ValueToProcess.split(",")[0]), 
									auction, auctionService, session_selected_broadcaster);
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
				case "ANIMATE-OUT-RTM_AVAILABLE":
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
					case "SQUAD":case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION":
					case "FF_FIVE_TOP_BUY_TEAM":
						switch (which_graphics_onscreen) {
						case "FF_RTM_AND_PURSE_REMAINING":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details CONTINUE\0");
							break;
						case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$TopBuys CONTINUE\0");
							break;
						case "SQUAD": case "ZONE-PLAYER_STATS":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad CONTINUE\0");
							break;
						case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Top5_Buys CONTINUE\0");
							break;
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Essentials CONTINUE\0");
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
					case "FLIPPER": case "FLIPPER_SQUAD":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Flipper CONTINUE \0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(2000);
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Flipper SHOW 0\0");
						break;
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
					+ "*GEOM*TEXT SET " + (Statistics.getInfo1() != null ? Statistics.getInfo1() : "") + "\0");
			
		}
	}

	public void ChangeOn(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException {
		switch (which_graphics_onscreen.toUpperCase()) {
			
		//FF	
		case "PLAYERPROFILE_FF": 
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Profile START\0");
		break;
		case "SQUAD": case "FF_RTM_AND_PURSE_REMAINING": case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM": 
		case "ZONE-PLAYER_STATS": case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header START\0");
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
				break;
			case "SQUAD": case "ZONE-PLAYER_STATS":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
				break;
			case "FF_RTM_AND_PURSE_REMAINING":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details$Change_Out START\0");
				break;
			case "FF_TOP_BUYS_AUCTION": case "FF_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
				break;
			case "FF_FIVE_TOP_BUYS_AUCTION": case "FF_FIVE_TOP_BUY_TEAM":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_Out START\0");
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
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Team_Details$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_In START\0");
			break;
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Top5_Buys$Change_In START\0");
			break;
		case "ANIMATE-IN-SQUAD":
			if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad START\0");
			}
			break;
			
		}
	}
	public void cutBack(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException { 
		
		switch (whatToProcess.toUpperCase()) {
			
		//FF
		case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-FF_TOP_BUYS_AUCTION": 
	    case "ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING": case "ANIMATE-IN-ZONE-PLAYER_STATS":
		case "ANIMATE-IN-FF_FIVE_TOP_BUYS_AUCTION": case "ANIMATE-IN-FF_FIVE_TOP_BUY_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Header SHOW 0.0\0");
			switch (whatToProcess.toUpperCase()) {
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
			}
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {		
		//Flipper	
		case "FLIPPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Header SHOW 0.0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Flipper$Text SHOW 0.0\0");
			break;
			
		//LT
		case "NAMESUPER":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LowerThird SHOW 0.0\0");
			break;
		
		//FF
		case "FF_RTM_AND_PURSE_REMAINING":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-FF_RTM_AND_PURSE_REMAINING")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Team_Details SHOW 0.00\0");	
			}
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
			
		case "SQUAD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframes$In_Out$Squad SHOW 0.0\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change_Fullframes$Squad SHOW 0.0\0");
			}
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
				+ "THE LEELA - MUMBAI" + "\0");
		
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
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side"+ which_side + Container + "$BottomLine$Flag$"
					+ "img_Flag*TEXTURE*IMAGE SET "+ flag_path + auctionService.getAllPlayer().get(playerId - 1).getNationality().trim().replace(" ", "_") + "\0");

			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterData$Side" + which_side + Container +  
					"$BottomLine$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1 \0");
			
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
		player_id = playerId;
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
						+ "*GEOM*TEXT SET " + (Statistics.getInfo1() != null ? Statistics.getInfo1() : "") + "\0");
				break;
			}
		}else if(show_stats.equalsIgnoreCase("without")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Profile$Profile$Side" + which_side + "$Stats*ACTIVE SET 0\0");
		}
	}
		
	public void populateFFRTMAndPurseRemaining(PrintWriter print_writer, int whichSide , Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		int rtmUsed=0,squadSize=0,totalAmountSpent=0,row=0;
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Header$Side" + whichSide + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 1\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Select_Graphics*FUNCTION*Omo*vis_con SET 5\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamFirstName*GEOM*TEXT SET " + "PLAYER" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$"
				+ "txt_TeamLastName*GEOM*TEXT SET " + "AUCTION 2025" + " \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$Main$Header$Header_Out$Header_In$Side" + whichSide + "$Style2$" 
				+ "img-TeamBadges*TEXTURE*IMAGE SET " + logo_path + "EVENT" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$Slect_ColumnNumber"
				+ "*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List1$0$txt_Name*GEOM*TEXT SET TEAM\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List2$0$txt_Name*GEOM*TEXT SET SQUAD SIZE\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List3$0$txt_Name*GEOM*TEXT SET RTM REM.\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List4$0$txt_Name*GEOM*TEXT SET PURSE REM.\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Fullframes$All_Graphics$Side" + whichSide + "$Team_Details$3_Column$List4$0$Select_LineNumber"
				+ "*FUNCTION*Omo*vis_con SET 1\0");
		
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
					"$txt_Name*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent)) + "\0");
			
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
				+ "*GEOM*TEXT SET " + team.getTeamName3() + "\0");
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
	
	public void populateNameSuper(PrintWriter print_writer, int whichSide, int nameSuperId, Auction auction, AuctionService auctionService, String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$TopTextGrp$txt_Age*GEOM*TEXT SET \0");
		NameSuper nameSuper = auctionService.getNameSupers().stream().filter(ns->ns.getNamesuperId() == nameSuperId).findAny().orElse(null);
		
		if(nameSuper.getSponsor() != null) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$Logo*FUNCTION*Omo*vis_con SET 1\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$img_TeamBadges*TEXTURE*IMAGE SET " 
					+ logo_path + nameSuper.getSponsor() + "\0");
		}else {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LowerThird$CenterData$Side" + whichSide + "$ImageGrp$Logo*FUNCTION*Omo*vis_con SET 0\0");
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
	public void populateFlipper(PrintWriter print_writer, int whichSide, String value, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int row=0,totalAmountSpent=0;
		switch (value.toLowerCase()) {
		case "top_buys":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + "TOP BUYS" + "\0");
			List<Player> top_sold = new ArrayList<Player>();
			if(auction.getPlayers() != null) {
				top_sold = auction.getPlayers();
			}
			Collections.sort(top_sold,new AuctionFunctions.PlayerStatsComparator());
			for(Player plyr : top_sold) {
				row = row + 1;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + plyr.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$FlagAll$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + plyr.getNationality().trim().replace(" ", "_") + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh(plyr.getSoldForPoints()) + "L TOKENS" + "\0");
			}
			
			for(int i=row+1;i<=8;i++) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + i + "*ACTIVE SET 0\0");
			}
			break;
		case "remain_purse":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + "PURSE REMAINING" + "\0");
			for(Team tm : auction.getTeam()) {
				row++;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
				
				for(Player auc : auction.getPlayers()) {
					if(tm.getTeamId() == auc.getTeamId()) {
						totalAmountSpent += auc.getSoldForPoints();
					}
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh((Integer.valueOf(tm.getTeamTotalPurse()) - totalAmountSpent)) + "L TOKENS" + "\0");
				
				totalAmountSpent = 0;
			}
			break;
		}
	}
	public void populateFlipperSquad(PrintWriter print_writer, int whichSide, int teamId, Auction auction, AuctionService auctionService, String session_selected_broadcaster) {
		
		int row=0;
		data_str.clear();
		data_str = AuctionFunctions.getSquadDataUTTZone(auction,team_id);
		Auction session_auction = auction;
		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
				session_auction.getPlayers(), session_auction.getPlayersList()));
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Header$txt_Header*GEOM*TEXT SET " + 
				auction.getTeam().get(teamId-1).getTeamName1() + "\0");
		PlayerCount teamZone = session_auction.getTeamZoneList().stream().filter(tz -> tz.getTeamId() == teamId).findFirst().orElse(null);
		if (teamZone != null) {
			for(Player ply : teamZone.getPlayer()) {
				row++;
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 1\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + ply.getFull_name() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$FlagAll$img_Flag"
						+ "*TEXTURE*IMAGE SET " + flag_path + ply.getNationality().trim().replace(" ", "_") + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET " + 
						AuctionFunctions.ConvertToLakh(ply.getSoldForPoints()) + "L TOKENS" + "\0");
			}
//			for(String Str:data_str) {
//				if(Str.matches("IND_M|IND_F|INT_M|INT_F")) {
//					row++;
//					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "*ACTIVE SET 1\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$Flag$Select_Flag*FUNCTION*Omo*vis_con SET 0\0");
//					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Value*GEOM*TEXT SET \0");
//					
//					switch(Str) {
//					case "IND_M":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INDIAN MALE" + "\0");
//						break;
//					case "IND_F":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INDIAN FEMALE" + "\0");
//						break;
//					case "INT_M":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INT. MALE" + "\0");
//						break;
//					case "INT_F":
//						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + row + "$txt_Name*GEOM*TEXT SET " + "INT. FEMALE" + "\0");
//						break;
//					}
//				}
//			}
		}
		for(int i=row+1;i<=8;i++) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Flipper$Data$Text_All$Scroll$" + i + "*ACTIVE SET 0\0");
		}
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
	public void populateProfileStats(PrintWriter print_writer, String whichType, int whichSide, Auction auction, AuctionService auctionService) {
		
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		Statistics stats = auctionService.getAllStats().stream().filter(st -> st.getPlayer_id() == auction.getPlayers().get(auction.getPlayers().size()-1).getPlayerId()).findAny().orElse(null);
		
		switch (whichType.toUpperCase()) {
		case "RANK":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET " + 
					"WORLD RANK - WEEK 15 " + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					stats.getRank() + "\0");
			break;
		case "STYLE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
					stats.getStyle() + " - " + stats.getGrip() + "\0");
			break;
		case "BIO":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info1*GEOM*TEXT SET \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_InfoBar$Shift_ForStats$CenterBottomStats$TextGrp$Side" + whichSide + "$txt_Info2*GEOM*TEXT SET " + 
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
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand+ "Change_InfoBar$BottomStats 0.800 Change_InfoBar$BottomStats$In 0.800";
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
			
			System.out.println(whatToProcess + " - " + which_graphics_onscreen);
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF_FIVE_TOP_BUYS_AUCTION": case "POPULATE-FF_FIVE_TOP_BUY_TEAM":
				if(!which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUYS_AUCTION") && !which_graphics_onscreen.equalsIgnoreCase("FF_FIVE_TOP_BUY_TEAM")) {
					previewCommand = previewCommand + "Change_Fullframes$Top5_Buys 1.500 Change_Fullframes$Top5_Buys$Change_Out 0.600 "
							+ "Change_Fullframes$Top5_Buys$Change_In 1.500";	
				}
				break;
			case "POPULATE-SQUAD":
				previewCommand = previewCommand + "Change_Fullframes$Squad 1.500 Change_Fullframes$Squad$Change_Out 0.640 "
						+ "Change_Fullframes$Squad$Change_In 1.500";
				break;
			case"POPULATE-FF_RTM_AND_PURSE_REMAINING":
				previewCommand = previewCommand + "Change_Fullframes$Team_Details 1.500 Change_Fullframes$Team_Details$Change_Out 0.600 "
						+ "Change_Fullframes$Team_Details$Change_In 1.500";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/gfx_Fullframes " + "C:/Temp/Preview.png " + previewCommand + "\0");
	}
	public void processPreviewLowerThirds(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-L3-NAMESUPER":
				previewCommand = "anim_LowerThird 1.400 anim_LowerThird$In_Out 0.800 anim_LowerThird$In_Out$CenterData 0.800 "
						+ "anim_LowerThird$In_Out$CenterData$Essentials$In 0.600 anim_LowerThird$In_Out$CenterData$Image$In 0.600 "
						+ "anim_LowerThird$In_Out$CenterData$Data$In 0.600";
				break;
			case "POPULATE-L3-FLIPPER": case "POPULATE-FLIPPER_SQUAD":
				previewCommand = "Flipper$In_Out$In 0.560 Scroll 8.540 ";
				break;
			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PLAYERPROFILE_LT": case "POPULATE-L3-NAMESUPER":
				previewCommand = "Change_LowerThird 0.900 Change_LowerThird$Change_Out 0.600 Change_LowerThird$Chnage_In 0.900";
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