import { useState } from 'react'
import { useAuth } from '../controllers/AuthController'
import { api } from '../models/api'

const EXAMPLES = [
  'I have a headache and mild fever for two days. What should I tell the doctor?',
  'Explain Paracetamol 500mg in simple words.',
  'My blood test says WBC is a bit high. What does that mean in plain language?',
]

export default function AiAssistant() {
  const { token } = useAuth()
  const [provider, setProvider] = useState('GEMINI')
  const [prompt, setPrompt] = useState(EXAMPLES[0])
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [busy, setBusy] = useState(false)

  async function ask() {
    setBusy(true)
    setError('')
    setResult(null)
    try {
      const data = await api('/ai/assist', {
        method: 'POST',
        token,
        body: { provider, prompt },
      })
      setResult(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div className="grid two">
      <section className="card">
        <h3>Ask the hospital AI helper</h3>
        <p className="muted">
          This uses a dummy Google Gemini or dummy Ollama reply. It does not call a live model and it is not a diagnosis.
        </p>
        {error && <p className="error">{error}</p>}
        <label>Provider</label>
        <select value={provider} onChange={(e) => setProvider(e.target.value)}>
          <option value="GEMINI">Google Gemini (dummy)</option>
          <option value="OLLAMA">Ollama local LLM (dummy)</option>
        </select>
        <label>Your question</label>
        <textarea rows={6} value={prompt} onChange={(e) => setPrompt(e.target.value)} />
        <div className="stack">
          <button className="btn" disabled={busy || !prompt.trim()} onClick={ask}>
            {busy ? 'Thinking…' : 'Get dummy answer'}
          </button>
        </div>
        <p className="muted" style={{ marginTop: 16 }}>Try an example:</p>
        <div className="stack">
          {EXAMPLES.map((example) => (
            <button key={example} className="btn secondary" type="button" onClick={() => setPrompt(example)}>
              {example}
            </button>
          ))}
        </div>
      </section>
      <section className="card">
        <h3>Reply</h3>
        {!result && <p className="muted">Ask a question to see a dummy Gemini or Ollama-style answer.</p>}
        {result && (
          <>
            <p>
              <span className="badge">{result.provider}</span>{' '}
              <span className="muted">{result.model} · {result.mode}</span>
            </p>
            <pre className="ai-answer">{result.answer}</pre>
            <p className="muted">{result.disclaimer}</p>
          </>
        )}
      </section>
    </div>
  )
}
