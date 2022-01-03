package com.choco_tyranno.todoapp.di.qualifier

import javax.inject.Qualifier

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CertificationCodeInputBox(val tag : String)
