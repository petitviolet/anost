export enum HttpMethod {
  GET = 'GET',
  POST = 'POST',
}

const httpHeader = (token: string): Headers => {
  if (token === '') {
    return new Headers({
      'Content-Type': 'application/json',
    });
  } else {
    return new Headers({
      'Content-Type': 'application/json',
      'Authroization': `Bearer ${token}`,
    });
  }
};

const ENDPOINT = `http://${process.env.REACT_APP_API_HOST || 'localhost'}:${process.env.REACT_APP_API_PORT || 80}`;

export function apiRequest(method: HttpMethod, path: string, token: string = '', body: object = {}): Promise<any> {
  const url = `${ENDPOINT}${path}`;
  console.log(`url: ${url}`);
  return fetch(url, { method: method, headers: httpHeader(token), body: (body === {}) ? JSON.stringify(body) : null })
  .then(response => {
    if (response.status === 200) {
      // chain promise
      return response.json();
    } else {
      return new Error(`illegal status code: ${response.status}`);
    }
  })
  .catch(error => {
    // console.dir(error);
    return error;
  });
}
