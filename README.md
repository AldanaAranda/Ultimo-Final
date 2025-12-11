# 🏦 Sistema de Gestión Bancaria – Trabajo Final  
Materia: Laboratorio de Computación III  
Alumno/a: **Aldana Aranda**

---

## 📘 Descripción del Proyecto

Este proyecto implementa un sistema bancario simplificado utilizando **Spring Boot**, siguiendo todas las especificaciones del Trabajo Final.  
El sistema permite administrar clientes, cuentas bancarias, realizar transferencias y consultar el historial de transacciones asociadas a cada cuenta.

La solución está organizada en capas (Controller, Service, DAO y Model), con persistencia simulada en memoria y endpoints REST completamente funcionales.

---

## 🏗️ Arquitectura del Proyecto

El proyecto está compuesto por las siguientes capas:

### ✔ Controller  
Maneja las solicitudes HTTP.

### ✔ Service  
Contiene la lógica de negocio de cada proceso.

### ✔ DAO (Data Access Object)  
Simula la persistencia en memoria utilizando listas y mapas.

### ✔ Models  
Representa entidades como Cliente, Cuenta y Transacción.

---

## 🚀 Funcionalidades Implementadas

### 🟣 1️⃣ Alta de Clientes
- Validación de duplicado por DNI.  
- Verificación de mayoría de edad.  
- Registro automático de fecha de alta.  
- Asignación de banco (o por defecto “No informado”).

---

### 🟣 2️⃣ Alta de Cuentas Bancarias
- Validación de existencia previa.  
- Validación de tipos de cuenta soportados por el banco:
  - CAJA_AHORRO
  - CUENTA_CORRIENTE
- Validación de cliente que ya posea una cuenta del mismo tipo+moneda.  
- Generación automática de número de cuenta.  
- Asignación de fecha de creación.

---

### 🟣 3️⃣ Transferencias Bancarias
Incluye todas las reglas del trabajo:

#### ✔ Validaciones:
- Cuenta origen debe existir.  
- Ambas cuentas deben tener la **misma moneda**.  
- Verificación de saldo disponible.  
- Cálculo automático de comisión:
  - 2% si supera $1.000.000 en pesos  
  - 0.5% si supera USD 5.000  
- Actualización de saldos en origen y destino.

#### ✔ Transferencias a otros bancos
- Si la cuenta destino no existe en el banco, se invoca un servicio simulado: **BanelcoService**.  
- Si Banelco rechaza, la transferencia se cancela.

---

### 🟣 4️⃣ Registro de Transacciones

Cada transferencia exitosa genera movimientos en el historial:

#### ✔ Para la cuenta origen
- tipo: **SALIENTE**
- descripción: “Transferencia enviada a X”
- monto  
- fecha  

#### ✔ Para la cuenta destino
- tipo: **ENTRANTE**
- descripción: “Transferencia recibida de X”
- monto  
- fecha  

#### ✔ Si el destino es otro banco
- Solo se registra en la cuenta origen  
- descripción: “Transferencia enviada a otro banco (cuenta XXXX)”


---

### 🟣 5️⃣ Endpoints

A continuación están TODOS los endpoints con sus JSON listos para copiar y pegar.

---

## 🟣 1️⃣ Crear Cliente

### **POST**

http://localhost:8080/api/cliente

### **Body**
```json
{
    "nombre": "Aldana",
    "apellido": "Aranda",
    "dni": 12345678,
    "fechaNacimiento": "2005-02-23",
    "tipoPersona": "PERSONA_FISICA",
    "banco": "NACION"
}

SEGUNDO CLIENTE

{
    "nombre": "Olivia",
    "apellido": "Martinez",
    "dni": 87654321,
    "fechaNacimiento": "2000-05-17",
    "tipoPersona": "PERSONA_FISICA",
    "banco": "NACION"
}

```

---


## 🟣 2️⃣ Crear Cuenta

### **POST**

http://localhost:8080/api/cuenta

### **Body**

```json
CUENTA DEL PRIMER CLIENTE EN PESOS
{
    "dniTitular": 12345678,
    "balance": 50000000,
    "moneda": "PESOS",
    "tipoCuenta": "CAJA_AHORRO"
}

CUENTA DEL SEGUNDO CLIENTE EN PESOS

{
    "dniTitular": 87654321,
    "balance": 70000000,
    "moneda": "PESOS",
    "tipoCuenta": "CAJA_AHORRO"
}

CUENTA DEL PRIMER CLIENTE EN DOLARES

{
    "dniTitular": 12345678,
    "balance": 10000,
    "moneda": "DOLARES",
    "tipoCuenta": "CAJA_AHORRO"
}

CUENTA DEL SEGUNDO CLIENTE EN DOLARES

{
    "dniTitular": 87654321,
    "balance": 500,
    "moneda": "DOLARES",
    "tipoCuenta": "CAJA_AHORRO"
}

```

## 🟣 3️⃣ Transferencia


### **POST**

http://localhost:8080/api/transfer

### **Body**

```json
{
    "cuentaOrigen": XXXX,
    "cuentaDestino": XXXX,
    "moneda": "PESOS",
    "monto": 50000
}
```

## 🟣 4️⃣ Transferencia con comisión

```json
{
    "cuentaOrigen": xxxx,
    "cuentaDestino": xxxx,
    "moneda": "PESOS",
    "monto": 1500000
}
```
## 🟣 5️⃣ Transferencia a otro banco

```json
{
    "cuentaOrigen": xxxx,
    "cuentaDestino": 99999,
    "moneda": "PESOS",
    "monto": 200000
}
```

## 🟣 6️⃣ Obtener Historial de Transacciones

### **GET**

http://localhost:8080/api/cuenta/xxxx/transacciones

###Ejemplo de Respuesta

```json
{
    "numeroCuenta": 5032,
    "transacciones": [
        {
            "tipo": "SALIENTE",
            "descripcion": "Transferencia enviada a 2737",
            "monto": 50000.0,
            "fecha": "2025-12-11T12:21:38.607095400"
        },
        {
            "tipo": "SALIENTE",
            "descripcion": "Transferencia enviada a 2737",
            "monto": 1500000.0,
            "fecha": "2025-12-11T12:21:53.754410500"
        },
        {
            "tipo": "SALIENTE",
            "descripcion": "Transferencia enviada a otro banco (cuenta 99999)",
                       "monto": 200000.0,
            "fecha": "2025-12-11T12:22:11.273906"
        }
    ]
}
```
--- 

## 📡 Resumen de Endpoints

| Acción                       | Método | Endpoint                                             |
|------------------------------|--------|-------------------------------------------------------|
| Crear cliente                | POST   | `/api/cliente`                                        |
| Obtener cliente por DNI      | GET    | `/api/cliente/{dni}`                                  |
| Crear cuenta                 | POST   | `/api/cuenta`                                         |
| Obtener cuentas por cliente  | GET    | `/api/cuenta/cliente/{dni}`                           |
| Realizar transferencia       | POST   | `/api/transfer`                                       |
| Historial de transacciones   | GET    | `/api/cuenta/{numeroCuenta}/transacciones`            |

---

## 🛠 Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Persistencia en memoria
- Postman
