package ru.hodorov.mireaAipThreeTaskTwo.fx

import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.CheckBoxTableCell
import javafx.scene.control.cell.ChoiceBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.Callback
import org.springframework.data.repository.CrudRepository
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import java.lang.reflect.Field
import java.lang.reflect.Type


abstract class EntityTableView<T : ObservableEntity>(private val entityClass: Class<T>, private val editable: Boolean) : TableView<T>() {

    init {
        super.setEditable(true)
        entityClass.declaredFields.map {
            Pair<Field, TableViewCol?>(it, it.getAnnotation(TableViewCol::class.java))
        }.filter {
            it.second != null
        }.forEach { pair ->
            val field = pair.first.name
            val col = TableColumn<T, Any>(pair.second!!.name)
            if (pair.second!!.width > 0) {
                col.prefWidth = pair.second!!.width
            }
            val cls: Class<*> = (pair.first.annotatedType.type as ParameterizedTypeImpl).actualTypeArguments[0] as Class<*>
            col.setCellValueFactory {
                it.value.observableByName[field]!! as ObservableValue<Any>
            }
            if (this.editable) {
                if (cls.isEnum) {
                    col.cellFactory = ChoiceBoxTableCell.forTableColumn<Any, Any>(*cls.enumConstants) as Callback<TableColumn<T, Any>, TableCell<T, Any>>
                } else {
                    col.cellFactory = when (cls) {
                        String::class.java -> TextFieldTableCell.forTableColumn<T>() as Callback<TableColumn<T, Any>, TableCell<T, Any>>
                        Integer::class.java -> TextFieldTableCell.forTableColumn<T>() as Callback<TableColumn<T, Any>, TableCell<T, Any>>
                        Boolean::class.javaObjectType -> ChoiceBoxTableCell.forTableColumn(true, false)
//                    Boolean::class.javaObjectType -> CheckBoxTableCell.forTableColumn(col as TableColumn<T, Boolean>) as Callback<TableColumn<T, Any>, TableCell<T, Any>>
                        else -> throw IllegalStateException("Unknown col type")
                    }
                }
            }
            columns.add(col)
        }
    }
}

/*public fun loadAll() {
    items.addAll(repository.findAll())
}*/

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class TableViewCol(
        val name: String,
        val width: Double = -1.0
)
