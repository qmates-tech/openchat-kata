import { ServerResponse } from 'http';
import adminRoute from './routes/admin';
import timelineRoute from './routes/timeline';
import usersRoute from './routes/users';

export type ParsedRequest = {
  method: string,
  url: string,
  requestBody: any
}

export function handleReceivedRequest(request: ParsedRequest, response: ServerResponse) {
  if (usersRoute.shouldHandle(request))
    return usersRoute.handle(request, response)
  if (timelineRoute.shouldHandle(request))
    return timelineRoute.handle(request, response)

  // TODO add this only on test env
  if (adminRoute.shouldHandle(request))
    return adminRoute.handle(request, response)

  console.log('Route not found!')
  textResponse(404, 'Route not found!', response)
}

export function textResponse(statusCode: number, text: string, response: ServerResponse) {
  response.writeHead(statusCode, { 'Content-Type': 'text/plain' })
  response.end(text)
}

export function jsonResponseWith(statusCode: number, body: object, response: ServerResponse) {
  response.writeHead(statusCode, { 'Content-Type': 'application/json' })
  response.end(JSON.stringify(body))
}

export function emptyResponse(statusCode: number, response: ServerResponse) {
  response.writeHead(statusCode)
  response.end()
}
