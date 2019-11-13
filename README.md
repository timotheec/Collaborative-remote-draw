# Collaborative Remote Draw

The aim of this project is to make an application that displays a picture on a computer screen or a projector while editing it on a smartphone. The edits performed on the phone are displayed in real time on the projector. It can be used during presentations to highlight parts of an image or to allow multiple users to work on the same image.

## From executables

These instructions will explain you how to launch and install the two applications (server and client) from executables files.

### 1 - Server

#### 1.1 - Prerequisites

Install java 11 or higher (lowest version haven't been tested).

#### 1.2 - Run the server app

Find the *server.jar* executable under *executables* folder.

Run the file with the way you prefer.

On linux you can run the following command from this folder: 
```
java -jar server.jar
```

### 2 - Client

#### 2.1 - Prerequisites

Install adb.

#### 2.2 - Install the client app on a device

Make sure your android device is connected to your computer and you accepted the computer as a valid source on your phone.

Find the *client.apk* executable under *executables* folder.

Run the following following command using adb cli from this folder:
```
adb install client.apk
```


## From source

These instructions will explain you how to launch and install the two applications (server and client) from source files.

### 1 - Server

Open eclipse (or a similar IDE), choose open a workspace and select *java* folder as workspace.

### 2 - Client

Open android studio (or similar IDE), open a project and select *android/CollaborativeRemoteDrawClient* folder.
