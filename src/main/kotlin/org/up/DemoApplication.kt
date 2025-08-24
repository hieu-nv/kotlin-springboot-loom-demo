package org.up

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.support.TaskExecutorAdapter
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.Executors


@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


@Configuration
class WebClientConfiguration(
    @Value("\${remote.url}")
    val remoteUrl:String) {


    @Bean
    fun restTemplate() = RestTemplateBuilder().rootUri(remoteUrl).build()

    @Bean
    fun webClient() = WebClient.builder().baseUrl(remoteUrl).build()


    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    fun asyncTaskExecutor(): AsyncTaskExecutor {
        return TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor())
    }

    @Bean
    fun protocolHandlerVirtualThreadExecutorCustomizer(): TomcatProtocolHandlerCustomizer<*> {
        return TomcatProtocolHandlerCustomizer<org.apache.coyote.ProtocolHandler> { protocolHandler: org.apache.coyote.ProtocolHandler ->
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor())
        }
    }
}
