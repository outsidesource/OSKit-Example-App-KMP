package com.outsidesource.oskitExample.common.service.s3

import com.outsidesource.oskitkmp.outcome.Outcome

interface IS3Service {
    suspend fun listS3Files(): Outcome<List<String>, Any>
}