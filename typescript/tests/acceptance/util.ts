import { expect } from '@jest/globals';
import { AxiosInstance } from "axios";
import * as uuid from 'uuid';

export async function registerUser(
  username: string,
  password: string,
  about: string,
  httpClient: AxiosInstance
): Promise<string> {
  const response = await httpClient.post('/users', {
    "username": username,
    "password": password,
    "about": about
  })
  expect(response.status).toBe(201)
  return response.data.id as string
}

export function isValidUUID4(value: string): boolean {
  return uuid.version(value) === 4
}

export function hasExpectedIsoDatetimeFormat(value: string): boolean {
  const parsed = new Date(Date.parse(value))
  // 2018-01-10T11:30:00Z (iso format but without milliseconds)
  const isoStringValue = parsed.toISOString().split('.')[0] + 'Z'
  return value == isoStringValue
}