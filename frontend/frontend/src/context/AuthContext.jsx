import { createContext, useContext, useEffect, useState } from "react"
import api from "../lib/api"

const AuthContext = createContext(null)

function parseJwt(t){
    try{
        const base64Url = t.split(".")[1]
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split(".")
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join(''),
            )
        const payload = JSON.parse(jsonPayload)
        return {
            username: payload.sub,
            exp: payload.exp
        }
    }
    catch{
        return null
    }
}

export function AuthProvider({ children }) {
    const [token, setToken] = useState(localStorage.getItem("token"))
    const [user, setUser] = useState(token ? parseJwt(token) : null)

    useEffect(() => {
        if (token) {
            localStorage.setItem("token", token)
            setUser(parseJwt(token))
        } else{
            localStorage.removeItem("token")
            setUser(null)
        }
    }, [token])

    const login = async (username, password) => {
        const res = await api.post("/api/auth/login", { username, password })
        const tk = res.data.token || res.data.accessToken || res.data
        setToken(tk)
    }

    const register = async (username, email, password) => {
        const res = await api.post("/api/auth/register", { username, email, password })
        return res.data
    }

    const logout = () => setToken(null)

    return (
        <AuthContext.Provider value={{ token, user, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)