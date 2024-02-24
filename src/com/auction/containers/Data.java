package com.auction.containers;

public class Data {
	
	private boolean data_on_screen;
	private boolean player_sold_or_unsold;
	private boolean bid_Start_or_not;
	private int player_id;

	public boolean isData_on_screen() {
		return data_on_screen;
	}

	public void setData_on_screen(boolean data_on_screen) {
		this.data_on_screen = data_on_screen;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	public boolean isPlayer_sold_or_unsold() {
		return player_sold_or_unsold;
	}

	public void setPlayer_sold_or_unsold(boolean player_sold_or_unsold) {
		this.player_sold_or_unsold = player_sold_or_unsold;
	}

	public boolean isBid_Start_or_not() {
		return bid_Start_or_not;
	}

	public void setBid_Start_or_not(boolean bid_Start_or_not) {
		this.bid_Start_or_not = bid_Start_or_not;
	}
	

}
