/* tslint:disable */
/* eslint-disable */

// Generated using typescript-generator version 3.1.1185 on 2023-12-01 19:17:16.

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
    userId?: number;
    contactType: ContactType;
    name: string;
    mail: string;
    phone: string;
    emailNotifications: boolean;
    smsNotifications: boolean;
    price: number;
}

export interface ExternalAccount {
    id?: number;
    login: string;
    password: string;
    serviceType: ExternalService;
}

export interface ExternalOffer {
    id?: number;
    serviceType: ExternalService;
    externalLink: string;
}

export interface Finance {
    id?: number;
    userId: number;
    apartmentId: number;
    eventType: string;
    source: string;
    price: number;
    date: Date;
    details: string;
}

export interface Membership {
    level: MembershipLevel;
    billingType: BillingType;
    billingInfo: string;
}

export interface NotificationSettings {
    emailNotifications: boolean;
    smsNotifications: boolean;
}

export interface Reservation {
    id?: number;
    apartmentId: number;
    startDate: Date;
    endDate: Date;
}

export interface ScheduledMessage {
    id?: number;
    userId: number;
    contactId: number;
    message: string;
    intervalType: IntervalType;
    intervalValue: number;
    triggerType: TriggerType;
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

export interface ContactDTO {
    id?: number;
    contactType: ContactType;
    name: string;
    price: number;
    mail: string;
    phone: string;
    notificationSettings: NotificationSettings;
    messages: ScheduledMessageDTO[];
    apartments: Apartment[];
}

export interface ReservationDTO {
    id?: number;
    apartment: Apartment;
    startDate: Date;
    endDate: Date;
}

export interface ScheduledMessageDTO {
    id?: number;
    apartments: Apartment[];
    message: string;
    intervalType: IntervalType;
    intervalValue: number;
    triggerType: TriggerType;
}

export interface UserDTO {
    id: number;
    login: string;
    emailAddress: string;
    level: MembershipLevel;
    billingType: BillingType;
    smsNotifications: boolean;
    emailNotifications: boolean;
}

export interface AuthRequest {
    login: string;
    password: string;
}

export interface EditUserRequest {
    username: string;
    emailAddress: string;
    password: string;
}

export interface GetReservationsRequest {
    from: Date;
    to: Date;
}

export interface RegisterRequest {
    login: string;
    emailAddress: string;
    password: string;
}

export type BillingType = "CARD" | "PAYPAL" | "NONE";

export type ContactType = "UNKNOWN" | "CLEANING" | "MECHANIC" | "ELECTRICIAN";

export type EventType = "UNKNOWN" | "RESERVATION" | "RENOVATION";

export type IntervalType = "HOURS" | "DAYS" | "WEEKS";

export type MembershipLevel = "FREE" | "COMMERCIAL" | "ENTERPRISE";

export type Source = "UNKNOWN" | "BOOKING" | "PROMOTION" | "FINE" | "TAX" | "CLEANING" | "REPAIR" | "MAINTENANCE";

export type TriggerType = "RESERVATION" | "CHECKIN" | "CHECKOUT";

export type ExternalService = "BOOKING" | "AIRBNB" | "TRIVAGO" | "NOCOWANIEPL" | "UNRECOGNIZED";
