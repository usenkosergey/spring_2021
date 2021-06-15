package org.example.web.dto;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RemoveObject {

    @NotBlank
    @NotEmpty
    @NotNull
    @Digits(integer = 4, fraction = 0)
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
