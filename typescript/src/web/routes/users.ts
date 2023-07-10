import { ServerResponse } from "http";
import RegisterUserUseCase, { UsernameAlreadyInUseError } from "../../domain/usecases/RegisterUserUseCase";
import AppFactory from "../AppFactory";
import { jsonResponseWith, ParsedRequest, textResponse } from "../router";
import User from "../../domain/entities/User";

function handle(request: ParsedRequest, response: ServerResponse): void {
  switch (request.method) {
    case 'GET': return getRequest(response)
    case 'POST': return postRequest(request, response)
  }

  textResponse(405, `Method ${request.method} not allowed!`, response)
  return
}

function getRequest(response: ServerResponse): void {
  const users: User[] = AppFactory.getUserRepository().getAll()
  const serializedUsers = users.map((u) => {
    return {
      id: u.id,
      username: u.username,
      about: u.about
    }
  })
  jsonResponseWith(serializedUsers, 200, response)
}

function postRequest(request: ParsedRequest, response: ServerResponse): void {
  try {
    const username: string = request.requestBody.username
    const password: string = request.requestBody.password
    const userAbout: string = request.requestBody.about

    const usecase = new RegisterUserUseCase(AppFactory.getUserRepository())
    const storedUserUUID: string = usecase.run(username, password, userAbout)

    jsonResponseWith({
      "id": storedUserUUID,
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
