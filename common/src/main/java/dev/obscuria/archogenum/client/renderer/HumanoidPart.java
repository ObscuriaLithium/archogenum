package dev.obscuria.archogenum.client.renderer;

public enum HumanoidPart {
    HEAD("head"),
    HAT("hat"),
    BODY("body"),
    RIGHT_ARM("right_arm"),
    LEFT_ARM("left_arm"),
    RIGHT_LEG("right_leg"),
    LEFT_LEG("left_leg");

    public final String partName;

    HumanoidPart(String partName) {
        this.partName = partName;
    }
}
