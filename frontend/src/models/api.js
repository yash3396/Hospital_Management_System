export async function api(path, { method = 'GET', body, token } = {}) {
  const headers = {}
  if (body !== undefined) {
    headers['Content-Type'] = 'application/json'
  }
  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const res = await fetch(`/api${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (res.status === 204) return null

  const text = await res.text()
  let data = null
  if (text) {
    try {
      data = JSON.parse(text)
    } catch {
      data = { message: text }
    }
  }

  if (!res.ok) {
    const message = data?.message || data?.error || `Request failed (${res.status})`
    throw new Error(message)
  }
  return data
}

export function roleNames(user) {
  return (user?.roles || []).map((r) => r.name || r)
}

export function primaryRole(user) {
  const names = roleNames(user)
  const order = [
    'ROLE_ADMIN',
    'ROLE_DOCTOR',
    'ROLE_RECEPTIONIST',
    'ROLE_LABTECHNICIAN',
    'ROLE_PHARMACIST',
    'ROLE_PATIENT',
  ]
  return order.find((role) => names.includes(role)) || names[0] || null
}

export function homePath(role) {
  switch (role) {
    case 'ROLE_ADMIN':
      return '/admin'
    case 'ROLE_DOCTOR':
      return '/doctor'
    case 'ROLE_PATIENT':
      return '/patient'
    case 'ROLE_RECEPTIONIST':
      return '/receptionist'
    case 'ROLE_LABTECHNICIAN':
      return '/lab'
    case 'ROLE_PHARMACIST':
      return '/pharmacy'
    default:
      return '/login'
  }
}

export function roleLabel(role) {
  return (role || '').replace('ROLE_', '').replace('LABTECHNICIAN', 'LAB TECH')
}
