# Estructura de Base de Datos MongoDB para Sistema JASS (Microservicios)

## 1. MS-ORGANIZACIONES (MILENKA MUÑOZ)

### Colección: organizaciones

```javascript
{
  "_id": ObjectId(),
  "nombre": "JASS Central",
  "direccion": "Av. Principal 123",
  "telefono": "987654321",
  "representante_legal": "Juan Pérez",
  "fecha_creacion": ISODate("2020-01-01"),
  "estado": true,
  "fecha_registro": ISODate("2025-01-01")
}
```

### Colección: sedes

```javascript
{
  "_id": ObjectId(),
  "organizacion_id": ObjectId(),
  "nombre": "Sede Rinconada de Conta - Bellavista de Conta",
  "direccion": "Calle Principal, Rinconada",
  "telefono": "912345678",
  "email": "sede@jass.org",
  "encargado": "Pedro Gómez",
  "estado": true,
  "fecha_registro": ISODate("2025-01-01")
}
```

## 2. MS-USUARIOS (ISAEL FATAMA)

### Colección: usuarios

```javascript
{
  "_id": ObjectId(),
  "sede_id": ObjectId(),
  "tipo_documento": "DNI",
  "numero_documento": "45678912",
  "nombres": "María",
  "apellidos": "López García",
  "telefono": "912345678",
  "email": "maria@example.com",
  "direccion": {
    "detalle": "Jr. Las Flores 123",
    "localidad_id": ObjectId(),
    "localidad_nombre": "Bellavista de Conta",
    "calle_id": ObjectId(),
    "calle_nombre": "Jr. Las Flores"
  },
  "rol": "CLIENTE",
  "estado": true,
  "fecha_registro": ISODate("2025-01-15"),
  "cajas": [
    {
      "caja_id": ObjectId(),
      "codigo": "CAJ001",
      "tipo": "CAÑO"
    }
  ]
}
```

## 3. MS-CAJAS (FRANK SALAZAR - DEYTON GARCIA - SANTIAGO PRADA)

### Colección: cajas

```javascript
{
  "_id": ObjectId(),
  "codigo": "CAJ001",
  "tipo": "CAÑO",
  "fecha_instalacion": ISODate("2025-02-15"),
  "estado": "ACTIVO",
  "observaciones": "Instalación estándar",
  "fecha_registro": ISODate("2025-02-15")
}
```

### Colección: asignacion_cajas

```javascript
{
  "_id": ObjectId(),
  "caja_id": ObjectId(),
  "usuario_id": ObjectId(),
  "fecha_asignacion": ISODate("2025-02-15"),
  "estado": true,
  "observaciones": "Asignación inicial",
  "fecha_registro": ISODate("2025-02-15"),
  "usuario_datos": {
    "nombre_completo": "María López García",
    "documento": "45678912",
    "direccion": "Jr. Las Flores 123, Bellavista de Conta"
  },
  "caja_datos": {
    "codigo": "CAJ001",
    "tipo": "CAÑO"
  }
}
```

## 4. MS-PAGOS (JOHAN MALASQUEZ - RONALDINHO CCENCHO)

### Colección: pagos

```javascript
{
  "_id": ObjectId(),
  "usuario_id": ObjectId(),
  "monto": 10.00,
  "fecha_pago": ISODate("2025-03-15"),
  "mes_servicio": "2025-03",
  "tipo_pago": "REGULAR",
  "metodo_pago": "EFECTIVO",
  "comprobante": "BOLETA",
  "numero_comprobante": "B001-00001",
  "estado": "PAGADO",
  "usuario_receptor_id": ObjectId(),
  "observaciones": "Pago mensual servicio de agua",
  "fecha_registro": ISODate("2025-03-15"),
  "usuario_datos": {
    "nombre_completo": "María López García",
    "documento": "45678912",
    "direccion": "Jr. Las Flores 123, Bellavista de Conta",
    "localidad": "Bellavista de Conta"
  }
}
```

### Colección: facturas

```javascript
{
  "_id": ObjectId(),
  "pago_id": ObjectId(),
  "usuario_id": ObjectId(),
  "serie": "F001",
  "numero": "00001",
  "fecha_emision": ISODate("2025-03-15"),
  "monto_total": 10.00,
  "igv": 1.53,
  "subtotal": 8.47,
  "estado": "EMITIDO",
  "fecha_registro": ISODate("2025-03-15"),
  "usuario_datos": {
    "nombre_completo": "María López García",
    "documento": "45678912",
    "direccion": "Jr. Las Flores 123, Bellavista de Conta"
  }
}
```

### Colección: reclamos

```javascript
{
  "_id": ObjectId(),
  "usuario_id": ObjectId(),
  "pago_id": ObjectId(),
  "tipo_reclamo": "FACTURACION",
  "descripcion": "Cobro incorrecto en el recibo del mes",
  "fecha_reclamo": ISODate("2025-03-20"),
  "estado": "PENDIENTE",
  "fecha_resolucion": null,
  "respuesta": "",
  "usuario_atencion_id": null,
  "fecha_registro": ISODate("2025-03-20"),
  "usuario_datos": {
    "nombre_completo": "María López García",
    "documento": "45678912",
    "direccion": "Jr. Las Flores 123, Bellavista de Conta",
    "telefono": "912345678"
  }
}
```

## 5. MS-DISTRIBUCIÓN

### Colección: zonas

```javascript
{
  "_id": ObjectId(),
  "nombre": "Bellavista de Conta",
  "descripcion": "Localidad principal de la sede",
  "sede_id": ObjectId(),
  "estado": true,
  "fecha_registro": ISODate("2025-01-01")
}
```

### Colección: calles

```javascript
{
  "_id": ObjectId(),
  "nombre": "Jr. Las Flores",
  "zona_id": ObjectId(),
  "zona_nombre": "Bellavista de Conta",
  "estado": true,
  "fecha_registro": ISODate("2025-01-01")
}
```

### Colección: programacion_distribucion

```javascript
{
  "_id": ObjectId(),
  "calle_id": ObjectId(),
  "zona_id": ObjectId(),
  "hora_inicio": "14:00",
  "hora_fin": "15:00",
  "es_diario": true,
  "estado": true,
  "observaciones": "Distribución diaria regular",
  "fecha_registro": ISODate("2025-01-15"),
  "responsable_id": ObjectId(),
  "calle_nombre": "Jr. Las Flores",
  "zona_nombre": "Bellavista de Conta"
}
```

### Colección: tarifas

```javascript
{
  "_id": ObjectId(),
  "zona_id": ObjectId(),
  "monto": 10.00,
  "descripcion": "Tarifa estándar mensual",
  "tipo_tarifa": "REGULAR",
  "fecha_inicio_vigencia": ISODate("2025-01-01"),
  "fecha_fin_vigencia": null,
  "estado": true,
  "fecha_registro": ISODate("2025-01-01"),
  "zona_nombre": "Bellavista de Conta"
}
```

### Colección: incidencias_distribucion

```javascript
{
  "_id": ObjectId(),
  "tipo_incidencia": "CORTE_TEMPORAL",
  "descripcion": "Rotura de tubería principal en Jr. Las Flores",
  "zona_id": ObjectId(),
  "zona_nombre": "Bellavista de Conta",
  "calles_afectadas": [
    {
      "calle_id": ObjectId(),
      "calle_nombre": "Jr. Las Flores"
    }
  ],
  "fecha_inicio": ISODate("2025-05-20T08:00:00Z"),
  "fecha_estimada_solucion": ISODate("2025-05-20T17:00:00Z"),
  "fecha_solucion_real": null,
  "estado": "EN_PROCESO",
  "prioridad": "ALTA",
  "reportado_por": ObjectId(),
  "asignado_a": ObjectId(),
  "materiales_requeridos": [
    {
      "nombre": "Tubería PVC 2 pulgadas",
      "cantidad": 2,
      "unidad": "metros"
    },
    {
      "nombre": "Pegamento PVC",
      "cantidad": 1,
      "unidad": "unidad"
    }
  ],
  "observaciones": "La rotura afecta a 15 familias aproximadamente",
  "actualizaciones": [
    {
      "fecha": ISODate("2025-05-20T10:30:00Z"),
      "estado": "EN_PROCESO",
      "descripcion": "Se ha enviado equipo de reparación",
      "usuario_id": ObjectId()
    }
  ],
  "notificado": true,
  "fecha_registro": ISODate("2025-05-20T08:15:00Z")
}
```

## 6. MS-NOTIFICACIONES

### Colección: notificaciones

```javascript
{
  "_id": ObjectId(),
  "tipo_notificacion": "PAGO_PENDIENTE",
  "usuario_id": ObjectId(),
  "titulo": "Recordatorio de pago",
  "mensaje": "Le recordamos que su pago del mes de Marzo está pendiente",
  "plantilla_id": ObjectId(),
  "fecha_envio": ISODate("2025-03-10"),
  "canal_envio": "SMS",
  "estado_envio": "ENVIADO",
  "leido": false,
  "fecha_lectura": null,
  "fecha_registro": ISODate("2025-03-10"),
  "usuario_datos": {
    "nombre_completo": "María López García",
    "telefono": "912345678",
    "email": "maria@example.com"
  }
}
```

### Colección: plantillas

```javascript
{
  "_id": ObjectId(),
  "nombre": "Recordatorio de pago",
  "codigo": "PAGO_PENDIENTE",
  "asunto": "Recordatorio de pago mensual",
  "contenido": "Estimado(a) {{nombre_usuario}}, le recordamos que su pago del mes de {{mes}} por un monto de S/. {{monto}} está pendiente. Fecha límite: {{fecha_limite}}",
  "tipo_plantilla": "SMS",
  "variables": ["nombre_usuario", "mes", "monto", "fecha_limite"],
  "estado": true,
  "fecha_registro": ISODate("2025-01-01")
}
```

## Resumen de colecciones por microservicio

1. **MS-ORGANIZACIONES**
   - organizaciones
   - sedes

2. **MS-USUARIOS**
   - usuarios

3. **MS-CAJAS**
   - cajas
   - asignacion_cajas

4. **MS-PAGOS**
   - pagos
   - facturas
   - reclamos

5. **MS-DISTRIBUCIÓN**
   - zonas
   - calles
   - programacion_distribucion
   - tarifas
   - incidencias_distribucion

6. **MS-NOTIFICACIONES**
   - notificaciones
   - plantillas

## Relaciones entre colecciones

1. **organizaciones → sedes**: Una organización tiene múltiples sedes (relación 1:N)

2. **sedes → usuarios:** Una sede tiene múltiples usuarios (relación 1:N)

3. **usuarios → cajas:** Un usuario puede tener múltiples cajas asignadas (relación N:M)

4. **usuarios → pagos:** Un usuario realiza múltiples pagos (relación 1:N)

5. **pagos → facturas:** Un pago puede generar una factura (relación 1:1)

6. **sedes → zonas:** Una sede tiene múltiples zonas (relación 1:N)

7. **zonas → calles:** Una zona tiene múltiples calles (relación 1:N)

8. **calles → programacion_distribucion:** Una calle puede tener programación de distribución (relación 1:N)

9. **zonas → tarifas:** Una zona tiene una tarifa asociada (relación 1:1)

10. **usuarios → notificaciones:** Un usuario recibe múltiples notificaciones (relación 1:N)
