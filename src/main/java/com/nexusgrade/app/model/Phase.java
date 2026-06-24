package com.nexusgrade.app.model;

public enum Phase {
    FOUNDATION("Foundation Phase", 0, 3),
    INTERMEDIATE("Intermediate Phase", 4, 6),
    SENIOR("Senior Phase", 7, 9),
    FET("FET Phase", 10, 12);

    private final String displayName;
    private final int minGrade;
    private final int maxGrade;

    Phase(String displayName, int min, int max) {
        this.displayName = displayName;
        this.minGrade = min;
        this.maxGrade = max;
    }

    // The "Magic" method
    public static Phase getByGrade(int grade) {
        for (Phase p : values()) {
            if (grade >= p.minGrade && grade <= p.maxGrade) return p;
        }
        throw new IllegalArgumentException("Invalid Grade: " + grade);
    }

    public String getDisplayName() { return displayName; }
}