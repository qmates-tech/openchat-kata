import { ServerResponse } from "http";
import { ParsedRequest, textResponse } from "../router";

function handle(request: ParsedRequest, response: ServerResponse): void {
  switch (request.method) {
    case 'GET': return getRequest(response)
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function getRequest(response: ServerResponse): void {
  textResponse(404, 'User not found.', response)
}

function shouldHandle(r: ParsedRequest) {
  return new RegExp('^\/users\/.+\/timeline$').test(r.url)
    && ['GET'].includes(r.method)
}

export default {
  shouldHandle: shouldHandle,
  handle: handle
}
