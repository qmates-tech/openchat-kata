import 'jest-extended';
import { mock } from 'jest-mock-extended';
import * as uuid from 'uuid';
import UserRepository from '../../../../src/domain/repositories/UserRepository';
import RegisterUserUseCase, { UsernameAlreadyInUseError } from "../../../../src/domain/usecases/RegisterUserUseCase";

describe("RegisterUserUseCase", () => {

  const userRepository = mock<UserRepository>();
  const usecase = new RegisterUserUseCase(userRepository)

  afterEach(() => {
    jest.clearAllMocks()
  })

  test("store the user in the repository", () => {
    userRepository.isUsernameAlreadyUsed.mockReturnValue(false)

    usecase.run("alice90", "pass123", "About alice.")

    expect(userRepository.store).toHaveBeenCalledTimes(1)
    expect(userRepository.store).toHaveBeenCalledWith(expect.objectContaining({
      id: expect.toSatisfy((v: string) => uuid.version(v) === 4),
      username: "alice90",
      password: "pass123",
      about: "About alice."
    }))
  })

  test("throws an error if username already used", () => {
    userRepository.isUsernameAlreadyUsed.mockReturnValue(true)

    expect(() => {
      usecase.run("alice90", "any", "any");
    }).toThrowWithMessage(UsernameAlreadyInUseError, "Username alice90 already in use!")

    expect(userRepository.store).not.toHaveBeenCalled()
  })

})