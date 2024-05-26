package edu.pidev.backend.common.enumuration;

public enum UserBadge {
    ADMIN("Admin"),
    MODERATOR("Moderator"),
    EXPERT("Expert"),
    TOP_CONTRIBUTOR("Top Contributor"),
    NEW_MEMBER("New Member");

    private final String displayName;

    UserBadge(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
