/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.1.1185 on 2023-11-02 10:19:46.

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

export interface Finance {
    id?: number;
    userId: number;
    apartmentId: number;
    eventId: number;
    eventType: EventType;
    source: Source;
    price: number;
    date: Date;
    details: string;
}

export interface Reservation {
    id?: number;
    apartmentId: number;
    startDate: Date;
    endDate: Date;
}

export interface User {
    id?: number;
    mail: string;
    password: string;
    active: boolean;
    roles: string[];
}

export interface UserSecurityDetails extends UserDetails {
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
    mail: string;
    password: string;
}

export interface RegisterRequest {
    login: string;
    emailAddress: string;
    password: string;
}

export interface GrantedAuthority extends Serializable {
    authority: string;
}

export interface UserDetails extends Serializable {
    accountNonLocked: boolean;
    authorities: GrantedAuthority[];
    username: string;
    password: string;
    enabled: boolean;
    credentialsNonExpired: boolean;
    accountNonExpired: boolean;
}

export interface Serializable {
}

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type EventType = "UNKNOWN" | "RESERVATION" | "RENOVATION";

export type ServiceType = "UNKNOWN" | "AIRBNB" | "BOOKING";

export type Source = "UNKNOWN" | "BOOKING" | "PROMOTION" | "FINE" | "TAX" | "CLEANING" | "REPAIR" | "MAINTENANCE";
