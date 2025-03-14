import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        loadKoinSwiftModules(
            swiftExampleService: SwiftExampleService(),
            s3Service: SwiftS3Service()
        )
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView()
		}
	}
}
