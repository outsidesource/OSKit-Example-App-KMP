import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        loadKoinSwiftModules(
            swiftExampleService: SwiftExampleService()
        )
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView()
		}
	}
}
