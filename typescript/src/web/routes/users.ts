import { ServerResponse } from "http";
import * as uuid from 'uuid';
import RegisterUserUseCase, { UsernameAlreadyInUseError } from "../../domain/usecases/RegisterUserUseCase";
import { jsonResponseWith, ParsedRequest, textResponse } from "../router";

function handle(request: ParsedRequest, response: ServerResponse): void {
  switch (request.method) {
    case 'GET': return getRequest(response)
    case 'POST': return postRequest(request, response)
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function getRequest(response: ServerResponse): void {
  jsonResponseWith([], 200, response)
}

function postRequest(request: ParsedRequest, response: ServerResponse): void {
  try {
    const username: string = request.requestBody.username
    const password: string = request.requestBody.password
    const userAbout: string = request.requestBody.about

    const usecase = new RegisterUserUseCase()
    usecase.run(username, password, userAbout)

    jsonResponseWith({
      "id": uuid.v4(),
      "username": username,
      "about": userAbout
    }, 201, response)
  } catch (err: any) {
    if (err instanceof UsernameAlreadyInUseError)
      return textResponse(400, 'Username already in use.', response)

    throw err
  }
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
