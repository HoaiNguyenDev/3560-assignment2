public class RootGroup extends Group {
    private static volatile RootGroup instance;

    private RootGroup() {
    }

    public static RootGroup getInstance() {
        if (instance == null) {
            synchronized (RootGroup.class) {
                if (instance == null) {
                    instance = new RootGroup();
                }
            }
        }
        return instance;
    }

    @Override
    public int countGroup() {
        return super.countGroup() - 1;
    }

    @Override
    public Group findGroup(String gid) {
        if ("root".equalsIgnoreCase(gid)) return this;
        for (Group g : this.subGroups) {
            Group result = g.findGroup(gid);
            if (result != null)
                return result;
        }
        return null;
    }
}
