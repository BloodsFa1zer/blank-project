package model;

public enum Preciousness {
    PRECIOUS("precious"),
    SEMI_PRECIOUS("semi-precious");

    private final String value;

    Preciousness(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Preciousness fromString(String value) {
        for (Preciousness p : Preciousness.values()) {
            if (p.value.equals(value)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown preciousness: " + value);
    }
}

