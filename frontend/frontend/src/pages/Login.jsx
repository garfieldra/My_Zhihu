import { useState } from 'react'
import { useAuth } from "../context/AuthContext"
import {useNavigate, Link, useLocation} from "react-router-dom"


export default function Login() {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [pending, setPending] = useState(false)
    const [error, setError] = useState('')

    const { login } = useAuth()
    const navigate = useNavigate()
    const location = useLocation()
    const from = location.state?.from?.pathname || '/';

    const onSubmit = async (e) => {
        e.preventDefault()
        setError('');
        setPending(true);
        try{
            await login(username.trim(), password)
            navigate(from, { replace: true })
        } catch (err) {
            const msg = err?.response?.data?.message ||
                err?.message ||
                '登录失败，请检查账号或密码';
            setError(msg);
        } finally {
            setPending(false)
        }
    }

    return (
        <div style={{ padding:24 }}>
            <h2>登录</h2>
            <form onSubmit={onSubmit} style={{ display:'grid', gap:12, maxWidth:360 }}>
                <label>
                    用户名
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="请输入用户名"
                        required
                        autoComplete="username"
                    />
                </label>
                <input placeholder="用户名" value={form.username} onChange={e => setForm({ ...form, username: e.target.value})} />
                <input placeholder="邮箱" value={form.email} onChange={e => setForm({ ...form, email: e.target.value})} />
                <button type="submit">登录</button>
                {error && <p>{error}</p>}
            </form>
            <p>没有账号？ <Link to="/register">去注册</Link></p>
        </div>
    )
}