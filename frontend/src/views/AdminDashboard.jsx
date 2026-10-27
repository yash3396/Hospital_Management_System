import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api, roleLabel } from '../models/api'
import { EmptyRow } from './UiBits'

const ROLE_OPTIONS = [
  'ROLE_ADMIN',
  'ROLE_DOCTOR',
  'ROLE_PATIENT',
  'ROLE_RECEPTIONIST',
  'ROLE_LABTECHNICIAN',
  'ROLE_PHARMACIST',
]

export default function AdminDashboard() {
  const { token } = useAuth()
  const [departments, setDepartments] = useState([])
  const [doctors, setDoctors] = useState([])
  const [users, setUsers] = useState([])
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [dept, setDept] = useState({ name: '', description: '' })
  const [doctor, setDoctor] = useState({ name: '', specialization: '', email: '', phone: '', departmentId: '' })
  const [schedule, setSchedule] = useState({ doctorId: '', date: '', slots: '09:00,10:00,11:00' })
  const [roleForm, setRoleForm] = useState({ id: '', role: 'ROLE_RECEPTIONIST' })
  const [passwordForm, setPasswordForm] = useState({ id: '', password: '' })

  async function refresh() {
    const [d, docs, u] = await Promise.all([
      api('/admin/departments', { token }),
      api('/admin/doctors', { token }),
      api('/admin/users', { token }),
    ])
    setDepartments(d)
    setDoctors(docs)
    setUsers(u)
  }

  useEffect(() => {
    refresh().catch((err) => setError(err.message))
  }, [token])

  async function run(action, success) {
    setError('')
    setMessage('')
    try {
      const result = await action()
      setMessage(success(result))
      await refresh()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="grid two">
      <section className="card">
        <div className="section-head"><h3>Departments & doctors</h3></div>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="kpi">
          <div><span className="muted">Departments</span><strong>{departments.length}</strong></div>
          <div><span className="muted">Doctors</span><strong>{doctors.length}</strong></div>
          <div><span className="muted">Users</span><strong>{users.length}</strong></div>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Doctor</th><th>Email</th><th>Dept</th><th></th></tr></thead>
            <tbody>
              {doctors.length === 0 && <EmptyRow cols={5} text="No doctors yet. Add one on the right." />}
              {doctors.map((d) => (
                <tr key={d.id}>
                  <td>{d.id}</td>
                  <td>{d.name}<br /><span className="muted">{d.specialization}</span></td>
                  <td>{d.email}</td>
                  <td>{d.department?.name}</td>
                  <td>
                    <button className="btn danger" onClick={() => run(() => api(`/admin/doctors/${d.id}`, { method: 'DELETE', token }), () => `Removed ${d.name}`)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <h3 style={{ marginTop: 24 }}>Users</h3>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Roles</th></tr></thead>
            <tbody>
              {users.length === 0 && <EmptyRow cols={4} text="No users registered yet." />}
              {users.map((u) => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.name}</td>
                  <td>{u.email}</td>
                  <td>{(u.roles || []).map((r) => roleLabel(r.name)).join(', ')}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      <section className="card">
        <h3>Add department</h3>
        <label>Name</label>
        <input value={dept.name} onChange={(e) => setDept({ ...dept, name: e.target.value })} />
        <label>Description</label>
        <input value={dept.description} onChange={(e) => setDept({ ...dept, description: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={() => run(() => api('/admin/departments', { method: 'POST', token, body: dept }), (d) => `Created department ${d.name}`)}>Save department</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Add doctor</h3>
        <label>Name</label>
        <input value={doctor.name} onChange={(e) => setDoctor({ ...doctor, name: e.target.value })} />
        <label>Specialization</label>
        <input value={doctor.specialization} onChange={(e) => setDoctor({ ...doctor, specialization: e.target.value })} />
        <label>Email</label>
        <input type="email" value={doctor.email} onChange={(e) => setDoctor({ ...doctor, email: e.target.value })} />
        <label>Phone</label>
        <input value={doctor.phone} onChange={(e) => setDoctor({ ...doctor, phone: e.target.value })} />
        <label>Department</label>
        <select value={doctor.departmentId} onChange={(e) => setDoctor({ ...doctor, departmentId: e.target.value })}>
          <option value="">Select</option>
          {departments.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
        </select>
        <div className="stack">
          <button className="btn" onClick={() => run(
            () => api('/admin/doctors', { method: 'POST', token, body: { ...doctor, departmentId: Number(doctor.departmentId) } }),
            (d) => `Doctor ${d.name} created. Reset their password below using user id ${d.user?.id}.`
          )}>Save doctor</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Doctor schedule</h3>
        <label>Doctor</label>
        <select value={schedule.doctorId} onChange={(e) => setSchedule({ ...schedule, doctorId: e.target.value })}>
          <option value="">Select</option>
          {doctors.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
        </select>
        <label>Date</label>
        <input type="date" value={schedule.date} onChange={(e) => setSchedule({ ...schedule, date: e.target.value })} />
        <label>Slots (comma-separated)</label>
        <input value={schedule.slots} onChange={(e) => setSchedule({ ...schedule, slots: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={() => run(
            () => api('/admin/schedules', { method: 'POST', token, body: { doctorId: Number(schedule.doctorId), date: schedule.date, slots: schedule.slots.split(',').map((s) => s.trim()).filter(Boolean) } }),
            () => 'Schedule saved'
          )}>Save schedule</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Assign role</h3>
        <label>User ID</label>
        <input value={roleForm.id} onChange={(e) => setRoleForm({ ...roleForm, id: e.target.value })} />
        <label>Role</label>
        <select value={roleForm.role} onChange={(e) => setRoleForm({ ...roleForm, role: e.target.value })}>
          {ROLE_OPTIONS.map((r) => <option key={r} value={r}>{roleLabel(r)}</option>)}
        </select>
        <div className="stack">
          <button className="btn" onClick={() => run(
            () => api(`/admin/users/${roleForm.id}/role`, { method: 'PUT', token, body: { role: roleForm.role } }),
            () => 'Role assigned'
          )}>Assign</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Reset password</h3>
        <label>User ID</label>
        <input value={passwordForm.id} onChange={(e) => setPasswordForm({ ...passwordForm, id: e.target.value })} />
        <label>New password (optional)</label>
        <input value={passwordForm.password} onChange={(e) => setPasswordForm({ ...passwordForm, password: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={() => run(
            () => api(`/admin/users/${passwordForm.id}/password`, { method: 'PUT', token, body: { password: passwordForm.password || null } }),
            (res) => `Temporary password: ${res.temporaryPassword}`
          )}>Reset</button>
        </div>
      </section>
    </div>
  )
}
