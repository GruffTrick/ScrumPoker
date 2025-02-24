package io.github.grufftrick.ScrumPoker.Game;

public class Player {
    private String id;
    private String name;
    private Integer selection;
    private boolean hasSelected;



    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.hasSelected = false;
        this.selection = null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSelection() {
        return selection;
    }

    public void setSelection(Integer selection) {
        this.selection = selection;
    }

    public boolean hasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
}
