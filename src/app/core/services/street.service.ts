import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

export interface Street {
  streetId?: string;
  name: string;
  zoneId: string;
  zoneName?: string;
  status?: boolean;
  dateRecord?: string;
}

export interface ApiResponse<T> {
  status: boolean;
  data: T;
  error?: {
    errorCode: number;
    message: string;
    details?: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class StreetService {
  private apiUrl = 'https://didactic-spork-7q76wrvqp9r3rw9j-8080.app.github.dev/api/v1/calles';

  constructor(private http: HttpClient) {}

  create(street: Street): Observable<ApiResponse<Street>> {
    return this.http.post<ApiResponse<Street>>(this.apiUrl, street);
  }

  getById(id: string): Observable<ApiResponse<Street>> {
    return this.http.get<ApiResponse<Street>>(`${this.apiUrl}/${id}`);
  }

  getAllByStatus(active: boolean = true): Observable<ApiResponse<Street[]>> {
    const endpoint = active ? 'active' : 'inactive';
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/${endpoint}`).pipe(
      map(response => ({
        status: response.status,
        data: response.data.map(item => ({
          streetId: item.streeId || item._id,
          name: item.name,
          zoneId: item.zoneId,
          zoneName: item.zoneName,
          status: item.status,
          dateRecord: item.dateRecord
        }))
      }))
    );
  }

  getAllActive(): Observable<ApiResponse<Street[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/active`).pipe(
      map(response => ({
        status: response.status,
        data: response.data.map(item => ({
          streetId: item.streetId || item.streeId || item._id || item.id,
          name: item.name,
          zoneId: item.zoneId,
          zoneName: item.zoneName,
          status: item.status,
          dateRecord: item.dateRecord
        }))
      }))
    );
  }

  getAllInactive(): Observable<ApiResponse<Street[]>> {
    return this.http.get<ApiResponse<any[]>>(`${this.apiUrl}/inactive`).pipe(
      map(response => {
        if (response.status && response.data && Array.isArray(response.data)) {
          return {
            status: true,
            data: response.data.map(item => ({
              streetId: item.streetId || item.streeId || item._id || item.id,
              name: item.name,
              zoneId: item.zoneId,
              zoneName: item.zoneName,
              status: item.status,
              dateRecord: item.dateRecord
            }))
          };
        } else {
          console.error('La respuesta de /inactive no tiene data válida:', response);
          return {
            status: false,
            data: []
          };
        }
      })
    );
  }

  update(id: string, updateRequest: Partial<{ name: string; zoneId: string; status: boolean }>): Observable<ApiResponse<Street>> {
    return this.http.put<ApiResponse<Street>>(`${this.apiUrl}/${id}`, updateRequest);
  }

  delete(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  activate(id: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivate(id: string): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/deactivate`, {});
  }
}
