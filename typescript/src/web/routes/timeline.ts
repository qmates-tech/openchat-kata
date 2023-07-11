import { ServerResponse } from "http";
import { ParsedRequest, Route, jsonResponseWith, textResponse } from "../router";

const routeRegExp = /^\/users\/(.+)\/timeline$/

export default {

  handle: (request: ParsedRequest, response: ServerResponse): void => {
    const matches = request.url.match(routeRegExp)!
    const userIdParameter = matches[1]

    switch (request.method) {
      case 'GET': return getRequest(userIdParameter, response)
    }

    textResponse(405, `Method ${request.method} not allowed!`, response)
    return
  },

  shouldHandle: (r: ParsedRequest): boolean => {
    return (r.url.match(routeRegExp) ?? false)
      && ['GET'].includes(r.method)
  }

} as Route

function getRequest(userId: string, response: ServerResponse): void {
  if(userId === "unexisting")
    return textResponse(404, 'User not found.', response)

  jsonResponseWith(200, [], response)
}
