import axios, { AxiosInstance, AxiosResponse } from 'axios';
import 'jest-extended';
import * as uuid from 'uuid';

describe('users API route', () => {

  const httpClient: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8000/',
    timeout: 5000,
  })

  beforeAll(async () => {
    await httpClient.delete('/admin')
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
    let response = await httpClient.post('/users', {
      "username": "alice90",
      "password": "pass1234",
      "about": "About alice user."
    })
    expect(response.status).toBe(201)
    expect(response.headers['content-type']).toBe("application/json")
    const responseBody = response.data
    const aliceUUID: string = responseBody.id
    expect(responseBody.username).toBe("alice90")
    expect(responseBody.about).toBe("About alice user.")
    expect(uuid.version(responseBody.id)).toBe(4)

    // ========================================= register some users

    const johnUUID = await registerUser("john91", "pass4321", "About john user.", httpClient)
    const martinUUID = await registerUser("martin85", "pass$$", "About martin user.", httpClient)

    // ========================================= retrieve registered users

    const retrieveUsersResponse = await httpClient.get('/users')
    expect(retrieveUsersResponse.status).toBe(200)
    expect(retrieveUsersResponse.data).toHaveLength(3)
    expect(retrieveUsersResponse.data).toSatisfyAny((user) =>
      user.id === aliceUUID &&
      user.username === "alice90" &&
      user.about === "About alice user."
    )
    expect(retrieveUsersResponse.data).toSatisfyAny((user) =>
      user.id === johnUUID &&
      user.username === "john91" &&
      user.about === "About john user."
    )
    expect(retrieveUsersResponse.data).toSatisfyAny((user) =>
      user.id === martinUUID &&
      user.username === "martin85" &&
      user.about === "About martin user."
    )
  })

  test('username already in use', async () => {
    registerUser("bob89", "123pass", "About bob user.", httpClient)

    let response: AxiosResponse
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

export async function registerUser(
  username: string,
  password: string,
  about: string,
  httpClient: AxiosInstance
) : Promise<string> {
  const response = await httpClient.post('/users', {
    "username": username,
    "password": password,
    "about": about
  })
  expect(response.status).toBe(201)
   return response.data.id as string
}
