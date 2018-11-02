package com.yuranos.reactive.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouteFunction {

    @Bean
    fun routes(eventHandler: EventHandler): RouterFunction<ServerResponse> {
        return route(GET("/eventRepository"), HandlerFunction<ServerResponse>(eventHandler::all))
                .andRoute(POST("/eventRepository"), HandlerFunction<ServerResponse>(eventHandler::create))
//                .andRoute(GET("/eventRepository/{id}"), HandlerFunction<ServerResponse>(eventHandler::get))
//                .andRoute(PUT("/eventRepository/{id}"), HandlerFunction<ServerResponse>(eventHandler::update))
                .andRoute(DELETE("/eventRepository/{id}"), HandlerFunction<ServerResponse>(eventHandler::delete))
    }
}
