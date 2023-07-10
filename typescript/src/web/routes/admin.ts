import { ServerResponse } from "http";
import AppFactory from "../AppFactory";
import { emptyResponse, ParsedRequest, textResponse } from "../router";

function handle(request: ParsedRequest, response: ServerResponse): void {
  if (request.method === 'DELETE') {
    AppFactory.resetRepositories()
    emptyResponse(200, response)
    return
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function shouldHandle(r: ParsedRequest) {
  return r.url === '/admin'
    && ['DELETE'].includes(r.method)
}

export default {
  shouldHandle: shouldHandle,
  handle: handle
}
