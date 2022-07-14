package xmmt.dituon.server.model;

public class AvatarUrlPo {
    private String fromAvatar;
    private String  toAvatar;
    private String groupAvatar;
    private String botAvatar;

    public AvatarUrlPo() {
    }

    public AvatarUrlPo(String fromAvatar, String toAvatar, String groupAvatar, String botAvatar) {
        this.fromAvatar = fromAvatar;
        this.toAvatar = toAvatar;
        this.groupAvatar = groupAvatar;
        this.botAvatar = botAvatar;
    }

    public String getFromAvatar() {
        return fromAvatar;
    }

    public void setFromAvatar(String fromAvatar) {
        this.fromAvatar = fromAvatar;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

    public String getBotAvatar() {
        return botAvatar;
    }

    public void setBotAvatar(String botAvatar) {
        this.botAvatar = botAvatar;
    }

    @Override
    public String toString() {
        return "AvatarUrlPo{" +
                "fromAvatar='" + fromAvatar + '\'' +
                ", toAvatar='" + toAvatar + '\'' +
                ", groupAvatar='" + groupAvatar + '\'' +
                ", botAvatar='" + botAvatar + '\'' +
                '}';
    }
}
