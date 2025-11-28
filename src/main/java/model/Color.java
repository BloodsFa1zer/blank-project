package model;

public enum Color {
    GREEN("green"),
    RED("red"),
    YELLOW("yellow"),
    BLUE("blue"),
    WHITE("white"),
    PINK("pink"),
    PURPLE("purple"),
    ORANGE("orange"),
    BLACK("black");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Color fromString(String value) {
        for (Color c : Color.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown color: " + value);
    }
}

