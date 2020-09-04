package org.dailykit.network

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.ResponseField
import com.apollographql.apollo.cache.normalized.CacheKey
import com.apollographql.apollo.cache.normalized.CacheKeyResolver
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory

import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.dailykit.type.CustomType
import java.text.ParseException


private val GRAPHQL_ENDPOINT: String = "https://test.dailykit.org/datahub/v1/graphql"
private val GRAPHQL_WEBSOCKET_ENDPOINT: String = "wss://test.dailykit.org/datahub/v1/graphql"

private val SQL_CACHE_NAME = "dailykit_assembly"


class Network {
    companion object{
        @JvmStatic
        lateinit var apolloClient: ApolloClient
    }

    fun setApolloClient(context: Context){
        val log: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(log)
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.header("X-Hasura-Admin-Secret","60ea76ab-5ab6-4f09-ad44-efeb00f978ce")
                chain.proceed(builder.build())
            }
            .build()

        val stringTypeAdapter = object : CustomTypeAdapter<String> {
            override fun decode(value: CustomTypeValue<*>): String {
                try {
                    return value.value.toString()
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }

            }

            override fun encode(value: String): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLString(value)
            }
        }

        val numericTypeAdapter = object : CustomTypeAdapter<Int> {
            override fun decode(value: CustomTypeValue<*>): Int {
                try {
                    return Integer.parseInt(value.value.toString())
                } catch (e: ParseException) {
                    throw RuntimeException(e)
                }

            }

            override fun encode(value: Int): CustomTypeValue<*> {
                return CustomTypeValue.GraphQLNumber(value)
            }
        }


        val apolloSqlHelper = ApolloSqlHelper(context, SQL_CACHE_NAME)
        val normalizedCacheFactory = LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)
            .chain(SqlNormalizedCacheFactory(apolloSqlHelper))

        val cacheKeyResolver: CacheKeyResolver = object : CacheKeyResolver() {
            override fun fromFieldRecordSet(field: ResponseField, recordSet: Map<String, Any>): CacheKey {
                if (recordSet.containsKey("dailykit_assembly")) {
                    val id = recordSet["dailykit_assembly"] as String
                    return CacheKey.from(id)
                }
                return CacheKey.NO_KEY
            }

            override fun fromFieldArguments(field: ResponseField, variables: Operation.Variables): CacheKey {
                return CacheKey.NO_KEY
            }
        }

        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPHQL_ENDPOINT)
                .okHttpClient(okHttpClient)
                .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
                .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory(GRAPHQL_WEBSOCKET_ENDPOINT, okHttpClient))
                .addCustomTypeAdapter(CustomType.OID, stringTypeAdapter)
                .addCustomTypeAdapter(CustomType.JSONB, stringTypeAdapter)
                .addCustomTypeAdapter(CustomType.NUMERIC, numericTypeAdapter)
                .build()
    }
}