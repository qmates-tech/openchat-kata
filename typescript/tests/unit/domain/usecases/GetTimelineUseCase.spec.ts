import 'jest-extended';
import { mock } from 'jest-mock-extended';
import * as uuid from 'uuid';
import Post from '../../../../src/domain/entities/Post';
import { RegisteredUser } from '../../../../src/domain/entities/User';
import PostRepository from '../../../../src/domain/repositories/PostRepository';
import UserRepository from '../../../../src/domain/repositories/UserRepository';
import GetTimelineUseCase from "../../../../src/domain/usecases/GetTimelineUseCase";
import UserNotFoundError from '../../../../src/domain/usecases/errors/UserNotFoundError';

describe('GetTimelineUseCase', () => {

  const registeredUser: RegisteredUser = {
    id: uuid.v4(),
    username: "alice90",
    about: "About alice user."
  }
  const postRepository = mock<PostRepository>();
  const userRepository = mock<UserRepository>();
  const usecase = new GetTimelineUseCase(postRepository, userRepository)

  beforeEach(() => {
    userRepository.getUserById.calledWith(registeredUser.id).mockReturnValue(registeredUser)
    postRepository.getAllByUser.mockReturnValue([])
  })

  afterEach(() => {
    jest.clearAllMocks()
  })

  test('returns empty posts collection for existing user', () => {
    const posts = usecase.run(registeredUser.id)
    expect(posts).toStrictEqual([])
  })

  test('returns posts from repository in descending order by datetime', () => {
    const now = new Date()
    postRepository.getAllByUser.mockReset().calledWith(registeredUser.id).mockReturnValue([
      postWith("Second post", addMinutes(now, 2)),
      postWith("First post", now),
      postWith("Last post", addMinutes(now, 60)),
      postWith("Third post", addMinutes(now, 5)),
    ])

    const posts = usecase.run(registeredUser.id)

    expect(posts).toHaveLength(4)
    expect(posts[0].text).toBe("Last post")
    expect(posts[0].dateTime).toStrictEqual(addMinutes(now, 60))
    expect(posts[1].text).toBe("Third post")
    expect(posts[2].text).toBe("Second post")
    expect(posts[3].text).toBe("First post")
    expect(posts[3].dateTime).toStrictEqual(now)
  })

  test('throws an error for unexisting user', () => {
    expect(() => {
      usecase.run("bfb62439-8711-447a-b0f8-bdbef5ceb4d7")
    }).toThrowWithMessage(UserNotFoundError, 'User with uuid [bfb62439-8711-447a-b0f8-bdbef5ceb4d7] not found!')
  })

})

function postWith(text: string, dateTime: Date): Post {
  return {
    id: uuid.v4(),
    userId: uuid.v4(),
    text: text,
    dateTime: dateTime
  }
}

function addMinutes(date: Date, minutes: number): Date {
  return new Date(date.getTime() + (minutes * 60 * 1000))
}
