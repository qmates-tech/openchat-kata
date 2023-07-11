import axios, { AxiosResponse } from 'axios';
import 'jest-extended';

describe('user timeline API route', () => {

  const httpClient = axios.create({
    baseURL: 'http://localhost:8000/',
    timeout: 5000,
  })

  beforeAll(async () => {
    await httpClient.delete('/admin')
  })

  afterEach(() => {
    httpClient.delete('/admin')
  })

  test('unexisting user timeline', async () => {
    let response: AxiosResponse
    try {
      response = await httpClient.get('/users/unexisting/timeline')
    } catch (error: any) {
      response = error.response as AxiosResponse
    }
    expect(response.status).toBe(404)
    expect(response.headers['content-type']).toBe("text/plain")
    expect(response.data).toBe("User not found.")
  })

})
