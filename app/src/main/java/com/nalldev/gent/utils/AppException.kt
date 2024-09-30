package com.nalldev.gent.utils

sealed class AppException(message: String) : Exception(message)

class NoInternetException(message: String) : AppException(message)
class ApiException(message: String) : AppException(message)
class DatabaseException(message: String) : AppException(message)
class UnknownException(message: String) : AppException(message)