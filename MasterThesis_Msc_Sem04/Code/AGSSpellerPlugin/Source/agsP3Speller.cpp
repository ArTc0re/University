//
// agsP300Speller.cpp : This Plugin is based on the ags_template.cpp from Chris Jones
// editor : Markus Strobel
//
#define WIN32_LEAN_AND_MEAN
#include <allegro.h>
#include <winalleg.h>
#include <winsock2.h>
#include <string>

#define THIS_IS_THE_PLUGIN
#include "agsplugin.h"

// private variables
	long socketResult;
	SOCKET sock;
	SOCKADDR_IN address;
	WSADATA wsa;
	HBITMAP gameScreen;
	bool IsConnected;


// DllMain - standard Windows DLL entry point.
// The AGS editor will cause this to get called when the editor first
// starts up, and when it shuts down at the end.
BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved) {

  switch (ul_reason_for_call)	{
		case DLL_PROCESS_ATTACH:
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:
			break;
  }
  return TRUE;
}

// ***** DESIGN TIME CALLS *******

IAGSEditor *editor;
const char *ourScriptHeader =  
  "import bool InitTCPIPConnection(String IP, int Port);\r\n"
  "import int RequestSpellerMatrixResult (int matrixLeft, int matrixTop, int matrixRight, int matrixBottom, int rows, int columns);\r\n"
  "import void SleepWait(int milliseconds);\r\n"
  "import bool AGSP3SpellerIsConnected();\r\n"
  "import void SocketCleanUp();\r\n";

LPCSTR AGS_GetPluginName(void) {
  // Return the plugin description
	return "Brain Computer Interface - BCI2000 AGSP3Speller plugin";
}

int  AGS_EditorStartup (IAGSEditor *lpEditor) {
  // User has checked the plugin to use it in their game

  // If it's an earlier version than what we need, abort.
  if (lpEditor->version < 1)
    return -1;

  editor = lpEditor;
  editor->RegisterScriptHeader (ourScriptHeader);

  // Return 0 to indicate success
  return 0;
}

void AGS_EditorShutdown () {
  // User has un-checked the plugin from their game
  editor->UnregisterScriptHeader (ourScriptHeader);
}

void AGS_EditorProperties (HWND parent) {
	// User has chosen to view the Properties of the plugin
	// We could load up an options dialog or something here instead
	MessageBox (parent, TEXT("BCI2000 AGSP3Speller plugin for providing brain computer interface control possibilites, copyright (c) 2014 Markus Strobel"), TEXT("About"), MB_OK | MB_ICONINFORMATION);
}

int AGS_EditorSaveGame (char *buffer, int bufsize) {
  // We don't want to save any persistent data
  return 0;
}

void AGS_EditorLoadGame (char *buffer, int bufsize) {
  // Nothing to load for this dummy plugin
}


// ******* END DESIGN TIME  *******

// ****** RUN TIME ********

IAGSEngine *engine;

void SleepWait(int milliseconds)
{
	Sleep(milliseconds);
}

bool AGSP3SpellerIsConnected()
{
	return IsConnected;
}

bool InitTCPIPConnection(const char *IP, int Port)
{
	long result;

	// WSAStartup
	result = WSAStartup(MAKEWORD(2,0), &wsa);
	if(result != 0)
	{
		return false;
	}
	
	// Init Socket
	sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP); // IPPROTO_TCP oder 0
	if(sock == SOCKET_ERROR)
	{
		return false;
	}

	// Init Address
	memset(&address, 0, sizeof(SOCKADDR_IN)); // first initiate all by 0
	address.sin_family = AF_INET; // TCPIP
	address.sin_port = htons(Port); 
	address.sin_addr.s_addr = inet_addr(IP); // set target = local host

	// Init Connection
	result = connect(sock, (SOCKADDR*)&address, sizeof(SOCKADDR)); 
	if(result == SOCKET_ERROR)
	{
		return false;
	}

	// set timeout
	DWORD sock_timeout = 25;
	setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, (char*)&sock_timeout, sizeof(sock_timeout));
	
	// Wait a little time
	Sleep(50);	
	IsConnected = true;
	return true;
}

// Clean/Close Socket/Connection
void SocketCleanUp()
{
	// this is for the BCI2000 AGSP300Speller and will be interpreted as quit/stop run command
	char spellerParams[64];
	sprintf_s(spellerParams, "%d;%d;%d;%d;%d;%d\0", -1, -1, -1, -1, -1, -1);
	
	// sending the parameters
	send(sock, spellerParams, sizeof(spellerParams), 0);
	
	IsConnected = false;

	if(sock != NULL)
		closesocket(sock);
	WSACleanup();
}

int GetBitmapData(BITMAPINFO* bi, BYTE* buf)
{
	WINDOWS_BITMAP bitmap;
	HDC ActiveDC = GetDC(GetDesktopWindow());
	HDC CopyDC = CreateCompatibleDC(ActiveDC);
		
	GetObject(gameScreen, sizeof(BITMAP), &bitmap);
	int cClrBits = bitmap.bmPlanes * bitmap.bmBitsPixel;

	BITMAPINFO bitemp;
	memset(&bitemp, 0, sizeof(BITMAPINFO));	
	memset(bi, 0, sizeof(BITMAPINFO));
	
	bi->bmiHeader.biSize = sizeof(BITMAPINFOHEADER);
	bi->bmiHeader.biWidth = bitmap.bmWidth;
	bi->bmiHeader.biHeight = bitmap.bmHeight;
	bi->bmiHeader.biPlanes = bitmap.bmPlanes;
	bi->bmiHeader.biBitCount = bitmap.bmBitsPixel;

	
	if(cClrBits<24)
	{
		bi->bmiHeader.biClrUsed = (1<<cClrBits);
	}
	HBITMAP test = CreateCompatibleBitmap(CopyDC, bitmap.bmWidth, bitmap.bmHeight);
	
	bi->bmiHeader.biCompression = BI_RGB;
	bi->bmiHeader.biSizeImage = ((bi->bmiHeader.biWidth * cClrBits + 31) & ~31 ) / 8 * bi->bmiHeader.biHeight;
	
	int i = GetDIBits(CopyDC, gameScreen, 0, bi->bmiHeader.biHeight, buf, bi, DIB_PAL_COLORS);

	ReleaseDC(GetDesktopWindow(), ActiveDC);
	DeleteDC(CopyDC);

	return i;
}

void ConvertBGRAtoRGBA(BYTE* input, BYTE* output, int size)
{
	for (int i = 0; i < size; i+=4)
	{
		output[i] = input[i+2];		// B -> R
		output[i+1] = input[i+1];	// G -> G
		output[i+2] = input[i];		// R -> B
		output[i+3] = input[i+3];	// A -> A
	}
}

// const char *  RequestSpellerMatrixResult(int matrixLeft, int matrixTop, int matrixRight, int matrixBottom, int rows, int columns)
int RequestSpellerMatrixResult(int matrixLeft, int matrixTop, int matrixRight, int matrixBottom, int rows, int columns)
{
	long width;
	long height;
	long coldepth;
	engine->GetScreenDimensions(&width, &height, &coldepth);

	/**
		SEND PARAMETER DATA
	**/
	char spellerParams[64];
	sprintf_s(spellerParams, "%d;%d;%d;%d;%d;%d\0", matrixLeft, matrixTop, matrixRight, matrixBottom, rows, columns);
	
	// sending the parameters
	send(sock, spellerParams, sizeof(spellerParams), 0);


	/**
		SEND SCREEN
	**/
	BITMAPINFO bi;
	BYTE* buf = new BYTE[256*256*256];
	int biSize = sizeof(BITMAPINFO);
	HDC hDC = GetDC(GetDesktopWindow());
	const int packetSize = 8192;

	// Get the Screen
	BITMAP* screen = engine->GetScreen();
			
	// Convert to Bitmap Handle
	gameScreen = convert_bitmap_to_hbitmap(screen);
	
	// screenshot and GetDIBits
	GetBitmapData(&bi, buf);
		
	// Swap Color Channels Red and Blue
	// BGRA to RGBA
	BYTE* buf2 = new BYTE[256*256*256];
	int imSize = bi.bmiHeader.biSizeImage;
	ConvertBGRAtoRGBA(buf, buf2, imSize);
	
	BYTE* biData = new BYTE[256*256*256];
	memcpy(biData, &bi, sizeof(BITMAPINFO));
	memcpy(biData+biSize, buf2, bi.bmiHeader.biSizeImage);

	int res = 0;
	int idx = 0;
	do
	{
		res = send(sock, (char*)biData+idx, packetSize, 0);
		idx += packetSize;
	}
	while(res > 0);
	
	delete buf;
	buf = nullptr;
	delete biData;
	biData = nullptr;
	delete buf2;
	buf2 = nullptr;
	
	
	// hide game window
	HWND hwnd = engine->GetWindowHandle();
	SetWindowPos(hwnd, HWND_BOTTOM, 0, 0, width, height, 0);

	/**
		RECEIVE RESULT
	**/	
	char spellerResult[32];
	bool resultsRecevied = false;
	res = 0;

	do
	{
		res = recv(sock, spellerResult, sizeof(spellerResult), 0);
		if(res == SOCKET_ERROR || res == 0)
		{
			Sleep(20);
			continue;
		}
		else
		{
			resultsRecevied = true;
			continue;
		}
	}
	while(!resultsRecevied);
		
	// show game window
	SetWindowPos(hwnd, HWND_TOPMOST, 0, 0, width, height, 0);

	int result = atoi(spellerResult);

	// if the result is -1, this will be interpreted as terminating command from AGSP300Speller
	if(result == -1)
		IsConnected = false;

	return atoi(spellerResult);
}

void AGS_EngineStartup (IAGSEngine *lpEngine) {
  engine = lpEngine;

  // Make sure it's got the version with the features we need
  if (engine->version < 3) {
    engine->AbortGame ("Engine interface is too old, need newer version of AGS.");
  }

  // TCPIP
  // Init/Clear
  engine->RegisterScriptFunction ("InitTCPIPConnection", InitTCPIPConnection);
  engine->RegisterScriptFunction ("SocketCleanUp", SocketCleanUp);
  engine->RegisterScriptFunction ("AGSP3SpellerIsConnected", AGSP3SpellerIsConnected);
  // Send
  engine->RegisterScriptFunction ("RequestSpellerMatrixResult", RequestSpellerMatrixResult);

  // Misc
  engine->RegisterScriptFunction("SleepWait", SleepWait);
  
  engine->RequestEventHook (AGSE_POSTSCREENDRAW);
}

void AGS_EngineShutdown() {
	// no work to do here - but if we had created any dynamic sprites,
	// we should delete them here
}

int AGS_EngineOnEvent (int event, int data) {

	/**
  if (event == AGSE_POSTSCREENDRAW) {
    // Called just before the real screen is updated
    // This is quite advanced stuff, but it demonstrates how
    // you can basically do whatever you like to the screen
    // with a plugin
    long screenWidth, colDepth, a;
    // Get a reference to the screen we'll draw onto
    BITMAP *virtsc = engine->GetVirtualScreen();
    // Get its surface, in both 8-bit and 16-bit flavours
    unsigned char **charbuffer = engine->GetRawBitmapSurface (virtsc);
    unsigned short **shortbuffer = (unsigned short**)charbuffer;

    // Find out the screen width and colour depth
    engine->GetScreenDimensions (&screenWidth, NULL, &colDepth);

    // Draw a bright blue line across the screen near the top
    for (a = 0; a < screenWidth; a++) {
      if (colDepth <= 8)
        charbuffer[20][a] = 10;
      else if (colDepth <= 16)
        shortbuffer[20][a] = 32155;
      else  // I've been lazy, and haven't implemented 24/32-bit
        engine->AbortGame("This plugin does not support 32-bit graphics");
    }

    // Release the screen so that the engine can continue
    engine->ReleaseBitmapSurface (virtsc);
  }
  **/
  return 0;
}

// *** END RUN TIME ****
