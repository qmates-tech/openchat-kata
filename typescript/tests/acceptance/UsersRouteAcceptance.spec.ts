import { describe, expect, test } from '@jest/globals';
import axios, { AxiosResponse } from 'axios';
import * as uuid from 'uuid';

describe('users API route', () => {

  const httpClient = axios.create({
    baseURL: 'http://localhost:8000/',
    timeout: 5000,
  })

  afterEach(() => {
    httpClient.delete('/admin')
  })

  test('retrieve empty list with no registered users', async () => {
    const response = await httpClient.get('/users')
    expect(response.status).toBe(200)
    expect(response.headers['content-type']).toBe("application/json")
    expect(response.data).toStrictEqual([])
  })

  test('register some users and retrieve them', async () => {
    const response = await httpClient.post('/users', {
      "username": "alice90",
      "password": "pass1234",
      "about": "About alice user."
    })
    expect(response.status).toBe(201)
    expect(response.headers['content-type']).toBe("application/json")
    const responseBody = response.data
    expect(responseBody.username).toBe("alice90")
    expect(responseBody.about).toBe("About alice user.")
    expect(uuid.version(responseBody.id)).toBe(4)
  })

  test('username already in use', async () => {
    const firstRegistrationResponse = await httpClient.post('/users', {
      "username": "bob89",
      "password": "123pass",
      "about": "About bob user."
    })
    expect(firstRegistrationResponse.status).toBe(201)

    let response: AxiosResponse;
    try {
      response = await httpClient.post('/users', {
        "username": "bob89",
        "password": "pass123",
        "about": "Another about."
      })
    } catch (error: any) {
      response = error.response as AxiosResponse
    }

    expect(response.status).toBe(400)
    expect(response.headers['content-type']).toBe("text/plain")
    expect(response.data).toBe("Username already in use.")
  })

})