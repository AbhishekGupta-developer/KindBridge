package com.myorganisation.KindBridge.enums;

public enum UserRoleType {
    ADMIN(true, true),
    SEEKER(true, false),
    DEPENDENT(false, false),
    PROVIDER(true, true);

    // ===== Fields =====
    private final boolean canAddDependents;
    private final boolean isServiceProvider;

    // ===== Constructor =====
    UserRoleType(boolean canAddDependents, boolean isServiceProvider) {
        this.canAddDependents = canAddDependents;
        this.isServiceProvider = isServiceProvider;
    }

    // ===== Getters =====
    public boolean canAddDependents() {
        return canAddDependents;
    }

    public boolean isServiceProvider() {
        return isServiceProvider;
    }
}
