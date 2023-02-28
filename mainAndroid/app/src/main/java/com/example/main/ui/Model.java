package com.example.main.ui;
//model for the condition list and recycler
public class Model {

    private boolean isSelected;
    private String condition;

    public Model(boolean isSelected, String condition) {
        this.isSelected = isSelected;
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
