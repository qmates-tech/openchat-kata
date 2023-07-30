import axios, { AxiosInstance, AxiosResponse } from 'axios';
import 'jest-extended';
import * as AcceptanceTestsUtil from './util';

describe('user timeline API route', () => {

  const httpClient: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8000/',
    timeout: 5000,
  })

  beforeAll(async () => {
    await AcceptanceTestsUtil.resetApplication(httpClient)
  })

  afterEach(() => {
    AcceptanceTestsUtil.resetApplication(httpClient)
  })

  test('empty user timeline', async () => {
    const aliceUUID = await AcceptanceTestsUtil.registerUser("alice90", "any", "any", httpClient)

    const response: AxiosResponse = await httpClient.get(`/users/${aliceUUID}/timeline`)

    expect(response.status).toBe(200)
    expect(response.headers['content-type']).toBe('application/json')
    expect(response.data).toStrictEqual([])
  })

  test('user publish a post', async () => {
    const aliceUUID = await AcceptanceTestsUtil.registerUser("alice90", "any", "any", httpClient)

    let response = await httpClient.post(`/users/${aliceUUID}/timeline`, {
      "text": "The first post of alice."
    })

    expect(response.status).toBe(201)
    expect(response.headers['content-type']).toBe('application/json')
    expect(response.data).toMatchObject({
      postId: expect.toSatisfy(AcceptanceTestsUtil.isValidUUID4),
      userId: aliceUUID,
      text: "The first post of alice.",
      dateTime: expect.toSatisfy(AcceptanceTestsUtil.hasExpectedIsoDatetimeFormat)
    })
  })

  test('submit some posts and get timeline posts in descending order', async () => {
    const aliceUUID = await AcceptanceTestsUtil.registerUser("alice90", "any", "any", httpClient)
    const bobUUID = await AcceptanceTestsUtil.registerUser("bob89", "any", "any", httpClient)

    // ========================================= submit some posts

    const alicePostsIds = [
      await AcceptanceTestsUtil.submitPost(aliceUUID, "Alice user, first post.", httpClient),
      await AcceptanceTestsUtil.submitPost(aliceUUID, "Alice user, second post.", httpClient),
      await AcceptanceTestsUtil.submitPost(aliceUUID, "Alice user, third post.", httpClient),
    ]
    const bobPostsIds = [
      await AcceptanceTestsUtil.submitPost(bobUUID, "Bob user, first post.", httpClient),
      await AcceptanceTestsUtil.submitPost(bobUUID, "Bob user, second post.", httpClient)
    ]

    // ========================================= check alice's timeline

    const aliceTimeline: AxiosResponse = await httpClient.get(`/users/${aliceUUID}/timeline`)

    expect(aliceTimeline.status).toBe(200)
    expect(aliceTimeline.headers['content-type']).toBe('application/json')
    // TODO complete

    // ========================================= check bob's timeline

    const bobTimeline: AxiosResponse = await httpClient.get(`/users/${aliceUUID}/timeline`)

    expect(bobTimeline.status).toBe(200)
    // TODO complete
  })

  test('unexisting user timeline', async () => {
    let response: AxiosResponse
    try {
      response = await httpClient.get('/users/unexisting/timeline')
    } catch (error: any) {
      response = error.response as AxiosResponse
    }
    expect(response.status).toBe(404)
    expect(response.headers['content-type']).toBe('text/plain')
    expect(response.data).toBe('User not found.')
  })


})
