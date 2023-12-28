package club.windofall.woftv4.entity;
public class ConfigE {
    private boolean LiveChat;
    private CE_BiliLive biliLive;
    public CE_BiliLive getBiliLive() {
        return biliLive;
    }
    public void setBiliLive(CE_BiliLive biliLive) {
        this.biliLive = biliLive;
    }

    public boolean isLiveChat() {
        return LiveChat;
    }

    public void setLiveChat(boolean liveChat) {
        LiveChat = liveChat;
    }

    public ConfigE(){
        this.LiveChat = false;
        this.biliLive = new CE_BiliLive();
    }
}


