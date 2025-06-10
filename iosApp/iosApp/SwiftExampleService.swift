import Foundation
import ComposeApp

typealias Outcome = Oskit_kmpOutcome

class SwiftExampleService : ISwiftExampleService {
    
    func createFlowInSwift() -> SkieSwiftFlow<String> {
        class _Flow : SwiftFlow<NSString> {
            override func __create() async throws {
                try await __emit(value: "One")
                try await Task.sleep(nanoseconds: 1_000_000_000)
                try await __emit(value: "Two")
                try await Task.sleep(nanoseconds: 1_000_000_000)
                try await __emit(value: "Three")
                try await Task.sleep(nanoseconds: 1_000_000_000)
                try await __emit(value: "Done!")
                try await Task.sleep(nanoseconds: 1_000_000_000)
            }
        }

        return SkieSwiftFlow(_Flow().unwrap())
    }
    
    func __collectFlowInSwift(flow: SkieSwiftFlow<String>) async throws {
        for await message in flow {
            print("Received: ", message)
        }
        print("Done Collecting")
    }
    
    func __suspendFunction() async throws -> Outcome<NSString, AnyObject> {
        try await Task.sleep(nanoseconds: 2_000_000_000)
        return SwiftOutcomeOk(value: "Done!").unwrap()
    }
    
}
