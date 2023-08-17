import { ServerResponse } from "http";
import AppFactory from "../../AppFactory";
import { RegisteredUser } from "../../domain/entities/User";
import { UsernameAlreadyInUseError } from "../../domain/usecases/RegisterUserUseCase";
import WebRequest from "../WebRequest";
import { Route, jsonResponseWith, textResponse } from "../router";

export default {

  handle: (request: WebRequest, response: ServerResponse): void => {
    switch (request.method) {
      case 'GET': return getRequest(response)
      case 'POST': return postRequest(request, response)
    }

    textResponse(405, `Method ${request.method} not allowed!`, response)
    return
  },

  shouldHandle: (r: WebRequest): boolean => {
    return r.url === '/users'
      && ['GET', 'POST'].includes(r.method)
  }

} as Route

function getRequest(response: ServerResponse): void {
  const usecase = AppFactory.buildGetAllUsersUseCase()
  const users: RegisteredUser[] = usecase.run()

  jsonResponseWith(200,
    users.map((u: RegisteredUser) => {
      return {
        "id": u.id,
        "username": u.username,
        "about": u.about
      }
    }),
    response)
}

function postRequest(request: WebRequest, response: ServerResponse): void {
  try {
    const username: string = request.requestBody.username
    const password: string = request.requestBody.password
    const userAbout: string = request.requestBody.about

    const usecase = AppFactory.buildRegisterUserUseCase()
    const storedUserUUID: string = usecase.run(username, password, userAbout)

    jsonResponseWith(201, {
      "id": storedUserUUID,
      "username": username,
      "about": userAbout
    }, response)
  } catch (err: any) {
    if (err instanceof UsernameAlreadyInUseError)
      return textResponse(400, 'Username already in use.', response)

    throw err
  }
}
