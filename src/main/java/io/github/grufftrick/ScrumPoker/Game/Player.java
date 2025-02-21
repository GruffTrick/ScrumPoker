package io.github.grufftrick.ScrumPoker.Game;

public class Player {
    private Long id;
    private String name;
    private Integer selection;
    private boolean hasSelected;

    /*
        CONSTRUCTORS
     */

    public Player(String id, String name) {
        this.name = name;
        this.hasSelected = false;
        this.selection = null;
    }


    /*
        METHODS
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isHasSelected() {
        return hasSelected;
    }

    public void setHasSelected(boolean hasSelected) {
        this.hasSelected = hasSelected;
    }
}
