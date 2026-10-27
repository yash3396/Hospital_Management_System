import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'
import { EmptyRow } from './UiBits'

export default function PharmacyDashboard() {
  const { token } = useAuth()
  const [prescriptions, setPrescriptions] = useState([])
  const [stock, setStock] = useState({ medicineName: '', quantity: 100 })
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  async function refresh() {
    setPrescriptions(await api('/pharmacy/prescriptions', { token }))
  }

  useEffect(() => {
    refresh().catch((err) => setError(err.message))
  }, [token])

  return (
    <div className="grid two">
      <section className="card">
        <h3>Waiting to dispense</h3>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Patient</th><th>Medicine</th><th></th></tr></thead>
            <tbody>
              {prescriptions.length === 0 && <EmptyRow cols={4} text="Nothing waiting to dispense." />}
              {prescriptions.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.appointment?.patient?.name}</td>
                  <td>{p.medicine}</td>
                  <td>
                    <button className="btn" onClick={async () => {
                      try {
                        await api('/pharmacy/dispense', { method: 'POST', token, body: { prescriptionId: p.id } })
                        setMessage(`Dispensed prescription ${p.id}`)
                        await refresh()
                      } catch (err) { setError(err.message) }
                    }}>Dispense</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <section className="card">
        <h3>Update stock</h3>
        <label>Medicine</label>
        <input value={stock.medicineName} onChange={(e) => setStock({ ...stock, medicineName: e.target.value })} />
        <label>Quantity</label>
        <input type="number" value={stock.quantity} onChange={(e) => setStock({ ...stock, quantity: Number(e.target.value) })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              const saved = await api('/pharmacy/stock', { method: 'PUT', token, body: stock })
              setMessage(`${saved.medicineName || stock.medicineName} stock is now ${saved.quantity ?? stock.quantity}`)
            } catch (err) { setError(err.message) }
          }}>Save stock</button>
        </div>
      </section>
    </div>
  )
}
