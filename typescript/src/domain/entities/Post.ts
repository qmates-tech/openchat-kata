import * as uuid from "uuid";

type Post = {
  id: string,
  userId: string,
  text: string,
  dateTime: Date
}

export function newPost(text: string, userId: string): Post {
  return {
    id: uuid.v4(),
    userId: userId,
    text: text,
    dateTime: new Date() 
  }
}

export default Post