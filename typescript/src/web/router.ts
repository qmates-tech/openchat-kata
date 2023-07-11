import { ServerResponse } from 'http';
import adminRoute from './routes/admin';
import timelineRoute from './routes/timeline';
import usersRoute from './routes/users';

export type ParsedRequest = {
  method: string,
  url: string,
  requestBody: any
}

export type Route = {
  handle: (req: ParsedRequest, resp: ServerResponse) => void,
  shouldHandle: (req: ParsedRequest) => boolean
}

export function handleReceivedRequest(request: ParsedRequest, response: ServerResponse) {
  const routes: Route[] = [
    usersRoute,
    timelineRoute,
    adminRoute  // TODO add this only on test env
  ]

  const handler = routes.find((r: Route) => r.shouldHandle(request))
  if(!handler) {
    console.log('Route not found!')
    textResponse(404, 'Route not found!', response)
    return
  }

  handler.handle(request, response)
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
