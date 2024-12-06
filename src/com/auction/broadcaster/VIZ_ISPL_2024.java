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

public class VIZ_ISPL_2024 extends Scene{

	private String status, side2ValueToProcess = "";
	private String slashOrDash = "-";
	public String session_selected_broadcaster = "VIZ_ISPL_2024";
	public Data data = new Data();
	public String which_graphics_onscreen = "";
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1, rowHighlight = 1,prevRowHighlight = 1,player_ID=0;

	private String base_path = "IMAGE*/Default/Essentials/Base/";
	private String text_path = "IMAGE*/Default/Essentials/Text/";
	
	private String base_path_1 = "IMAGE*/Default/Essentials/Base1/";
	private String text_path_1 = "IMAGE*/Default/Essentials/Text1/";
	private String base_path_2 = "IMAGE*/Default/Essentials/Base2/";
	private String text_path_2 = "IMAGE*/Default/Essentials/Text2/";
	
	private String logo_path = "IMAGE*/Default/Essentials/TeamLogo/";
	private String icon_path = "IMAGE*/Default/Essentials/Icons/";
	private String photo_path  = "C:\\Images\\AUCTION\\Photos\\";
	
	public boolean isProfileStatsOnScreen = false;
	
	public VIZ_ISPL_2024() {
		super();
	}

	public VIZ_ISPL_2024(String scene_path, String which_Layer) {
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
			populatePlayerProfile(true,print_writer, 2, data.getPlayer_id(),auctionService.getAllStats(),auction, 
					session_curr_bid,auctionService, session_selected_broadcaster);
			
			
			if(data.getPreviousBid() < session_curr_bid.getCurrentPlayers().getSoldForPoints()) {
				data.setPreviousBid(session_curr_bid.getCurrentPlayers().getSoldForPoints());
				
				BidChangeOn(print_writer, session_curr_bid, data.getWhichside());
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Bid_Value START \0");
				TimeUnit.MILLISECONDS.sleep(600);
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
		case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER": case "POPULATE-PLAYERPROFILE_FF":
			switch (session_selected_broadcaster.toUpperCase()) {
			case "VIZ_ISPL_2024":
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
						whichSide = 2;
					}else {
						whichSide = 1;
					}
					player_ID = Integer.valueOf(valueToProcess.split(",")[0]);
					populatePlayerProfileFF(print_writer,whichSide,player_ID, auctionService.getAllStats(),auction, auctionService, session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSide);
					break;
					
				case "POPULATE-REMAINING_PURSE_ALL":
					populateRemainingPurse(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-SQUAD":
					populateSquad(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService,session_selected_broadcaster);
					break;
				case "POPULATE-TOP_SOLD":
					populateTopSold(print_writer, valueToProcess.split(",")[0], auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-IDENT":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSide = 2;
					}else {
						whichSide = 1;
					}
					populateIdent(print_writer,whichSide,session_selected_broadcaster);
					processPreviewFullFrames(print_writer, whatToProcess, whichSide);
					break;
				case "POPULATE-TOP_SOLD_TEAM":
					populateTopSoldTeam(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-L3-NAMESUPER":
					populateNameSuper(print_writer, valueToProcess.split(",")[0], Integer.valueOf(valueToProcess.split(",")[1]), auction,auctionService, session_selected_broadcaster);
					break;
				case "POPULATE-RTM_AVAILABLE":
					if(which_graphics_onscreen.equalsIgnoreCase("RTM")) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateRTMAvailable(print_writer, whichSideNotProfile, session_curr_bid, auctionService);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				case "POPULATE-RTM_ENABLED":
					if(which_graphics_onscreen.equalsIgnoreCase("RTM")) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					populateRTMEnabled(print_writer, whichSideNotProfile);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
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
					populateProfileStats(print_writer, valueToProcess, whichSideNotProfile, session_curr_bid, auctionService);
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
				case "POPULATE-SQUAD-PLAYER":
					if(!which_graphics_onscreen.isEmpty()) {
						whichSideNotProfile = 2;
					}else {
						whichSideNotProfile = 1;
					}
					side2ValueToProcess = valueToProcess;
					populateSquadPlayer(print_writer, Integer.valueOf(valueToProcess.split(",")[0]), whichSideNotProfile, auction,auctionService,session_selected_broadcaster);
					processPreview(print_writer, whatToProcess, whichSideNotProfile);
					break;
				}
			}
		case "ANIMATE-OUT-PROFILE":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-RTM_ENABLED": case "ANIMATE-IN-GOOGLY_POWER": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD": case "ANIMATE-IN-SQUAD-PLAYER": 
		case "ANIMATE-IN-PLAYERPROFILE_FF":
		
			switch (session_selected_broadcaster.toUpperCase()) {
			case "VIZ_ISPL_2024":
				switch (whatToProcess.toUpperCase()) {
				case "ANIMATE-OUT-PLAYER_STAT":
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						isProfileStatsOnScreen = false;
					}
					break;
				case "ANIMATE-IN-PROFILE_STATS":
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats$Change_Out START \0");
						TimeUnit.MILLISECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats$Change_In START \0");
						populateProfileStats(print_writer, side2ValueToProcess, 1, session_curr_bid, auctionService);
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Stats SHOW 0\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats START \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats START \0");
					}
					which_graphics_onscreen = "PLAYERPROFILE";
					isProfileStatsOnScreen = true;
					break;
				case "ANIMATE-IN-CURR_BID":
					populateCurrentBid(print_writer, 2);
					if(data.getBid_result().equalsIgnoreCase("GAVEL") || data.getBid_result().equalsIgnoreCase("BID")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Gavel START \0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$CurrentBid START \0");
					}
					
					data.setBid_Start_or_not(true);
					data.setBid_result("BID");
					
					TimeUnit.MILLISECONDS.sleep(2500);
					populateCurrentBid(print_writer, 1);
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					break;
				
				case "ANIMATE-IN-PLAYERPROFILE": 
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Essentials START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Left_Data START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$RightData START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Loop START \0");
					data.setBid_result("GAVEL");
					data.setData_on_screen(true);
					which_graphics_onscreen = "PLAYERPROFILE";
					break;
				case "ANIMATE-IN-SQUAD":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "SQUAD";
					break;
				case "ANIMATE-IN-REMAINING_PURSE_ALL":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "REMAINING_PURSE_ALL";
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
				case "ANIMATE-IN-NAMESUPER":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "NAMESUPER";
					break;
					
				case "ANIMATE-IN-IDENT": case "ANIMATE-IN-PLAYERPROFILE_FF":
					if(which_graphics_onscreen.isEmpty()) {
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare START \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials START\0");
						switch(whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId START\0");
							which_graphics_onscreen = "IDENT";
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header START\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile START\0");
							which_graphics_onscreen = "PLAYERPROFILE_FF";
							break;
						}
					}else {
						ChangeOn(print_writer, which_graphics_onscreen, whatToProcess);
						switch (whatToProcess.toUpperCase()) {
						case "ANIMATE-IN-IDENT":
							populateIdent(print_writer,1,session_selected_broadcaster);
							break;
						case "ANIMATE-IN-PLAYERPROFILE_FF":
							populatePlayerProfileFF(print_writer,1,player_ID, auctionService.getAllStats(),auction,auctionService,session_selected_broadcaster);
							break;
						}
						TimeUnit.MILLISECONDS.sleep(2000);
						cutBack(print_writer, which_graphics_onscreen, whatToProcess);
						which_graphics_onscreen = whatToProcess.replace("ANIMATE-IN-", "");
					}
					break;
					
				case "ANIMATE-IN-RTM_AVAILABLE":
					if(which_graphics_onscreen.equalsIgnoreCase("RTM")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_Out START\0");
						TimeUnit.MILLISECONDS.sleep(250);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_In START\0");
						TimeUnit.MILLISECONDS.sleep(500);
						populateRTMAvailable(print_writer, 1, session_curr_bid, auctionService);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out START \0");
					}
					which_graphics_onscreen = "RTM";
					break;
				case "ANIMATE-IN-RTM_ENABLED":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_Out START\0");
					TimeUnit.MILLISECONDS.sleep(250);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM$Text$Change_In START\0");
					TimeUnit.MILLISECONDS.sleep(500);
					populateRTMEnabled(print_writer, 1);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RTM SHOW 0\0");
					which_graphics_onscreen = "RTM";
					break;
				case "ANIMATE-IN-GOOGLY_POWER":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out START\0");
					which_graphics_onscreen = "GOOGLY";
					break;
				case "ANIMATE-IN-LOF_REMAINING_PURSE": case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
				case "ANIMATE-IN-SQUAD-PLAYER":
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
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys START \0");
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
						case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
							populateLofTeamTopSold(print_writer, Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
							break;
						case "ANIMATE-IN-SQUAD-PLAYER":
							populateSquadPlayer(print_writer,  Integer.valueOf(side2ValueToProcess), 1, auction, auctionService, session_selected_broadcaster);
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
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*StartFlare SHOW 0.0 \0");
					
		            which_graphics_onscreen = "";
		            side2ValueToProcess ="";
		            whichSideNotProfile = 1;
		            data.setBid_Start_or_not(false);
		            data.setData_on_screen(false);
		            isProfileStatsOnScreen = false;
					data.setBid_result("");
					break;
				case "ANIMATE-OUT-PROFILE":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Essentials CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Left_Data CONTINUE REVERSE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$RightData CONTINUE \0");
					if(isProfileStatsOnScreen) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug$In_Out$Stats CONTINUE\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Move_For_Stats CONTINUE REVERSE\0");
						isProfileStatsOnScreen = false;
					}
					
					TimeUnit.MILLISECONDS.sleep(1000);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_ScoreBug SHOW 0\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData SHOW 0.0 \0");
					which_graphics_onscreen = "";
					data.setBid_Start_or_not(false);
					data.setData_on_screen(false);
					data.setBid_result("");
					break;
				
				case "ANIMATE-OUT":
					switch(which_graphics_onscreen) {
					case "RTM":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM$In_Out CONTINUE\0");
						which_graphics_onscreen = "";
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_RTM SHOW 0\0");
						break;
					case "GOOGLY":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly$In_Out CONTINUE\0");
						TimeUnit.MILLISECONDS.sleep(500);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_Googly SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER":
						switch (which_graphics_onscreen) {
						case "LOF_REMAINING_PURSE":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name CONTINUE \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo CONTINUE \0");
							which_graphics_onscreen = "";
							break;
							
						case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$TopBuys CONTINUE \0");
							which_graphics_onscreen = "";
							break;
						case "SQUAD-PLAYER":
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$SquadSize_Category CONTINUE\0");

							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight CONTINUE REVERSE\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight CONTINUE REVERSE \0");
							break;
						}
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Essentials CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Header CONTINUE \0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$SubHeader CONTINUE \0");
						TimeUnit.MILLISECONDS.sleep(1000);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF SHOW 0\0");
						which_graphics_onscreen = "";
						break;

					case "IDENT": case "PLAYERPROFILE_FF":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Essentials CONTINUE\0");
						switch (which_graphics_onscreen) {
						case "IDENT":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId CONTINUE\0");
							break;
						case "PLAYERPROFILE_FF":
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile CONTINUE\0");
							break;
						}
						which_graphics_onscreen = "";
						break;
						
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER": case "TOP_SOLD_TEAM":
						AnimateOutGraphics(print_writer, whatToProcess.toUpperCase());
						which_graphics_onscreen = "";
						break;
					}
					break;
				}
				
			}
			
			
		}
		return null;
	}
	public void ChangeOn(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException {
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD": case "SQUAD-PLAYER":
			if(whichGraphicOnScreen.equalsIgnoreCase("SQUAD-PLAYER") && whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader START \0");
			}
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse START \0");
			break;
		case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys START \0");
			break;
		case "SQUAD-PLAYER":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-SQUAD-PLAYER")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SquadSize_Category START \0");
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*CtegoryHighlight$Side1$"+prevRowHighlight +" CONTINUE REVERSE\0");
			TimeUnit.MILLISECONDS.sleep(200);
			for(int i=1;i<=6;i++) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForCatHighlight$Side1$"+i+" CONTINUE REVERSE\0");
			}
			break;
		case "IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
			break;
		case "PLAYERPROFILE_FF":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header CONTINUE\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
			break;
		}
		TimeUnit.MILLISECONDS.sleep(500);
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			if(!whichGraphicOnScreen.equalsIgnoreCase("LOF_REMAINING_PURSE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse START \0");
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo START \0");
			break;

		case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
			if(!whichGraphicOnScreen.equalsIgnoreCase("LOF_TOP_SOLD") && !whichGraphicOnScreen.equalsIgnoreCase("LOF_TEAM_TOP_SOLD")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$TopBuys START \0");
			}
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
		case "ANIMATE-IN-IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident START\0");
			break;
		case "ANIMATE-IN-PLAYERPROFILE_FF":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header START\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile START\0");
			break;
		}
	}
	public void cutBack(PrintWriter print_writer, String whichGraphicOnScreen, String whatToProcess) throws InterruptedException { 
		
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Header SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$SubHeader SHOW 0\0");
			break;
		}
		
		switch (whatToProcess.toUpperCase()) {
		case "ANIMATE-IN-LOF_REMAINING_PURSE":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Name SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$Logo SHOW 2.000\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
			break;
		case "ANIMATE-IN-LOF_TOP_SOLD": case "ANIMATE-IN-LOF_TEAM_TOP_SOLD":
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
		case "ANIMATE-IN-IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 2.480\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
			break;
		case "ANIMATE-IN-PLAYERPROFILE_FF":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header SHOW 2.480\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 2.480\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
			break;
		}
		switch (which_graphics_onscreen.toUpperCase()) {
		case "LOF_REMAINING_PURSE":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_REMAINING_PURSE")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*anim_LOF$In_Out$Main$RemainingPurse SHOW 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Name SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$Logo SHOW 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_LOF$RemainingPurse SHOW 0\0");
			}
			break;
		case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
			if(!whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TOP_SOLD") && !whatToProcess.equalsIgnoreCase("ANIMATE-IN-LOF_TEAM_TOP_SOLD")) {
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
		
		case "IDENT":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$MatchId SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Ident SHOW 0.0\0");
			break;
		case "PLAYERPROFILE_FF":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Header SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*anim_Fullframe$In_Out$Main$Profile SHOW 0.0\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Change$Profile SHOW 0.0\0");
			break;
		}
		prevRowHighlight = rowHighlight;
	}

	public void populateIdent(PrintWriter print_writer,int which_side,String session_selected_broadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header1$txt_Header1*GEOM*TEXT SET " 
				+ "PLAYER" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$HeaderAll$Header2$txt_Header2*GEOM*TEXT SET " 
				+ "AUCTION 2024" + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info1$txt_Info*GEOM*TEXT SET " 
				+ "INDIAN STREET PREMIER LEAGUE SEASON 2" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Ident$InfoGrp$Info2$txt_Info*GEOM*TEXT SET " 
				+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		
	}
	
	public void populateCurrentBid(PrintWriter print_writer,int which_side) {
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$CurrentBid$ValueGrp$Side1$txt_CurrentBid"
				+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(data.getPreviousBid()) + "\0");
	}
	public void BidChangeOn(PrintWriter print_writer, Auction session_curr_bid, int which_side) {
		if(data.isBid_Start_or_not()) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side1$CurrentBid$ValueGrp$Side" + which_side + "$txt_CurrentBid"
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
				}
				
				//Auction Result
				if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + 
							"$Select_DataType*FUNCTION*Omo*vis_con SET 2 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$Sold$TextOut$txt_SoldValue"
							+ "*GEOM*TEXT SET " + AuctionFunctions.ConvertToLakh(auction.getPlayers().get(i).getSoldForPoints()) + "L" + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$Sold$LogoOut$img_TeamLogo"
							+ "*TEXTURE*IMAGE SET " + logo_path + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
							+ "*TEXTURE*IMAGE SET " + text_path_1 + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
							+ "*TEXTURE*IMAGE SET " + base_path_1 + auction.getTeam().get(auction.getPlayers().get(i).getTeamId()-1).getTeamName4() + " \0");
					
					if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$Sold$TextOut$txt_Title"
								+ "*GEOM*TEXT SET " + "SOLD" + " \0");
					}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + "$Sold$TextOut$txt_Title"
								+ "*GEOM*TEXT SET " + "SOLD - RTM" + " \0");
					}
					data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
					data.setPlayer_sold_or_unsold(true);
				}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + 
							"$Select_DataType*FUNCTION*Omo*vis_con SET 3 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
							+ "*TEXTURE*IMAGE SET " + text_path_1 + "UNSOLD" + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
							+ "*TEXTURE*IMAGE SET " + base_path_1 + "UNSOLD" + " \0");
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
			if(data.isPlayer_sold_or_unsold() == false) {
				PlayerSoldOrUnsold(print_writer, auction, playerId, which_side);
				
				if(data.isPlayer_sold_or_unsold() == true) {
					if(data.getBid_result() != null && !data.getBid_result().isEmpty()) {
						if(data.getBid_result().equalsIgnoreCase(AuctionUtil.SOLD) || data.getBid_result().equalsIgnoreCase(AuctionUtil.RTM)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$CurrentBid START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Sold START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*MoveForSold START \0");
						}else if(data.getBid_result().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Gavel START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$Unsold START \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ImageChangeForUnsold START \0");
						}
					}
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_TopData START \0");
					TimeUnit.MILLISECONDS.sleep(2000);
					PlayerSoldOrUnsold(print_writer, auction, playerId, 1);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change SHOW 0.0 \0");
				}
			}
		}
		
		if(is_this_updating == false) {
			data.setPlayer_sold_or_unsold(false);
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
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$GenericImage$img_Player*TEXTURE*IMAGE SET "+ photo_path + 
					auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$ImageGrp$UnsoldImage$img_Player*TEXTURE*IMAGE SET "+ photo_path + 
					auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
					+ "*TEXTURE*IMAGE SET " + text_path_1 + "GENERIC" + " \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
					+ "*TEXTURE*IMAGE SET " + base_path_1 + "GENERIC" + " \0");
			
			if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
				data.setPreviousBid(300000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$LeftDataGrp$txt_BasePrice*GEOM*TEXT SET " + "3L" + " \0");
			}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("600")) {
				data.setPreviousBid(600000);
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$LeftDataGrp$txt_BasePrice*GEOM*TEXT SET " + "6L" + " \0");
			}
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + 
					"$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0");
		}
	}
	
	public void populatePlayerProfileFF(PrintWriter print_writer,int which_side, int playerId,List<Statistics> stats, Auction auction, 
			AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Header$HeaderType1$txt_Header*GEOM*TEXT SET " + "AUCTION 2024" + "\0");
		
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
				+ "*TEXTURE*IMAGE SET "+ photo_path + auctionService.getAllPlayer().get(playerId - 1).getPhotoName() + AuctionUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$PlayerRole$txt_Role*GEOM*TEXT SET " + 
				auctionService.getAllPlayer().get(playerId - 1).getRole() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$Category$txt_Category*GEOM*TEXT SET " + 
				auctionService.getAllPlayer().get(playerId - 1).getCategory() + "\0");
		
		if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("300")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
					+ "Geom-Graphic-Bold-Blur-1*GEOM*TEXT SET " + "3L" + "\0");
		}else if(auctionService.getAllPlayer().get(playerId - 1).getBasePrice().equalsIgnoreCase("600")) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_FullFrames$Main$Side" + which_side + "$Profile$BasePrice$"
					+ "Geom-Graphic-Bold-Blur-1*GEOM*TEXT SET " + "6L" + "\0");
		}
		
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
	}
	
	public void populateLofRemainingPurse(PrintWriter print_writer,String which_type,int which_side, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		
		int total = 0,row=0;
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " + "ISPL SEASON 2" + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " + "AUCTION" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "PURSE REMAINING" + " \0");
		
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
						row = row + 1;
						total = total + auction.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$RemainingPurse$Row" + (i+1) + "$txt_Value*GEOM*TEXT SET " 
					+ AuctionFunctions.ConvertToLakh((Integer.valueOf(auction.getTeam().get(i).getTeamTotalPurse()) - total)) + " \0");
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
	        		
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_FirstName"
	    					+ "*GEOM*TEXT SET " + auction.getTeam().get(top_sold.get(m).getTeamId()-1).getTeamName4() + " \0");
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_LastName"
	    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getTicker_name() + " \0");
	        		
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
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + i + "*ACTIVE SET 0 \0");
		}
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0 \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$img_TeamLogo*TEXTURE*IMAGE SET "
				+ logo_path + auction.getTeam().get(team_id-1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header1*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName2() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side" + which_side + "$HeaderStyle1$txt_Header2*GEOM*TEXT SET " 
				+ auction.getTeam().get(team_id-1).getTeamName3() + " \0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side" + which_side + "$txt_SubHeader*GEOM*TEXT SET " + "TOP BUYS" + " \0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
		
		for(int m=0; m<= top_sold.size() - 1; m++) {
			if(top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.SOLD) || top_sold.get(m).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.RTM)) {
				row = row + 1;
	        	if(row <= 4) {
	        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "*ACTIVE SET 1 \0");
	        		
	        		if(auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() != null) {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getSurname() + " \0");
	        		}else {
	        			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_FirstName"
		    					+ "*GEOM*TEXT SET " + "" + " \0");
		        		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + which_side + "$TopBuys$Row" + row + "$NameGrp$txt_LastName"
		    					+ "*GEOM*TEXT SET " + auctionService.getAllPlayer().get(top_sold.get(m).getPlayerId()-1).getFirstname() + " \0");
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

	private void populateSquadPlayer(PrintWriter print_writer,int team_id, int whichSide , Auction match,AuctionService auctionService, 
		String session_selected_broadcaster) {
		Auction session_auction = match;

		session_auction.setTeamZoneList(AuctionFunctions.PlayerCountPerTeamZoneWise(session_auction.getTeam(), 
			session_auction.getPlayers(), session_auction.getPlayersList()));

		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side"+whichSide+"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1" + "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$Select_HeaderStyle*FUNCTION*Omo*vis_con SET 0"+ "\0");
		
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header1*GEOM*TEXT SET ISPL SEASON 2"+ "\0");
		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$Header$Side"+whichSide+"$HeaderStyle1$txt_Header2*GEOM*TEXT SET AUCTION"+ "\0");

		print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$SubHead$Side"+whichSide+"$txt_SubHeader*GEOM*TEXT SET CATEGORY"+ "\0");
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
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()) + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side2$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET " +
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()) + "\0");

		        for(int k=1; k<=2; k++) {
		        	for (int j = 1; j <= 7; j++) {
			            // Zone names
			            String zoneName = (j == 1 ? "EAST ZONE" :
			                    j == 2 ? "WEST ZONE" :
			                    j == 3 ? "NORTH ZONE" :
			                    j == 4 ? "SOUTH ZONE" :
			                    j == 5 ? "CENTRAL ZONE" : "");

			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Category*GEOM*TEXT SET " + zoneName + "\0");
			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET \0");

			            // Zone values
			            String zoneValue = (j == 1 ? String.valueOf(teamZone.getEastZone()) :
			                    j == 2 ? String.valueOf(teamZone.getWestZone()) :
			                    j == 3 ? String.valueOf(teamZone.getNorthZone()) :
			                    j == 4 ? String.valueOf(teamZone.getSouthZone()) :
			                    j == 5 ? String.valueOf(teamZone.getCentralZone()) : "");

			            print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + k + "$SquadSize_Category$Team" + i + "$Highlight$Category$" + j + "$txt_Value*GEOM*TEXT SET " + zoneValue + "\0");
			        }
		        }
		    } else {
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_TeamName*GEOM*TEXT SET " + teamZone.getTeamName4() + "\0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Highlight$txt_Value*GEOM*TEXT SET \0");
		        print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_LOF$AllGraphics$Side" + whichSide + "$SquadSize_Category$Team" + i + "$Dehighlight$txt_Value*GEOM*TEXT SET " +
		                (teamZone.getEastZone() + teamZone.getWestZone() + teamZone.getNorthZone() + teamZone.getSouthZone() + teamZone.getCentralZone()) + "\0");
		    }
		}
	}
	public void populateNameSuper(PrintWriter print_writer,String viz_scene, int nameSuperId, Auction auction,AuctionService auctionService, String session_selected_broadcaster) {
		print_writer.println("-1 RENDERER*TREE*$Main$SelectPointer*FUNCTION*Omo*vis_con SET 1 \0");
		print_writer.println("-1 RENDERER*TREE*$Main$SelectPointer*FUNCTION*Omo*vis_con SET 1 \0");
		for(NameSuper ns : auctionService.getNameSupers()) {
			if(ns.getNamesuperId() == nameSuperId) {
				if(ns.getSurname() != null) {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_LastName*GEOM*TEXT SET "+ ns.getSurname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_FirstName*GEOM*TEXT SET "+ ns.getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_LastName*GEOM*TEXT SET "+ "" + "\0");
				}
				if(ns.getSubLine() != null) {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_Text1*GEOM*TEXT SET "+ ns.getSubLine() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$1-Point$txt_Text1*GEOM*TEXT SET "+ ns.getSubLine() + "\0");
				}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.300 \0");
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
	public void populateRemainingPurse(PrintWriter print_writer,String viz_scene, Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		int total = 0;
		int row = 0;
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Data$SubHeaderGrp$txt_PurseValueHead*GEOM*TEXT SET "+ "PURSE REMAINING" + "\0");
		for(int i=0; i <= match.getTeam().size()-1; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_FirstName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$NameGrp$txt_LastName*GEOM*TEXT SET "+ match.getTeam().get(i).getTeamName3() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$img_Logo*TEXTURE*IMAGE SET "+ logo_path + match.getTeam().get(i).getTeamName4() + "\0");
			
			if(match.getPlayers() != null ) {
				for(int j=0; j <= match.getPlayers().size()-1; j++) {
					if(match.getPlayers().get(j).getTeamId() == match.getTeam().get(i).getTeamId()) {
						row = row + 1;
						total = total + match.getPlayers().get(j).getSoldForPoints();
					}
				}
			}
			
			
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_SquadSize*GEOM*TEXT SET "+ row + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ AuctionFunctions.ConvertToLakh((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total))  + "\0");
			if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) == 100000) {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "LAKH" + "\0");
			}else if((Integer.valueOf(match.getTeam().get(i).getTeamTotalPurse()) - total) <= 0) {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_PurseValue*GEOM*TEXT SET "+ "-"  + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$RupeeSymbol*ACTIVE SET 1 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$Row"+(i+1)+"$txt_Unit*GEOM*TEXT SET "+ "LAKHS" + "\0");
			}
			row = 0;
			total = 0;
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.300 \0");
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
	public void populateSquad(PrintWriter print_writer,String viz_scene,int team_id , Auction match,AuctionService auctionService, String session_selected_broadcaster) throws InterruptedException 
	{
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_SubHeader*GEOM*TEXT SET "+ "ISPL 2024 PLAYER AUCTION" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamFirstName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName2() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$txt_TeamLastName*GEOM*TEXT SET "+ auctionService.getTeams().get(team_id - 1).getTeamName3() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$Header$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		
		
		int row = 0;
		for(int i = 1; i <= 20; i++) {
			print_writer.println("-1 RENDERER*TREE*$Main$Row"+i+"*ACTIVE SET 0\0");
		}
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$OutWipe$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$TeamBadgeGrp$img_TeamLogo*TEXTURE*IMAGE SET "+ logo_path + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$BgAll$img_Base1*TEXTURE*IMAGE SET "+ base_path + "1/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
		
		if(match.getPlayers() != null) {
			for(int j=0; j <= match.getPlayers().size()-1; j++) {
				if(match.getPlayers().get(j).getTeamId() == team_id) {
					row = row + 1;
					print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"*ACTIVE SET 1\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$Base$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$NameAll$img_Text2*TEXTURE*IMAGE SET "+ text_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$Base$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Data$Row"+row+"$HighLight$NameAll$img_Base2*TEXTURE*IMAGE SET "+ base_path + "2/" + auctionService.getTeams().get(team_id - 1).getTeamName4() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_FirstName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getFirstname() + "\0");
					if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()!=null) {
						if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().toUpperCase() + "\0");
							
						}else {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getSurname()
									+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().charAt(0) +"Z" + "\0");
						}

					}else {
						if(auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().equalsIgnoreCase("U19")) {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().toUpperCase() + "\0");
							
						}else {
							print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+" - "+auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().charAt(0) +"Z" + "\0");
						}
						//print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$txt_LastName*GEOM*TEXT SET "+ " - "+ auctionService.getAllPlayer().get( match.getPlayers().get(j).getPlayerId()-1).getCategory().substring(0, 3).toUpperCase() + "\0");
					}
					
					
					if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("WICKET-KEEPER")) {
						print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Keeper" + "\0");
					}else {
						if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BATSMAN") || 
								auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BAT/KEEPER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("RHB")) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman" + "\0");
							}
							else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBatsmanStyle().toUpperCase().equalsIgnoreCase("LHB")) {
								System.out.println(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase());
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "Batsman_Lefthand" + "\0");
							}
						}else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("BOWLER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowler" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerIcon" + "\0");
									break;
								}
							}
						}else if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getRole().toUpperCase().equalsIgnoreCase("ALL-ROUNDER")) {
							if(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle() == null) {
								print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
							}else {
								switch(auctionService.getAllPlayer().get(match.getPlayers().get(j).getPlayerId()-1).getBowlerStyle().toUpperCase()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "FastBowlerAllrounder" + "\0");
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									print_writer.println("-1 RENDERER*TREE*$Main$Row"+row+"$HighLight$Img_Icon*TEXTURE*IMAGE SET "+ icon_path + "SpinBowlerAllrounder" + "\0");
									break;
								}
							}
						}
					}
				}
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 2.900 \0");
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
	public void populateProfileStats(PrintWriter print_writer, String whichType, int whichSide, Auction current_bid, AuctionService auctionService) {
		
		Player player = auctionService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == current_bid.getCurrentPlayers().getPlayerId()).findAny().orElse(null);
		String BowlStyle = "";
		switch (whichType.toUpperCase()) {
		case "CATEGORY":
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 0\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Title*GEOM*TEXT SET CATEGORY\0");
			if(player.getU19() != null && player.getU19().equalsIgnoreCase("YES")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET U19 - " + player.getCategory() + "\0");
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + player.getCategory() + "\0");
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
					if(player.getBatsmanStyle() == "RHB") {
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
					switch (player.getBowlerStyle().toUpperCase()) {
					case "F": case "FM": case "MF": case "M": case "SM":
						BowlStyle = "SEAM";
						break;

					case "SL": case "SO": case "CH": case "LB": case "LG": case "OB": case "WSL": case "WSR":
						BowlStyle = "SPIN";
						break;
					}
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Text*GEOM*TEXT SET BAT & "+BowlStyle+"\0");
				}else {
					BowlStyle = "SEAM";
				}
				break;
			case "BAT/KEEPER": case "WICKET-KEEPER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Double_Data$txt_Title*GEOM*TEXT SET BATTING STYLE\0");
				if(player.getBatsmanStyle() != null) {
					if(player.getBatsmanStyle() == "RHB") {
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
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Single_Data$txt_Text*GEOM*TEXT SET " + auctionService.getTeams().get(player.getLastYearTeam() - 1).getTeamName1() + "\0");
			break;
		case "STATS":
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$StatsGrp$Side"+whichSide+"$Select_DataType*FUNCTION*Omo*vis_con SET 2\0");
			for(Statistics stats : auctionService.getAllStats()) {
				if(stats.getPlayer_id() == player.getPlayerId()) {
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
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand + "anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = previewCommand + "anim_RTM$In_Out 0.500 anim_RTM$In_Out$Essentials 0.500 anim_RTM$In_Out$Essentials$In 0.500 anim_RTM$In_Out$Text$In 0.500";
				break;
			case "POPULATE-GOOGLY_POWER":
				previewCommand = previewCommand + "anim_Googly$In_Out 0.500 anim_Googly$In_Out$In 0.500";
				break;
			case "POPULATE-LOF_REMAINING_PURSE": case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD": case "POPULATE-SQUAD-PLAYER":
				previewCommand = previewCommand + "anim_LOF$In_Out 2.000 anim_LOF$In_Out$Essentials 2.000 anim_LOF$In_Out$Essentials$In 1.100 "
						+ "anim_LOF$In_Out$Header 2.000 anim_LOF$In_Out$Header$In 1.200 anim_LOF$In_Out$SubHeader 2.000 anim_LOF$In_Out$SubHeader$In 1.300 anim_LOF$In_Out$Main 2.000 ";
				switch (whatToProcess.toUpperCase()) {
				case "POPULATE-LOF_REMAINING_PURSE":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$RemainingPurse 2.000 "
							+ "anim_LOF$In_Out$Main$RemainingPurse$In 2.000 anim_LOF$In_Out$Main$Name 2.000 anim_LOF$In_Out$Main$Name$In 1.100 anim_LOF$In_Out$Main$Logo 2.000 anim_LOF$In_Out$Main$Logo$In 1.100";
					break;

				case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$TopBuys 2.000 anim_LOF$In_Out$Main$TopBuys$In 1.700";
					break;
				case "POPULATE-SQUAD-PLAYER":
					previewCommand = previewCommand + "anim_LOF$In_Out$Main$SquadSize_Category 2.000 anim_LOF$In_Out$Main$SquadSize_Category$In 1.900 CtegoryHighlight$Side1$"+rowHighlight +" 1.200";
					for(int i = rowHighlight; i<=5; i++) {
						previewCommand = previewCommand + " MoveForCatHighlight$Side1$"+(i+1) +" 1.200";
					}
					break;
				}
				break;
			}
		}else {
			if(data.isData_on_screen() && !whatToProcess.equalsIgnoreCase("POPULATE-FF-PLAYERPROFILE")) {
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500 ";
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE": case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$Header$Change_Out 0.500 Change_LOF$Header$Change_In 1.000 Change_LOF$SubHeader$Change_Out 0.500 Change_LOF$SubHeader$Change_In 1.000 ";
				break;
			}
			switch (which_graphics_onscreen.toUpperCase()) {
			case "LOF_REMAINING_PURSE":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_Out 0.860 Change_LOF$Name$Change_Out 0.600 Change_LOF$Logo$Change_Out 0.600 ";
				break;

			case "LOF_TOP_SOLD": case "LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_Out 0.680 ";
				break;
			case "SQUAD-PLAYER":
				if(!whatToProcess.equalsIgnoreCase("POPULATE-SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_Out 0.800";
				}
				break;
			}
			
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS":
				previewCommand = previewCommand+ "Change$Stats$Change_Out 1.000 Change$Stats$Change_In 1.300";
				break;
			case "POPULATE-LOF_REMAINING_PURSE":
				previewCommand = previewCommand + "Change_LOF$RemainingPurse$Change_In 1.600 Change_LOF$Name$Change_In 1.100 Change_LOF$Logo$Change_In 1.100";
				break;
			case "POPULATE-LOF_TOP_SOLD": case "POPULATE-LOF_TEAM_TOP_SOLD":
				previewCommand = previewCommand + "Change_LOF$TopBuys$Change_In 1.500";
				break;
			case "POPULATE-SQUAD-PLAYER":
				if(!which_graphics_onscreen.equalsIgnoreCase("SQUAD-PLAYER")) {
					previewCommand = previewCommand + "Change_LOF$SquadSize_Category$Change_In 1.700";
				}
				break;
			}	
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
		
	}
	public void processPreviewFullFrames(PrintWriter print_writer, String whatToProcess, int whichSide) {
		String previewCommand = "";
		
		if(whichSide == 1) {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT": case "POPULATE-PLAYERPROFILE_FF":
				previewCommand = "anim_Fullframe$In_Out 2.480 anim_Fullframe$In_Out$Essentials 2.480 anim_Fullframe$In_Out$Essentials$In 1.300 ";
				switch(whatToProcess.toUpperCase()) {
				case "POPULATE-IDENT":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Main$MatchId 2.480 anim_Fullframe$In_Out$Main$MatchId$In 2.340";
					break;
				case "POPULATE-PLAYERPROFILE_FF":
					previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
							+ "anim_Fullframe$In_Out$Main$Profile 2.480 anim_Fullframe$In_Out$Main$Profile$In 2.180";
					break;
				}
				break;
			}
		}else {
			switch (which_graphics_onscreen.toUpperCase()) {
			case "IDENT":
				previewCommand = "Change$Ident 2.340 Change$Ident$Change_Out 0.700 Change$Ident$Change_In 2.340 ";
				break;
			case "PLAYERPROFILE_FF":
				previewCommand = "anim_Fullframe$In_Out$Header 0.0 anim_Fullframe$In_Out$Header$In 0.0 Change$Profile 2.180 "
						+ "Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				break;
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-IDENT":
				previewCommand = previewCommand + "Change$Ident 2.340 Change$Ident$Change_Out 0.700 Change$Ident$Change_In 2.340 ";
				break;
			case "POPULATE-PLAYERPROFILE_FF":
				previewCommand = previewCommand + "anim_Fullframe$In_Out$Header 2.480 anim_Fullframe$In_Out$Header$In 2.340 "
						+ "Change$Profile 2.180 Change$Profile$Change_Out 0.620 Change$Profile$Change_In 2.180 ";
				break;
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/FullFrames " + "C:/Temp/Preview.jpg " + previewCommand + "\0");
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	
}