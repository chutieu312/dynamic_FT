import { useEffect, useMemo, useState } from 'react'
import './App.css'
import { listOrders, createOrder, updateOrder, deleteOrder } from './api'

const fmt = (n) => Number(n).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })

export default function App() {
  const [orders, setOrders] = useState([])
  const [item, setItem] = useState('')
  const [price, setPrice] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [filter, setFilter] = useState('')

  const filtered = useMemo(() => {
    const f = filter.trim().toLowerCase()
    return f ? orders.filter(o => (o.item || '').toLowerCase().includes(f) || String(o.id).includes(f)) : orders
  }, [orders, filter])

  async function load() {
    setLoading(true); setError('')
    try {
      console.log('Loading orders...');
      const data = await listOrders()
      console.log('Orders loaded:', data);
      setOrders(data)
    } catch (e) {
      console.error('Load error:', e);
      setError(e.message || 'Load failed')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  async function onCreate(e) {
    e.preventDefault()
    if (!item.trim()) return
    try {
      await createOrder({ item, price: Number(price || 0) })
      setItem(''); setPrice('')
      await load()
    } catch (e) {
      setError(e.message || 'Create failed')
    }
  }

  async function onConfirm(id) {
    try {
      await updateOrder(id, { status: 'CONFIRMED' })
      await load()
    } catch (e) { setError(e.message || 'Update failed') }
  }

  async function onDelete(id) {
    try {
      await deleteOrder(id)
      await load()
    } catch (e) { setError(e.message || 'Delete failed') }
  }

  return (
    <div className="app">
      <header className="header">
        <h1>📦 Orders Management System</h1>
        <p>Spring Boot + React Orders UI | Dynamic FT Project</p>
      </header>

      {error && (
        <div className="error-banner">
          <strong>Error:</strong> {error}
          <button onClick={() => setError('')}>✕</button>
        </div>
      )}

      <main className="main-content">

        <div className="actions-bar">
          <form onSubmit={onCreate} className="order-form-inline">
            <input
              placeholder="Item"
              value={item}
              onChange={(e) => setItem(e.target.value)}
              className="form-input"
            />
            <input
              placeholder="Price"
              type="number"
              step="0.01"
              value={price}
              onChange={(e) => setPrice(e.target.value)}
              className="form-input price-input"
            />
            <button type="submit" className="btn btn-primary">
              ➕ Add Order
            </button>
          </form>
          
          <div className="filter-section">
            <input
              placeholder="Filter by item or id…"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
              className="form-input filter-input"
            />
            <button onClick={load} disabled={loading} className="btn btn-secondary">
              {loading ? '⏳ Loading…' : '🔄 Refresh'}
            </button>
          </div>
        </div>

        <div className="orders-container">
          <h3>📋 Orders List</h3>
          {loading && <div className="loading">Loading orders...</div>}
          
          {!loading && filtered.length === 0 ? (
            <div className="empty-state">
              <p>No orders found. Create your first order!</p>
            </div>
          ) : (
            <div className="orders-grid">
              {filtered.map(order => (
                <div key={order.id} className="order-card">
                  <div className="order-header">
                    <h4>Order #{order.id}</h4>
                    <span className={`status-badge status-${order.status?.toLowerCase() || 'pending'}`}>
                      {order.status || 'PENDING'}
                    </span>
                  </div>
                  <div className="order-details">
                    <p><strong>Item:</strong> {order.item}</p>
                    <p><strong>Price:</strong> ${fmt(order.price ?? 0)}</p>
                  </div>
                  <div className="order-actions">
                    <button 
                      className="btn btn-edit"
                      onClick={() => onConfirm(order.id)}
                    >
                      ✅ Confirm
                    </button>
                    <button 
                      className="btn btn-delete"
                      onClick={() => onDelete(order.id)}
                    >
                      🗑️ Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>

      <footer className="footer">
        <p>🚀 Dynamic FT Microservices Project | Spring Boot API: localhost:7011 | React UI: localhost:4011</p>
      </footer>
    </div>
  )
}
