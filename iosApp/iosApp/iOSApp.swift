import SwiftUI
import composeUI

@main
struct iOSApp: App {
    init() {
        loadKoinSwiftModules(swiftExampleService: SwiftExampleService())
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView()
		}
	}
}
