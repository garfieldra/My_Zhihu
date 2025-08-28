import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import api from '../lib/api'

export default function QuestionDetail() {
    const { id } = useParams()
    const [q, getQ] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')

    useEffect(() => {
        (async () => {
            try{
                const res = await api.get(`/questions/${id}`)
                setQ(res.data)
            } catch (err) {
                setError("加载失败")
            } finally {
                setLoading(false)
            }
        })()
    }, [id])

    if (loading) return <div style={{ padding:24 }}>加载中...</div>
    if(error) return <div style={{ padding:24 }}>{error}</div>
    if(!q) return null

    return (
        <div style={{ padding:24 }}>
            <h2>{q.title}</h2>
            <p>{q.content}</p>
        </div>
    )
}