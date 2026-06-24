package com.nexusgrade.app.model;

public enum Department {

    // 1. Languages (HL, FAL, SAL)
    LANGUAGES("Languages", "Home and First Additional Languages (English, isiZulu, Afrikaans, etc.)"),

    // 2. Mathematical Sciences
    MATHEMATICS("Mathematics", "Mathematics, Mathematical Literacy, and Technical Mathematics"),

    // 3. Natural Sciences
    SCIENCES("Natural Sciences", "Physical Sciences, Life Sciences, and Natural Science (Grade 8-9)"),

    // 4. Business, Commerce, and Management (BCM)
    COMMERCE("Commerce", "Accounting, Business Studies, Economics, and EMS"),

    // 5. Human and Social Sciences
    HUMANITIES("Humanities", "History, Geography, and Social Sciences (Grade 8-9)"),

    // 6. Technology and Computer Science
    SERVICES_AND_TECH("Tech & Services", "CAT, IT, Tourism, Hospitality, and Consumer Studies"),

    // 7. Life Orientation
    LIFE_ORIENTATION("Life Orientation", "Life Orientation and Physical Education"),

    // 8. Creative Arts
    ARTS("Arts", "Visual Arts, Dramatic Arts, and Music");

    private final String displayName;
    private final String description;

    Department(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}