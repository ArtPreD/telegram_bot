package ua.com.meraya.game.modules;

import org.telegram.telegrambots.api.objects.User;
import ua.com.meraya.game.Game;

import java.util.HashMap;
import java.util.Map;

public class GameHandler {

    private Map<Integer, Game> gameList;

    public GameHandler() {
        this.gameList = new HashMap<>();
    }

    public void addGame(Integer userId, Game game){
        gameList.put(userId, game);
    }

    public void removeGame(Integer userId){
        gameList.remove(userId);
    }

    public Game findGameByUser(Integer userId){
        return gameList.get(userId);
    }
}
