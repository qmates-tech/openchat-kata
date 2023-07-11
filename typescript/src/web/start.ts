import http, { IncomingMessage, ServerResponse } from 'http';
import { ParsedRequest, emptyResponse, handleReceivedRequest } from './router';

console.log("Starting the server at localhost:8000 ...");
http.createServer((request: IncomingMessage, response: ServerResponse) => {
  let receivedData = '';
  request.on('data', chunk => receivedData += chunk);
  request.on('end', () => parseAndProcessRequest(request, receivedData, response));
}).listen(8000)

function parseAndProcessRequest(request: IncomingMessage, receivedData: string, response: ServerResponse) {
  const { url, method } = request
  console.log(`Received ${method} on ${url}`)

  try {
    const requestBody = parseRequestBody(receivedData)
    const parsedRequest: ParsedRequest = {
      method: method!,
      url: url!,
      requestBody: requestBody
    }
    handleReceivedRequest(parsedRequest, response)
  } catch (error) {
    console.log('Error during request handling!', error)
    emptyResponse(500, response)
  }
}

function parseRequestBody(receivedData: string): any {
  if (!receivedData || receivedData == '') {
    console.log('Empty request body')
    return undefined
  }

  try {
    const requestBody: any = JSON.parse(receivedData)
    console.log(`Received request body: ${JSON.stringify(requestBody)}`)
    return requestBody
  } catch (error) {
    console.log('Invalid not-empty request body', receivedData)
    return undefined
  }
}
