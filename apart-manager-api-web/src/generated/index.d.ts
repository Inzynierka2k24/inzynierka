/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.1.1185 on 2023-10-31 11:14:02.

export interface Apartment {
    id?: number;
    dailyPrice: number;
    title: string;
    country: string;
    city: string;
    street: string;
    buildingNumber: string;
    apartmentNumber: string;
}

export interface Contact {
    id?: number;
    contactType: ContactType;
    receiver: string;
    message: string;
}

export interface ExternalAccount {
    id?: number;
    login: string;
    password: string;
    emailAddress: string;
    serviceType: ServiceType;
}

export interface ExternalOffer {
    id?: number;
    serviceType: ServiceType;
    externalLink: string;
}

export interface Membership {
    level: MembershipLevel;
    billingType: BillingType;
    billingInfo: string;
}

export interface Reservation {
    id?: number;
    apartmentId: number;
    startDate: Date;
    endDate: Date;
}

export interface User {
    id?: number;
    login: string;
    emailAddress: string;
    active: boolean;
    roles: string[];
}

export interface UserPreferences {
    id?: number;
    sms: boolean;
    email: boolean;
}

export interface UserDTO {
    login: string;
    emailAddress: string;
    level: MembershipLevel;
    billingType: BillingType;
    smsNotifications: boolean;
    emailNotifications: boolean;
}

export interface ApiErrorResponse {
    status: number;
    message: string;
}

export interface AuthRequest {
    login: string;
    password: string;
}

export interface EditUserRequest {
    emailAddress: string;
    password: string;
}

export interface RegisterRequest {
    login: string;
    emailAddress: string;
    password: string;
}

export type BillingType = "CARD" | "PAYPAL" | "NONE";

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type MembershipLevel = "FREE" | "COMMERCIAL" | "ENTERPRISE";

export type ServiceType = "UNKNOWN" | "AIRBNB" | "BOOKING";
