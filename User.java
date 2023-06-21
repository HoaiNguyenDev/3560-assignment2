import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class User extends Observable {
    private String uid;
    private List<User> followers;
    private List<User> followings;
    private List<String> tweets;

    public User(String uid) {
        this.uid = uid;
        followers = new ArrayList<>();
        followings = new ArrayList<>();
        tweets = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowings() {
        return followings;
    }

    public void setFollowings(List<User> followings) {
        this.followings = followings;
    }

    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }


    void follow(User user) {
        this.followings.add(user);
        user.addFollower(this);
    }

    private void addFollower(User user) {
        this.followers.add(user);
    }

    void tweet(String tweet) {
        this.tweets.add(tweet);
        setChanged();
        notifyObservers();
    }
}
