package fund.cyber.markets.poloniex

import fund.cyber.markets.model.CurrencyPair
import org.knowm.xchange.poloniex.PoloniexExchange
import org.knowm.xchange.poloniex.service.PoloniexMarketDataService
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

/**
 * Poloniex exchange utilities container.
 *
 * @author hleb.albau@gmail.com
 */


val WS_ADDRESS = "wss://api2.poloniex.com/"

val HEARTBEAT_CODE = 1010
val TICKER_CODE = 1002


/**
 * Warning! Sends messages on session.
 */
fun WebSocketSession.subscribeChannel(channelId: Int) {
    sendMessage(TextMessage("{\"command\":\"subscribe\",\"channel\":\"$channelId\"}"))
}

/**
 * Warning! Invokes http request to obtain data.
 */
fun PoloniexExchange.getCurrencyPairsWithChannelIds(): Map<Int, CurrencyPair> {

    remoteInit()
    val poloniexTickers = (marketDataService as PoloniexMarketDataService).getAllPoloniexTickers()

    val channelIdForCurrencyPair: MutableMap<Int, CurrencyPair> = HashMap()
    poloniexTickers?.forEach { currencyPairSymbol, metaInfo ->
        val pairId = metaInfo.additionalProperties["id"] as Int
        channelIdForCurrencyPair.put(pairId, toCurrencyPair(currencyPairSymbol))
    }
    return channelIdForCurrencyPair
}

private fun toCurrencyPair(currencyPairSymbol: String): CurrencyPair {
    val split = currencyPairSymbol.split("_")
    return CurrencyPair(split[0], split[1])
}