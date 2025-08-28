import { useState } from 'react'
import { useAuth } from "../context/AuthContext"
import { useNavigate, Link } from "react-router-dom"


export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const { login } = useAuth()
    const navigate = useNavigate()

    const onSubmit = async (e) => {
        e.preventDefault()
        try{
            await login(username, password)
            navigate('/')
        } catch (err) {
            setError(err?.response?.data?.message || '登录失败')
        }
    }

    return (
        <div style={{ padding:24 }}>
            <h2>登录</h2>
            <form onSubmit={onSubmit} style={{ display:'grid', gap:12, maxWidth:320 }}>
                <input placeholder="用户名" value={form.username} onChange={e => setForm({ ...form, username: e.target.value})} />
                <input placeholder="邮箱" value={form.email} onChange={e => setForm({ ...form, email: e.target.value})} />
                <button type="submit">登录</button>
                {error && <p>{error}</p>}
            </form>
            <p>没有账号？ <Link to="/register">去注册</Link></p>
        </div>
    )
}