import {
  Component,
  OnInit,
  OnChanges,
  Input,
  Output,
  EventEmitter,
  SimpleChanges,
  inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import Swal from 'sweetalert2';
import { Street, StreetService } from '../../../../core/services/street.service';
import { ApiResponse, Zone, ZoneService } from '../../../../core/services/zone.service';

@Component({
  selector: 'app-street-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './street-form.component.html',
  styleUrls: ['./street-form.component.css']
})
export class StreetFormComponent implements OnInit, OnChanges {
  @Input() editMode: boolean = false;
  @Input() streetId?: string;

  @Output() guardado = new EventEmitter<void>();
  @Output() cerrar = new EventEmitter<void>();

  form!: FormGroup;
  zonas: Zone[] = [];

  private fb = inject(FormBuilder);
  private streetService = inject(StreetService);
  private zoneService = inject(ZoneService);

  streets: Street[] | undefined;
  mostrarInactivas: any;
  selectedZoneName: string | undefined;

  ngOnInit(): void {
    this.form = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ\s]+$/)
        ]
      ],
      zoneId: ['', Validators.required]
    });

    this.cargarZonas();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['streetId'] && this.editMode && this.streetId) {
      console.log('streetId cambiado a:', this.streetId);
      this.cargarDatos();
    }
  }

  private cargarZonas(): void {
    this.zoneService.getAllActive().subscribe({
      next: (resp) => {
        this.zonas = resp.data;
        console.log('Zonas cargadas:', this.zonas);

        if (this.editMode && this.streetId) {
          this.cargarDatos();
        }
      },
      error: (err) => {
        console.error('Error cargando zonas:', err);
      }
    });
  }

  private cargarDatos(): void {
    if (!this.streetId) return;

    this.streetService.getById(this.streetId).subscribe({
      next: (res) => {
        const street = res.data;
        console.log('Datos de la calle recibidos:', street);

        this.form.patchValue({
          name: street.name,
          zoneId: street.zoneId
        });

        const zonaSeleccionada = this.zonas.find(z => z.zoneId === street.zoneId);
        this.selectedZoneName = zonaSeleccionada ? zonaSeleccionada.name : undefined;
      },
      error: (err) => {
        console.error('Error al cargar los datos de la calle:', err);
      }
    });
  }

 guardar(): void {
  if (this.form.invalid) {
    console.log('Formulario inválido');
    return;
  }

  const payload = this.form.value;
  console.log('Payload:', payload);

  if (this.editMode && this.streetId) {
    this.streetService.update(this.streetId, payload).subscribe({
      next: (res) => {
        console.log('Calle actualizada con éxito', res);
        Swal.fire('Éxito', 'Calle actualizada con éxito', 'success');
        this.guardado.emit();
        this.cerrar.emit();
      },
      error: (err) => {
        console.error('Error al actualizar la calle:', err);
        Swal.fire('Error', 'Error al actualizar la calle', 'error');
      }
    });
  } else {
    this.streetService.create(payload).subscribe({
      next: (res) => {
        console.log('Calle creada con éxito', res);
        Swal.fire('Éxito', 'Calle creada con éxito', 'success');
        this.guardado.emit();
        this.cerrar.emit();
      },
      error: (err) => {
        console.error('Error al crear la calle:', err);
        Swal.fire('Error', 'Error al crear la calle', 'error');
      }
    });
  }
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
        console.log('Calles cargadas:', this.streets);
      },
      error: (err: any) => {
        this.streets = [];
        console.error('Error al obtener calles:', err);
      }
    });
  }

  cancelar(): void {
    this.form.reset();
    this.cerrar.emit();
  }
}
