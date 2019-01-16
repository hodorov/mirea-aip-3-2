package ru.hodorov.mireaAipThreeTaskTwo.entity

import javafx.beans.property.SimpleObjectProperty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.hodorov.mireaAipThreeTaskTwo.fx.EntityTableView
import ru.hodorov.mireaAipThreeTaskTwo.fx.ObservableEntity
import ru.hodorov.mireaAipThreeTaskTwo.fx.TableViewCol
import javax.persistence.*

@Entity
@Access(AccessType.PROPERTY)

class Book : ObservableEntity() {

    // @TableViewCol("ID")
    val idProperty = SimpleObjectProperty<Int?>()
        @Transient get
    @TableViewCol("Название")
    val nameProperty = SimpleObjectProperty<String?>()
        @Transient get
    @TableViewCol("Автор")
    val authorProperty = SimpleObjectProperty<String?>()
        @Transient get
    @TableViewCol("Количество страниц", 170.0)
    val pagesProperty = SimpleObjectProperty<String?>()
        @Transient get
    @TableViewCol("Тип переплёта", 130.0)
    val bindingTypeProperty = SimpleObjectProperty<Binding>(Binding.UNKNOWN)
        @Transient get
    @TableViewCol("Цена")
    val priceProperty = SimpleObjectProperty<String?>()
        @Transient get

    init {
        registerAllObservable()
    }

    var id
        @Id
        @GeneratedValue
        get() = idProperty.get()
        set(value) = idProperty.set(value)
    var name
        get() = nameProperty.get()
        set(value) = nameProperty.set(value)
    var author
        get() = authorProperty.get()
        set(value) = authorProperty.set(value)
    var pages
        get() = pagesProperty.get()
        set(value) = pagesProperty.set(value)
    var bindingType
        get() = bindingTypeProperty.get()
        set(value) = bindingTypeProperty.set(value)
    var price
        get() = priceProperty.get()
        set(value) = priceProperty.set(value)
}

@Repository
interface BookRepository : JpaRepository<Book, Int>


class BookTableView : EntityTableView<Book>(Book::class.java, false)
class EditableBookTableView : EntityTableView<Book>(Book::class.java, true)