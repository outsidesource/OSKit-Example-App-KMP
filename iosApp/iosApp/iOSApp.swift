import SwiftUI
import composeUI
import Network

@main
struct iOSApp: App {
    init() {
        MainKt.doInitKoin(testInterface: TestImpl())
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView()
		}
	}
}

class TestImpl : ITestInterface {
    func test1() -> String {
        return "Hello KMP!"
    }
    
    func test2() -> Outcome<NSArray, KotlinException> {
        return SwiftOutcomeOk(value: [Foo(one: "", two: 2)]).unwrap()
    }
    
    func test3() -> Outcome<NSString, KotlinException> {
        return SwiftOutcomeOk(value: "Hello").unwrap()
    }
    
    func test4() -> SkieSwiftFlow<String> {
        class _Flow : SwiftFlow<NSString> {
            override func __create() async throws {
                try await __emit(value: "one")
                try await Task.sleep(nanoseconds: 2_000_000_000)
                try await __emit(value: "two")
                try await Task.sleep(nanoseconds: 2_000_000_000)
                try await __emit(value: "three")
            }
        }

        return SkieSwiftFlow(_Flow().unwrap())
    }
    
    func __test5(flow: SkieSwiftFlow<String>) async throws {
        for await message in flow {
            print(message)
        }
        print("done")
    }
}
