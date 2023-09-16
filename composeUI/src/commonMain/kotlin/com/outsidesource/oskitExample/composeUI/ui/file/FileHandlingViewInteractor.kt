package com.outsidesource.oskitExample.composeUI.ui.file

import com.outsidesource.oskitkmp.file.IKMPFileHandler
import com.outsidesource.oskitkmp.file.KMPFileMetadata
import com.outsidesource.oskitkmp.file.KMPFileRef
import com.outsidesource.oskitkmp.file.sink
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrElse
import kotlinx.coroutines.launch
import okio.buffer
import okio.use

data class FileHandlingViewState(
    val selectedFile: KMPFileRef? = null,
    val renameFile: String = "",
    val renameFileResult: Outcome<KMPFileRef, Exception>? = null,
    val selectedFolder: KMPFileRef? = null,
    val renameFolder: String = "",
    val renameFolderResult: Outcome<KMPFileRef, Exception>? = null,
    val metadata: KMPFileMetadata? = null,
    val deleteFileResult: Outcome<Unit, Exception>? = null,
    val deleteFolderResult: Outcome<Unit, Exception>? = null,
    val fileList: List<KMPFileRef> = emptyList(),
    val fileExistsResult: Boolean? = null,
    val folderExistsResult: Boolean? = null,
    val createFileName: String = "",
    val createFileResult: Outcome<KMPFileRef, Exception>? = null,
    val createFileContent: String = "",
    val createFolderName: String = "",
    val createFolderResult: Outcome<KMPFileRef, Exception>? = null,
)

class FileHandlingViewInteractor(
    private val fileHandler: IKMPFileHandler,
): Interactor<FileHandlingViewState>(
    initialState = FileHandlingViewState()
) {

    fun pickFile() {
        interactorScope.launch {
            val outcome = fileHandler.pickFile()
            if (outcome is Outcome.Ok) update { state -> state.copy(selectedFile = outcome.value) }
        }
    }

    fun pickFolder() {
        interactorScope.launch {
            val outcome = fileHandler.pickFolder()
            if (outcome is Outcome.Ok) update { state -> state.copy(selectedFolder = outcome.value) }
        }
    }

    fun createFileNameChanged(value: String) {
        update { state -> state.copy(createFileName = value) }
    }

    fun createFileContentChanged(value: String) {
        update { state -> state.copy(createFileContent = value) }
    }

    fun createFile() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val file = fileHandler.resolveFile(folder, state.createFileName, create = true).unwrapOrElse {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            val sink = file.sink().unwrapOrElse {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            sink.buffer().use {
                it.writeUtf8(state.createFileContent)
            }

            update { state -> state.copy(createFileResult = Outcome.Ok(file)) }
        }
    }

    fun createFolderNameChanged(value: String) {
        update { state -> state.copy(createFolderName = value) }
    }

    fun createFolder() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val createdFolder = fileHandler.resolveDirectory(folder, state.createFolderName, create = true).unwrapOrElse {
                update { state -> state.copy(createFolderResult = this) }
                return@launch
            }

            update { state -> state.copy(createFolderResult = Outcome.Ok(createdFolder)) }
        }
    }

    // TODO: Create with segments

    fun readMetaData() {
        interactorScope.launch {
            val outcome = fileHandler.readMetadata(state.selectedFile ?: return@launch)
            if (outcome is Outcome.Ok) update { state -> state.copy(metadata = outcome.value) }
        }
    }

    fun renameFileNameChanged(value: String) {
        update { state -> state.copy(renameFile = value) }
    }

    fun renameFile() {
        interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val outcome = fileHandler.rename(file, state.renameFile)
            update { state -> state.copy(renameFileResult = outcome) }
        }
    }

    fun renameFolderNameChanged(value: String) {
        update { state -> state.copy(renameFolder = value) }
    }

    fun renameFolder() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val outcome = fileHandler.rename(folder, state.renameFolder)
            update { state -> state.copy(renameFolderResult = outcome) }
        }
    }

    fun deleteFile() {
        interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val outcome = fileHandler.delete(file)
            update { state -> state.copy(deleteFileResult = outcome) }
        }
    }

    fun deleteFolder() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val outcome = fileHandler.delete(folder)
            update { state -> state.copy(deleteFolderResult = outcome) }
        }
    }

    fun list() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val files = fileHandler.list(folder, isRecursive = true).unwrapOrElse { return@launch }
            update { state -> state.copy(fileList = files) }
        }
    }

    fun exists() {
        interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val folder = state.selectedFolder ?: return@launch
            val fileExists = fileHandler.exists(file)
            val folderExists = fileHandler.exists(folder)

            update { state ->
                state.copy(fileExistsResult = fileExists, folderExistsResult = folderExists)
            }
        }
    }
}