public class NormalGroup extends Group {
    private String gid;

    public NormalGroup(String gid) {
        this.gid = gid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public Group findGroup(String gid) {
        if (this.gid.equalsIgnoreCase(gid))
            return this;
        for (Group g : this.subGroups) {
            Group result = g.findGroup(gid);
            if (result != null)
                return result;
        }
        return null;
    }
}
