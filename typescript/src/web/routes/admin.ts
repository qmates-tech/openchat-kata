import { ServerResponse } from "http";
import AppFactory from "../AppFactory";
import WebRequest from "../WebRequest";
import { emptyResponse, textResponse } from "../router";

function handle(request: WebRequest, response: ServerResponse): void {
  if (request.method === 'DELETE') {
    AppFactory.resetRepositories()
    emptyResponse(200, response)
    return
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function shouldHandle(r: WebRequest) {
  return r.url === '/admin'
    && ['DELETE'].includes(r.method)
}

export default {
  shouldHandle: shouldHandle,
  handle: handle
}
