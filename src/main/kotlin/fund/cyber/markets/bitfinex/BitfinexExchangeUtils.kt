package fund.cyber.markets.bitfinex

import fund.cyber.markets.model.CurrencyPair
import org.knowm.xchange.bitfinex.v1.BitfinexExchange
import org.knowm.xchange.bitfinex.v1.service.BitfinexMarketDataService
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.*


val WS_ADDRESS = "wss://api.bitfinex.com/ws/2"

/**
 * Warning! Sends messages on session.
 */
fun WebSocketSession.subscribeChannel(channelSymbol: String) {
    sendMessage(TextMessage("{\"event\":\"subscribe\",\"channel\":\"trades\",\"symbol\":\"$channelSymbol\"}"))
}

/**
 * Warning! Invokes http request to obtain data.
 */
fun BitfinexExchange.getChannelSymbolForCurrencyPair(): Map<String, CurrencyPair> {

    remoteInit()
    val exchangeSymbols = (marketDataService as BitfinexMarketDataService).getExchangeSymbols()

    val channelSymbolForCurrencyPair: MutableMap<String, CurrencyPair> = HashMap()
    exchangeSymbols.forEach { pair ->
        val bitfinexSymbol = "t" + (pair.base.symbol + pair.counter.symbol).toUpperCase()
        channelSymbolForCurrencyPair.put(bitfinexSymbol, CurrencyPair(pair.base.symbol, pair.counter.symbol))
    }
    return channelSymbolForCurrencyPair
}

