import {Apartment} from '../../generated';

// export class MockFactory {
//   static createMockApartment(options: Partial<Apartment>): Apartment {
//     return {
//       id: options.id ?? 0,
//       dailyPrice: options.dailyPrice ?? 5,
//       title: options.title ?? 'TestApartment',
//       country: options.country ?? 'Poland',
//       city: options.city ?? 'Wroclaw',
//       street: options.street ?? 'Sezamkowa',
//       buildingNumber: options.buildingNumber ?? '1',
//       apartmentNumber: options.apartmentNumber ?? '2',
//       rating: options.rating ?? 3,
//     };
//   }
//
//   static createMockContact(options: Partial<ContactDTO>): ContactDTO {
//     return {
//       contactType: options.contactType ?? 'UNKNOWN',
//       name: options.name ?? 'Pan Pawel',
//       messages: options.messages ?? [],
//       apartments: options.apartments ?? [],
//     };
//   }
// }

export function apartmentStreetToString(a: Apartment) {
  return (
    a.city + ', ' + a.street + ' ' + a.buildingNumber + '/' + a.apartmentNumber
  );
}
