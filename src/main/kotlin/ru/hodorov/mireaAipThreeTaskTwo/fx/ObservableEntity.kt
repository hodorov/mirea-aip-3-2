package ru.hodorov.mireaAipThreeTaskTwo.fx

import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.beans.property.ObjectProperty
import mu.KotlinLogging
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

abstract class ObservableEntity : Observable {
    private val log = KotlinLogging.logger { }


    val observableByName = HashMap<String, ObjectProperty<*>>()

    public fun registerAllObservable() {
        this.javaClass.declaredFields.forEach {
            if (ObjectProperty::class.java.isAssignableFrom(it.type)) {
                it.isAccessible = true
                val objProp = it.get(this) as ObjectProperty<*>
                observableByName[it.name] = objProp
                val valueCls: Class<*> = (it.annotatedType.type as ParameterizedTypeImpl).actualTypeArguments[0] as Class<*>
                /*try {
                    val valueOfMethod = valueCls.getMethod("valueOf", String::class.java)
                    objProp.addListener { observable, old, now ->
                        run {
                            log.info { "$observable, $old, $now" }
                            if (now::class.java == String::class.java) {
                                log.info { "Try cast" }
                                objProp.value = valueOfMethod.invoke(null, now)
                            }
                        }
                    }
                } catch (e: Exception) {*/
                    objProp.addListener { observable, old, now -> log.info { "$observable, $old, $now" } }
                //}
            }
        }
    }

    override fun addListener(listener: InvalidationListener?) {
        observableByName.values.forEach { it.addListener(listener) }
    }

    override fun removeListener(listener: InvalidationListener?) {
        observableByName.values.forEach { it.removeListener(listener) }
    }
}