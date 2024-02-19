package com.outsidesource.oskitExample.composeUI.ui.file

import com.outsidesource.oskitkmp.file.*
import com.outsidesource.oskitkmp.interactor.Interactor
import com.outsidesource.oskitkmp.outcome.Outcome
import com.outsidesource.oskitkmp.outcome.unwrapOrReturn
import kotlinx.coroutines.launch
import okio.buffer
import okio.use

data class FileHandlingViewState(
    val selectedFile: KMPFileRef? = null,
    val selectedFolder: KMPFileRef? = null,
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
    val saveFile: Outcome<KMPFileRef?, Exception>? = null,
    val storedFile: KMPFileRef? = null,
    val storedFolder: KMPFileRef? = null,
    val appendFileResult: Outcome<Unit, Exception>? = null,
    val appendFileContent: String = "",
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

    fun pickSaveFile() {
        interactorScope.launch {
            val saveFileOutcome = fileHandler.pickSaveFile("untitled.txt")
            update { state -> state.copy(saveFile = saveFileOutcome) }
        }
    }

    fun pickFolder() {
        interactorScope.launch {
            val outcome = fileHandler.pickDirectory()
            if (outcome is Outcome.Ok) update { state -> state.copy(selectedFolder = outcome.value) }
        }
    }

    fun storeFile() {
        // TODO: Actually store
        val string = state.selectedFile?.toPersistableString() ?: return
        println(string)
        println(KMPFileRef.fromPersistableString(string))
    }

    fun storeFolder() {
        // TODO: Actually store
        val string = state.selectedFolder?.toPersistableString() ?: return
        println(string)
        println(KMPFileRef.fromPersistableString(string))
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
            val file = fileHandler.resolveFile(folder, state.createFileName, create = true).unwrapOrReturn {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            val sink = file.sink().unwrapOrReturn {
                update { state -> state.copy(createFileResult = this) }
                return@launch
            }

            sink.buffer().use {
                it.writeUtf8(state.createFileContent)
            }

            update { state -> state.copy(createFileResult = Outcome.Ok(file)) }
        }
    }

    fun appendToFileContentChanged(value: String) {
        interactorScope.launch {
            update { state -> state.copy(appendFileContent = value) }
        }
    }

    fun appendToFile() {
       interactorScope.launch {
            val file = state.selectedFile ?: return@launch
            val sink = file.sink(mode = KMPFileWriteMode.Append).unwrapOrReturn {
                update { state -> state.copy(appendFileResult = this) }
                return@launch
            }

           sink.buffer().use {
               it.writeUtf8(state.appendFileContent)
           }

           update { state -> state.copy(appendFileResult = Outcome.Ok(Unit)) }
       }
    }

    fun createFolderNameChanged(value: String) {
        update { state -> state.copy(createFolderName = value) }
    }

    fun createFolder() {
        interactorScope.launch {
            val folder = state.selectedFolder ?: return@launch
            val createdFolder = fileHandler.resolveDirectory(folder, state.createFolderName, create = true).unwrapOrReturn {
                update { state -> state.copy(createFolderResult = this) }
                return@launch
            }

            update { state -> state.copy(createFolderResult = Outcome.Ok(createdFolder)) }
        }
    }

    fun readMetaData() {
        interactorScope.launch {
            val outcome = fileHandler.readMetadata(state.selectedFolder ?: return@launch)
            if (outcome is Outcome.Ok) update { state -> state.copy(metadata = outcome.value) }
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
            val files = fileHandler.list(folder, isRecursive = true).unwrapOrReturn { return@launch }
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