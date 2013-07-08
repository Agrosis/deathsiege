package com.jantox.siege.gamejolt;

public class Trophy {

    int id;
    String title, desc, diff;
    boolean achieved;

    public Trophy(int id, String title, String desc, String diff, String achieved) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.diff = diff;

        if(achieved.equals("false"))
            this.achieved = false;
        else
            this.achieved = true;

        //System.out.println("id: " + id + " title: " + title + " desc: " + desc + " 2diff: " + diff + " achieved: " + String.valueOf(achieved));
    }

    public int getID() {
        return id;
    }

    public void setAchieved(boolean b) {
        achieved = b;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAchieved() {
        return achieved;
    }
}
