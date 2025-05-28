import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Zone {
zoneName: any;
  zoneId?: string;
  name: string;
  description: string;
  officeId?: string;
  status?: string;
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
export class ZoneService {
  private apiUrl = 'https://didactic-spork-7q76wrvqp9r3rw9j-8080.app.github.dev/api/v1/zonas';

  constructor(private http: HttpClient) {}

  /** Obtiene una zona por ID */
  getById(id: string): Observable<{ status: boolean; data: Zone }> {
    return this.http.get<{ status: boolean; data: Zone }>(`${this.apiUrl}/${id}`);
  }

  
getAllActive(): Observable<ApiResponse<Zone[]>> {
  return this.http.get<ApiResponse<Zone[]>>(`${this.apiUrl}/activas`).pipe(
    map(response => {
      // Aquí puedes revisar la respuesta antes de devolverla
      console.log('Zonas activas:', response.data);
      return response;  // Regresa la respuesta correctamente tipada
    })
  );
}

getAllInactive(): Observable<any> {
  return this.http.get(`${this.apiUrl}/inactivas`);
}


  /** Crea una nueva zona */
  create(zone: Zone): Observable<Zone> {
    return this.http.post<Zone>(this.apiUrl, zone);
  }

  /** Actualiza una zona existente */
  update(id: string, zone: Zone): Observable<Zone> {
    return this.http.put<Zone>(`${this.apiUrl}/${id}`, zone);
  }

  /** Elimina (lógico) una zona */
  delete(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /** Reactiva una zona inactiva */
  // zone.service.ts
activate(id: string): Observable<any> {
  return this.http.patch(`${this.apiUrl}/${id}/activate`, {});
  }
}
