import { useState } from "react"
import { useAuth } from '../context/AuthContext'
import { useNavigate, Link } from "react-router-dom"

export default function Register() {
    const { register } = useAuth()
    const navigate = useNavigate()
    const [form, setForm] = useState({ username: '', email: '', password: '' })
    const [error, setError] = useState('')

    const onSubmit = async (e) =>{
        e.preventDefault()
        try{
            await register(form.username, form.email, form.password)
            navigate('/login')
        }catch(err){
            setError(err?.response?.data?.message || '注册失败')
        }
    }

    return (
        <div style={{ padding:24 }}>
            <h2>注册</h2>
            <form onSubmit={onSubmit} style={{ display: 'grid', gap:12, maxWidth:320 }}>
                <input placeholder="用户名" value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} />
                <input placeholder="邮箱" value={form.email} onChange={e => setForm({ ...form, username: e.target.value})} />
                <input placeholder="密码" type="password" value={form.password} onChange={e => setForm({ ...form, username: e.target.value })} />
                <button type="submit">注册</button>
                {error && <p>{error}</p>}
            </form>
            <p>已有账号？ <Link to="/login">去登录</Link></p>
        </div>
    )
}