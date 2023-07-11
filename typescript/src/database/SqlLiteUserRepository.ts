import Database from 'better-sqlite3';
import { RegisteredUser, UserToRegister } from "../domain/entities/User";
import UserRepository from "../domain/repositories/UserRepository";

export default class SqlLiteUserRepository implements UserRepository {

    private readonly db

    constructor(filename: string) {
        this.db = new Database(filename)
    }

    store(user: UserToRegister): void {
        const result = this.db.prepare(
            'INSERT INTO users (id, username, password, about) VALUES (?, ?, ?, ?)'
        ).run(user.id, user.username, user.password, user.about)

        if (result.changes != 1)
            throw Error("Error during user store operation!")
    }

    isUsernameAlreadyUsed(usernameToFind: string): boolean {
        const result: any = this.db.prepare(
            'SELECT count(*) as count FROM users WHERE username = ?'
        ).get(usernameToFind)
        return result.count > 0
    }

    getAll(): RegisteredUser[] {
        const result: any[] = this.db.prepare('SELECT * FROM users').all()

        return result.map((u: any) => ({
            id: u.id,
            username: u.username,
            about: u.about,
        }))

    }

}