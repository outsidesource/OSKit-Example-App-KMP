package service.s3

import com.outsidesource.oskitkmp.outcome.Outcome

interface IS3Service {
    suspend fun listS3Files(): Outcome<List<String>, Any>
}

class NoOpS3Service : IS3Service {
    override suspend fun listS3Files(): Outcome<List<String>, Any> {
        TODO("Not yet implemented")
    }
}