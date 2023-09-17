package com.outsidesource.oskitExample.composeUI.ui.file

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.unit.dp
import com.outsidesource.oskitExample.composeUI.ui.common.Screen
import com.outsidesource.oskitcompose.interactor.collectAsState
import com.outsidesource.oskitcompose.lib.rememberInjectForRoute

@Composable
fun FileHandlingScreen(
    interactor: FileHandlingViewInteractor = rememberInjectForRoute()
) {
    val state = interactor.collectAsState()

    Screen("File Handling") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column {
                Text("Selected File: ${state.selectedFile?.name ?: "None"}")
                Button(
                    onClick = interactor::pickFile
                ) {
                    Text("Pick File")
                }
            }

            Column {
                Text("Selected Folder: ${state.selectedFolder?.name ?: "None"}")
                Button(
                    onClick = interactor::pickFolder
                ) {
                    Text("Pick Folder")
                }
            }

            Column {
                Text("Pick Save File: ${state.saveFile ?: "None"}")
                Button(
                    onClick = interactor::pickSaveFile
                ) {
                    Text("Pick Save File")
                }
            }

            Column {
                Text("Create File: ${state.createFileResult ?: ""}")
                TextField(value = state.createFileName, placeholder = { Text("Name") }, onValueChange = interactor::createFileNameChanged)
                TextField(value = state.createFileContent, placeholder = { Text("Content") }, onValueChange = interactor::createFileContentChanged)
                Button(
                    onClick = interactor::createFile
                ) {
                    Text("Create File")
                }
            }

            Column {
                Text("Create Folder: ${state.createFolderResult ?: ""}")
                TextField(value = state.createFolderName, placeholder = { Text("Name") }, onValueChange = interactor::createFolderNameChanged)
                Button(
                    onClick = interactor::createFolder
                ) {
                    Text("Create Folder")
                }
            }

            Column {
                Text("Read Selected File Metadata: ${state.metadata ?: "None"}")
                Button(
                    onClick = interactor::readMetaData
                ) {
                    Text("Read Metadata")
                }
            }

            Column {
                Text("Rename Selected File: ${state.renameFileResult ?: ""}")
                TextField(value = state.renameFile, onValueChange = interactor::renameFileNameChanged)
                Button(
                    onClick = interactor::renameFile
                ) {
                    Text("Rename File")
                }
            }

            Column {
                Text("Rename Selected Folder: ${state.renameFolderResult ?: ""}")
                TextField(value = state.renameFolder, onValueChange = interactor::renameFolderNameChanged)
                Button(
                    onClick = interactor::renameFolder
                ) {
                    Text("Rename Folder")
                }
            }

            Column {
                Text("Delete Selected File: ${state.deleteFileResult ?: ""}")
                Button(
                    onClick = interactor::deleteFile
                ) {
                    Text("Delete File")
                }
            }

            Column {
                Text("Delete Selected Folder: ${state.deleteFolderResult ?: ""}")
                Button(
                    onClick = interactor::deleteFolder
                ) {
                    Text("Delete Folder")
                }
            }

            Column {
                Text("List Files In Selected Folder")
                Button(
                    onClick = interactor::list
                ) {
                    Text("List Files")
                }
                Column {
                    state.fileList.forEach {
                        Text(it.name)
                    }
                }
            }

            Column {
                Text("Check Selected File and Folder Exists")
                Text("File Exists: ${state.fileExistsResult}    Folder Exists: ${state.folderExistsResult}")
                Button(
                    onClick = interactor::exists
                ) {
                    Text("Check Exists")
                }
            }
        }
    }
}