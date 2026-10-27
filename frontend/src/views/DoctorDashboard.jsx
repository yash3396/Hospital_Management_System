import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'
import { EmptyRow, Status } from './UiBits'

export default function DoctorDashboard() {
  const { token } = useAuth()
  const [appointments, setAppointments] = useState([])
  const [history, setHistory] = useState([])
  const [patientId, setPatientId] = useState('')
  const [rx, setRx] = useState({ appointmentId: '', diagnosis: '', medicine: '' })
  const [lab, setLab] = useState({ appointmentId: '', testName: 'Blood Test' })
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  async function load() {
    setAppointments(await api('/doctor/appointments', { token }))
  }

  useEffect(() => {
    load().catch((err) => setError(err.message))
  }, [token])

  return (
    <div className="grid two">
      <section className="card">
        <h3>Appointments</h3>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Patient</th><th>Date / slot</th><th>Status</th><th>Symptoms</th></tr></thead>
            <tbody>
              {appointments.length === 0 && <EmptyRow cols={5} text="No appointments assigned yet." />}
              {appointments.map((a) => (
                <tr key={a.id}>
                  <td>{a.id}</td>
                  <td>{a.patient?.name}<br /><span className="muted">#{a.patient?.id}</span></td>
                  <td>{a.date} {a.slot}</td>
                  <td><Status value={a.status} /></td>
                  <td>{a.symptoms}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <h3 style={{ marginTop: 24 }}>Patient history</h3>
        <label>Patient ID</label>
        <input value={patientId} onChange={(e) => setPatientId(e.target.value)} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              setHistory(await api(`/doctor/patients/${patientId}/history`, { token }))
            } catch (err) {
              setError(err.message)
            }
          }}>Load history</button>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Date</th><th>Slot</th><th>Status</th></tr></thead>
            <tbody>
              {history.length === 0 && <EmptyRow cols={4} text="Load a patient ID to see history." />}
              {history.map((a) => (
                <tr key={a.id}><td>{a.id}</td><td>{a.date}</td><td>{a.slot}</td><td><Status value={a.status} /></td></tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <section className="card">
        <h3>Write prescription</h3>
        <label>Appointment ID</label>
        <input value={rx.appointmentId} onChange={(e) => setRx({ ...rx, appointmentId: e.target.value })} />
        <label>Diagnosis</label>
        <input value={rx.diagnosis} onChange={(e) => setRx({ ...rx, diagnosis: e.target.value })} />
        <label>Medicine</label>
        <textarea value={rx.medicine} onChange={(e) => setRx({ ...rx, medicine: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              await api('/doctor/prescriptions', { method: 'POST', token, body: { ...rx, appointmentId: Number(rx.appointmentId) } })
              setMessage('Prescription saved')
            } catch (err) { setError(err.message) }
          }}>Save prescription</button>
        </div>
        <h3 style={{ marginTop: 24 }}>Request lab test</h3>
        <label>Appointment ID</label>
        <input value={lab.appointmentId} onChange={(e) => setLab({ ...lab, appointmentId: e.target.value })} />
        <label>Test name</label>
        <input value={lab.testName} onChange={(e) => setLab({ ...lab, testName: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              await api('/doctor/lab-tests', { method: 'POST', token, body: { ...lab, appointmentId: Number(lab.appointmentId) } })
              setMessage('Lab test requested')
            } catch (err) { setError(err.message) }
          }}>Request test</button>
        </div>
      </section>
    </div>
  )
}
