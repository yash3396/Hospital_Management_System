import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../controllers/AuthController'
import { homePath } from '../models/api'
import Brand from './Brand'

const ROLES = [
  { value: 'ROLE_PATIENT', label: 'Patient' },
  { value: 'ROLE_ADMIN', label: 'Admin' },
  { value: 'ROLE_DOCTOR', label: 'Doctor' },
  { value: 'ROLE_RECEPTIONIST', label: 'Receptionist' },
  { value: 'ROLE_LABTECHNICIAN', label: 'Lab technician' },
  { value: 'ROLE_PHARMACIST', label: 'Pharmacist' },
]

export default function Register() {
  const { register, user, role } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState({
    name: '',
    email: '',
    password: '',
    phone: '',
    address: '',
    role: 'ROLE_PATIENT',
  })
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  if (user) return <Navigate to={homePath(role)} replace />

  function set(key, value) {
    setForm((prev) => ({ ...prev, [key]: value }))
  }

  async function onSubmit(e) {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      const result = await register(form)
      navigate(result.path)
    } catch (err) {
      setError(err.message || 'Registration failed')
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-shell">
      <section className="auth-hero">
        <Brand light />
        <p className="badge">Join MediConnect</p>
        <h1>Create a patient or staff account.</h1>
        <p>Patients can self-register. Staff roles should only be created in a trusted hospital environment.</p>
      </section>
      <section className="auth-panel">
        <form className="card auth-card" onSubmit={onSubmit}>
          <Brand />
          <h2>Register</h2>
          {error && <p className="error">{error}</p>}
          <label>Full name</label>
          <input value={form.name} onChange={(e) => set('name', e.target.value)} required />
          <label>Email</label>
          <input type="email" value={form.email} onChange={(e) => set('email', e.target.value)} required />
          <div className="row">
            <div>
              <label>Password</label>
              <input type="password" value={form.password} onChange={(e) => set('password', e.target.value)} required />
            </div>
            <div>
              <label>Role</label>
              <select value={form.role} onChange={(e) => set('role', e.target.value)}>
                {ROLES.map((r) => (
                  <option key={r.value} value={r.value}>{r.label}</option>
                ))}
              </select>
            </div>
          </div>
          <div className="row">
            <div>
              <label>Phone</label>
              <input value={form.phone} onChange={(e) => set('phone', e.target.value)} />
            </div>
            <div>
              <label>Address</label>
              <input value={form.address} onChange={(e) => set('address', e.target.value)} />
            </div>
          </div>
          <div className="stack">
            <button className="btn" disabled={busy}>{busy ? 'Creating…' : 'Create account'}</button>
            <Link className="btn secondary" to="/login">Back to sign in</Link>
          </div>
        </form>
      </section>
    </div>
  )
}
