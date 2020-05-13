export enum UserType {
  PROVIDER,
  ADMINSIGMA,
  ADMINENTITE,
  PURCHASER,
  RESPONSABLE
}

export class User {
  id: string;
  firstName: string;
  lastName: string;
  mail: string;
  password: string;

  constructor(firstName: string, lastName: string, mail: string) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.mail = mail;
  }

}
