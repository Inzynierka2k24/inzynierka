/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.1.1185 on 2023-10-08 11:00:01.

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
    mail: string;
    serviceType: ServiceType;
}

export interface ExternalOffer {
    id?: number;
    serviceType: ServiceType;
    externalLink: string;
}

export interface Reservation {
    id?: number;
    startDate: Date;
    endDate: Date;
}

export interface User {
    id?: number;
    login: string;
    password: string;
    mail: string;
    active: boolean;
    roles: string[];
}

export interface UserDetails {
}

export interface EditUserRequest {
    password: string;
    mail: string;
}

export interface LoginRequest {
    login: string;
    password: string;
}

export interface RegisterRequest {
    login: string;
    password: string;
    mail: string;
}

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type ServiceType = "UNKNOWN" | "AIRBNB" | "BOOKING";
