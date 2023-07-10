import { ServerResponse } from 'http';
import usersRoute from './routes/users';
import adminRoute from './routes/admin';

export type ParsedRequest = {
  method: string,
  url: string,
  requestBody: any
}

export function handleReceivedRequest(request: ParsedRequest, response: ServerResponse) {
  if (usersRoute.shouldHandle(request))
    return usersRoute.handle(request, response)

  // TODO add this only on test env
  if (adminRoute.shouldHandle(request))
    return adminRoute.handle(request, response)

  console.log('Route not found!')
  textResponse(404, "Route not found!", response)
}

export function textResponse(statusCode: number, text: string, response: ServerResponse) {
  response.writeHead(statusCode, { 'Content-Type': 'text/plain' })
  response.end(text)
}

export function jsonResponseWith(body: object, statusCode: number, response: ServerResponse) {
  response.writeHead(statusCode, { 'Content-Type': 'application/json' })
  response.end(JSON.stringify(body))
}

export function emptyResponse(statusCode: number, response: ServerResponse) {
  response.writeHead(statusCode)
  response.end()
}
