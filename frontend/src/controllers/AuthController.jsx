import { createContext, useContext, useMemo, useState } from 'react'
import { api, homePath, primaryRole } from '../models/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('hms_token'))
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('hms_user')
    return raw ? JSON.parse(raw) : null
  })

  const value = useMemo(() => {
    const role = primaryRole(user)

    async function login(email, password) {
      const res = await api('/auth/login', { method: 'POST', body: { email, password } })
      const nextToken = res.token
      localStorage.setItem('hms_token', nextToken)
      setToken(nextToken)
      const me = await api('/auth/me', { token: nextToken })
      localStorage.setItem('hms_user', JSON.stringify(me))
      setUser(me)
      return { user: me, path: homePath(primaryRole(me)) }
    }

    async function register(payload) {
      await api('/auth/register', { method: 'POST', body: payload })
      return login(payload.email, payload.password)
    }

    function logout() {
      localStorage.removeItem('hms_token')
      localStorage.removeItem('hms_user')
      setToken(null)
      setUser(null)
    }

    return { token, user, role, login, register, logout }
  }, [token, user])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used inside AuthProvider')
  return ctx
}
