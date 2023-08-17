import 'jest-extended';
import { any, mock } from 'jest-mock-extended';
import * as uuid from 'uuid';
import Post from '../../../../src/domain/entities/Post';
import PostRepository from '../../../../src/domain/repositories/PostRepository';
import SubmitPostUseCase from "../../../../src/domain/usecases/SubmitPostUseCase";

describe('SubmitPostUseCase', () => {

  const postRepository = mock<PostRepository>();
  const usecase = new SubmitPostUseCase(postRepository)

  afterEach(() => {
    jest.clearAllMocks()
  })

  test('returns the stored post', () => {
    const registeredUserId = uuid.v4()
    
    const stored: Post = usecase.run(registeredUserId, "Post text.")

    expect(stored.id).toSatisfy((v: string) => uuid.version(v) === 4)
    expect(stored.userId).toBe(registeredUserId)
    expect(stored.text).toBe("Post text.")
    expect(stored.dateTime).toBeBefore(new Date())
  })

  test('storeThePostInRepository', () => {
    const registeredUserId = uuid.v4()

    usecase.run(registeredUserId, "Post text.")

    expect(postRepository.store).toHaveBeenCalledExactlyOnceWith(expect.objectContaining({
      id: expect.toSatisfy((v: string) => uuid.version(v) === 4),
      userId: registeredUserId,
      text: "Post text.",
      dateTime: any()
    }))
  })

})