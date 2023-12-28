package club.windofall.woftv4.entity;

public class CE_BiliLive {
    private String prefix;
    private long room_id;
    private long period;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public CE_BiliLive(String prefix, long room_id, long period) {
        this.prefix = prefix;
        this.room_id = room_id;
        this.period = period;
    }
    public CE_BiliLive(){
        this.prefix = "Live";
        this.room_id = 0;
        this.period = 1000;
    }
}