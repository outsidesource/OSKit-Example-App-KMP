package service

import service.s3.IS3Service
import com.outsidesource.oskitkmp.outcome.Outcome
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.coroutines.suspendCoroutine

/**
 * This S3 Service is implemented with cocoapods. In order to use cocoapods in Kotlin follow these steps:
 * 1. Add `kotlin("native.cocoapods")` to the build.gradle plugins section
 * 2. Add cocoapod dependencies via the cocoapods dsl in build.gradle.
 * 3. Add the cocoapod dependencies to the ios project as well
 */
@OptIn(ExperimentalForeignApi::class)
class IOSS3Service : IS3Service {

//    private val s3: AWSS3
//    private val credentials = AWSAnonymousCredentialsProvider()
//    private val credentials = object : NSObject(), AWSCredentialsProviderProtocol {
//        override fun credentials(): AWSTask =
//            AWSTask.taskWithResult(AWSCredentials(id, secret, null, null))
//        override fun invalidateCachedTemporaryCredentials() {}
//    }

    init {
//        AWSServiceManager.defaultServiceManager()?.setDefaultServiceConfiguration(
//            AWSServiceConfiguration(
//                AWSRegionType.AWSRegionUSEast2,
//                AWSServiceType.AWSServiceS3,
//                credentials,
//                false,
//            )
//        )
//
//        s3 = AWSS3.defaultS3()
    }

    override suspend fun listS3Files(): Outcome<List<String>, Any> = suspendCoroutine {
        Outcome.Error(Unit)
//        s3.listObjects(AWSS3ListObjectsRequest(mapOf("bucket" to "kmp-poc-public"), null)) { result, error ->
//            when {
//                result != null -> {
//                    val files = result.contents?.mapNotNull {
//                        if (it !is AWSS3Object) return@mapNotNull null
//                        it.key
//                    } ?: emptyList()
//
//                    it.resume(Outcome.Ok(files))
//                }
//                error != null -> it.resume(Outcome.Error(error))
//                else -> it.resume(Outcome.Error(Unit))
//            }
//        }
    }
}