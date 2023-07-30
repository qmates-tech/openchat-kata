import { ServerResponse } from "http";
import WebRequest from "./WebRequest";
import adminRoute from './routes/admin';
import timelineRoute from './routes/timeline';
import usersRoute from './routes/users';

export type Route = {
  handle: (req: WebRequest, resp: ServerResponse) => void,
  shouldHandle: (req: WebRequest) => boolean
}

export default function handle(webRequest: WebRequest, response: ServerResponse) {
  try {
    const routes: Route[] = [
      usersRoute,
      timelineRoute,
      adminRoute
    ]

    const handler = routes.find((r: Route) => r.shouldHandle(webRequest))
    if (!handler) {
      console.log('Route not found!')
      textResponse(404, 'Route not found!', response)
      return
    }

    handler.handle(webRequest, response)
  } catch (error) {
    console.log('Error during request handling!', error)
    emptyResponse(500, response)
  }
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