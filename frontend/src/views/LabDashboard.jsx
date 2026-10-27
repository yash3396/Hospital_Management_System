import { useEffect, useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'
import { EmptyRow, Status } from './UiBits'

export default function LabDashboard() {
  const { token } = useAuth()
  const [tests, setTests] = useState([])
  const [result, setResult] = useState({ testRequestId: '', result: '' })
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')

  async function refresh() {
    setTests(await api('/lab/tests/pending', { token }))
  }

  useEffect(() => {
    refresh().catch((err) => setError(err.message))
  }, [token])

  async function updateStatus(testId, status) {
    try {
      await api(`/lab/tests/${testId}`, { method: 'PUT', token, body: { status } })
      setMessage(`Test ${testId} marked ${status}`)
      await refresh()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="grid two">
      <section className="card">
        <h3>Pending tests</h3>
        {error && <p className="error">{error}</p>}
        {message && <p className="ok">{message}</p>}
        <div className="table-wrap">
          <table>
            <thead><tr><th>ID</th><th>Test</th><th>Patient</th><th>Status</th><th></th></tr></thead>
            <tbody>
              {tests.length === 0 && <EmptyRow cols={5} text="No pending lab tests." />}
              {tests.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <td>{t.testName}</td>
                  <td>{t.appointment?.patient?.name}</td>
                  <td><Status value={t.status} /></td>
                  <td>
                    <div className="stack">
                      <button className="btn secondary" onClick={() => updateStatus(t.id, 'IN_PROGRESS')}>In progress</button>
                      <button className="btn" onClick={() => updateStatus(t.id, 'COMPLETED')}>Complete</button>
                      <button className="btn danger" onClick={() => updateStatus(t.id, 'CANCELLED')}>Cancel</button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
      <section className="card">
        <h3>Post result</h3>
        <label>Test request ID</label>
        <input value={result.testRequestId} onChange={(e) => setResult({ ...result, testRequestId: e.target.value })} />
        <label>Result</label>
        <textarea value={result.result} onChange={(e) => setResult({ ...result, result: e.target.value })} />
        <div className="stack">
          <button className="btn" onClick={async () => {
            try {
              await api('/lab/results', { method: 'POST', token, body: result })
              setMessage('Result saved')
              await refresh()
            } catch (err) { setError(err.message) }
          }}>Save result</button>
        </div>
      </section>
    </div>
  )
}
