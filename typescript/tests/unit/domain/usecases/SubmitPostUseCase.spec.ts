import 'jest-extended';
import { any, mock } from 'jest-mock-extended';
import * as uuid from 'uuid';
import Post from '../../../../src/domain/entities/Post';
import PostRepository from '../../../../src/domain/repositories/PostRepository';
import UserRepository from '../../../../src/domain/repositories/UserRepository';
import SubmitPostUseCase, { InappropriateLanguageError } from "../../../../src/domain/usecases/SubmitPostUseCase";
import { RegisteredUser } from '../../../../src/domain/entities/User';
import UserNotFoundError from '../../../../src/domain/usecases/errors/UserNotFoundError';

describe('SubmitPostUseCase', () => {

  const registeredUser: RegisteredUser = {
    id: uuid.v4(),
    username: "alice90",
    about: "About alice user."
  }
  const postRepository = mock<PostRepository>();
  const userRepository = mock<UserRepository>();
  const usecase = new SubmitPostUseCase(postRepository, userRepository)

  beforeEach(() => {
    userRepository.getUserById.calledWith(registeredUser.id).mockReturnValue(registeredUser)
  })

  afterEach(() => {
    jest.clearAllMocks()
  })

  test('returns the stored post', () => {
    const stored: Post = usecase.run(registeredUser.id, "Post text.")

    expect(stored.id).toSatisfy((v: string) => uuid.version(v) === 4)
    expect(stored.userId).toBe(registeredUser.id)
    expect(stored.text).toBe("Post text.")
    expect(stored.dateTime).toBeBefore(new Date())
  })

  test('store the post in repository', () => {
    usecase.run(registeredUser.id, "Post text.")

    expect(postRepository.store).toHaveBeenCalledExactlyOnceWith(expect.objectContaining({
      id: expect.toSatisfy((v: string) => uuid.version(v) === 4),
      userId: registeredUser.id,
      text: "Post text.",
      dateTime: any()
    }))
  })

  test('throws exception for unexisting user', () => {
    expect(() => {
      usecase.run("293307a4-86cd-49a8-ac5f-9347a3276e75", "any")
    }).toThrowWithMessage(UserNotFoundError, 'User with uuid [293307a4-86cd-49a8-ac5f-9347a3276e75] not found!')
  })

  it.each([
    "I like orange juice !",
    "Orange is my favorite fruit.",
    "I LOVE ORANGE !",
  ])('throws exception for inappropriated language in post text: %p', (postText: string) => {
    expect(() => {
      usecase.run(registeredUser.id, postText)
    }).toThrowWithMessage(InappropriateLanguageError,
      'Post text contains inappropriate language: ' + postText
    )
  })

})