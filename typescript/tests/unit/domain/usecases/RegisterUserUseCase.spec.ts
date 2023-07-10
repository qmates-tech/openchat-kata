import { mock } from 'jest-mock-extended';
import * as uuid from 'uuid';
import UserRepository from '../../../../src/domain/repositories/UserRepository';
import RegisterUserUseCase from "../../../../src/domain/usecases/RegisterUserUseCase";

import * as jestExtendedMatchers from 'jest-extended';
expect.extend(jestExtendedMatchers)


describe("RegisterUserUseCase", () => {

  test("store the user in the repository", () => {
    const userRepository = mock<UserRepository>();
    const usecase = new RegisterUserUseCase(userRepository)

    usecase.run("alice90", "pass123", "About alice.")

    expect(userRepository.store).toHaveBeenCalledTimes(1)
    expect(userRepository.store).toHaveBeenCalledWith(expect.objectContaining({
      id: expect.toSatisfy((v: string) => uuid.version(v) === 4),
      username: "alice90",
      password: "pass123",
      about: "About alice."
    }))
  })

})