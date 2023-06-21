import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class Group extends Observable {
    protected List<User> users;
    protected List<NormalGroup> subGroups;

    public Group() {
        users = new ArrayList<>();
        subGroups = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<NormalGroup> getGroups() {
        return subGroups;
    }

    public void setGroups(List<NormalGroup> subGroups) {
        this.subGroups = subGroups;
    }

    public int countUser() {
        int result = users.size();
        for (Group g : subGroups) {
            result += g.countUser();
        }
        return result;
    }

    public int countGroup() {
        int result = 1;
        for (Group g : subGroups) {
            result += g.countGroup();
        }
        return result;
    }

    public int countMessage() {
        int result = 0;
        for (User user : users) {
            result += user.getTweets().size();
        }
        for (Group g : subGroups) {
            result += g.countMessage();
        }
        return result;
    }

    public double percentPositive() {
        double total = 0, positive = 0;
        for (User user : users) {
            total += user.getTweets().size();
            for (String tweet : user.getTweets()) {
                if (tweet.contains("good") || tweet.contains("excellent") || tweet.contains("great")) {
                    positive++;
                }
            }
        }
        for (Group g : subGroups) {
            total += g.countMessage();
        }
        return positive / total;
    }

    public void addUser(User user) {
        this.users.add(user);
        setChanged();
        notifyObservers();
    }

    public abstract Group findGroup(String gid);

    public void addGroup(NormalGroup group) {
        this.subGroups.add(group);
        setChanged();
        notifyObservers();
    }

    public User findUser(String uid) {
        for (User u : this.users) {
            if (u.getUid().equalsIgnoreCase(uid)) return u;
        }
        for (Group g : this.subGroups) {
            User result = g.findUser(uid);
            if (result != null)
                return result;
        }
        return null;
    }
}
