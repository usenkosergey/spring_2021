package org.example.web.dto;

public class RemoveObject {

    private String removeValue;
    private String removeTitle;

    public RemoveObject() {
    }

    public String getRemoveValue() {
        return removeValue;
    }

    public void setRemoveValue(String removeValue) {
        this.removeValue = removeValue;
    }

    public String getRemoveTitle() {
        return removeTitle;
    }

    public void setRemoveTitle(String removeTitle) {
        this.removeTitle = removeTitle;
    }

    @Override
    public String toString() {
        return "RemoveObject -> {" +
                "removeValue='" + removeValue + '\'' +
                ", removeTitle='" + removeTitle + '\'' +
                '}';
    }
}
