package com.nexusgrade.app.model;

/**
 * Comprehensive color enum for consistent theming across the application.
 * Includes semantic colors, status colors, and UI accent colors.
 */
public enum Color {

    // ── Primary Colors ──
    PRIMARY("#4f46e5", "Indigo", "primary"),
    PRIMARY_LIGHT("#818cf8", "Indigo Light", "primary-light"),
    PRIMARY_DARK("#3730a3", "Indigo Dark", "primary-dark"),

    // ── Secondary Colors ──
    SECONDARY("#64748b", "Slate", "secondary"),
    SECONDARY_LIGHT("#94a3b8", "Slate Light", "secondary-light"),
    SECONDARY_DARK("#475569", "Slate Dark", "secondary-dark"),

    // ── Success Colors ──
    SUCCESS("#16a34a", "Green", "success"),
    SUCCESS_LIGHT("#86efac", "Green Light", "success-light"),
    SUCCESS_DARK("#15803d", "Green Dark", "success-dark"),
    SUCCESS_BG("#f0fdf4", "Green Background", "success-bg"),

    // ── Danger Colors ──
    DANGER("#dc2626", "Red", "danger"),
    DANGER_LIGHT("#fca5a5", "Red Light", "danger-light"),
    DANGER_DARK("#b91c1c", "Red Dark", "danger-dark"),
    DANGER_BG("#fef2f2", "Red Background", "danger-bg"),

    // ── Warning Colors ──
    WARNING("#d97706", "Amber", "warning"),
    WARNING_LIGHT("#fcd34d", "Amber Light", "warning-light"),
    WARNING_DARK("#b45309", "Amber Dark", "warning-dark"),
    WARNING_BG("#fffbeb", "Amber Background", "warning-bg"),

    // ── Info Colors ──
    INFO("#2563eb", "Blue", "info"),
    INFO_LIGHT("#60a5fa", "Blue Light", "info-light"),
    INFO_DARK("#1d4ed8", "Blue Dark", "info-dark"),
    INFO_BG("#eff6ff", "Blue Background", "info-bg"),

    // ── Neutral Colors ──
    WHITE("#ffffff", "White", "white"),
    BLACK("#0f172a", "Slate 900", "black"),

    GRAY_50("#f8fafc", "Gray 50", "gray-50"),
    GRAY_100("#f1f5f9", "Gray 100", "gray-100"),
    GRAY_200("#e2e8f0", "Gray 200", "gray-200"),
    GRAY_300("#cbd5e1", "Gray 300", "gray-300"),
    GRAY_400("#94a3b8", "Gray 400", "gray-400"),
    GRAY_500("#64748b", "Gray 500", "gray-500"),
    GRAY_600("#475569", "Gray 600", "gray-600"),
    GRAY_700("#334155", "Gray 700", "gray-700"),
    GRAY_800("#1e293b", "Gray 800", "gray-800"),
    GRAY_900("#0f172a", "Gray 900", "gray-900"),

    // ── Brand Colors ──
    BRAND("#0081cf", "Brand Blue", "brand"),
    BRAND_LIGHT("#4ba3e0", "Brand Blue Light", "brand-light"),
    BRAND_DARK("#005e9e", "Brand Blue Dark", "brand-dark"),

    // ── Accent Colors ──
    ACCENT_PINK("#ec4899", "Pink", "pink"),
    ACCENT_PINK_LIGHT("#f9a8d4", "Pink Light", "pink-light"),
    ACCENT_PURPLE("#8b5cf6", "Purple", "purple"),
    ACCENT_PURPLE_LIGHT("#c4b5fd", "Purple Light", "purple-light"),
    ACCENT_CYAN("#06b6d4", "Cyan", "cyan"),
    ACCENT_CYAN_LIGHT("#67e8f9", "Cyan Light", "cyan-light"),
    ACCENT_TEAL("#14b8a6", "Teal", "teal"),
    ACCENT_TEAL_LIGHT("#5eead4", "Teal Light", "teal-light"),
    ACCENT_ORANGE("#f97316", "Orange", "orange"),
    ACCENT_ORANGE_LIGHT("#fdba74", "Orange Light", "orange-light"),
    ACCENT_ROSE("#f43f5e", "Rose", "rose"),
    ACCENT_ROSE_LIGHT("#fda4af", "Rose Light", "rose-light"),
    ACCENT_EMERALD("#10b981", "Emerald", "emerald"),
    ACCENT_EMERALD_LIGHT("#6ee7b7", "Emerald Light", "emerald-light"),

    // ── Status Colors ──
    STATUS_ACTIVE("#16a34a", "Active Green", "status-active"),
    STATUS_INACTIVE("#dc2626", "Inactive Red", "status-inactive"),
    STATUS_PENDING("#d97706", "Pending Amber", "status-pending"),
    STATUS_COMPLETED("#2563eb", "Completed Blue", "status-completed"),
    STATUS_ARCHIVED("#64748b", "Archived Gray", "status-archived"),

    // ── Notification Colors ──
    NOTIFICATION_INFO("#2563eb", "Info Blue", "notif-info"),
    NOTIFICATION_SUCCESS("#16a34a", "Success Green", "notif-success"),
    NOTIFICATION_WARNING("#d97706", "Warning Amber", "notif-warning"),
    NOTIFICATION_ERROR("#dc2626", "Error Red", "notif-error"),

    // ── UI Element Colors ──
    UI_BORDER("#e2e8f0", "Border Color", "ui-border"),
    UI_BG("#f8fafc", "Background Color", "ui-bg"),
    UI_CARD("#ffffff", "Card Background", "ui-card"),
    UI_SHADOW("rgba(0,0,0,0.04)", "Shadow Color", "ui-shadow"),
    UI_SHADOW_DARK("rgba(0,0,0,0.12)", "Dark Shadow", "ui-shadow-dark"),
    UI_HOVER("#f1f5f9", "Hover Background", "ui-hover"),

    // ── Text Colors ──
    TEXT_PRIMARY("#0f172a", "Primary Text", "text-primary"),
    TEXT_SECONDARY("#475569", "Secondary Text", "text-secondary"),
    TEXT_MUTED("#94a3b8", "Muted Text", "text-muted"),
    TEXT_WHITE("#ffffff", "White Text", "text-white"),
    TEXT_SUCCESS("#16a34a", "Success Text", "text-success"),
    TEXT_DANGER("#dc2626", "Danger Text", "text-danger"),
    TEXT_WARNING("#d97706", "Warning Text", "text-warning"),
    TEXT_INFO("#2563eb", "Info Text", "text-info"),

    // ── Gradient Colors ──
    GRADIENT_PRIMARY("linear-gradient(135deg, #4f46e5, #7c3aed)", "Primary Gradient", "gradient-primary"),
    GRADIENT_SUCCESS("linear-gradient(135deg, #16a34a, #22c55e)", "Success Gradient", "gradient-success"),
    GRADIENT_DANGER("linear-gradient(135deg, #dc2626, #ef4444)", "Danger Gradient", "gradient-danger"),
    GRADIENT_WARNING("linear-gradient(135deg, #d97706, #f59e0b)", "Warning Gradient", "gradient-warning"),
    GRADIENT_INFO("linear-gradient(135deg, #2563eb, #3b82f6)", "Info Gradient", "gradient-info"),
    GRADIENT_BRAND("linear-gradient(135deg, #0081cf, #4ba3e0)", "Brand Gradient", "gradient-brand"),
    GRADIENT_SUNSET("linear-gradient(135deg, #f43f5e, #f97316)", "Sunset Gradient", "gradient-sunset"),
    GRADIENT_OCEAN("linear-gradient(135deg, #06b6d4, #2563eb)", "Ocean Gradient", "gradient-ocean"),
    GRADIENT_FOREST("linear-gradient(135deg, #10b981, #14b8a6)", "Forest Gradient", "gradient-forest"),
    GRADIENT_NIGHT("linear-gradient(135deg, #1e293b, #0f172a)", "Night Gradient", "gradient-night");

    // ── Fields ──
    private final String hexValue;
    private final String displayName;
    private final String cssClass;

    // ── Constructor ──
    Color(String hexValue, String displayName, String cssClass) {
        this.hexValue = hexValue;
        this.displayName = displayName;
        this.cssClass = cssClass;
    }

    // ── Getters ──
    public String getHexValue() {
        return hexValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssClass() {
        return cssClass;
    }

    /**
     * Get RGB values from hex color.
     * @return int[] containing [red, green, blue]
     */
    public int[] getRgb() {
        String hex = hexValue.replace("#", "");
        return new int[]{
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16)
        };
    }

    /**
     * Get RGBA string for CSS.
     * @param alpha Opacity value (0.0 - 1.0)
     * @return RGBA string
     */
    public String getRgba(double alpha) {
        int[] rgb = getRgb();
        return String.format("rgba(%d, %d, %d, %.2f)", rgb[0], rgb[1], rgb[2], alpha);
    }

    /**
     * Check if color is light or dark for text contrast.
     * @return true if light, false if dark
     */
    public boolean isLight() {
        int[] rgb = getRgb();
        // Using relative luminance formula
        double luminance = (0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]) / 255;
        return luminance > 0.5;
    }

    /**
     * Get recommended text color (black or white) for this background.
     * @return Color.WHITE or Color.BLACK
     */
    public Color getContrastColor() {
        return isLight() ? BLACK : WHITE;
    }

    /**
     * Get the Tailwind CSS class for this color.
     * @param variant The variant (bg, text, border, etc.)
     * @return Tailwind CSS class string
     */
    public String getTailwindClass(String variant) {
        String prefix = switch (variant) {
            case "bg" -> "bg";
            case "text" -> "text";
            case "border" -> "border";
            case "ring" -> "ring";
            default -> "";
        };

        if (prefix.isEmpty()) return "";

        // Map enum to Tailwind color names
        String tailwindColor = switch (this) {
            case PRIMARY, PRIMARY_LIGHT, PRIMARY_DARK -> "indigo";
            case SECONDARY, SECONDARY_LIGHT, SECONDARY_DARK -> "slate";
            case SUCCESS, SUCCESS_LIGHT, SUCCESS_DARK, SUCCESS_BG -> "green";
            case DANGER, DANGER_LIGHT, DANGER_DARK, DANGER_BG -> "red";
            case WARNING, WARNING_LIGHT, WARNING_DARK, WARNING_BG -> "amber";
            case INFO, INFO_LIGHT, INFO_DARK, INFO_BG -> "blue";
            case BRAND, BRAND_LIGHT, BRAND_DARK -> "blue";
            default -> "gray";
        };

        String shade = switch (this) {
            case PRIMARY, SUCCESS, DANGER, WARNING, INFO, BRAND -> "600";
            case PRIMARY_LIGHT, SUCCESS_LIGHT, DANGER_LIGHT, WARNING_LIGHT, INFO_LIGHT, BRAND_LIGHT -> "400";
            case PRIMARY_DARK, SUCCESS_DARK, DANGER_DARK, WARNING_DARK, INFO_DARK, BRAND_DARK -> "800";
            case SUCCESS_BG, DANGER_BG, WARNING_BG, INFO_BG -> "50";
            default -> "500";
        };

        return prefix + "-" + tailwindColor + "-" + shade;
    }

    /**
     * Get Bootstrap class for this color.
     * @param variant The variant (bg, text, border)
     * @return Bootstrap class string
     */
    public String getBootstrapClass(String variant) {
        String suffix = switch (this) {
            case PRIMARY, PRIMARY_LIGHT, PRIMARY_DARK, BRAND, BRAND_LIGHT, BRAND_DARK -> "primary";
            case SUCCESS, SUCCESS_LIGHT, SUCCESS_DARK, SUCCESS_BG -> "success";
            case DANGER, DANGER_LIGHT, DANGER_DARK, DANGER_BG -> "danger";
            case WARNING, WARNING_LIGHT, WARNING_DARK, WARNING_BG -> "warning";
            case INFO, INFO_LIGHT, INFO_DARK, INFO_BG -> "info";
            default -> "secondary";
        };

        String prefix = switch (variant) {
            case "bg" -> "bg";
            case "text" -> "text";
            case "border" -> "border";
            default -> "";
        };

        if (prefix.isEmpty()) return "";

        return prefix + "-" + suffix;
    }

    /**
     * Convert hex to CSS-friendly format.
     * @return CSS color string
     */
    public String toCss() {
        return hexValue;
    }

    // ── Helper Methods ──

    /**
     * Find color by hex value (case-insensitive).
     * @param hex Hex color code (with or without #)
     * @return Color or null if not found
     */
    public static Color fromHex(String hex) {
        String normalized = hex.startsWith("#") ? hex : "#" + hex;
        for (Color color : values()) {
            if (color.hexValue.equalsIgnoreCase(normalized)) {
                return color;
            }
        }
        return null;
    }

    /**
     * Find color by CSS class.
     * @param cssClass CSS class name
     * @return Color or null if not found
     */
    public static Color fromCssClass(String cssClass) {
        for (Color color : values()) {
            if (color.cssClass.equals(cssClass)) {
                return color;
            }
        }
        return null;
    }

    /**
     * Get all status colors.
     * @return Array of status colors
     */
    public static Color[] getStatusColors() {
        return new Color[]{
                STATUS_ACTIVE,
                STATUS_INACTIVE,
                STATUS_PENDING,
                STATUS_COMPLETED,
                STATUS_ARCHIVED
        };
    }

    /**
     * Get all notification colors.
     * @return Array of notification colors
     */
    public static Color[] getNotificationColors() {
        return new Color[]{
                NOTIFICATION_INFO,
                NOTIFICATION_SUCCESS,
                NOTIFICATION_WARNING,
                NOTIFICATION_ERROR
        };
    }

    /**
     * Get all gradient colors.
     * @return Array of gradient colors
     */
    public static Color[] getGradients() {
        return new Color[]{
                GRADIENT_PRIMARY,
                GRADIENT_SUCCESS,
                GRADIENT_DANGER,
                GRADIENT_WARNING,
                GRADIENT_INFO,
                GRADIENT_BRAND,
                GRADIENT_SUNSET,
                GRADIENT_OCEAN,
                GRADIENT_FOREST,
                GRADIENT_NIGHT
        };
    }

    /**
     * Get all semantic colors.
     * @return Array of semantic colors
     */
    public static Color[] getSemanticColors() {
        return new Color[]{
                PRIMARY,
                SUCCESS,
                DANGER,
                WARNING,
                INFO,
                BRAND
        };
    }

    @Override
    public String toString() {
        return displayName + " (" + hexValue + ")";
    }
}
