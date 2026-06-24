package com.nexusgrade.app.dto;

public record SubmissionStats(
        long totalEnrolled,
        long submittedCount,
        double average, // Matches your 'average' in the record header
        int maxPoints,
        long passCount
) {
    public long getCompletionPercent() {
        return totalEnrolled == 0 ? 0 : (submittedCount * 100) / totalEnrolled;
    }

    public double getAveragePercent() {
        return maxPoints == 0 ? 0 : (average / maxPoints) * 100;
    }

    public long getPassRate() {
        return submittedCount == 0 ? 0 : (passCount * 100) / submittedCount;
    }
}
