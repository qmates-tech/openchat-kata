import axios from 'axios'

describe('users API route', () => {

  test('retrieve empty list with no registered users', async () => {
    const response = await axios.get('http://localhost:8000/users')
    expect(response.status).toBe(200)
    expect(response.headers['content-type']).toBe("application/json")
    expect(response.data).toStrictEqual([])
  })

})