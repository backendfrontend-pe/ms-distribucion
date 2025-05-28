export const Estados = {
  ACTIVO: 'ACTIVO',
  INACTIVO: 'INACTIVO'
} as const;

export type EstadoType = typeof Estados[keyof typeof Estados];

export const EstadosOptions = [
  { value: Estados.ACTIVO, label: 'Activo', color: 'bg-green-500' },
  { value: Estados.INACTIVO, label: 'Inactivo', color: 'bg-red-500' }
];
