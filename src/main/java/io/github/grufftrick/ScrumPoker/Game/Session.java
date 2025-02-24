package io.github.grufftrick.ScrumPoker.Game;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class Session {
    private String id;
	private int maxPlayers;
	private List<Player> players;

	@Autowired
	public Session(String id, int maxPlayers) {
		this.maxPlayers = maxPlayers;
		this.id = id;
		this.players = new ArrayList<>();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public List<Player> getPlayers() {
        return players;
    }

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

}
