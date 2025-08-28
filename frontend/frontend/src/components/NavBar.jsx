import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function NavBar() {
    const { user, logout } = useAuth()
    const navigate = useNavigate()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <nav style={{ display: 'flex', gap:12, padding: 16, borderBottom: '1px solid #eee' }}>
            <Link to="/">MyZhihu</Link>
            <Link to="questions">Questions</Link>
            <div style={{ marginLeft:'auto' }}>
                {user ? (
                    <>
                    <span style={{ marginRight:8 }}>Hi, {user.username}</span>
                    <button onClick={handleLogout}>Logout</button>
                    </>
                ) : (
                    <Link to="/login">Login</Link>
                )}
            </div>
        </nav>
    )
}