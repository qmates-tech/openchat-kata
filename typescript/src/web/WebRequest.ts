import { IncomingMessage } from "http"

export default class WebRequest {
  readonly method: string
  readonly url: string
  readonly requestBody: any

  static from(request: IncomingMessage, receivedData: string): WebRequest {
    const requestBody = this.parseRequestBody(receivedData)
    return new WebRequest(request.method!, request.url!, requestBody)
  }

  private constructor(method: string, url: string, requestBody: any) {
    this.method = method
    this.url = url
    this.requestBody = requestBody
  }

  private static parseRequestBody(receivedData: string): any {
    if (!receivedData || receivedData == '')
      return undefined

    try {
      return JSON.parse(receivedData)
    } catch (error) {
      console.log('Invalid not-empty request body', receivedData)
      return undefined
    }
  }

}