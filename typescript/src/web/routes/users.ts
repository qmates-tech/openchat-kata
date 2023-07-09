import { ServerResponse } from "http";
import { jsonResponseWith, ParsedRequest, textResponse } from "../router";
import * as uuid from 'uuid';

function handle(request: ParsedRequest, response: ServerResponse): void {
  switch (request.method) {
    case 'GET':
      jsonResponseWith([], 200, response)
      return
    case 'POST':
      const username: String = request.requestBody.username
      const userAbout: String = request.requestBody.about

      jsonResponseWith({
        "id": uuid.v4(),
        "username": username,
        "about": userAbout
      }, 201, response)
      return
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function shouldHandle(r: ParsedRequest) {
  return r.url === '/users'
    && ['GET', 'POST'].includes(r.method)
}

export default {
  shouldHandle: shouldHandle,
  handle: handle
}
