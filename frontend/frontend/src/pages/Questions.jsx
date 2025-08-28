import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import api from '../lib/api'

export default function Questions() {
    const [list, setList] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setErroe] = useState('')

    useEffect(() => {
        (async () => {
            try{
                const res = await api.get('/questions')
                setList(Array.isArray(res.data) ? res.data : [])
            } catch(err){
                setError('加载失败')
            } finally {
                setLoading(false)
            }
        })()
    }, [])

    if (loading)  return <div style={{ padding: 24 }}>加载中...</div>
    if(error) return <div style={{ padding:24 }}>{error}</div>

    return (
        <div style={{ padding:24 }}>
            <h2>问题列表</h2>
            <ul>
                {list.map(q => (
                    <li key={q.id}>
                        <Link to={`/questions/${q.id}`}>{q.title}</Link>
                    </li>
                ))}
            </ul>
        </div>
    )
}