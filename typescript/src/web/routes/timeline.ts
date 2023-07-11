import { ServerResponse } from "http";
import { ParsedRequest, Route, textResponse } from "../router";

export default {

  handle: (request: ParsedRequest, response: ServerResponse): void => {
    switch (request.method) {
      case 'GET': return getRequest(response)
    }

    textResponse(405, `Method ${request.method} not allowed!`, response)
    return
  },

  shouldHandle: (r: ParsedRequest): boolean => {
    return new RegExp('^\/users\/.+\/timeline$').test(r.url)
      && ['GET'].includes(r.method)
  }

} as Route

function getRequest(response: ServerResponse): void {
  textResponse(404, 'User not found.', response)
}
