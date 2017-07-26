export class User {
  public id: string;
  public name: string;
  public email: string
  public token: Token;

  constructor(id: string, name: string, email: string, token: Token) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.token = token;
  }
}

export class Token {
  public value: string;
  constructor(value: string) {
    this.value = value;
  }
}
