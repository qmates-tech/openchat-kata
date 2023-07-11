import { ServerResponse } from "http";
import { ParsedRequest, Route, jsonResponseWith, textResponse } from "../router";
import Post, { newPost } from "../../domain/entities/Post";

const routeRegExp = /^\/users\/(.+)\/timeline$/

export default {

  handle: (request: ParsedRequest, response: ServerResponse): void => {
    const matches = request.url.match(routeRegExp)!
    const userIdParameter = matches[1]

    switch (request.method) {
      case 'GET': return getRequest(userIdParameter, response)
      case 'POST': return postRequest(userIdParameter, request, response)
    }

    textResponse(405, `Method ${request.method} not allowed!`, response)
    return
  },

  shouldHandle: (r: ParsedRequest): boolean => {
    return (r.url.match(routeRegExp) ?? false)
      && ['GET', 'POST'].includes(r.method)
  }

} as Route

function getRequest(userId: string, response: ServerResponse): void {
  if (userId === "unexisting")
    return textResponse(404, 'User not found.', response)

  jsonResponseWith(200, [], response)
}

function postRequest(userId: string, request: ParsedRequest, response: ServerResponse): void {
  const postText = request.requestBody.text
  const post: Post = newPost(postText, userId)
  
  jsonResponseWith(201, {
    postId: post.id,
    userId: post.userId,
    text: post.text,
    dateTime: serializeDatetime(post.dateTime)
  }, response)
}

function serializeDatetime(datetime: Date): string {
  // 2018-01-10T11:30:00Z (iso format but without milliseconds)
  return datetime.toISOString().split('.')[0] + 'Z'
}
