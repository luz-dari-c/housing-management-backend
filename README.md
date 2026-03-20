# 🏠 PLATAFORMA INTELIGENTE DE GESTIÓN INMOBILIARIA
### 📍 Tu hogar, una plataforma. Tu tranquilidad, un contrato.

---

<div align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-14-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white" alt="Supabase">
  <img src="https://img.shields.io/badge/Leaflet-199900?style=for-the-badge&logo=leaflet&logoColor=white" alt="Leaflet">
</div>

---

## 🌟 INTRODUCCIÓN

> **"Del anuncio al contrato, del contrato a la confianza. Todo en un solo lugar."**

**Bienvenido a la primera plataforma que integra TODO el ciclo de vida inmobiliario.** No somos solo otro portal de anuncios. Somos el ecosistema completo donde:

| 🔍 | 📄 | 💰 | ⭐ |
|---|---|---|---|
| **Encuentras** | **Contratas** | **Pagas** | **Confías** |
| Búsqueda inteligente | Contratos digitales | Pagos simulados | Reputación verificable |

---

## 🚨 EL PROBLEMA QUE RESOLVEMOS

<div align="center">
  <table>
    <tr>
      <td align="center"><b>😤 HOY</b></td>
      <td align="center"><b>➡️</b></td>
      <td align="center"><b>✨ MAÑANA</b></td>
    </tr>
    <tr>
      <td>5 portales diferentes</td>
      <td>➡️</td>
      <td><b>1 plataforma unificada</b></td>
    </tr>
    <tr>
      <td>Información desactualizada</td>
      <td>➡️</td>
      <td><b>Disponibilidad en tiempo real</b></td>
    </tr>
    <tr>
      <td>Contratos en papel</td>
      <td>➡️</td>
      <td><b>Contratos digitales</b></td>
    </tr>
    <tr>
      <td>Pagos sin respaldo</td>
      <td>➡️</td>
      <td><b>Trazabilidad completa</b></td>
    </tr>
    <tr>
      <td>Desconfianza total</td>
      <td>➡️</td>
      <td><b>Reputación verificable</b></td>
    </tr>
  </table>
</div>

### 🎯 El Dolor Puntual

> **"No existe un 'historial crediticio' en el mundo inmobiliario. Los buenos inquilinos no tienen cómo demostrarlo y los malos propietarios no tienen consecuencias."**

**Nuestra plataforma cambia eso.** Cada contrato, cada pago, cada relación construye un historial inmutable que acompaña al usuario de por vida.

---

## 🏛️ ARQUITECTURA HEXAGONAL

<div align="center">
  

## 📖 Descripción del Proyecto

**Plataforma web inteligente de gestión inmobiliaria** enfocada en el arriendo de propiedades a largo plazo y la compraventa de viviendas. El sistema integra todo el ciclo de vida de una relación inmobiliaria: desde la publicación y búsqueda de propiedades, hasta la firma de contratos digitales, el registro de pagos y la construcción de reputación verificable entre las partes.

### ✨ Características Principales

- **Publicación de propiedades** para arriendo y/o venta
- **Gestión de habitaciones** individuales dentro de una misma propiedad
- **Contratos digitales** de arrendamiento a largo plazo
- **Sistema de pagos simulados** (administrativos y de arriendo)
- **Reputación bidireccional** basada en contratos reales
- **Búsqueda georreferenciada** con mapas interactivos
- **Múltiples roles de usuario** no excluyentes

---

## 🎯 Problema que Resuelve

### El Problema Central

**"Las personas que necesitan arrendar o comprar una vivienda, y los propietarios que desean rentabilizar sus inmuebles, carecen de una plataforma integrada que centralice todo el proceso: desde la publicación y búsqueda, hasta la firma de contratos, el pago de arriendos y la generación de confianza mediante reputación verificable."**

### Situación Actual (Fragmentación)

| Actor | Problemas Actuales |
|-------|-------------------|
| **Arrendatario/Comprador** | Múltiples portales, información desactualizada, sin historial del propietario, contratos informales |
| **Propietario** | Duplicación de publicaciones, filtrado manual, sin historial del inquilino, seguimiento manual de pagos |
| **Arriendo por habitaciones** | Gestión compleja de múltiples contratos y pagos individuales |

### La Oportunidad

Crear un ecosistema donde:
- Propietarios e inquilinos puedan **verificarse mutuamente** mediante historial real
- Los **contratos sean digitales** y estandarizados
- Los **pagos queden registrados** generando trazabilidad
- La **reputación se construya** con cada relación contractual



---

## 🏗️ Arquitectura del Sistema

El proyecto implementa **Arquitectura Hexagonal** (Puertos y Adaptadores) en un único código base, organizada en tres capas principales:

┌─────────────────────────────────────────────────────────────┐
│ INFRASTRUCTURE LAYER                                        │
│ (Controllers, Repositories JPA, Security, External Services)│
└─────────────────────────────────────────────────────────────┘

▲
│ (Implementa puertos)
▼

┌─────────────────────────────────────────────────────────────┐
│ APPLICATION LAYER                                           │
│ (Casos de Uso, Servicios de Aplicación)                     │
└─────────────────────────────────────────────────────────────┘

▲
│ (Usa puertos)
▼

┌─────────────────────────────────────────────────────────────┐
│ DOMAIN LAYER                                                │
│ (Entidades, Objetos de Valor, Repositorios Interfaz)        │
└─────────────────────────────────────────────────────────────┘



### Principios Arquitectónicos

- **Dominio puro:** Sin dependencias externas
- **Casos de uso:** Orquestan la lógica de negocio
- **Infraestructura:** Implementa detalles técnicos (bases de datos, APIs)
- **Comunicación entre módulos:** A través de casos de uso y eventos de dominio

---

## 💻 Tecnologías Utilizadas

### Backend
| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.x | Framework de aplicación |
| Spring Security | 3.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia |
| JWT | 0.11.x | Tokens de autenticación |
| Maven | 3.8+ | Gestión de dependencias |

### Base de Datos
| Tecnología | Propósito |
|------------|-----------|
| PostgreSQL | Base de datos relacional |
| Supabase Storage | Almacenamiento de imágenes |

### Frontend (por definir)
- HTML5, CSS3, JavaScript
- Leaflet.js (mapas interactivos)
- Framework moderno (React/Vue/Angular - por definir)

### Herramientas de Desarrollo
- Git (control de versiones)
- JUnit (pruebas unitarias)
- Postman (pruebas de API)
- Swagger/OpenAPI (documentación)

---


