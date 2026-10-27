export default function Brand({ light = false }) {
  return (
    <div className={`brand-mark${light ? ' light' : ''}`}>
      <span className="logo-dot" aria-hidden="true">+</span>
      <span>
        MediConnect
        <small>Hospital workspace</small>
      </span>
    </div>
  )
}
