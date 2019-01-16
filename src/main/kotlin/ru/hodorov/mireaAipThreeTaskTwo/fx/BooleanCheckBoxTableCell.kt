package ru.hodorov.mireaAipThreeTaskTwo.fx

import javafx.beans.value.ObservableValue
import javafx.scene.control.cell.CheckBoxTableCell

class BooleanCheckBoxTableCell(val booleanObsValue: ObservableValue<Boolean>) : CheckBoxTableCell<Any, Any>() {
    init {
        this.properties.forEach { System.out.println("${it.key} ${it.value}") }
    }
}