import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'
import { EmptyRow, Status } from './UiBits'

export default function PatientDashboard() {
  const { token } = useAuth()
  const [doctors, setDoctors] = useState([])
  const [appointments, setAppointments] = useState([])
  const [prescriptions, setPrescriptions] = useState([])
  const [labs, setLabs] = useState([])
  const [form, setForm] = useState({ doctorId: '', date: '', slot: '10:00', symptoms: '' })
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  async function refresh() {
    const [docs, appts, rx, tests] = await Promise.all([
      api('/catalog/doctors', { token }),
      api('/patient/appointments', { token }),
      api('/patient/prescriptions', { token }),
      api('/patient/lab-results', { token }),
    ])
    setDoctors(docs)
    setAppointments(appts)
    setPrescriptions(rx)
    setLabs(tests)
  }

  useEffect(() => {
    refresh().catch((err) => setError(err.message))
  }, [token])

  return (
    <div className="grid two">
      <section className="card">
        <h3>My appointments</h3>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Doctor</th><th>When</th><th>Status</th></tr></thead>
            <tbody>
              {appointments.length === 0 && <EmptyRow cols={4} text="No appointments yet. Book one on the right." />}
              {appointments.map((a) => (
                <tr key={a.id}>
                  <td>{a.id}</td>
                  <td>{a.doctor?.name}</td>
                  <td>{a.date} {a.slot}</td>
                  <td><Status value={a.status} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <h3 style={{ marginTop: 24 }}>Prescriptions</h3>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Diagnosis</th><th>Medicine</th><th>Dispensed</th></tr></thead>
            <tbody>
              {prescriptions.length === 0 && <EmptyRow cols={4} text="No prescriptions yet." />}
              {prescriptions.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.diagnosis}</td>
                  <td>{p.medicine}</td>
                  <td className={p.dispensed ? 'yes' : 'no'}>{p.dispensed ? 'Yes' : 'No'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <h3 style={{ marginTop: 24 }}>Lab results</h3>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Test</th><th>Status</th><th>Result</th></tr></thead>
            <tbody>
              {labs.length === 0 && <EmptyRow cols={4} text="No lab results yet." />}
              {labs.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <td>{t.testName}</td>
                  <td><Status value={t.status} /></td>
                  <td>{t.result || '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <section className="card">
        <h3>Book appointment</h3>
        <label>Doctor</label>
        <select value={form.doctorId} onChange={(e) => setForm({ ...form, doctorId: e.target.value })}>
          <option value="">Select doctor</option>
          {doctors.map((d) => <option key={d.id} value={d.id}>{d.name} — {d.specialization || 'General'}</option>)}
        </select>
        <label>Date</label>
        <input type="date" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} />
        <label>Slot</label>
        <input value={form.slot} onChange={(e) => setForm({ ...form, slot: e.target.value })} />
        <label>Symptoms</label>
        <textarea value={form.symptoms} onChange={(e) => setForm({ ...form, symptoms: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              await api('/patient/appointments', {
                method: 'POST',
                token,
                body: { ...form, doctorId: Number(form.doctorId) },
              })
              setMessage('Appointment booked')
              await refresh()
            } catch (err) {
              setError(err.message)
            }
          }}>Book</button>
        </div>
      </section>
    </div>
  )
}
