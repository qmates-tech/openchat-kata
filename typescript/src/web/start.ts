import http, { IncomingMessage, ServerResponse } from 'http';
import { emptyResponse, handleReceivedRequest, ParsedRequest } from './router';

console.log("Starting the server at localhost:8000 ...");

http.createServer((request: IncomingMessage, response: ServerResponse) => {
  try {
    const { url, method } = request
    console.log(`Received ${method} on ${url}`)

    let receivedData = '';
    request.on('data', chunk => receivedData += chunk);
    request.on('end', () => {
      const requestBody = parseRequestBody(receivedData)
      const parsedRequest: ParsedRequest = {
        method: method!,
        url: url!,
        requestBody: requestBody
      }
      handleReceivedRequest(parsedRequest, response)
    });
  } catch (error) {
    console.log('Error during request handling!', error)
    emptyResponse(500, response)
  }
}).listen(8000);

function parseRequestBody(receivedData: string): any {
  if(!receivedData || receivedData == '') {
    console.log('Empty request body')
    return undefined
  }

  try {
    const requestBody: any = JSON.parse(receivedData)
    console.log(`Received request body: ${JSON.stringify(requestBody)}`)
    return requestBody
  } catch(error) {
    console.log('Invalid not-empty request body', receivedData)
    return undefined
  }
}
