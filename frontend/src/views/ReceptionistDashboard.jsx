import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'
import { EmptyRow } from './UiBits'

export default function ReceptionistDashboard() {
  const { token } = useAuth()
  const [patients, setPatients] = useState([])
  const [doctors, setDoctors] = useState([])
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')
  const [patient, setPatient] = useState({ name: '', email: '', password: '', phone: '' })
  const [booking, setBooking] = useState({ patientId: '', doctorId: '', date: '', slot: '10:00', symptoms: '' })
  const [checkin, setCheckin] = useState({ id: '', status: 'CHECKED_IN' })

  async function refresh() {
    const [p, d] = await Promise.all([
      api('/receptionist/patients', { token }),
      api('/receptionist/doctors', { token }),
    ])
    setPatients(p)
    setDoctors(d)
  }

  useEffect(() => {
    refresh().catch((err) => setError(err.message))
  }, [token])

  return (
    <div className="grid two">
      <section className="card">
        <h3>Patients</h3>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Phone</th></tr></thead>
            <tbody>
              {patients.length === 0 && <EmptyRow cols={4} text="No patients registered yet." />}
              {patients.map((p) => (
                <tr key={p.id}><td>{p.id}</td><td>{p.name}</td><td>{p.email}</td><td>{p.phone}</td></tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <section className="card">
        <h3>Register patient</h3>
        <label>Name</label>
        <input value={patient.name} onChange={(e) => setPatient({ ...patient, name: e.target.value })} />
        <label>Email</label>
        <input type="email" value={patient.email} onChange={(e) => setPatient({ ...patient, email: e.target.value })} />
        <label>Temporary password</label>
        <input value={patient.password} onChange={(e) => setPatient({ ...patient, password: e.target.value })} />
        <label>Phone</label>
        <input value={patient.phone} onChange={(e) => setPatient({ ...patient, phone: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              const created = await api('/receptionist/patients', { method: 'POST', token, body: patient })
              setMessage(`Patient created with id ${created.id}`)
              await refresh()
            } catch (err) { setError(err.message) }
          }}>Register</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Book for patient</h3>
        <label>Patient</label>
        <select value={booking.patientId} onChange={(e) => setBooking({ ...booking, patientId: e.target.value })}>
          <option value="">Select</option>
          {patients.map((p) => <option key={p.id} value={p.id}>{p.name} (#{p.id})</option>)}
        </select>
        <label>Doctor</label>
        <select value={booking.doctorId} onChange={(e) => setBooking({ ...booking, doctorId: e.target.value })}>
          <option value="">Select</option>
          {doctors.map((d) => <option key={d.id} value={d.id}>{d.name}</option>)}
        </select>
        <label>Date</label>
        <input type="date" value={booking.date} onChange={(e) => setBooking({ ...booking, date: e.target.value })} />
        <label>Slot</label>
        <input value={booking.slot} onChange={(e) => setBooking({ ...booking, slot: e.target.value })} />
        <label>Symptoms</label>
        <input value={booking.symptoms} onChange={(e) => setBooking({ ...booking, symptoms: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              const a = await api(`/receptionist/appointments?patientId=${booking.patientId}`, {
                method: 'POST',
                token,
                body: { doctorId: Number(booking.doctorId), date: booking.date, slot: booking.slot, symptoms: booking.symptoms },
              })
              setMessage(`Appointment #${a.id} booked`)
            } catch (err) { setError(err.message) }
          }}>Book</button>
        </div>

        <h3 style={{ marginTop: 24 }}>Check-in</h3>
        <label>Appointment ID</label>
        <input value={checkin.id} onChange={(e) => setCheckin({ ...checkin, id: e.target.value })} />
        <label>Status</label>
        <select value={checkin.status} onChange={(e) => setCheckin({ ...checkin, status: e.target.value })}>
          <option>CHECKED_IN</option>
          <option>SCHEDULED</option>
          <option>COMPLETED</option>
          <option>CANCELLED</option>
        </select>
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              const a = await api(`/receptionist/appointments/${checkin.id}/checkin`, {
                method: 'PUT',
                token,
                body: { status: checkin.status },
              })
              setMessage(`Appointment #${a.id} is ${a.status}`)
            } catch (err) { setError(err.message) }
          }}>Update</button>
        </div>
      </section>
    </div>
  )
}
