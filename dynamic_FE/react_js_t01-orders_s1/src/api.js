const API = import.meta.env.VITE_API_URL || 'http://localhost:7011';
console.log('API URL:', API);

export async function listOrders() {
  console.log('Fetching from:', `${API}/api/v1/orders`);
  const r = await fetch(`${API}/api/v1/orders`);
  if (!r.ok) throw new Error('Failed to load');
  return r.json();
}

export async function createOrder(payload) {
  const r = await fetch(`${API}/api/v1/orders`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  if (!r.ok) throw new Error('Create failed');
  return r.json();
}

export async function updateOrder(id, payload) {
  const r = await fetch(`${API}/api/v1/orders/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
  if (!r.ok) throw new Error('Update failed');
  return r.json();
}

export async function deleteOrder(id) {
  const r = await fetch(`${API}/api/v1/orders/${id}`, { method: 'DELETE' });
  if (!r.ok && r.status !== 204) throw new Error('Delete failed');
}
