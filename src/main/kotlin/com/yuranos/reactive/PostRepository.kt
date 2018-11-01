package com.yuranos.reactive

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PostRepository : ReactiveMongoRepository<Post, String>
