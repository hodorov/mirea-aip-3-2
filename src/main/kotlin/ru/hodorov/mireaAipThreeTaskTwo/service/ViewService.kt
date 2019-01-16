package ru.hodorov.mireaAipThreeTaskTwo.service

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import ru.hodorov.mireaAipThreeTaskTwo.controller.BaseController
import ru.hodorov.mireaAipThreeTaskTwo.controller.MainController
import kotlin.random.Random


@Service
class ViewService {
    private val log = KotlinLogging.logger { }

    @Autowired
    private lateinit var context: ApplicationContext

    fun getMainView(stage: Stage): Parent {
        return loadView<MainController>("fxml/main.fxml", stage).first
    }

    internal fun <T: BaseController>loadView(url: String, stage: Stage): Pair<Parent, T>  {
        javaClass.classLoader.getResourceAsStream(url).use {
            val loader = FXMLLoader()
            try {
                loader.load<Parent?>(it)
            } catch (e: Exception) {
                log.error(e) { "Error while load"}
            }
            val controller = loader.getController<T>()
            controller.stage = stage
            val beanName = "${controller.javaClass}-${Random.nextInt(1, 9999)}"
            log.debug { "Registry controller as bean with name \"$beanName\"" }
            context.autowireCapableBeanFactory.autowireBean(controller)
            context.autowireCapableBeanFactory.initializeBean(controller, beanName)
            return Pair(loader.getRoot<Parent>(), controller)
        }
    }
}