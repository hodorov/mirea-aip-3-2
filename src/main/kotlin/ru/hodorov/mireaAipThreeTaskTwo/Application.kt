package ru.hodorov.mireaAipThreeTaskTwo

import javafx.scene.Scene
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Lazy
import ru.hodorov.mireaAipThreeTaskTwo.service.ViewService

@Lazy
@SpringBootApplication
class Application : javafx.application.Application() {

	private lateinit var context: ConfigurableApplicationContext

	override fun init() {
		context = SpringApplicationBuilder(javaClass).headless(false).run(*savedArgs)
		context.autowireCapableBeanFactory.autowireBean(this)
	}

	override fun stop() {
		super.stop()
		context.close()
		//TODO: rewrite
		//Need to close AWT threads(SystemTray in NotificationService)
		System.exit(0)
	}

	@Autowired
	private lateinit var viewService: ViewService

	override fun start(stage: Stage) {
		stage.title = "АиП 3.2. Книги"
		stage.scene = Scene(viewService.getMainView(stage))
		stage.isResizable = true
		stage.centerOnScreen()
	}

	companion object {
		lateinit var savedArgs: Array<String>

		@JvmStatic
		fun main(args: Array<String>) {
			Application.savedArgs = args
			javafx.application.Application.launch(Application::class.java, *args)
		}
	}
}