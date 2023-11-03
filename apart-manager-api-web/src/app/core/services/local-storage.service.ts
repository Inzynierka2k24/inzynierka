import { Injectable } from '@angular/core';

export type LocalStorageKeys = 'JWT_TOKEN' | 'AUTH_USERNAME';

@Injectable({
  providedIn: 'root',
})
export class LocalStorageService {
  constructor() {}

  public saveData(key: LocalStorageKeys, value: string) {
    localStorage.setItem(key, value);
  }

  public getData(key: LocalStorageKeys) {
    return localStorage.getItem(key);
  }

  public removeData(key: LocalStorageKeys) {
    localStorage.removeItem(key);
  }

  public clearData() {
    localStorage.clear();
  }
}
