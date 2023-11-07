package com.inzynierka2k24.apiserver.model;

public record Membership(MembershipLevel level, BillingType billingType, String billingInfo) {}
