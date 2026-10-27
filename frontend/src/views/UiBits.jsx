export function Status({ value }) {
  const key = (value || 'unknown').toLowerCase().replaceAll('_', '-')
  return <span className={`status ${key}`}>{String(value || '—').replaceAll('_', ' ')}</span>
}

export function EmptyRow({ cols, text = 'Nothing here yet' }) {
  return (
    <tr>
      <td colSpan={cols} className="empty-cell">{text}</td>
    </tr>
  )
}
