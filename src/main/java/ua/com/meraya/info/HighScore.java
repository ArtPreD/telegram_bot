package ua.com.meraya.info;

import ua.com.meraya.database.entity.MyUser;
import ua.com.meraya.database.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

public class HighScore {

    private UserRepository userRepository;

    public HighScore(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLeaders(){
        String leaders = "";
        List<MyUser> users = userRepository.findAll();
        users.sort(new Comparator<MyUser>() {
            @Override
            public int compare(MyUser o2, MyUser o1) {
                return (int) (o1.getScore() - o2.getScore());
            }
        });
        MyUser[] leaderList = users.toArray(new MyUser[users.size()]);

        leaders = leaders.concat("Десять самых умных: \n");
        int count;

        if (leaderList.length < 10){
            count = leaderList.length;
        }else {
            count = 10;
        }

        for (int i = 0; i < count; i++){
            MyUser user = leaderList[i];
            String username = user.getUsername();
            if (username == null){
                username = "\"я не умею ставить ник\"";
            }
            leaders = leaders.concat(i+1 + ". " + username + " --- " + user.getScore() + "\n");
        }

        return leaders;
    }
}
