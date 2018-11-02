package com.yuranos.reactive

import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import javax.persistence.Entity

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}


@Component
class DataInitializr(val eventRepository: EventRepository) : CommandLineRunner {
    private val log = getLogger(DataInitializr::class.java)

    override fun run(vararg strings: String) {
        log.info("start data initialization ...")
        this.eventRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .just("Event one", "Event two")
                                .flatMap { this.eventRepository.save(Event(title = it, description = "description of " + it)) }
                )
                .log()
                .subscribe(
                        null,
                        null,
                        { log.info("done initialization...") }
                )
    }
}


interface EventRepository : CrudRepository<Event, Integer>

@Entity
data class Event(@Id var id: Integer? = null,
                 var title: String? = null,
                 var description: String? = null,
                 @CreatedDate var createdDate: LocalDateTime = LocalDateTime.now()
)
