import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../controllers/AuthController'
import { homePath } from '../models/api'
import Brand from './Brand'

export default function Login() {
  const { login, user, role } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  if (user) return <Navigate to={homePath(role)} replace />

  async function onSubmit(e) {
    e.preventDefault()
    setBusy(true)
    setError('')
    try {
      const result = await login(email, password)
      navigate(result.path)
    } catch (err) {
      setError(err.message || 'Login failed')
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="auth-shell">
      <section className="auth-hero">
        <Brand light />
        <h1>Care stays connected, from booking to discharge.</h1>
        <p>Appointments, labs, pharmacy, and staff workspaces in one place — with a dashboard for every role.</p>
        <ul className="hero-points">
          <li>Role-based access for admin, doctors, and patients</li>
          <li>Front desk check-in, lab results, and dispensing</li>
          <li>Secure JWT sign-in for every team</li>
        </ul>
      </section>
      <section className="auth-panel">
        <form className="card auth-card" onSubmit={onSubmit}>
          <Brand />
          <h2>Sign in</h2>
          <p className="muted">Use an account created here or assigned by an administrator.</p>
          {error && <p className="error">{error}</p>}
          <label htmlFor="login-email">Email</label>
          <input id="login-email" type="email" autoComplete="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          <label htmlFor="login-password">Password</label>
          <input id="login-password" type="password" autoComplete="current-password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          <div className="stack">
            <button className="btn" disabled={busy}>{busy ? 'Signing in…' : 'Sign in'}</button>
            <Link className="btn secondary" to="/register">Create account</Link>
          </div>
        </form>
      </section>
    </div>
  )
}
