package org.neopool.project.core.state

import androidx.compose.runtime.Stable

@Stable
sealed interface StateType<out LoadingExt, out DataExt, out ErrorExt> {
    companion object {
        fun <LoadingExt> loading(loadingExt: LoadingExt): LoadingType<LoadingExt> {
            return LoadingType(loadingExt)
        }

        fun loading() = loading(Unit)
        fun <LoadingExt> LoadingExt.asLoadingState(): LoadingType<LoadingExt> = loading(this)

        fun <DataExt> data(dataExt: DataExt): DataType<DataExt> {
            return DataType(dataExt)
        }

        fun data() = data(Unit)
        fun <DataExt> DataExt.asDataState(): DataType<DataExt> = data(this)

        fun <ErrorExt> error(errorExt: ErrorExt): ErrorType<ErrorExt> {
            return ErrorType(errorExt)
        }

        fun error() = error(Unit)
        fun <ErrorExt> ErrorExt.asErrorState(): ErrorType<ErrorExt> = error(this)
    }

    fun isLoading(): Boolean = this is LoadingType
    fun isData(): Boolean = this is DataType
    fun isError(): Boolean = this is ErrorType
}

@Stable
data class LoadingType<LoadingExt>(
    val loadingExt: LoadingExt,
) : StateType<LoadingExt, Nothing, Nothing> {
    override fun toString(): String {
        return if (loadingExt == null || loadingExt is Unit) "LoadingType" else "LoadingType(loadingExt=$loadingExt)"
    }
}

@Stable
data class DataType<DataExt>(
    val dataExt: DataExt,
) : StateType<Nothing, DataExt, Nothing> {
    override fun toString(): String {
        return if (dataExt == null || dataExt is Unit) "DataType" else "DataType(dataExt=$dataExt)"
    }
}

@Stable
data class ErrorType<ErrorExt>(
    val errorExt: ErrorExt,
) : StateType<Nothing, Nothing, ErrorExt> {
    override fun toString(): String {
        return if (errorExt == null || errorExt is Unit) "ErrorType" else "ErrorType(errorExt=$errorExt)"
    }
}

typealias DefaultStateType = StateType<Unit, Unit, Unit>

typealias StateTypeWithData<DataExt> = StateType<Unit, DataExt, Unit>

typealias StateTypeWithError<ErrorExt> = StateType<Unit, Unit, ErrorExt>

typealias StateTypeWithDataError<DataExt, ErrorExt> = StateType<Unit, DataExt, ErrorExt>

typealias StateTypeWithLoading<LoadingExt> = StateType<LoadingExt, Unit, Unit>

inline fun <LoadingExt, DataExt, ErrorExt, T> StateType<LoadingExt, DataExt, ErrorExt>.map(
    loading: (LoadingExt) -> T? = { null },
    data: (DataExt) -> T? = { null },
    error: (ErrorExt) -> T? = { null },
): T? {
    return when (this) {
        is LoadingType -> loading(loadingExt)
        is DataType -> data(dataExt)
        is ErrorType -> error(errorExt)
    }
}

inline fun <LoadingExt, DataExt, ErrorExt, T> StateType<LoadingExt, DataExt, ErrorExt>.mapData(
    data: (DataExt) -> T,
): StateType<LoadingExt, T, ErrorExt> {
    return when (this) {
        is LoadingType -> LoadingType(loadingExt)
        is DataType -> DataType(data(dataExt))
        is ErrorType -> ErrorType(errorExt)
    }
}