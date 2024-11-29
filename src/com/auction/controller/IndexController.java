package com.auction.controller;

import java.io.File;
import java.io.FileFilter;

import java.io.IOException;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auction.broadcaster.Doad;
import com.auction.broadcaster.ISPL;
import com.auction.broadcaster.ISPL_VIZ;
import com.auction.containers.Configurations;
import com.auction.containers.Data;
import com.auction.containers.Scene;
import com.auction.model.Auction;
import com.auction.model.NameSuper;
import com.auction.model.Player;
import com.auction.model.Team;
import com.auction.service.AuctionService;
import com.auction.util.AuctionFunctions;
import com.auction.util.AuctionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class IndexController 
{
	@Autowired
	AuctionService auctionService;
	public static Configurations session_Configurations;
	public static Auction session_auction;
	public static Auction session_curr_bid;
	public static Socket session_socket;
	public static Doad this_doad;
	public static ISPL this_ispl;
	public static ISPL_VIZ this_ispl_viz;
	public static PrintWriter print_writer;
	public static String expiry_date = "2024-12-31";
	public static String error_message = "";
	public static String current_date = "";
	public static String Current_File_Name = "";
	public int current_layer = 1;
	
	List<NameSuper> session_nameSupers = new ArrayList<NameSuper>();
	List<Team> session_team = new ArrayList<Team>();
	List<Player> session_player = new ArrayList<Player>();
	
	List<Scene> scene = new ArrayList<Scene>();
	List<Auction> auction_file = new ArrayList<Auction>();
	List<Scene> session_selected_scenes = new ArrayList<Scene>();
	Data data = new Data();
	Auction auc = new Auction();
	
	int whichInning,player_id,team_id,session_port;
	String session_selected_broadcaster,selected_layer,selected_scene,session_selected_ip, viz_scene_path, which_graphics_onscreen;
	boolean is_Infobar_on_Screen = false;
	boolean is_director_on_bottom = false;
	boolean is_Ident_on_Screen = false;
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException, IOException, ParseException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = AuctionFunctions.getOnlineCurrentDate();
		}
		
			model.addAttribute("session_viz_scenes", new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".via") && pathname.isFile();
			    }
			}));
	
			model.addAttribute("match_files", new File(AuctionUtil.AUCTION_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}));
			
			if(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + AuctionUtil.OUTPUT_XML).exists()) {
				session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
						new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + AuctionUtil.OUTPUT_XML));
			} else {
				session_Configurations = new Configurations();
				JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
						new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + AuctionUtil.OUTPUT_XML));
			}
			
			model.addAttribute("session_Configurations",session_Configurations);
		
			return "initialise";
	}

	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String outputPage(ModelMap model,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "which_layer", required = false, defaultValue = "") String which_layer,
			@RequestParam(value = "which_scene", required = false, defaultValue = "") String which_scene,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber) 
					throws UnknownHostException, IOException, JAXBException, IllegalAccessException, InvocationTargetException, ParseException, InterruptedException 
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
		
			session_port =  vizPortNumber;
			session_selected_ip = vizIPAddresss;
			
			data = new Data();
			this_doad = new Doad();
			this_ispl = new ISPL();
			this_ispl_viz = new ISPL_VIZ();
			session_selected_broadcaster = select_broadcaster;
			selected_layer = which_layer;
			selected_scene = which_scene;
			session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
			print_writer = new PrintWriter(session_socket.getOutputStream(), true);
			
			switch (session_selected_broadcaster) {
				
			case "HANDBALL":
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Sports/Everest_Handball_Auction_2023/Scenes/BG.sum"
						,"3")); // Front layer
				session_selected_scenes.add(new Scene("","1"));
				session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*In START;");
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Loop START;");
				this_doad.which_graphics_onscreen = "BG";
				break;
			case "ISPL":
				session_selected_scenes.add(new Scene("D:/DOAD_In_House_Everest/Everest_Cricket/Everest_ISPL_Auction_2024/Scenes/BG.sum"
						,"3")); // Front layer
				session_selected_scenes.add(new Scene("","1"));
				session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*In START;");
//				print_writer.println("LAYER3*EVEREST*STAGE*DIRECTOR*Loop START;");
				this_doad.which_graphics_onscreen = "BG";
				break;	
			}
			
			getDataFromDB();
			
			session_Configurations = new Configurations(selectedMatch, select_broadcaster, vizIPAddresss, vizPortNumber);
			
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CONFIGURATIONS_DIRECTORY + AuctionUtil.OUTPUT_XML));
			
			session_auction = new Auction();
			session_auction = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON), Auction.class);
			//session_auction = AuctionFunctions.populateMatchVariables(auctionService, session_auction);
			
			session_curr_bid = new Auction();
			session_curr_bid = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON), Auction.class);
			
			Current_File_Name = selectedMatch;
			
			model.addAttribute("session_auction", session_auction);
			model.addAttribute("session_port", session_port);
			model.addAttribute("session_selected_ip", session_selected_ip);
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			model.addAttribute("selected_layer", selected_layer);
			model.addAttribute("selected_scene", selected_scene);
			model.addAttribute("licence_expiry_message","Software licence expires on " + 
					new SimpleDateFormat("E, dd MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			return "output";
		}
	}

	@RequestMapping(value = {"/processAuctionProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processAuctionProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws IOException, IllegalAccessException, InvocationTargetException, JAXBException, InterruptedException 
	{
		
		switch (whatToProcess.toUpperCase()) {
		case "RE_READ_DATA":
			getDataFromDB();
			return JSONObject.fromObject(session_auction).toString();
		case "READ-MATCH-AND-POPULATE":
			session_auction = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.AUCTION_JSON), Auction.class);
			session_curr_bid = new ObjectMapper().readValue(new File(AuctionUtil.AUCTION_DIRECTORY + AuctionUtil.CURRENT_BID_JSON), Auction.class);
			
			switch (session_selected_broadcaster) {
			case "HANDBALL": case "ISPL":
				this_doad.updateData(session_selected_scenes.get(0), session_auction,auctionService,print_writer);
				break;
			case "ISPL_VIZ":
				if(this_ispl_viz.data.isBid_Start_or_not() == true) {
					this_ispl_viz.data.setWhichside(2);
				}
				this_ispl_viz.updateData(session_selected_scenes.get(0), session_auction,
						session_curr_bid,auctionService,print_writer);
				break;
			}
			
			return JSONObject.fromObject(session_auction).toString();
		
		default:
			if(whatToProcess.contains("_GRAPHICS-OPTIONS")) {
				return JSONArray.fromObject(GetSpecificDataList(whatToProcess)).toString();
			}
			switch (session_selected_broadcaster.toUpperCase()) {
			case "HANDBALL": case "ISPL": 
				this_doad.ProcessGraphicOption(whatToProcess, session_auction, auctionService, print_writer, session_selected_scenes, valueToProcess);
			case "ISPL_VIZ":
				this_ispl_viz.ProcessGraphicOption(whatToProcess, session_auction, session_curr_bid, auctionService, print_writer, session_selected_scenes, valueToProcess);
				break;
			}
			return JSONObject.fromObject(session_auction).toString();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> GetSpecificDataList(String whatToProcess) throws IOException {
		switch (whatToProcess) {
		case "NAMESUPER_GRAPHICS-OPTIONS":
		    return (List<T>) session_nameSupers;
		case "PLAYERPROFILE_GRAPHICS-OPTIONS": 
		    return (List<T>) session_player;
		case "SQUAD_GRAPHICS-OPTIONS": case "SINGLE_PURSE_GRAPHICS-OPTIONS": case "TOP-SOLD_TEAM_GRAPHICS-OPTIONS": 
		    return (List<T>) session_team;
		}
	    return null;
	}

	public void getDataFromDB()
	{
		session_nameSupers = auctionService.getNameSupers();
		session_team = auctionService.getTeams();
		session_player = auctionService.getAllPlayer();
	}
}