import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { ApiResponse } from '../../../../core/services/zone.service';
import { Street, StreetService } from '../../../../core/services/street.service';
import { StreetFormComponent } from '../street-form/street-form.component';

@Component({
  selector: 'app-street-list',
  standalone: true,
  imports: [
    CommonModule,
    StreetFormComponent
  ],
  templateUrl: './street-list.component.html',
  styleUrls: ['./street-list.component.css']
})

export class StreetListComponent implements OnInit {
  streets: Street[] = [];
  mostrarModal = false;
  editMode = false;
  editingStreetId?: string;
  mostrarInactivas = false;

  constructor(private streetService: StreetService) {}

  ngOnInit(): void {
    this.loadStreets();
  }

 loadStreets(): void {
  const request = this.mostrarInactivas
    ? this.streetService.getAllInactive()
    : this.streetService.getAllActive();

  request.subscribe({
    next: (resp: ApiResponse<Street[]>) => {
      this.streets = resp.data.map(street => ({
        ...street,
        streetId: street.streetId || (street as any)._id || (street as any).streeId
      }));
    },
    error: (err) => {
      console.error('Error al obtener calles:', err);
      this.streets = [];
    }
  });
}



  alternarCalles(): void {
    this.mostrarInactivas = !this.mostrarInactivas;
    this.loadStreets();
  }

  abrirModal(): void {
    this.editMode = false;
    this.editingStreetId = undefined;
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
  }

 recargarLista(): void {
  this.loadStreets();  // ← Esta función debe recargar las calles del backend
  this.cerrarModal();  // Cierra el modal si aún está abierto
}


 onEdit(street: Street): void {
  console.log('Calle seleccionada para editar:', street);
  this.editingStreetId = street.streetId;  // Asegúrate que aquí se esté asignando el streetId correctamente
  console.log('editingStreetId asignado:', this.editingStreetId); // Agrega esta línea
  this.editMode = true;  // Habilita el modo de edición
  this.mostrarModal = true; // Mostrar el modal
}

  onDelete(street: Street): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar la calle "${street.name}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'No, cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.streetService.delete(street.streetId!).subscribe({
          next: () => {
            Swal.fire('Eliminada', 'La calle ha sido eliminada correctamente.', 'success');
            this.recargarLista();
          },
          error: err => {
            console.error('Error eliminando calle:', err);
            Swal.fire('Error', 'No se pudo eliminar la calle.', 'error');
          }
        });
      }
    });
  }

  onActivate(street: Street): void {
    Swal.fire({
      title: '¿Reactivar calle?',
      text: `¿Deseas reactivar la calle "${street.name}"?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, reactivar',
      cancelButtonText: 'No'
    }).then(result => {
      if (result.isConfirmed) {
        this.streetService.activate(street.streetId!).subscribe({
          next: () => {
            Swal.fire('Reactivada', 'La calle ha sido activada nuevamente.', 'success');
            this.recargarLista();
          },
          error: err => {
            console.error('Error reactivando calle:', err);
            Swal.fire('Error', 'No se pudo reactivar la calle.', 'error');
          }
        });
      }
    });
  }

  onDeactivate(street: Street): void {
  console.log('Intentando desactivar calle con ID:', street.streetId);  // <-- Aquí el log

  Swal.fire({
    title: '¿Desactivar calle?',
    text: `¿Deseas desactivar la calle "${street.name}"?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, desactivar',
    cancelButtonText: 'No'
  }).then(result => {
    if (result.isConfirmed) {
      this.streetService.deactivate(street.streetId!).subscribe({
        next: () => {
          Swal.fire('Desactivada', 'La calle ha sido desactivada correctamente.', 'success');
          this.recargarLista();
        },
        error: err => {
          console.error('Error desactivando calle:', err);
          Swal.fire('Error', 'No se pudo desactivar la calle.', 'error');
        }
      });
    }
  });
}


  // Método trackBy para optimizar la lista
  trackByStreetId(index: number, street: Street): string {
    if (street && street.streetId) {
      return street.streetId;  // Usamos el streetId como identificador único
    }
    return '';  // Si no existe streetId, devuelve un valor vacío
  }

  formatStatus(status?: boolean): string {
    return status ? 'A' : 'I';
  }

  formatDate(date?: string): string {
    return date ? new Date(date).toLocaleDateString('es-PE') : '';
  }
}
