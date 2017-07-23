export class User {
  public id: string;
  public name: string;
  public token: string;

  constructor(id: string, name: string, token: string) {
    this.id = id;
    this.name = name;
    this.token = token;
  }
}

