/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.1.1185 on 2023-10-24 15:34:13.

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
    accountNonLocked: boolean;
    authorities: GrantedAuthority[];
    username: string;
    password: string;
    accountNonExpired: boolean;
    credentialsNonExpired: boolean;
}

export interface Serializable {
}

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type ServiceType = "UNKNOWN" | "AIRBNB" | "BOOKING";
