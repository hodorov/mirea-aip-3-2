package ru.hodorov.mireaAipThreeTaskTwo.controller

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import ru.hodorov.mireaAipThreeTaskTwo.entity.*
import ru.hodorov.mireaAipThreeTaskTwo.service.*
import javax.annotation.PostConstruct


@Suppress("SpringJavaAutowiredMembersInspection")
class MainController : BaseController() {
    private val log = KotlinLogging.logger { }

    @Autowired
    private lateinit var viewService: ViewService

    @Autowired
    private lateinit var bookRepository: BookRepository

    @FXML
    private lateinit var bookTable: EditableBookTableView

    @FXML
    private lateinit var deleteBookButton: Button

    @FXML
    private lateinit var addBookButton: Button

    @FXML
    private lateinit var saveBookButton: Button

    private val bookChanged = object : SimpleBooleanProperty(false) {
        override fun get(): Boolean {
            return !super.get()
        }
    }

    @PostConstruct
    fun postConstruct() {
        bookTable.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            deleteBookButton.isDisable = newValue == null;
        }
        deleteBookButton.setOnMouseClicked {
            val selected = bookTable.selectionModel.selectedItem
            bookTable.items.remove(selected)
            bookRepository.delete(selected)
        }
        saveBookButton.disableProperty().bind(bookChanged)
        bookTable.items = FXCollections.observableArrayList(bookRepository.findAll())
        bookChanged.set(false)
        addBookButton.setOnMouseClicked {
            val book = Book()
            bookChanged.set(true)
            book.addListener { bookChanged.set(true) }
            bookTable.items.add(book) }
        saveBookButton.setOnMouseClicked {
            bookRepository.saveAll(bookTable.items)
            Thread {
                Thread.sleep(1000)
                bookChanged.set(false)
            }.start()
        }
        this.stage.show()
    }

}