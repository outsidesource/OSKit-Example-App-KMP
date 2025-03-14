import ComposeApp
import Foundation
import AWSCore
import AWSS3

class SwiftS3Service : IS3Service {
    
    class AnonymousCredentialsProvider : NSObject, AWSCredentialsProvider {
        func credentials() -> AWSTask<AWSCredentials> {
            AWSTask(result: AWSCredentials(accessKey: "", secretKey: "", sessionKey: nil, expiration: nil))
        }
        
        func invalidateCachedTemporaryCredentials() {}
    }
    
    init() {
        AWSServiceManager.default().defaultServiceConfiguration = AWSServiceConfiguration(
            region: AWSRegionType.USEast2,
            serviceType: AWSServiceType.S3,
            credentialsProvider: AnonymousCredentialsProvider(),
            localTestingEnabled: false
        )
    }
    
    func __listS3Files() async throws -> Outcome<NSArray, AnyObject> {
//        do {
            let config = try await S3Client.S3ClientConfiguration(region: "us-east-2")
            let client = S3Client(config: config)
            let request = ListObjectsV2Input(bucket: "kmp-poc-public")
            let result = try await client.listObjectsV2(input: request)
            
            guard let objList = result.contents else {
                return SwiftOutcomeError(error: NSString("No objects found")).unwrap()
            }
            
            let items = objList.compactMap { $0.key }
           
            return SwiftOutcomeOk(value: NSArray(array: items)).unwrap()
//        } catch {
//            print(error.localizedDescription)
//            return SwiftOutcomeError(error: NSString("Error")).unwrap()
//        }
    }
    
}
