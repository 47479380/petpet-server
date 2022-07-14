package xmmt.dituon.server.model;

import xmmt.dituon.share.TextExtraData;

import java.util.ArrayList;
import java.util.List;

public class TextDataPo {
    private String fromName="";
    private  String toName="";
    private String groupName="";

    private List<String> textList=new ArrayList<>();

    public TextDataPo() {
    }

    public TextDataPo(String fromName, String toName, String groupName, List<String> textList) {
        this.fromName = fromName;
        this.toName = toName;
        this.groupName = groupName;
        this.textList = textList;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getTextList() {
        return textList;
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
    }

    @Override
    public String toString() {
        return "TextDataPo{" +
                "fromName='" + fromName + '\'' +
                ", toName='" + toName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", textList=" + textList +
                '}';
    }
}
