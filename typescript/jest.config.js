module.exports = {
    preset: 'ts-jest',
    setupFilesAfterEnv: ["jest-extended/all"],
    testEnvironment: 'node',
    testPathIgnorePatterns: [".d.ts", ".js"],
    transform: { '^.+\\.ts?$': 'ts-jest' },
    transformIgnorePatterns: ['<rootDir>/node_modules/'],
};