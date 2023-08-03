import Post from "../entities/Post"

export default interface PostRepository {
  store(post: Post): void
  getAllByUser(userId: string): Post[]
  reset(): void
}