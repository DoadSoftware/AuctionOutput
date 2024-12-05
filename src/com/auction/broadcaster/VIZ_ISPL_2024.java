package com.auction.broadcaster;

import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Player;
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
	public String which_graphics_onscreen = "BG";
	public int current_layer = 2, whichSide = 1, whichSideNotProfile=1;

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
		case "POPULATE-GOOGLY_POWER": case "POPULATE-PROFILE_STATS":
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
					populateIdent(print_writer, valueToProcess.split(",")[0],session_selected_broadcaster);
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
				}
			}
		case "ANIMATE-OUT-PROFILE":
		case "ANIMATE-OUT": case "CLEAR-ALL": case "ANIMATE-IN-PLAYERPROFILE": case "ANIMATE-IN-SQUAD": case "ANIMATE-IN-REMAINING_PURSE_ALL": case "ANIMATE-IN-SINGLE_PURSE":
		case "ANIMATE-IN-TOP_SOLD": case "ANIMATE-IN-NAMESUPER": case "ANIMATE-IN-IDENT": case "ANIMATE-IN-TOP_SOLD_TEAM": case "ANIMATE-IN-CURR_BID":
		case "ANIMATE-IN-RTM_AVAILABLE": case "ANIMATE-IN-RTM_ENABLED": case "ANIMATE-IN-GOOGLY_POWER": case "ANIMATE-IN-PROFILE_STATS": case "ANIMATE-OUT-PLAYER_STAT":
		
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
				case "ANIMATE-IN-IDENT":
					print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
					which_graphics_onscreen = "IDENT";
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
					
		            which_graphics_onscreen = "";
		            data.setBid_Start_or_not(false);
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
					case "SQUAD": case "REMAINING_PURSE_ALL": case "SINGLE_PURSE": case "TOP_SOLD": case "NAMESUPER": case "TOP_SOLD_TEAM": case "IDENT":
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

	public void populateIdent(PrintWriter print_writer,String viz_scene,String session_selected_broadcaster) {
		print_writer.println("-1 RENDERER*TREE*$Main$txt_Info2*GEOM*TEXT SET "+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$txt_Info2*GEOM*TEXT SET "+ "LOTUS BALLROOM - JIO WORLD CONVENTION CENTRE - MUMBAI" + "\0");
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene + " C:/Temp/Preview.png In 1.600 \0");
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
					data.setPlayer_sold_or_unsold(true);
				}else if(auction.getPlayers().get(i).getSoldOrUnsold().equalsIgnoreCase(AuctionUtil.UNSOLD)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$RightDataGrp$Side" + which_side + 
							"$Select_DataType*FUNCTION*Omo*vis_con SET 3 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Text1"
							+ "*TEXTURE*IMAGE SET " + text_path_1 + "UNSOLD" + " \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$gfx_ScoreBug$MoveForStats$TopDataGrp$Side" + which_side + "$img_Base1"
							+ "*TEXTURE*IMAGE SET " + base_path_1 + "UNSOLD" + " \0");
					
					data.setPlayer_sold_or_unsold(true);
				}
				data.setBid_result(auction.getPlayers().get(i).getSoldOrUnsold());
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
							print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change$CurrentBid START \0");
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
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-FF-PLAYERPROFILE":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500";
				break;
			case "POPULATE-PROFILE_STATS":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500 anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				break;
			case "POPULATE-RTM_AVAILABLE":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$CurrentBid 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$CurrentBid$In 1.500 anim_RTM$In_Out 0.500 anim_RTM$In_Out$Essentials 0.500 anim_RTM$In_Out$Essentials$In 0.500 anim_RTM$In_Out$Text$In 0.500";
				if(isProfileStatsOnScreen) {
					previewCommand =previewCommand+ " anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				}
				break;
			case "POPULATE-GOOGLY_POWER":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$CurrentBid 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$CurrentBid$In 1.500 anim_Googly$In_Out 0.500 anim_Googly$In_Out$In 0.500";
				if(isProfileStatsOnScreen) {
					previewCommand =previewCommand+ " anim_ScoreBug$In_Out$Stats 2.500 anim_ScoreBug$In_Out$Stats$In 1.760";
				}
				break;

			}
		}else {
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-PROFILE_STATS":
				previewCommand = "anim_ScoreBug$In_Out 2.500 anim_ScoreBug$In_Out$Essentials 2.500 anim_ScoreBug$In_Out$Essentials$In 1.500 "
						+ "anim_ScoreBug$In_Out$Left_Data 1.500 anim_ScoreBug$In_Out$Left_Data$In 1.500 anim_ScoreBug$In_Out$RightData 2.500 anim_ScoreBug$In_Out$RightData$Gavel 2.500 "
						+ "anim_ScoreBug$In_Out$RightData$Gavel$in 2.500 Change$Stats$Change_Out 1.000 Change$Stats$Change_In 1.300";
				break;
			}	
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*/Default/Overlays " + "C:/Temp/Preview.tga "+previewCommand+"\0");
		
	}
	
	public void processAnimation(PrintWriter print_writer, String animationName,String animationCommand, String which_broadcaster,int which_layer)
	{
		print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
	}
	
	public String toString() {
		return "Doad [status=" + status + ", slashOrDash=" + slashOrDash + "]";
	}
	
	
}