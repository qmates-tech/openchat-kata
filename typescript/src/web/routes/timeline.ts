import { ServerResponse } from "http";
import AppFactory from "../../AppFactory";
import Post from "../../domain/entities/Post";
import { InappropriateLanguageError } from "../../domain/usecases/SubmitPostUseCase";
import UserNotFoundError from "../../domain/usecases/errors/UserNotFoundError";
import WebRequest from "../WebRequest";
import { Route, jsonResponseWith, textResponse } from "../router";

const ROUTE_REGEXP = /^\/users\/(.+)\/timeline$/

export default {

  handle: (request: WebRequest, response: ServerResponse): void => {
    const matches = request.url.match(ROUTE_REGEXP)!
    const userIdParameter = matches[1]

    switch (request.method) {
      case 'GET': return getRequest(userIdParameter, response)
      case 'POST': return postRequest(userIdParameter, request, response)
    }

    textResponse(405, `Method ${request.method} not allowed!`, response)
    return
  },

  shouldHandle: (r: WebRequest): boolean => {
    return (r.url.match(ROUTE_REGEXP) ?? false)
      && ['GET', 'POST'].includes(r.method)
  }

} as Route

function getRequest(userId: string, response: ServerResponse): void {
  try {
    const usecase = AppFactory.buildGetTimelineUseCase()
    const posts: Post[] = usecase.run(userId)

    jsonResponseWith(200,
      posts.map((p: Post) => {
        return {
          "postId": p.id,
          "userId": p.userId,
          "text": p.text,
          "dateTime": serializeDatetime(p.dateTime)
        }
      }),
      response)
  } catch (err: any) {
    if (err instanceof UserNotFoundError)
      return textResponse(404, 'User not found.', response)

    throw err
  }
}

function postRequest(userId: string, request: WebRequest, response: ServerResponse): void {
  try {
    const postText: string = request.requestBody.text

    const usecase = AppFactory.buildSubmitPostUseCase()
    const submittedPost: Post = usecase.run(userId, postText)

    jsonResponseWith(201, {
      postId: submittedPost.id,
      userId: submittedPost.userId,
      text: submittedPost.text,
      dateTime: serializeDatetime(submittedPost.dateTime)
    }, response)
  } catch (err: any) {
    if (err instanceof InappropriateLanguageError)
      return textResponse(400, 'Post contains inappropriate language.', response)

    throw err
  }
}

function serializeDatetime(datetime: Date): string {
  // 2018-01-10T11:30:00Z (iso format but without milliseconds)
  return datetime.toISOString().split('.')[0] + 'Z'
}
