import { NavLink, Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../controllers/AuthController'
import { homePath, roleLabel } from '../models/api'
import Brand from './Brand'

const AI_LINK = { to: '/ai', label: 'AI assistant' }

const LINKS = {
  ROLE_ADMIN: [{ to: '/admin', label: 'Admin desk' }, AI_LINK],
  ROLE_DOCTOR: [{ to: '/doctor', label: 'Doctor workspace' }, AI_LINK],
  ROLE_PATIENT: [{ to: '/patient', label: 'My care' }, AI_LINK],
  ROLE_RECEPTIONIST: [{ to: '/receptionist', label: 'Front desk' }, AI_LINK],
  ROLE_LABTECHNICIAN: [{ to: '/lab', label: 'Laboratory' }, AI_LINK],
  ROLE_PHARMACIST: [{ to: '/pharmacy', label: 'Pharmacy' }, AI_LINK],
}

const PAGE = {
  ROLE_ADMIN: { title: 'Hospital administration', subtitle: 'Departments, doctors, roles, and schedules' },
  ROLE_DOCTOR: { title: 'Doctor workspace', subtitle: 'Appointments, history, prescriptions, and lab requests' },
  ROLE_PATIENT: { title: 'My care', subtitle: 'Book visits and review prescriptions and lab results' },
  ROLE_RECEPTIONIST: { title: 'Front desk', subtitle: 'Register patients, book visits, and check in arrivals' },
  ROLE_LABTECHNICIAN: { title: 'Laboratory', subtitle: 'Pending tests and result posting' },
  ROLE_PHARMACIST: { title: 'Pharmacy', subtitle: 'Dispense medicines and keep stock current' },
}

const AI_PAGE = { title: 'AI assistant', subtitle: 'Dummy Google Gemini and Ollama helper for hospital notes' }

export default function Layout({ allow }) {
  const { user, role, logout } = useAuth()
  const location = useLocation()

  if (!user || !role) return <Navigate to="/login" replace />
  if (allow && role !== allow) return <Navigate to={homePath(role)} replace />

  const page = location.pathname === '/ai'
    ? AI_PAGE
    : (PAGE[role] || { title: `Welcome, ${user.name}`, subtitle: user.email })

  return (
    <div className="app-shell">
      <header className="mobile-bar">
        <Brand light />
        <button className="linkish" onClick={logout}>Sign out</button>
      </header>
      <aside className="sidebar">
        <Brand light />
        <p className="muted" style={{ color: '#b7d7d1', margin: '4px 0 0' }}>{user.name}</p>
        <p><span className="badge">{roleLabel(role)}</span></p>
        <nav className="sidebar-nav">
          {(LINKS[role] || []).map((link) => (
            <NavLink key={link.to} to={link.to} end>
              {link.label}
            </NavLink>
          ))}
        </nav>
        <button className="linkish" onClick={logout} style={{ marginTop: 'auto' }}>Sign out</button>
      </aside>
      <main className="main">
        <div className="topbar">
          <div>
            <h2>{page.title}</h2>
            <p className="muted">{page.subtitle}</p>
          </div>
          <p className="muted">{user.email}</p>
        </div>
        <Outlet />
      </main>
    </div>
  )
}
