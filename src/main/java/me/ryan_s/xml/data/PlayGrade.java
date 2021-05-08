package me.ryan_s.xml.data;

/**
 * Created by SimplyBallistic on 19/07/2019
 *
 * @author SimplyBallistic
 **/
public enum PlayGrade {//outline the grades and their styles for styling
    A_GRADE("A-Grade", "success"), B_GRADE("B-Grade", "warning"), C_GRADE("C-Grade", "danger"), ASSOCIATE("Associate", "primary");

    private final String name;
    private final String style;

    PlayGrade(String name, String style) {

        this.name = name;
        this.style = style;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getStyle() {

        return style;
    }
}
