import { ServerResponse } from "http";
import { jsonResponseWith, ParsedRequest } from "../router";

function handle(request: ParsedRequest, response: ServerResponse): void {
  jsonResponseWith([], 200, response)
}

function shouldHandle(r: ParsedRequest) {
  return r.url === '/users' && r.method == 'GET'
}

export default {
  shouldHandle: shouldHandle,
  handle: handle
}
