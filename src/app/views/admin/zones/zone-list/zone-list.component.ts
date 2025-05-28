import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

import { ZoneFormComponent } from '../zone-form/zone-form.component';
import { ApiResponse, Zone, ZoneService } from '../../../../core/services/zone.service';

@Component({
  selector: 'app-zone-list',
  standalone: true,
  imports: [
    CommonModule,
    ZoneFormComponent
  ],
  templateUrl: './zone-list.component.html',
  styleUrls: ['./zone-list.component.css']
})
export class ZoneListComponent implements OnInit {
  zones: Zone[] = [];
  mostrarModal = false;
  editMode = false;
  editingZoneId?: string;
  mostrarInactivas = false;

  constructor(private zoneService: ZoneService) {}

  ngOnInit(): void {
    this.loadZones();
  }

  loadZones(): void {
    if (this.mostrarInactivas) {
      this.zoneService.getAllInactive().subscribe({
        next: (resp: ApiResponse<Zone[]>) => this.zones = resp.data,
        error: err => console.error('Error al obtener zonas inactivas:', err)
      });
    } else {
      this.zoneService.getAllActive().subscribe({
        next: (resp: ApiResponse<Zone[]>) => this.zones = resp.data,
        error: err => console.error('Error al obtener zonas activas:', err)
      });
    }
  }

  alternarZonas(): void {
    this.mostrarInactivas = !this.mostrarInactivas;
    this.loadZones();
  }

  abrirModal(): void {
    this.editMode = false;
    this.editingZoneId = undefined;
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
  }

  recargarLista(): void {
    this.cerrarModal();
    this.loadZones();
  }

  cargarZonas(): void {
  const request = this.mostrarInactivas
    ? this.zoneService.getAllInactive()
    : this.zoneService.getAllActive();

  request.subscribe({
    next: (response) => {
      if (response.status) {
        this.zones = response.data;
      } else {
        this.zones = [];
        console.error('Error al obtener zonas:', response.error?.details);
      }
    },
    error: (err) => {
      this.zones = [];
      console.error('Error de conexión:', err);
    }
  });
}

  onEdit(zone: Zone): void {
    this.editMode = true;
    this.editingZoneId = zone.zoneId;
    this.mostrarModal = true;
  }

  onDelete(zone: Zone): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar la zona "${zone.name}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'No, cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.zoneService.delete(zone.zoneId!).subscribe({
          next: () => {
            Swal.fire('Eliminada', 'La zona ha sido eliminada correctamente.', 'success');
            this.recargarLista();
          },
          error: err => {
            console.error('Error eliminando zona:', err);
            Swal.fire('Error', 'No se pudo eliminar la zona.', 'error');
          }
        });
      }
    });
  }

  onActivate(zone: Zone): void {
    Swal.fire({
      title: '¿Reactivar zona?',
      text: `¿Deseas reactivar la zona "${zone.name}"?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, reactivar',
      cancelButtonText: 'No'
    }).then(result => {
      if (result.isConfirmed) {
        this.zoneService.activate(zone.zoneId!).subscribe({
          next: () => {
            Swal.fire('Reactivada', 'La zona ha sido activada nuevamente.', 'success');
            this.recargarLista();
          },
          error: err => {
            console.error('Error reactivando zona:', err);
            Swal.fire('Error', 'No se pudo reactivar la zona.', 'error');
          }
        });
      }
    });
  }

  formatStatus(status?: string): string {
    return status === 'ACTIVE' ? 'A' : 'I';
  }

  formatDate(dateRecord?: string): string {
    if (!dateRecord) return '';
    return new Date(dateRecord).toLocaleDateString('es-PE');
  }
}
