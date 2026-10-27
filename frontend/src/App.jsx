import { Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './controllers/AuthController'
import AdminDashboard from './views/AdminDashboard'
import AiAssistant from './views/AiAssistant'
import DoctorDashboard from './views/DoctorDashboard'
import LabDashboard from './views/LabDashboard'
import Layout from './views/Layout'
import Login from './views/Login'
import PatientDashboard from './views/PatientDashboard'
import PharmacyDashboard from './views/PharmacyDashboard'
import ReceptionistDashboard from './views/ReceptionistDashboard'
import Register from './views/Register'

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route element={<Layout allow="ROLE_ADMIN" />}>
          <Route path="/admin" element={<AdminDashboard />} />
        </Route>
        <Route element={<Layout allow="ROLE_DOCTOR" />}>
          <Route path="/doctor" element={<DoctorDashboard />} />
        </Route>
        <Route element={<Layout allow="ROLE_PATIENT" />}>
          <Route path="/patient" element={<PatientDashboard />} />
        </Route>
        <Route element={<Layout allow="ROLE_RECEPTIONIST" />}>
          <Route path="/receptionist" element={<ReceptionistDashboard />} />
        </Route>
        <Route element={<Layout allow="ROLE_LABTECHNICIAN" />}>
          <Route path="/lab" element={<LabDashboard />} />
        </Route>
        <Route element={<Layout allow="ROLE_PHARMACIST" />}>
          <Route path="/pharmacy" element={<PharmacyDashboard />} />
        </Route>
        <Route element={<Layout />}>
          <Route path="/ai" element={<AiAssistant />} />
        </Route>
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </AuthProvider>
  )
}
