package com.yuranos.reactive.web

import com.yuranos.reactive.Event
import com.yuranos.reactive.EventRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI
import java.time.Duration

@Component
class EventHandler(private val eventRepository: EventRepository) {


    fun all(req: ServerRequest): Mono<ServerResponse> {
        return ok().body(this.eventRepository.findAll(), Event::class.java)
    }

    fun stream(req: ServerRequest): Mono<ServerResponse> {
        val eventStream = Flux
                .zip(Flux.interval(Duration.ofSeconds(1)), this.eventRepository.findAll())
                .map { it.t2 }
        return ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        eventStream,
                        Event::class.java
                )
    }

    fun create(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono(Event::class.java)
                .flatMap { event -> this.eventRepository.save(event) }
                .flatMap { (id) -> created(URI.create("/eventRepository/$id")).build() }
    }

//    fun get(req: ServerRequest): Mono<ServerResponse> {
//        return this.eventRepository.findById(req.pathVariable("id"))
//                .flatMap { event -> ok().body(Mono.just(event), Event::class.java) }
//                .switchIfEmpty(notFound().build())
//    }
//
//    fun update(req: ServerRequest): Mono<ServerResponse> {
//        return this.eventRepository.findById(req.pathVariable("id"))
//                .zipWith(req.bodyToMono(Event::class.java))
//                .map { it.t1.copy(title = it.t2.title, description = it.t2.description) }
//                .flatMap { this.eventRepository.save(it) }
//                .flatMap { noContent().build() }
//    }

    fun delete(req: ServerRequest): Mono<ServerResponse> {
        return this.eventRepository.deleteById(req.pathVariable("id"))
                .flatMap { noContent().build() }
    }
}

