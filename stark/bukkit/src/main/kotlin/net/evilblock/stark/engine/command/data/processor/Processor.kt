package net.evilblock.stark.engine.command.data.processor

interface Processor<T, R> {

    fun process(type: T): R

}