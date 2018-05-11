package fund.cyber.markets.cassandra.model

import com.datastax.driver.mapping.annotations.ClusteringColumn
import com.datastax.driver.mapping.annotations.Frozen
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import com.datastax.driver.mapping.annotations.UDT
import fund.cyber.markets.common.model.Order
import fund.cyber.markets.common.model.OrderBook
import fund.cyber.markets.common.model.TokensPair
import java.math.BigDecimal
import java.util.*

@Table(keyspace = "markets", name = "orderbook",
    readConsistency = "QUORUM", writeConsistency = "QUORUM",
    caseSensitiveKeyspace = false, caseSensitiveTable = false)
data class CqlOrderBook(
    @PartitionKey(0)
    val exchange: String,

    @PartitionKey(1)
    val pair: CqlTokensPair,

    @PartitionKey(2)
    val epochHour: Long,

    @ClusteringColumn(0)
    val timestamp: Date,

    @Frozen
    val bids: List<CqlBaseOrder>,

    @Frozen
    val asks: List<CqlBaseOrder>
) {
    constructor(exchange: String, pair: TokensPair, orderBook: OrderBook): this(
        exchange = exchange,
        pair = CqlTokensPair(pair),
        epochHour = orderBook.timestamp / 1000 / 60 / 60,
        timestamp = Date(orderBook.timestamp),
        bids = orderBook.bids.map { order -> CqlBaseOrder(order) },
        asks = orderBook.asks.map { order -> CqlBaseOrder(order) }
    )
}

@UDT(name = "orderbook_order")
data class CqlBaseOrder(
    val type: String,
    val timestamp: Date,
    val amount: BigDecimal,
    val price: BigDecimal
) {
    constructor(order: Order): this(
        type = order.type.toString(),
        timestamp = Date(order.timestamp),
        amount = order.amount,
        price = order.price
    )
}