package com.example.policy_service.model;

public enum ProposalStatus {
    INITIALIZED,
    SUBSCRIBER_ADDED,
    CAR_ADDED,
    DRIVERS_ADDED,
    INSURANCE_PLAN_ADDED,
    CONFIRMED,
    PAID;

    public boolean canTransitionTo(ProposalStatus newStatus) {
        if (newStatus == null) {
            return false;
        }
        return this.ordinal() <= newStatus.ordinal();
    }
}


