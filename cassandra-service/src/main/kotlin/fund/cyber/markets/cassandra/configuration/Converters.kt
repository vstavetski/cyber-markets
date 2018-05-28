package fund.cyber.markets.cassandra.configuration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fund.cyber.markets.cassandra.model.CqlTickerPrice
import org.springframework.core.convert.converter.Converter
import java.math.BigDecimal

/**
 * Used to create multimap with volumes from byted json.
 */
class VolumesReadConverter(private val jsonDeserializer: ObjectMapper) : Converter<ByteArray, Map<String, Map<String, BigDecimal>>> {

    override fun convert(source: ByteArray): Map<String, Map<String, BigDecimal>> =
        jsonDeserializer.readValue(source, object: TypeReference<Map<String, Map<String, BigDecimal>>>() {})!!
}

/**
 * Used to convert multimap with volumes to byted json.
 */
class VolumesWriteConverter(private val jsonSerializer: ObjectMapper) : Converter<Map<String, Map<String, BigDecimal>>, ByteArray> {

    override fun convert(source: Map<String, Map<String, BigDecimal>>) = jsonSerializer.writeValueAsBytes(source)!!
}

/**
 * Used to create multimap with prices from byted json.
 */
class PricesReadConverter(private val jsonDeserializer: ObjectMapper) : Converter<ByteArray, Map<String, Map<String, CqlTickerPrice>>> {

    override fun convert(source: ByteArray): Map<String, Map<String, CqlTickerPrice>> =
        jsonDeserializer.readValue(source, object: TypeReference<Map<String, Map<String, CqlTickerPrice>>>() {})!!
}

/**
 * Used to convert multimap with prices to byted json.
 */
class PricesWriteConverter(private val jsonSerializer: ObjectMapper) : Converter<Map<String, Map<String, CqlTickerPrice>>, ByteArray> {

    override fun convert(source: Map<String, Map<String, CqlTickerPrice>>) = jsonSerializer.writeValueAsBytes(source)!!
}
