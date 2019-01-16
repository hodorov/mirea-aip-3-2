package ru.hodorov.mireaAipThreeTaskTwo.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.hodorov.mireaAipThreeTaskTwo.service.AppFolderService
import javax.sql.DataSource


@Configuration
class H2Configuration {

    @Autowired
    private lateinit var appFolderService: AppFolderService

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
                .url("jdbc:h2:${appFolderService.getAppFolder().canonicalPath}/db.h2;DB_CLOSE_ON_EXIT=FALSE;")
                .build()
    }
}