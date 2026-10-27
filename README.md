# MediConnect

MediConnect is a small hospital workspace.

Staff and patients use it to:

- create accounts and log in
- manage departments and doctors
- book and check in appointments
- write prescriptions and request lab tests
- update lab results and pharmacy stock
- ask a **dummy AI helper** (Google Gemini style or Ollama style)

It is a student-style full stack app:

- **Backend:** Spring Boot (Java 21), REST APIs, JWT login
- **Database:** MySQL, with **Spring JDBC** and SQL files (not Hibernate)
- **Frontend:** React + Vite in the `frontend` folder

---

## What you need on your computer

1. **Java 21**
2. **Maven** (or use `mvnw.cmd` in this folder)
3. **MySQL 8** running on port `3306`
4. **Node.js** (for the React UI)
5. A browser

You do **not** need a real Google Gemini key or a real Ollama server. The AI feature is dummy.

---

## How the project is built (simple MVC)

| Layer | Where | Job |
|---|---|---|
| **Controller** | `src/main/java/.../controller` | Receives HTTP requests |
| **Model** | `src/main/java/.../model` | Plain Java objects (User, Doctor, Appointment, …) |
| **View (API)** | `src/main/java/.../view` | Request and response JSON |
| **Service** | `src/main/java/.../service` | Business rules |
| **SQL / JDBC** | `src/main/resources/*.sql` and `.../repository` | Real SQL sent to MySQL |
| **UI views** | `frontend/src/views` | Screens the user sees |
| **UI controller** | `frontend/src/controllers` | Login session |
| **UI model** | `frontend/src/models/api.js` | Calls `/api/...` |

---

## MySQL setup (one time)

1. Start MySQL.
2. Create the database if it is missing:

```sql
CREATE DATABASE IF NOT EXISTS hospitaldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Match the file `src/main/resources/application.properties`:

- URL: `jdbc:mysql://localhost:3306/hospitaldb`
- Username: `root`
- Password: `root`

If your MySQL password is different, change that file.

On first start, Spring runs:

- `src/main/resources/schema.sql` — creates tables
- `src/main/resources/data.sql` — inserts default roles

SQL used by the app day to day lives in:

`src/main/java/com/example/HospitalManagementSystem/repository/`

Example: login looks up a user with:

```sql
SELECT u.id, u.name, u.email, u.password, ...
FROM users u
LEFT JOIN user_roles ur ON ur.user_id = u.id
LEFT JOIN roles r ON r.id = ur.role_id
WHERE u.email = ?
```

---

## Run the backend

From the project root:

```bash
mvn spring-boot:run
```

Windows with the wrapper:

```bash
mvnw.cmd spring-boot:run
```

Wait until it starts.

- API base: http://localhost:8080
- Swagger (try APIs in the browser): http://localhost:8080/swagger-ui.html

---

## Run the frontend

In a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**

The UI talks to the backend through a Vite proxy (`/api` → `http://localhost:8080`).

---

## First login (easy path)

1. Open Register.
2. Create an admin:

- Name: `Admin User`
- Email: `admin@example.com`
- Password: `AdminPass123!`
- Role: `ROLE_ADMIN`

3. You are taken to the admin desk.
4. Create a department, then a doctor.
5. Register a patient (`ROLE_PATIENT`) and book an appointment.

**Roles you can pick**

| Role | Who uses it |
|---|---|
| `ROLE_ADMIN` | Hospital admin |
| `ROLE_DOCTOR` | Doctor |
| `ROLE_PATIENT` | Patient |
| `ROLE_RECEPTIONIST` | Front desk |
| `ROLE_LABTECHNICIAN` | Lab |
| `ROLE_PHARMACIST` | Pharmacy |

After login, the app stores a JWT token and sends it as:

`Authorization: Bearer <token>`

---

## Dummy AI (Google Gemini + Ollama)

Every logged-in user sees **AI assistant** in the side menu (`/ai`).

You choose:

- **Google Gemini (dummy)** — pretends to be `gemini-1.5-flash`
- **Ollama (dummy)** — pretends to be local `llama3.2` at `http://localhost:11434`

The backend **does not call Google or Ollama**. It returns a canned, keyword-based hospital hint and a disclaimer.

Settings in `application.properties`:

```properties
app.ai.gemini.api-key=dummy-gemini-key
app.ai.gemini.model=gemini-1.5-flash
app.ai.ollama.base-url=http://localhost:11434
app.ai.ollama.model=llama3.2
```

### API

`POST /api/ai/assist`  
Header: `Authorization: Bearer <token>`

```json
{
  "provider": "GEMINI",
  "prompt": "I have a headache and mild fever. What should I tell the doctor?"
}
```

Use `"OLLAMA"` instead of `"GEMINI"` for the other dummy provider.

Example reply:

```json
{
  "provider": "GEMINI",
  "model": "gemini-1.5-flash",
  "mode": "DUMMY",
  "answer": "...dummy text...",
  "disclaimer": "Dummy AI only. This is not a diagnosis..."
}
```

This is a demo only. It is **not** medical advice.

To use real APIs later, replace the dummy method in `AiAssistantService` with HTTP calls to Gemini and Ollama, and put a real key in `app.ai.gemini.api-key`.

---

## Main APIs (short list)

**Auth**

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

**Admin**

- `POST /api/admin/departments` · `GET /api/admin/departments`
- `POST /api/admin/doctors` · `GET /api/admin/doctors` · `PUT /api/admin/doctors/{id}` · `DELETE /api/admin/doctors/{id}`
- `POST /api/admin/schedules`
- `PUT /api/admin/users/{id}/role`
- `PUT /api/admin/users/{id}/password`

**Doctor**

- `GET /api/doctor/appointments`
- `GET /api/doctor/patients/{patientId}/history`
- `POST /api/doctor/prescriptions`
- `POST /api/doctor/lab-tests`

**Patient**

- `POST /api/patient/appointments` · `GET /api/patient/appointments`
- `GET /api/patient/prescriptions`
- `GET /api/patient/lab-results`

**Receptionist**

- `GET /api/receptionist/patients` · `POST /api/receptionist/patients`
- `GET /api/receptionist/doctors`
- `POST /api/receptionist/appointments?patientId=`
- `PUT /api/receptionist/appointments/{id}/checkin`

**Lab**

- `GET /api/lab/tests/pending`
- `PUT /api/lab/tests/{testId}`
- `POST /api/lab/results`

**Pharmacy**

- `GET /api/pharmacy/prescriptions`
- `POST /api/pharmacy/dispense`
- `PUT /api/pharmacy/stock`

**Catalog / AI**

- `GET /api/catalog/doctors`
- `POST /api/ai/assist`

Full try-out UI: Swagger at `/swagger-ui.html`.

---

## Example: book a visit (from Swagger)

1. Register admin, then login. Copy `token`.
2. Click **Authorize** and paste `Bearer <token>`.
3. Create a department:

```json
{ "name": "Neurology", "description": "Nerve related treatments" }
```

4. Add a doctor (use the department id from step 3):

```json
{
  "name": "Dr Karthiga",
  "specialization": "Neurology",
  "email": "karthiga.doctor@example.com",
  "phone": "1234567890",
  "departmentId": 1
}
```

Admin can reset that doctor’s password with `PUT /api/admin/users/{id}/password`.

5. Register a patient with role `ROLE_PATIENT`, login, then book:

```json
{
  "doctorId": 1,
  "date": "2026-12-01",
  "slot": "10:00",
  "symptoms": "Headache and fever"
}
```

---

## Database tables

Created by `schema.sql`:

| Table | Stores |
|---|---|
| `roles` | Role names (`ROLE_ADMIN`, …) |
| `users` | People who can log in |
| `user_roles` | Which roles a user has |
| `departments` | Hospital departments |
| `doctors` | Doctor profiles (linked to a user) |
| `schedules` / `schedule_slots` | Doctor time slots |
| `appointments` | Booked visits |
| `prescriptions` | Diagnosis and medicine |
| `lab_tests` | Lab requests and results |
| `medicine_stock` | Pharmacy stock |

---

## Docker (optional)

`docker-compose.yml` starts MySQL and the Spring app.

The React UI is still started with `npm run dev` unless you add it to Docker yourself.

---

## Project folders

```
Hospital_Management_System/
  pom.xml
  README.md
  src/main/java/.../controller, model, view, service, repository, security
  src/main/resources/application.properties, schema.sql, data.sql
  frontend/          React app
  docker-compose.yml
```

---

## If something fails

| Problem | What to check |
|---|---|
| Backend will not start | MySQL is running; database `hospitaldb` exists; user/password match `application.properties` |
| Login fails | Register first; use the same email and password |
| Frontend cannot load data | Backend is on port 8080; you are logged in |
| AI always says DUMMY | That is expected. It is not a live Gemini/Ollama call |
| Java version error | Use JDK **21** (`JAVA_HOME`) |

---

## Note on care

MediConnect is a demo. Dummy AI text is not a diagnosis. Real patient care needs a licensed clinician.
