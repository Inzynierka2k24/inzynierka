/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.1.1185 on 2023-10-31 13:07:26.

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
    eventType: number;
    costSource: number;
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

export interface AuthRequest {
    mail: string;
    password: string;
}

export interface EditUserRequest {
    mail: string;
    password: string;
}

export interface GrantedAuthority extends Serializable {
    authority: string;
}

export interface UserDetails extends Serializable {
    enabled: boolean;
    username: string;
    password: string;
    credentialsNonExpired: boolean;
    accountNonExpired: boolean;
    authorities: GrantedAuthority[];
    accountNonLocked: boolean;
}

export interface Serializable {
}

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type CostSource = "UNKNOWN" | "BOOKING" | "PROMOTION" | "FINE" | "TAX" | "CLEANING" | "REPAIR" | "MAINTENANCE";

export type EventType = "UNKNOWN" | "RESERVATION" | "RENOVATION";

export type ServiceType = "UNKNOWN" | "AIRBNB" | "BOOKING";
