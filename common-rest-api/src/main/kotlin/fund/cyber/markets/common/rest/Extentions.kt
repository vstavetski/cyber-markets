package fund.cyber.markets.common.rest

import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun <T> Mono<T>.asServerResponse() = this.flatMap { obj -> ServerResponse.ok().body(BodyInserters.fromObject(obj)) }
    .switchIfEmpty(ServerResponse.notFound().build())

fun <T> Flux<T>.asServerResponse() = this.collectList().flatMap { obj ->
    when {
        obj.isNotEmpty() -> ServerResponse.ok().body(BodyInserters.fromObject(obj))
        else -> Mono.empty<ServerResponse>()
    }
}.switchIfEmpty(ServerResponse.notFound().build())