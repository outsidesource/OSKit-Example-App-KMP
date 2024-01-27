package com.outsidesource.oskitExample.common.service

import com.outsidesource.oskitkmp.outcome.Outcome

interface IS3Service {
    suspend fun getS3Files(): Outcome<List<String>, Any>
}