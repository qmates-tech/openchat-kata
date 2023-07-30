import http, { IncomingMessage, ServerResponse } from 'http';
import WebRequest from './WebRequest';
import handle from './router';

console.log('Starting the server at localhost:8000 ...')

http.createServer((request: IncomingMessage, response: ServerResponse) => {
  console.log(`Received ${request.method} on ${request.url}`)
  let receivedData = ''
  request.on('data', chunk => receivedData += chunk)
  request.on('end', () => {
    const webRequest: WebRequest = WebRequest.from(request, receivedData)
    return handle(webRequest, response);
  })
}).listen(8000)
